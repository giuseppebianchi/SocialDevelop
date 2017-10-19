package it.socialdevelop.controller;

import it.univaq.f4i.iw.framework.data.DataLayerException;
import it.univaq.f4i.iw.framework.result.FailureResult;
import it.univaq.f4i.iw.framework.result.TemplateManagerException;
import it.univaq.f4i.iw.framework.result.TemplateResult;
import java.io.IOException;
import static java.lang.Math.ceil;
import java.sql.Date;
import java.sql.SQLException;
import java.util.HashMap;
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
import it.socialdevelop.data.model.Skill;
import it.socialdevelop.data.model.SocialDevelopDataLayer;
import it.socialdevelop.data.model.Task;

/**
 *
 * @author Hello World Group
 */
public class Projects extends SocialDevelopBaseController {

    private void action_error(HttpServletRequest request, HttpServletResponse response) {
        if (request.getAttribute("exception") != null) {
            (new FailureResult(getServletContext())).activate((Exception) request.getAttribute("exception"), request, response);
        }
    }

    private void action_projects(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, TemplateManagerException, SQLException, NamingException, DataLayerException {
        HttpSession s = request.getSession(true);
        Map data = new HashMap();
        data.put("request", request);
        data.put("page_title", "Social Develop - Projects");

        data.put("page_subtitle", "Progetti");
        SocialDevelopDataLayer datalayer = (SocialDevelopDataLayer) request.getAttribute("datalayer");
        if (s.getAttribute("userid") != null && ((int) s.getAttribute("userid")) > 0) {
            data.put("auth_user", s.getAttribute("userid"));
            data.put("foto", s.getAttribute("foto"));
            data.put("fullname", s.getAttribute("fullname"));
            data.put("menu_active", "projects");
        }
        int npage = 1;
        int limit = 10;
        if (request.getParameter("n") == null) {
            npage = 1;
        } else {
            npage = Integer.parseInt(request.getParameter("n"));
        }
        int n = ((npage) - 1) * limit;
        
        
        double pagesize = ceil((double) (datalayer.getProjects().size()) / limit);
        data.put("page", pagesize);
        data.put("selected", request.getParameter("n"));
        List<Project> pro;
        if(request.getParameter("q") != null){
          String q = request.getParameter("q");
          pro = datalayer.getProjects(q);
          data.put("q", q);
        }else{
          pro = datalayer.getProjectsLimit(n);  
        }
        
        if (pro.size() != 0) {
            data.put("listaprogetti", pro);
            Date startdate[] = new Date[pro.size()];
            Developer coordinatore;
            int ncollaboratori[] = new int[pro.size()];
            int count = 0;
            int c = 0;
            int[] ntasks = new int[pro.size()];
            startdate[c] = null;
            for (Project progetto : pro) {
                coordinatore = datalayer.getDeveloper(progetto.getCoordinatorKey());
                List<Task> tasks = datalayer.getTasks(progetto.getKey());
                ncollaboratori[count] = 0;
                ntasks[count] = tasks.size();
                startdate[c] = datalayer.getDateOfTaskByProject(progetto.getKey());
                for (Task task : tasks) {
                    ncollaboratori[count] += task.getNumCollaborators();
                }
                progetto.setCoordinator(coordinatore);
                count++;
                c++;
            }
            data.put("start", startdate);
            data.put("ncollaboratori", ncollaboratori);
            data.put("ntasks", ntasks);
        } else {
            data.put("listaprogetti", pro);
        }
        List<Skill> skills = datalayer.getSkills();
        
        datalayer.destroy();
        TemplateResult res = new TemplateResult(getServletContext());
        res.activate("projects.ftl.html", data, response);
    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {
        try {
            action_projects(request, response);
        } catch (IOException ex) {
            request.setAttribute("exception", ex);
            action_error(request, response);
        } catch (DataLayerException ex) {
            request.setAttribute("exception", ex);
            action_error(request, response);
        } catch (SQLException ex) {
            request.setAttribute("exception", ex);
            action_error(request, response);
        } catch (NamingException ex) {
            request.setAttribute("exception", ex);
            action_error(request, response);
        } catch (TemplateManagerException ex) {
            request.setAttribute("excpetion", ex);
            action_error(request, response);
        }

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
