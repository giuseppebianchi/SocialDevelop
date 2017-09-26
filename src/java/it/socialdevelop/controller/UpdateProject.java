/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import it.socialdevelop.data.model.Project;
import it.socialdevelop.data.model.Skill;
import it.socialdevelop.data.model.SocialDevelopDataLayer;
import it.socialdevelop.data.model.Task;
import it.socialdevelop.data.model.Type;

/**
 *
 * @author Hello World Group
 */
public class UpdateProject extends SocialDevelopBaseController {
    
    private void action_error(HttpServletRequest request, HttpServletResponse response) {
        if (request.getAttribute("exception") != null) {
            (new FailureResult(getServletContext())).activate((Exception) request.getAttribute("exception"), request, response);
        }
    }
    
    
     private void action_updateproject(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, TemplateManagerException, SQLException, NamingException, DataLayerException {
        HttpSession s = request.getSession(true);
        if(s.getAttribute("userid") != null && ((int) s.getAttribute("userid"))>0) {
            request.setAttribute("logout", "Logout");
            request.setAttribute("page_title", "Update Project");
            request.setAttribute("page_subtitle", "Update your project infos");
            SocialDevelopDataLayer datalayer = (SocialDevelopDataLayer) request.getAttribute("datalayer");
            Admin admin = datalayer.getAdmin((int) s.getAttribute("userid"));
            if(admin!=null){
                request.setAttribute("admin", "admin");
            }
            //recupero skills che non hanno figli
            List<Type> types = datalayer.getTypes();
            if(types!=null){
                request.setAttribute("types", types);
            }
            List<Skill> skills = datalayer.getSkillsParentList();

            //ora recuperiamo per ognuna di esse le skills figlie
            if(skills!=null){
                for(Skill skill : skills){
                    List<Skill> child = datalayer.getChild(skill.getKey());
                    if(child!=null){
                        skill.setChild(child);
                    }
                }
            request.setAttribute("skills", skills);
            
            //recuperiamo i dati relativi al progetto da modificare
            int project_key = Integer.parseInt(request.getParameter("n"));
            request.setAttribute("project_key", project_key);
            Project project = datalayer.getProject(project_key);
            if(project!=null){
                request.setAttribute("project", project);
            }
            List<Task> tasks = datalayer.getTasks(project_key);
            List<Type> tasks_types = new ArrayList<>();
            ArrayList skills_level = new ArrayList();
            if(tasks!=null){
                for(Task t : tasks){
                    Type type = datalayer.getType(t.getType_key());
                    tasks_types.add(type);
                    Map<Skill,Integer> sl = datalayer.getSkillsByTask(t.getKey());
                    skills_level.add(sl);
                }
                request.setAttribute("tasks", tasks);
                request.setAttribute("skills_level", skills_level);
                request.setAttribute("tasks_types", tasks_types);
            }
            }
            datalayer.destroy();
            
            String act_url = request.getRequestURI();
            s.setAttribute("previous_url", act_url);
            TemplateResult res = new TemplateResult(getServletContext());
            res.activate("update_project.html",request, response);
        }else{
            response.sendRedirect("index");
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
            action_error(request, response);        }
    }
}
