package it.socialdevelop.controller;

import it.univaq.f4i.iw.framework.data.DataLayerException;
import it.univaq.f4i.iw.framework.result.FailureResult;
import it.univaq.f4i.iw.framework.result.TemplateManagerException;
import it.univaq.f4i.iw.framework.result.TemplateResult;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import it.socialdevelop.data.model.Admin;
import it.socialdevelop.data.model.Developer;
import it.socialdevelop.data.model.Message;
import it.socialdevelop.data.model.Project;
import it.socialdevelop.data.model.Skill;
import it.socialdevelop.data.model.SocialDevelopDataLayer;
import it.socialdevelop.data.model.Task;
import it.socialdevelop.data.model.Type;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

/**
 *
 * @author Hello World Group
 */
public class UpdateDevelopersProject extends SocialDevelopBaseController {

    private void action_error(HttpServletRequest request, HttpServletResponse response) {
        if (request.getAttribute("exception") != null) {
            (new FailureResult(getServletContext())).activate((Exception) request.getAttribute("exception"), request, response);
        }
    }

    private void action_updateproject(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, TemplateManagerException, SQLException, NamingException, DataLayerException {
        HttpSession s = request.getSession(true);
        Map data = new HashMap();
        data.put("request", request);
        
        if (s.getAttribute("userid") != null && ((int) s.getAttribute("userid")) > 0) {
            data.put("logout", "Logout");
            data.put("page_title", "Update Project");
            data.put("page_subtitle", "Update your project infos");
            data.put("auth_user", s.getAttribute("userid"));
                data.put("foto", s.getAttribute("foto"));
                data.put("fullname", s.getAttribute("fullname"));
            
            SocialDevelopDataLayer datalayer = (SocialDevelopDataLayer) request.getAttribute("datalayer");
            Admin admin = datalayer.getAdmin((int) s.getAttribute("userid"));
            if (admin != null) {
                data.put("admin", "admin");
            }
            

                //recuperiamo i dati relativi al progetto da modificare
                //int key = Integer.parseInt(request.getParameter("n")); //project_key
                String pathInfo = request.getPathInfo(); // /{value}/test
                String[] pathParts = pathInfo.split("/");
                int project_key = Integer.parseInt(pathParts[1]); // {value}
                data.put("project_key", project_key);
                Project project = datalayer.getProject(project_key);
                if (project != null) {
                    data.put("project", project);
                }
                List<Task> tasks = datalayer.getTasks(project_key);
                List<Type> tasks_types = new ArrayList<>();
                List<Map<Developer, Integer>> task_collaborators = new ArrayList<Map<Developer, Integer>>();
                if (tasks != null) {
                    for (Task t : tasks) {
                        Type type = datalayer.getType(t.getType_key());
                        tasks_types.add(type);
                        //ottengo i developers per ogni task del progetto
                        Map<Developer, Integer> tdevs = datalayer.getCollaboratorsByTask(t.getKey());
                        task_collaborators.add(tdevs);  
                    }
                    data.put("tasks", tasks);
                    data.put("task_collaborators", task_collaborators);
                    data.put("tasks_types", tasks_types);
                }
            datalayer.destroy();
            
            String act_url = request.getRequestURI();
            s.setAttribute("previous_url", act_url);
            TemplateResult res = new TemplateResult(getServletContext());
            
            if(request.getParameterMap().containsKey("updated_project") && (Integer.parseInt(request.getParameter("updated_project")) == 1)) { 
                data.put("updated_project", 1);
            }
            //res.activate("update_project.html", request, response);
            res.activate("project_developers_settings.ftl.html", data, response);
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
