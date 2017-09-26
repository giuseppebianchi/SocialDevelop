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
public class DeveloperCollaborations extends SocialDevelopBaseController {
    
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
    
    
    
    private void action_devcollaborations(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, TemplateManagerException, SQLException, NamingException, DataLayerException {
            SocialDevelopDataLayer datalayer = (SocialDevelopDataLayer)request.getAttribute("datalayer");
            int dev_key = Integer.parseInt(request.getParameter("n"));
            Developer dev = datalayer.getDeveloper(dev_key);
            if(dev!=null){
                request.setAttribute("username", dev.getUsername());
                request.setAttribute("fullname", dev.getName()+" "+dev.getSurname());
                long currentTime = System.currentTimeMillis();
                Calendar now = Calendar.getInstance();
                now.setTimeInMillis(currentTime);
                //Get difference between years
                request.setAttribute("age", now.get(Calendar.YEAR) - dev.getBirthDate().get(Calendar.YEAR));
                request.setAttribute("bio", dev.getBiography());
                request.setAttribute("mail", dev.getMail());
                request.setAttribute("id", dev_key);
                getImg(request, response, dev);
                
                Map<Task, Integer> tasks = datalayer.getCurrentTasksByDeveloper(dev.getKey());
                List<Developer> coordinators = new ArrayList();
                //recupero progetto e coordinatore
                for(Map.Entry<Task, Integer> entry : tasks.entrySet()){
                    //Type type = datalayer.getType(entry.getKey().getType_key());
                    //entry.getKey().setType(type);
                    Project p = datalayer.getProjectByTask(entry.getKey().getKey());
                    Developer c = datalayer.getDeveloper(p.getCoordinatorKey());
                    entry.getKey().setProject(p);
                    coordinators.add(c);   
                }
                
                Map<Task, Integer> tasksEnded = datalayer.getEndedTasksByDeveloper(dev.getKey());
                List<Developer> coordinatorsEnded = new ArrayList();
                //recupero progetto e coordinatore
                for(Map.Entry<Task, Integer> entryEnded : tasksEnded.entrySet()){
                   
                    Project pEnded = datalayer.getProjectByTask(entryEnded.getKey().getKey());
                    Developer cEnded = datalayer.getDeveloper(pEnded.getCoordinatorKey());
                    entryEnded.getKey().setProject(pEnded);
                    coordinatorsEnded.add(cEnded);   
                }
               
                HttpSession s = request.getSession(true);
                 if (s.getAttribute("userid") != null && ((int) s.getAttribute("userid"))>0) {  
                    request.setAttribute("logout", "Logout");
                    Admin admin = datalayer.getAdmin((int) s.getAttribute("userid"));
                    if(admin!=null){
                        request.setAttribute("admin", "admin");
                    }
                }else{
                    request.setAttribute("MyProfile", "hidden");
                }
                datalayer.destroy();
                request.setAttribute("tasksList", tasks);
                request.setAttribute("coordinators", coordinators);
                request.setAttribute("tasksListEnded", tasksEnded);
                request.setAttribute("coordinatorsEnded", coordinatorsEnded);
                request.setAttribute("page_title", "Collaborations");
                request.setAttribute("page_subtitle", dev.getUsername());
                request.setAttribute("notmy", "notmy");
                TemplateResult res = new TemplateResult(getServletContext());
                res.activate("my_collaborations.html",request, response);
                
            }else{
                 response.sendRedirect("index");
            }
            
           
    }
    
    
    
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException{
        
        try {
            action_devcollaborations(request, response);
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

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
   
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}