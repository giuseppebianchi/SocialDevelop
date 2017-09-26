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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import it.socialdevelop.data.model.Admin;
import it.socialdevelop.data.model.Skill;
import it.socialdevelop.data.model.SocialDevelopDataLayer;
import it.socialdevelop.data.model.Type;

/**
 *
 * @author Hello World Group
 */
public class CreateProject extends SocialDevelopBaseController {
    
    private void action_error(HttpServletRequest request, HttpServletResponse response) {
        if (request.getAttribute("exception") != null) {
            (new FailureResult(getServletContext())).activate((Exception) request.getAttribute("exception"), request, response);
        }
    }
    
    
    private void action_createproject(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, TemplateManagerException, SQLException, NamingException, DataLayerException {
        HttpSession s = request.getSession(true);
        
        
        if(s.getAttribute("userid") != null && ((int) s.getAttribute("userid"))>0) {
            request.setAttribute("logout", "Logout");
            request.setAttribute("page_title", "New Project");
            request.setAttribute("page_subtitle", "realize your ideas!");
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
            }
            datalayer.destroy();
            String act_url = request.getRequestURI();
            s.setAttribute("previous_url", act_url);
            TemplateResult res = new TemplateResult(getServletContext());
            res.activate("create_project.html",request, response);
        }else{
            response.sendRedirect("index");
        }
    }
    
    
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {
        try {
            action_createproject(request, response);
        }  catch (IOException ex) {
            request.setAttribute("exception", ex);
            action_error(request, response);
        } catch (TemplateManagerException ex) {
            request.setAttribute("exception", ex);
            action_error(request, response);
        } catch (DataLayerException ex) {
            request.setAttribute("exception", ex);
            action_error(request, response);
        } catch (SQLException ex) {
            request.setAttribute("exception", ex);
            action_error(request, response);
        } catch (NamingException ex) {
            Logger.getLogger(CreateProject.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
