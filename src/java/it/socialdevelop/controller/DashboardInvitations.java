package it.socialdevelop.controller;

import it.univaq.f4i.iw.framework.data.DataLayerException;
import it.univaq.f4i.iw.framework.result.FailureResult;
import it.univaq.f4i.iw.framework.result.StreamResult;
import it.univaq.f4i.iw.framework.result.TemplateManagerException;
import it.univaq.f4i.iw.framework.result.TemplateResult;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import it.socialdevelop.data.model.Admin;
import it.socialdevelop.data.model.CollaborationRequest;
import it.socialdevelop.data.model.Developer;
import it.socialdevelop.data.model.Files;
import it.socialdevelop.data.model.Project;
import it.socialdevelop.data.model.Skill;
import it.socialdevelop.data.model.SocialDevelopDataLayer;
import it.socialdevelop.data.model.Task;
import it.socialdevelop.data.model.Type;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;

/**
 *
 * @author Hello World Group
 */
public class DashboardInvitations extends SocialDevelopBaseController {

    private void action_error(HttpServletRequest request, HttpServletResponse response) {
        if (request.getAttribute("exception") != null) {
            (new FailureResult(getServletContext())).activate((Exception) request.getAttribute("exception"), request, response);
        }
    }

    

    private void action_inviti(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, TemplateManagerException, SQLException, NamingException, DataLayerException {
        HttpSession s = request.getSession(true);
        request.setAttribute("request", request);
        request.setAttribute("page_title", "Dashboard - Invitations");
        request.setAttribute("page_subtitle", "manage your invites");
        if (s.getAttribute("userid") != null && ((int) s.getAttribute("userid")) > 0) {
            SocialDevelopDataLayer datalayer = (SocialDevelopDataLayer) request.getAttribute("datalayer");
            Admin admin = datalayer.getAdmin((int) s.getAttribute("userid"));
            if (admin != null) {
                request.setAttribute("admin", "admin");
            }
            //recuperiamo sviluppatore a cui appartiene il pannello
            Developer dev = datalayer.getDeveloper((int) s.getAttribute("userid"));
            int dev_key = dev.getKey();
            request.setAttribute("username", dev.getUsername());
            //request.setAttribute("fullname", dev.getName() + " " + dev.getSurname());
            request.setAttribute("auth_user", s.getAttribute("userid"));
            request.setAttribute("foto", s.getAttribute("foto"));
            request.setAttribute("fullname", s.getAttribute("fullname"));
            request.setAttribute("bio", dev.getBiography());
            request.setAttribute("mail", dev.getMail());
            request.setAttribute("logout", "Logout");


            //recuperiamo gli inviti 
            List<CollaborationRequest> invites = datalayer.getInvitesByCoordinator(dev.getKey());
            List<CollaborationRequest> invitesToSend = new ArrayList();
            for (CollaborationRequest i : invites) {
                Task task = datalayer.getTask(i.getTaskKey());
                Project project = datalayer.getProjectByTask(task.getKey());
                Developer invitato = datalayer.getDeveloper(i.getCollaboratorKey());
                
                task.setProject(project);
                i.setTaskRequest(task);
                i.setCollaboratorRequest(invitato);
                invitesToSend.add(i);
            }

            request.setAttribute("invites", invitesToSend);
            
            //developer data
            Map<Task, Integer> tasks = datalayer.getTasksByDeveloper(dev_key); //lista task 
            
            int sum = 0;
            int length = 0;
            for (Map.Entry<Task, Integer> key : tasks.entrySet()){
                Task task = key.getKey();
                
                if (task.isCompleted()) {
                    sum+= (int) key.getValue();
                    length ++;
                }
            }

            int votes = 0;
            if(length > 0){
                votes = sum/length;
            }
            request.setAttribute("votes", votes);
            request.setAttribute("dev", dev);
            TemplateResult res = new TemplateResult(getServletContext());
            res.activate("dashboard_invitations.ftl.html", request, response);

        } else {
            response.sendRedirect("/SocialDevelop");
        }

    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {

        try {
            action_inviti(request, response);
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
