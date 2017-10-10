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
import java.util.Map;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import it.socialdevelop.data.model.Admin;
import it.socialdevelop.data.model.Developer;
import it.socialdevelop.data.model.Files;
import it.socialdevelop.data.model.Project;
import it.socialdevelop.data.model.SocialDevelopDataLayer;
import it.socialdevelop.data.model.Task;

/**
 *
 * @author Hello World Group
 */
public class MyCollaborators extends SocialDevelopBaseController {

    private void action_error(HttpServletRequest request, HttpServletResponse response) {
        if (request.getAttribute("exception") != null) {
            (new FailureResult(getServletContext())).activate((Exception) request.getAttribute("exception"), request, response);
        }
    }

    

    private void action_mycollaborators(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, TemplateManagerException, SQLException, NamingException, DataLayerException {
        HttpSession s = request.getSession(true);
        request.setAttribute("page_title", "My Collaborators");
        request.setAttribute("page_subtitle", "manage your collaborators");
        if (s.getAttribute("userid") != null && ((int) s.getAttribute("userid")) > 0) {
            SocialDevelopDataLayer datalayer = (SocialDevelopDataLayer) request.getAttribute("datalayer");
            Admin admin = datalayer.getAdmin((int) s.getAttribute("userid"));
            if (admin != null) {
                request.setAttribute("admin", "admin");
            }
            Developer dev = datalayer.getDeveloper((int) s.getAttribute("userid"));
            request.setAttribute("username", dev.getUsername());
            request.setAttribute("fullname", dev.getName() + " " + dev.getSurname());

            request.setAttribute("bio", dev.getBiography());
            request.setAttribute("mail", dev.getMail());
            request.setAttribute("logout", "Logout");

            //recupero progetti gestiti dall'utente (progetti dei quali è il coordinatore)
            List<Project> projects = datalayer.getProjectsByCoordinator(dev.getKey());
            List<List<Task>> tasks = new ArrayList();

            if (projects.size() > 0) {
                for (Project p : projects) {
                    List<Task> project_tasks = datalayer.getTasks(p.getKey());
                    if (project_tasks != null) {
                        List<Task> tasks2 = new ArrayList();
                        for (Task task : project_tasks) {
                            Map<Developer, Integer> map = datalayer.getCollaboratorsByTask(task.getKey());

                           

                            task.setCollaborators(map);
                            tasks2.add(task);
                        }
                        tasks.add(tasks2);
                    }
                }
            }
            request.setAttribute("projects", projects);
            request.setAttribute("tasks", tasks);
            datalayer.destroy();
            TemplateResult res = new TemplateResult(getServletContext());
            res.activate("my_collaborators.html", request, response);

        } else {
            response.sendRedirect("index");
        }

    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {

        try {
            action_mycollaborators(request, response);
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
