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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import it.socialdevelop.data.model.Admin;
import it.socialdevelop.data.model.Developer;
import it.socialdevelop.data.model.Files;
import it.socialdevelop.data.model.SocialDevelopDataLayer;

/**
 *
 * @author Hello World Group
 */
public class Profile extends SocialDevelopBaseController {
    
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
            request.setAttribute("foto_profilo", "theme/images/uploaded-images/" + foto_profilo.getLocalFile());
         }else{
            request.setAttribute("foto_profilo", "theme/images/uploaded-images/foto_profilo_default.png");             
         }
        
    }
    
    
    
    private void action_profile(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, TemplateManagerException, SQLException, NamingException, DataLayerException {
            HttpSession s = request.getSession(true);
            
            if (s.getAttribute("userid") != null && ((int) s.getAttribute("userid"))>0) {
                Map data = new HashMap();
                data.put("request", request);
                data.put("auth_user", s.getAttribute("userid"));
                data.put("page_title", "Social Develop - Profile");
                SocialDevelopDataLayer datalayer = (SocialDevelopDataLayer)request.getAttribute("datalayer");
                Developer dev = datalayer.getDeveloper((int) s.getAttribute("userid"));
                data.put("username", dev.getUsername());
                data.put("fullname", dev.getName()+" "+dev.getSurname());
                SimpleDateFormat fmt = new SimpleDateFormat("dd-MMM-yyyy");
                fmt.setCalendar(dev.getBirthDate());
                String dateFormatted = fmt.format(dev.getBirthDate().getTime());
                data.put("date", dateFormatted);
                data.put("bio", dev.getBiography());
                data.put("foto", dev.getPicture());
                data.put("resume", dev.getBiography());
                data.put("mail", dev.getMail());
                data.put("curriculum", dev.getCurriculumString());
                data.put("curriculum_pdf", dev.getCurriculumFile());
                datalayer.destroy();
                TemplateResult res = new TemplateResult(getServletContext());
                res.activate("profile.ftl.html",data, response);
            }else{
                 response.sendRedirect("home");
            }
           
    }
    
    
    
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException{
        
        try {
            action_profile(request, response);
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