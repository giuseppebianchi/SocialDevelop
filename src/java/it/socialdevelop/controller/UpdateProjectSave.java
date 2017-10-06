package it.socialdevelop.controller;

import it.univaq.f4i.iw.framework.data.DataLayerException;
import it.univaq.f4i.iw.framework.result.FailureResult;
import it.univaq.f4i.iw.framework.result.TemplateManagerException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.GregorianCalendar;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import it.socialdevelop.data.impl.ProjectImpl;
import it.socialdevelop.data.impl.TaskImpl;
import it.socialdevelop.data.model.Admin;
import it.socialdevelop.data.model.Project;
import it.socialdevelop.data.model.SocialDevelopDataLayer;
import it.socialdevelop.data.model.Task;

/**
 *
 * @author Hello World Group
 */
public class UpdateProjectSave extends SocialDevelopBaseController {

    private void action_error(HttpServletRequest request, HttpServletResponse response) {
        if (request.getAttribute("exception") != null) {
            (new FailureResult(getServletContext())).activate((Exception) request.getAttribute("exception"), request, response);
        }
    }

    private void action_updateproject(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, TemplateManagerException, SQLException, NamingException, DataLayerException {
        //recuperare coordinatore dalla sessione!
        HttpSession s = request.getSession(true);
        if (s.getAttribute("userid") != null && ((int) s.getAttribute("userid")) > 0) {
            
            //controllo se chi sta modificando il progetto è veramente il propretario
            int userid = (int) s.getAttribute("userid");
            int project_key = Integer.parseInt(request.getParameter("project_id"));
            SocialDevelopDataLayer datalayer = (SocialDevelopDataLayer) request.getAttribute("datalayer");
            Project project = datalayer.getProject(project_key);
            int coordinator_key = project.getCoordinatorKey();
            
            if (userid == coordinator_key) {

                String project_name = request.getParameter("project_name");
                String project_category = request.getParameter("project_category");
                String project_location = request.getParameter("project_location");
                String project_company = request.getParameter("project_company");
                String project_descr = request.getParameter("project_description");
                
                
                //memorizziamo il progetto
                ProjectImpl p = new ProjectImpl(datalayer);
                p.setCoordinatorKey(userid);
                p.setName(project_name);
                p.setCategory(project_category);
                p.setLocation(project_location);
                p.setCompany(project_company);
                p.setDescription(project_descr);
                
                p.setKey(project_key);
                s.removeAttribute("projectKey");
                datalayer.storeProject(p);
                
                datalayer.destroy();
                response.sendRedirect("/SocialDevelop/projects/settings/" + project_key + "?updated_project=1");
            } else {
                response.sendRedirect("/SocialDevelop/projects/" + project_key);
            }
        } else {
            response.sendRedirect("/SocialDevelop");
        }

    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {
        try {
            action_updateproject(request, response);
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
