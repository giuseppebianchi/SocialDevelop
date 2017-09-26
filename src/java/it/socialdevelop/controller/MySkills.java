/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.socialdevelop.controller;

import it.univaq.f4i.iw.framework.data.DataLayerException;
import it.univaq.f4i.iw.framework.result.FailureResult;
import it.univaq.f4i.iw.framework.result.StreamResult;
import it.univaq.f4i.iw.framework.result.TemplateManagerException;
import it.univaq.f4i.iw.framework.result.TemplateResult;
import java.io.IOException;
import java.sql.SQLException;
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
import it.socialdevelop.data.model.Skill;
import it.socialdevelop.data.model.SocialDevelopDataLayer;

/**
 *
 * @author Hello World Group
 */
public class MySkills extends SocialDevelopBaseController {
    
    private void action_error(HttpServletRequest request, HttpServletResponse response) {
        if (request.getAttribute("exception") != null) {
            (new FailureResult(getServletContext())).activate((Exception) request.getAttribute("exception"), request, response);
        }
    }
    
    
    private void getImg(HttpServletRequest request, HttpServletResponse response, Developer dev) throws IOException, SQLException, DataLayerException, NamingException {
        StreamResult result = new StreamResult(getServletContext());
        
         SocialDevelopDataLayer datalayer = (SocialDevelopDataLayer) request.getAttribute("datalayer");
         if(dev.getFoto() != 0){
            Files foto_profilo = datalayer.getFile(dev.getFoto());
            request.setAttribute("foto_profilo", "uploaded-images/" + foto_profilo.getLocalFile());
         }else{
            request.setAttribute("foto_profilo", "uploaded-images/foto_profilo_default.png");             
         }
        
    }
    
    
    
    private void action_myskills(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, TemplateManagerException, SQLException, NamingException, DataLayerException {
            HttpSession s = request.getSession(true);
            request.setAttribute("page_title", "My Skills");
            request.setAttribute("page_subtitle", "manage your skills");
            if (s.getAttribute("userid") != null && ((int) s.getAttribute("userid"))>0) {
                SocialDevelopDataLayer datalayer = (SocialDevelopDataLayer)request.getAttribute("datalayer");
                Admin admin = datalayer.getAdmin((int) s.getAttribute("userid"));
                if(admin!=null){
                    request.setAttribute("admin", "admin");
                }
                Developer dev = datalayer.getDeveloper((int) s.getAttribute("userid"));
                request.setAttribute("username", dev.getUsername());
                request.setAttribute("fullname", dev.getName()+" "+dev.getSurname());
                long currentTime = System.currentTimeMillis();
                Calendar now = Calendar.getInstance();
                now.setTimeInMillis(currentTime);
                 //Get difference between years
                request.setAttribute("age", now.get(Calendar.YEAR) - dev.getBirthDate().get(Calendar.YEAR));
                request.setAttribute("mail", dev.getMail());
                request.setAttribute("userid", dev.getKey());
                request.setAttribute("logout", "Logout");
                getImg(request, response, dev);
                
                 List<Skill> skills = datalayer.getSkillsParentList();
                //ora recuperiamo per ognuna di esse le skills figlie
                if(skills!=null){
                    for(Skill skill : skills){
                        List<Skill> child = datalayer.getChild(skill.getKey());
                        if(child!=null){
                            skill.setChild(child);
                        }
                    }
                }
                 request.setAttribute("skills", skills);
                Map<Skill, Integer> skills_level = datalayer.getSkillsByDeveloper((int) s.getAttribute("userid"));
                request.setAttribute("skills_level", skills_level);
                String skill_input = "";
                 for(Map.Entry<Skill, Integer> sl : skills_level.entrySet()){
                    skill_input = skill_input+String.valueOf(sl.getKey().getKey())+":"+String.valueOf(sl.getValue())+",";                       
                }
                datalayer.destroy();
                request.setAttribute("skill-input", skill_input);
                TemplateResult res = new TemplateResult(getServletContext());
                res.activate("manage_skills.html",request, response);
            }else{
                 response.sendRedirect("index");
            }
           
    }
    
    
    
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException{
        
        try {
            action_myskills(request, response);
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