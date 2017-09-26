/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.socialdevelop.controller;

import it.univaq.f4i.iw.framework.result.FailureResult;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import it.socialdevelop.data.model.SocialDevelopDataLayer;

/**
 *
 * @author Hello World Group
 */
public class addSkill extends SocialDevelopBaseController {
    
    private void action_error(HttpServletRequest request, HttpServletResponse response) {
        if (request.getAttribute("exception") != null) {
            (new FailureResult(getServletContext())).activate((Exception) request.getAttribute("exception"), request, response);
        }
    }
        
     private void add_skill(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, NamingException, NoSuchAlgorithmException, Exception {
        
        SocialDevelopDataLayer datalayer = (SocialDevelopDataLayer) request.getAttribute("datalayer");
       
        int developer_key = Integer.parseInt(request.getParameter("userid"));        
        String skill_level = request.getParameter("skill_level");
        
        int skill = Integer.parseInt(skill_level.split(":")[0]);
        int level = Integer.parseInt(skill_level.split(":")[1]);
        
        int ret = datalayer.storeSkillHasDeveloper(skill,developer_key,level);
        datalayer.destroy();
        response.setContentType("text/plain"); 
        response.setCharacterEncoding("UTF-8"); 
        PrintWriter out = response.getWriter();

        try {
            out.println(ret);
        } finally {
            out.close();
        }
    }
    
     
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {
        try {
            add_skill(request, response);
        } catch (SQLException ex) {
           request.setAttribute("exception", ex);
            action_error(request, response);  
        } catch (NamingException ex) {
           request.setAttribute("exception", ex);
            action_error(request, response);  
        } catch (Exception ex) { 
           request.setAttribute("exception", ex);
            action_error(request, response);  
        } 
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    
    public String getServletInfo() {
        return "Short description";
    }
    // </editor-fold>
}
