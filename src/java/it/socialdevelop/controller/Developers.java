package it.socialdevelop.controller;

import it.univaq.f4i.iw.framework.data.DataLayerException;
import it.univaq.f4i.iw.framework.result.FailureResult;
import it.univaq.f4i.iw.framework.result.TemplateManagerException;
import it.univaq.f4i.iw.framework.result.TemplateResult;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import it.socialdevelop.data.model.Admin;
import it.socialdevelop.data.model.Developer;
import it.socialdevelop.data.model.Files;
import it.socialdevelop.data.model.Skill;
import it.socialdevelop.data.model.SocialDevelopDataLayer;
import it.socialdevelop.data.model.Task;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * @author Hello World Group
 */
public class Developers extends SocialDevelopBaseController {

    private void action_error(HttpServletRequest request, HttpServletResponse response) {
        if (request.getAttribute("exception") != null) {
            (new FailureResult(getServletContext())).activate((Exception) request.getAttribute("exception"), request, response);
        }
    }

    private void action_developers(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, TemplateManagerException, SQLException, NamingException, DataLayerException {
        HttpSession s = request.getSession(true);
        request.setAttribute("request", request);
        request.setAttribute("page_title", "Search Developers");
        request.setAttribute("page_subtitle", "who are you looking for?");
        SocialDevelopDataLayer datalayer = (SocialDevelopDataLayer) request.getAttribute("datalayer");
        if (s.getAttribute("userid") != null && ((int) s.getAttribute("userid")) > 0) {
            request.setAttribute("logout", "Logout");
            request.setAttribute("auth_user", s.getAttribute("userid"));
            request.setAttribute("foto", s.getAttribute("foto"));
            request.setAttribute("fullname", s.getAttribute("fullname"));
            Admin admin = datalayer.getAdmin((int) s.getAttribute("userid"));
            if (admin != null) {
                request.setAttribute("admin", "admin");
            }
        }

        
        List<Developer> devs = datalayer.getDevelopers();
        request.setAttribute("devs", devs);
        int[] votes = new int[devs.size()];
        
        for (int i=0; i < devs.size(); i++) {
            int j = 0;
            Map<Task, Integer> tasks = datalayer.getTasksByDeveloper(devs.get(i).getKey());
            
            for (Entry<Task,Integer> t : tasks.entrySet()){
                Task task = t.getKey();    
                if (task.isCompleted()) {
                    votes[i] += t.getValue();
                    j++;
                }
            }
            if(j != 0){
               votes[i] = (int) votes[i]/j;   
            }
                      
        }
        
        
        List<Skill> skills = datalayer.getSkills();
        request.setAttribute("skills", skills);
        
        request.setAttribute("votes", votes);
        
        datalayer.destroy();
        TemplateResult res = new TemplateResult(getServletContext());
        res.activate("developers.ftl.html", request, response);
    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {
        try {
            action_developers(request, response);
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
