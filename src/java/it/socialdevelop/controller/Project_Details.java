package it.socialdevelop.controller;

import it.univaq.f4i.iw.framework.data.DataLayerException;
import it.univaq.f4i.iw.framework.result.FailureResult;
import it.univaq.f4i.iw.framework.result.StreamResult;
import it.univaq.f4i.iw.framework.result.TemplateManagerException;
import it.univaq.f4i.iw.framework.result.TemplateResult;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import it.socialdevelop.data.model.Admin;
import it.socialdevelop.data.model.Developer;
import it.socialdevelop.data.model.Files;
import it.socialdevelop.data.model.Message;
import it.socialdevelop.data.model.Project;
import it.socialdevelop.data.model.Skill;
import it.socialdevelop.data.model.SocialDevelopDataLayer;
import it.socialdevelop.data.model.Task;
import it.socialdevelop.data.model.Type;
import java.util.HashMap;
import java.util.Map.Entry;

/**
 *
 * @author Hello World Group
 */
public class Project_Details extends SocialDevelopBaseController {

    private void action_error(HttpServletRequest request, HttpServletResponse response) {
        if (request.getAttribute("exception") != null) {
            (new FailureResult(getServletContext())).activate((Exception) request.getAttribute("exception"), request, response);
        }
    }

    

    private void action_project(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, TemplateManagerException, SQLException, NamingException, DataLayerException {

        SocialDevelopDataLayer datalayer = (SocialDevelopDataLayer) request.getAttribute("datalayer");
        HttpSession s = request.getSession(true);
        Map data = new HashMap();
        data.put("request", request);
        //int key = Integer.parseInt(request.getParameter("n")); //project_key
        String pathInfo = request.getPathInfo(); // /{value}/test
        String[] pathParts = pathInfo.split("/");
        String value = pathParts[1]; // {value}
        int key = Integer.parseInt(value);
        boolean flag = false; //serve per controllare se l'utente loggato (se c'è) è tra i collaboratori del progetto
        Project project = datalayer.getProject(key);
        int coordinator_key = project.getCoordinatorKey();
        if (s.getAttribute("userid") != null && ((int) s.getAttribute("userid")) > 0) {
            data.put("logout", "Logout");
            data.put("auth_user", s.getAttribute("userid"));
            int usedid = (int) s.getAttribute("userid");
            data.put("profile_picture", s.getAttribute("profile_picture"));
            data.put("fullname", s.getAttribute("fullname"));
            if (coordinator_key == (int) s.getAttribute("userid")) {
                data.put("userid", coordinator_key);
                data.put("isCoordinator", 1);
                data.put("isCollaborator", 1);
                flag = true;
            }
            Admin admin = datalayer.getAdmin((int) s.getAttribute("userid"));
            if (admin != null) {
                data.put("admin", "admin");
            }

        }
        data.put("page_title", "Project" + " " + project.getName());
        data.put("page_subtitle", "Check project info");
        data.put("projectname", project.getName());
        data.put("projectcategory", project.getCategory());
        data.put("projectlocation", project.getLocation());
        data.put("projectcompany", project.getCompany());
        data.put("projectdescr", project.getDescription());
        data.put("projectkey", key);
        List<Task> tasks = datalayer.getTasks(project.getKey()); //lista task del progetto
        List<Task> tasksClosed = new ArrayList();  //lista di quelli terminati
        List<Task> tasksCompleted = new ArrayList();  //lista di quelli terminati
        List<Type> tasks_types = new ArrayList<>(); //lista dei tipi di ogni task
        int nProjectCollaborators = 0; //numero collaboratori totali task
        ArrayList skills = new ArrayList();
        Map<Integer, Developer> collaborators = new HashMap<Integer, Developer>();
        ArrayList tasks_developer = new ArrayList();
        data.put("tasks", tasks);
        
        int dev_requests = 0;
        Map<Integer, Integer> dev_votes = new HashMap<Integer, Integer>();
        for (Task task : tasks) {
            if (!task.isOpen()) {
                tasksClosed.add(task);
            }
            if (task.isCompleted()) {
                tasksCompleted.add(task);
            }
            int nrequests = datalayer.getRequestsByTask(task.getKey());
            dev_requests += nrequests;
            Type type = datalayer.getType(task.getType_key());
            tasks_types.add(type);
            
            Map<Developer, Integer> developers = datalayer.getCollaboratorsByTask(task.getKey());
            int i = 0;
            int[] votes = new int[developers.size()];
            for (Entry<Developer,Integer> pair : developers.entrySet()){
                int j = 0;
                Developer dev = pair.getKey(); 
                //controllo se l'utente in sessione è un collaboratore 
                if((s.getAttribute("userid") != null) && (((int) s.getAttribute("userid")) > 0) && ((int) s.getAttribute("userid") == dev.getKey())){
                    data.put("isCollaborator", 1);
                    flag = true;
                }
                //int vote = pair.getValue();
                //iterate over the pairs
                Map<Task, Integer> tasks0 = datalayer.getTasksByDeveloper(dev.getKey());

                for (Entry<Task,Integer> t : tasks0.entrySet()){
                    Task tas = t.getKey();    
                    if (tas.isCompleted()) {
                        votes[i] += t.getValue();
                        j++;
                    }
                }
                if(j != 0){
                    votes[i] = (int) votes[i]/j;   
                }
                dev_votes.put(dev.getKey(), votes[i]);
                collaborators.put(dev.getKey(), dev);
                i++;
                
            }

            nProjectCollaborators += task.getNumCollaborators();
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
            if (startDate != null && endDate != null) {
                long startTime = startDate.getTime();
                long endTime = endDate.getTime();
                long diffTime = endTime - startTime;
                long diffDays = diffTime / (1000 * 60 * 60 * 24);
                data.put("daysleft", diffDays);
            }
            Map<Skill, Integer> skillsList = datalayer.getSkillsByTask(task.getKey());
            skills.add(skillsList);

        }
        data.put("nrequests", dev_requests);
        data.put("hired", collaborators.size());
        data.put("tasks_completed", tasksCompleted.size());
        data.put("collaborators", collaborators);
        data.put("tasks_developers", tasks_developer);
        int[] v = new int[dev_votes.size()];
        int i = 0;
        for (Entry<Integer,Integer> pair : dev_votes.entrySet()){
            //iterate over the pairs
            v[i] = pair.getValue();
        }
        data.put("votes", v);
        data.put("tasks_closed", tasksClosed.size());
        data.put("tasks_open", tasks.size() - tasksClosed.size());
        data.put("nProjectCollaborators", nProjectCollaborators);
        data.put("nTask", tasks.size());
        data.put("skills", skills);
        data.put("tasks_types", tasks_types);
        double percProg = Math.round(((double) tasksCompleted.size() / (double) tasks.size()) * 100);
        data.put("percProg", percProg);
        List<Message> messages = new ArrayList();
        if (flag) {
            messages = datalayer.getMessages(project.getKey());
        } else {
            messages = datalayer.getPublicMessages(project.getKey());
        }
 
        data.put("messages", messages);
        List<Developer> by = new ArrayList();
        for (Message message : messages) {
            Developer dev2 = message.getDeveloper();
            by.add(dev2);
        }
        data.put("by", by);
        
        Developer coordinator = datalayer.getDeveloper(project.getCoordinatorKey());
        data.put("coordinator", coordinator);
        int vote = 0, j=0;
        Map<Task, Integer> ts = datalayer.getTasksByDeveloper(coordinator.getKey());       
        for (Map.Entry<Task,Integer> t : ts.entrySet()){
            Task tas = t.getKey();    
            if (tas.isCompleted()) {
                vote += t.getValue();
                j++;
            }
        }
        if(j != 0){
           vote = (int) vote/j;   
        }
        data.put("vote", vote);
        datalayer.destroy();
        String act_url = request.getRequestURI();
        s.setAttribute("previous_url", act_url);
        TemplateResult res = new TemplateResult(getServletContext());
        res.activate("project_details.ftl.html", data, response);

    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            action_project(request, response);
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
}
