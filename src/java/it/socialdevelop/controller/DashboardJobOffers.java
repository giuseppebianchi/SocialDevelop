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
import it.socialdevelop.data.model.SocialDevelopDataLayer;
import it.socialdevelop.data.model.Task;
import it.socialdevelop.data.model.Type;
import java.util.Map;

/**
 *
 * @author Hello World Group
 */
public class DashboardJobOffers extends SocialDevelopBaseController {

    private void action_error(HttpServletRequest request, HttpServletResponse response) {
        if (request.getAttribute("exception") != null) {
            (new FailureResult(getServletContext())).activate((Exception) request.getAttribute("exception"), request, response);
        }
    }

    

    private void action_offerte(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, TemplateManagerException, SQLException, NamingException, DataLayerException {
        HttpSession s = request.getSession(true);
        request.setAttribute("page_title", "Panel of Offers");
        request.setAttribute("page_subtitle", "manage your offers!");
        request.setAttribute("request", request);
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
            request.setAttribute("profile_picture", s.getAttribute("profile_picture"));
            request.setAttribute("fullname", s.getAttribute("fullname"));
            request.setAttribute("bio", dev.getBiography());
            request.setAttribute("mail", dev.getMail());
            request.setAttribute("logout", "Logout");

            
            
            //recuperiamo le proposte
            List<CollaborationRequest> proposals = datalayer.getProposalsByCollaborator(dev.getKey());
            request.setAttribute("nProposals", proposals.size());
            
            //recuperiamo le domande
            List<CollaborationRequest> demends = datalayer.getQuestionsByCoordinator(dev.getKey());
            request.setAttribute("nRequests", demends.size());
            
            //recuperiamo le job applications
            List<CollaborationRequest> jobapps = datalayer.getQuestionsByDeveloper(dev.getKey());
            request.setAttribute("nJobApplications", jobapps.size());
            
            //recuperiamo gli inviti
            List<CollaborationRequest> invites = datalayer.getInvitesByCoordinator(dev.getKey());
            request.setAttribute("nInvitations", invites.size());

            //recuperiamo le proposte
            List<Task> tasks = datalayer.getOffertsByDeveloper(dev.getKey());

            //recuperiamo il task relativo alla proposta e il progetto a cui appartiene
            List<Task> taskToSet = new ArrayList();
            List<Type> tasks_types = new ArrayList<>(); //lista dei tipi di ogni task
            for (Task task : tasks) {

                Project pr = datalayer.getProject(task.getProjectKey());
                Developer coordinator = datalayer.getDeveloper(pr.getCoordinatorKey());
                pr.setCoordinator(coordinator);
                task.setProject(pr);
                Type type = datalayer.getType(task.getType_key());
                tasks_types.add(type);

                taskToSet.add(task);
            }
            
            //developer data
            Map<Task, Integer> tas = datalayer.getTasksByDeveloper(dev.getKey()); //lista task 
            
            int sum = 0;
            int length = 0;
            for (Map.Entry<Task, Integer> key : tas.entrySet()){
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
            request.setAttribute("tasks_types", tasks_types);
            request.setAttribute("joboffers", taskToSet);
            TemplateResult res = new TemplateResult(getServletContext());
            res.activate("dashboard_job_offers.ftl.html", request, response);

        } else {
            response.sendRedirect("index");
        }

    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {

        try {
            action_offerte(request, response);
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
