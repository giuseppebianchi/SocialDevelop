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
public class Task_Detail extends SocialDevelopBaseController {

    private void action_error(HttpServletRequest request, HttpServletResponse response) {
        if (request.getAttribute("exception") != null) {
            (new FailureResult(getServletContext())).activate((Exception) request.getAttribute("exception"), request, response);
        }
    }

    

    private void action_task(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, TemplateManagerException, SQLException, NamingException, DataLayerException {

        SocialDevelopDataLayer datalayer = (SocialDevelopDataLayer) request.getAttribute("datalayer");
        HttpSession s = request.getSession(true);
        Map data = new HashMap();
        data.put("request", request);
        //int key = Integer.parseInt(request.getParameter("n")); //project_key
        String pathInfo = request.getPathInfo(); // /{value}/test
        String[] pathParts = pathInfo.split("/");
        String value = pathParts[1]; // {value}
        int key = Integer.parseInt(value);
        
        Task task = datalayer.getTask(key);
        task.setType(datalayer.getType(task.getType_key()));
        
        Project project = datalayer.getProject(task.getProjectKey());
        
        
        data.put("task", task);
        data.put("taskType", task.getType().getType());
        data.put("project", project);
        
        int coordinator_key = project.getCoordinatorKey();
        if (s.getAttribute("userid") != null && ((int) s.getAttribute("userid")) > 0) {
            data.put("logout", "Logout");
            data.put("auth_user", s.getAttribute("userid"));
            data.put("foto", s.getAttribute("foto"));
            data.put("fullname", s.getAttribute("fullname"));
            int dev_key = (int) s.getAttribute("userid");
            if (coordinator_key == (int) dev_key) {
                data.put("userid", coordinator_key);
                data.put("isCoordinator", 1);
            }else{
                Map<String, Integer> thd = datalayer.getTaskDeveloper(dev_key, task.getKey());
                for (Entry<String, Integer> pair : thd.entrySet()){
                    //iterate over the pairs
                    data.put(pair.getKey(), pair.getValue());
                }
            }
            Admin admin = datalayer.getAdmin((int) s.getAttribute("userid"));
            if (admin != null) {
                data.put("admin", "admin");
            }

        }
        
        boolean flag = false; //serve per controllare se l'utente loggato (se c'è) è tra i collaboratori del progetto

        
            //se l'utente è loggato controlliamo se è un collaboratore del progetto.
            //se lo è rendiamo visibili i messaggi privati e inoltre rendiamo visibile
            //il form di inserimento del messaggio
            
            
            Map<Developer, Integer> collaborators = datalayer.getCollaboratorsByTask(task.getKey());
            int[] votes = new int[collaborators.size()];
            int i = 0;
            for (Map.Entry<Developer, Integer> m : collaborators.entrySet()) {
                int j = 0;
                Map<Task, Integer> tasks = datalayer.getTasksByDeveloper(m.getKey().getKey());

                for (Entry<Task,Integer> t : tasks.entrySet()){
                    Task tas = t.getKey();    
                    if (tas.isCompleted()) {
                        votes[i] += t.getValue();
                        j++;
                    }
                }
                if(j != 0){
                   votes[i] = (int) votes[i]/j;   
                }
                i++;

            }
            data.put("collaborators", collaborators);
            data.put("votes", votes);
            
            int nrequests = datalayer.getRequestsByTask(key);
            data.put("nrequests", nrequests);
            
            data.put("datalayer", datalayer);
            
           
        
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

        data.put("skills", skillsList);
        //double percProg = Math.round(((double) tasksEnded.size() / (double) tasks.size()) * 100);
        //data.put("percProg", percProg);
        List<Message> messages = new ArrayList();
        if (flag) {
            messages = datalayer.getMessages(project.getKey());
        } else {
            messages = datalayer.getPublicMessages(project.getKey());
        }

        data.put("messages", messages);
        List<String> foto_msg = new ArrayList();

        List<Developer> by = new ArrayList();
        for (Message message : messages) {
            Developer dev2 = message.getDeveloper();
            by.add(dev2);
        }
        data.put("by", by);
        data.put("foto_msg", foto_msg);
        
        Developer coordinator = datalayer.getDeveloper(project.getCoordinatorKey());
        data.put("coordinator", coordinator);
        
        int vote = 0, j=0;
        Map<Task, Integer> tasks = datalayer.getTasksByDeveloper(coordinator.getKey());       
        for (Entry<Task,Integer> t : tasks.entrySet()){
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
        res.activate("task_detail.ftl.html", data, response);

    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            action_task(request, response);
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
