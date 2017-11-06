package it.socialdevelop.controller;

import it.univaq.f4i.iw.framework.data.DataLayerException;
import it.univaq.f4i.iw.framework.result.FailureResult;
import it.univaq.f4i.iw.framework.result.StreamResult;
import it.univaq.f4i.iw.framework.result.TemplateManagerException;
import it.univaq.f4i.iw.framework.result.TemplateResult;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Calendar;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import it.socialdevelop.data.model.Admin;
import it.socialdevelop.data.model.Developer;
import it.socialdevelop.data.model.Files;
import it.socialdevelop.data.model.Project;
import it.socialdevelop.data.model.Skill;
import it.socialdevelop.data.model.SocialDevelopDataLayer;
import it.socialdevelop.data.model.Task;
import it.socialdevelop.data.model.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * @author Hello World Group
 */
public class DeveloperProfile extends SocialDevelopBaseController {

    private void action_error(HttpServletRequest request, HttpServletResponse response) {
        if (request.getAttribute("exception") != null) {
            (new FailureResult(getServletContext())).activate((Exception) request.getAttribute("exception"), request, response);
        }
    }

    

    private void action_devprofile(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, TemplateManagerException, SQLException, NamingException, DataLayerException {
        Map data = new HashMap();
        data.put("request", request);
        
        //recuperare developer
        SocialDevelopDataLayer datalayer = (SocialDevelopDataLayer) request.getAttribute("datalayer");
        //int dev_key = Integer.parseInt(request.getParameter("n")); //project_key
        String pathInfo = request.getPathInfo(); // /{value}/test
        String[] pathParts = pathInfo.split("/");
        String value = pathParts[1]; // {value}
        int dev_key = Integer.parseInt(value);
        Developer dev = datalayer.getDeveloper(dev_key);
        if (dev != null) {
            data.put("id", dev_key);
            data.put("username", dev.getUsername());
            data.put("fullname", dev.getName() + " " + dev.getSurname());
            data.put("headline", dev.getHeadline());
            data.put("bio", dev.getBiography());
            data.put("resume", dev.getResume());
            data.put("profile_picture_dev", dev.getPicture());
            data.put("mail", dev.getMail());
            data.put("page_title", "Social Develop - " + dev.getName() + " " + dev.getSurname());
            data.put("page_subtitle", dev.getName() + " " + dev.getSurname());

            HttpSession s = request.getSession(true);
            if (s.getAttribute("userid") != null && ((int) s.getAttribute("userid")) > 0) {
                data.put("logout", "Logout");
                
                data.put("auth_user", s.getAttribute("userid"));
                data.put("auth_user_fullname", s.getAttribute("fullname"));
                data.put("profile_picture", s.getAttribute("profile_picture"));
                
                
                
                if(dev_key == (int) s.getAttribute("userid")){
                    data.put("isProfile", 1);
                }
                
                Admin admin = datalayer.getAdmin((int) s.getAttribute("userid"));
                if (admin != null) {
                    data.put("admin", "admin");
                }
            } else {
                data.put("MyProfile", "hidden");
            }
            Map<Task, Integer> tasks = datalayer.getTasksByDeveloper(dev_key); //lista task 
            ArrayList tasksEnded = new ArrayList();  //lista di quelli terminati
            List<Type> tasks_types = new ArrayList<>(); //lista dei tipi di ogni task
            ArrayList task_skills = new ArrayList();
            
            int sum = 0;
            int length = 0;
            for (Map.Entry<Task, Integer> key : tasks.entrySet()){
                Task task = key.getKey();
                
                if (task.isCompleted()) {
                    sum+= (int) key.getValue();
                    length ++;
                    tasksEnded.add(key.getValue());
                }else{
                    tasksEnded.add(0);
                }
                Type type = datalayer.getType(task.getType_key());
                tasks_types.add(type);
                
                GregorianCalendar start = task.getStartDate();
                GregorianCalendar end = task.getEndDate();
                Date startDate = new Date();
                Date endDate = new Date();
                if (startDate != null) {
                    startDate = start.getTime();
                }
                if (endDate != null) {
                    endDate = end.getTime();
                }
                
                Map<Skill, Integer> skillsList = datalayer.getSkillsByTask(task.getKey());
                task_skills.add(skillsList);

                }
        data.put("tasks", tasks);
        int votes = 0;
        if(length > 0){
            votes = sum/length;
        }
        data.put("votes", votes);
        data.put("ncompleted", length);
        data.put("current_tasks", tasks.size() - length);
        List<Project> projects = datalayer.getProjectsByCoordinator(dev_key); //lista progetti
        data.put("projects", projects);
        int[] nProTasks = new int[projects.size()];
        int count = 0;
            int c = 0;
        int[] nProjectCollaborators = new int[projects.size()];
        for (Project progetto : projects) {
            
            List<Task> tasks0 = datalayer.getTasks(progetto.getKey());
            nProTasks[count] = tasks0.size();
            for(Task t: tasks0){
                nProjectCollaborators[count] += t.getNumCollaborators();
            }
            count++;
            c++;
        }
        data.put("nProTasks", nProTasks);
        Map<Skill, Integer> skills = datalayer.getSkillsByDeveloper(dev_key);
        HashMap<String, ArrayList> skills_by_types = new HashMap<String, ArrayList>();
        for (Entry<Skill, Integer> sk: skills.entrySet()){
            Skill skill = sk.getKey();
            int level = sk.getValue();
            Map item = new HashMap();
            item.put("skill", skill);
            item.put("level", level);
            if(skills_by_types.get(skill.getType().getType()) != null){
                skills_by_types.get(skill.getType().getType()).add(item);
            }else{
                ArrayList a = new ArrayList();
                a.add(item);
                skills_by_types.put(skill.getType().getType(), a);
            }
        }
        
        
        data.put("ncollaboratori", nProjectCollaborators);
        data.put("nTask", tasks.size());
        data.put("skills", task_skills);
        data.put("skillsList", skills_by_types);
        data.put("tasks_types", tasks_types);
        data.put("tasks_ended", tasksEnded);
        double percProg = Math.round(((double) tasksEnded.size() / (double) tasks.size()) * 100);
        data.put("percProg", percProg);
            
            datalayer.destroy();
            TemplateResult res = new TemplateResult(getServletContext());
            res.activate("user.ftl.html", data, response);
        } else {
            response.sendRedirect("index");
        }

    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {

        try {
            action_devprofile(request, response);
        } catch (IOException ex) {
            request.setAttribute("exception", ex);
            action_error(request, response);
        } catch (TemplateManagerException ex) {
            request.setAttribute("exception", ex);
            action_error(request, response);
        } catch (SQLException ex) {
            request.setAttribute("exception", ex);
            action_error(request, response);
        } catch (NamingException ex) {
            request.setAttribute("exception", ex);
            action_error(request, response);
        } catch (DataLayerException ex) {
            request.setAttribute("exception", ex);
            action_error(request, response);
        }

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
