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

    private String getImg(HttpServletRequest request, HttpServletResponse response, Developer dev) throws IOException, SQLException, DataLayerException, NamingException {
        StreamResult result = new StreamResult(getServletContext());

        SocialDevelopDataLayer datalayer = (SocialDevelopDataLayer) request.getAttribute("datalayer");
        if (dev.getFoto() != 0) {
            Files foto_profilo = datalayer.getFile(dev.getFoto());
            return "uploaded-images/" + foto_profilo.getLocalFile();
        } else {
            return "uploaded-images/foto_profilo_default.png";
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
        
        Project project = datalayer.getProject(task.getProjectKey());
        
        data.put("task", task);
        
        data.put("project", project);
        
        int coordinator_key = project.getCoordinatorKey();
        if (s.getAttribute("userid") != null && ((int) s.getAttribute("userid")) > 0) {
            data.put("logout", "Logout");
            data.put("auth_user", s.getAttribute("userid"));
            data.put("foto", s.getAttribute("foto"));
            data.put("fullname", s.getAttribute("fullname"));
            if (coordinator_key == (int) s.getAttribute("userid")) {
                data.put("userid", coordinator_key);
            }
            Admin admin = datalayer.getAdmin((int) s.getAttribute("userid"));
            if (admin != null) {
                data.put("admin", "admin");
            }

        }
        
        boolean flag = false; //serve per controllare se l'utente loggato (se c'è) è tra i collaboratori del progetto

        if (s.getAttribute("userid") != null && ((int) s.getAttribute("userid")) > 0) {
            //se l'utente è loggato controlliamo se è un collaboratore del progetto.
            //se lo è rendiamo visibili i messaggi privati e inoltre rendiamo visibile
            //il form di inserimento del messaggio
            
            int dev_key = (int) s.getAttribute("userid");
            Map<Developer, Integer> collaborators = datalayer.getCollaboratorsByTask(task.getKey());
            for (Map.Entry<Developer, Integer> m : collaborators.entrySet()) {
                if (m.getKey().getKey() == dev_key || dev_key == coordinator_key) {
                    flag = true;
                    data.put("userid", dev_key);
                }

            }
            if (dev_key == coordinator_key) {
                data.put("isCoordinator", 1);
            }
        }
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
            String foto = getImg(request, response, dev2);
            foto_msg.add(foto);
            by.add(dev2);
        }
        data.put("by", by);
        data.put("foto_msg", foto_msg);
        Developer coordinator = datalayer.getDeveloper(project.getCoordinatorKey());
        data.put("coordinator", coordinator);
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
