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
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
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
public class SearchProject extends SocialDevelopBaseController {
     
     private void action_error(HttpServletRequest request, HttpServletResponse response) {
        if (request.getAttribute("exception") != null) {
            (new FailureResult(getServletContext())).activate((Exception) request.getAttribute("exception"), request, response);
        }
    }
    
     private void action_searchProject(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, TemplateManagerException, SQLException, NamingException, DataLayerException {
        HttpSession s = request.getSession(true);
        request.setAttribute("page_title", "Search Projects");
        request.setAttribute("page_subtitle", "What project are you looking for?");
        SocialDevelopDataLayer datalayer = (SocialDevelopDataLayer) request.getAttribute("datalayer");
        if (s.getAttribute("userid") != null && ((int) s.getAttribute("userid"))>0) {
                request.setAttribute("logout", "Logout");
                Admin admin = datalayer.getAdmin((int) s.getAttribute("userid"));
                if(admin!=null){
                    request.setAttribute("admin", "admin");
                }
            }
        String keyWord = request.getParameter("keyWord");
        
        List<Project> pro = datalayer.getProjects(keyWord);
         if (pro.size()!=0) {
            request.setAttribute("listaprogetti", pro);
            Files foto = null ;
            Date startdate[] = new Date[pro.size()];
            Developer coordinatore ;
            int ncollaboratori[] = new int[pro.size()];
            String fotos[] = new String[pro.size()];
            int count = 0;
            int c = 0;
            startdate[c] = null;
            for(Project progetto : pro){
                coordinatore=datalayer.getDeveloper(progetto.getCoordinatorKey());
                List <Task> tasks = datalayer.getTasks(progetto.getKey());
                ncollaboratori[count] = 0;
                startdate[c] = datalayer.getDateOfTaskByProject(progetto.getKey());
                for(Task task : tasks){
                    ncollaboratori[count]+=task.getNumCollaborators();
                }
                progetto.setCoordinator(coordinatore);
                int foto_key=coordinatore.getFoto();
                if(foto_key != 0){
                    foto = datalayer.getFile(foto_key);
                    fotos[count] = "uploaded-images/" + foto.getLocalFile();
                }else{
                    fotos[count] = "uploaded-images/foto_profilo_default.png";
                }
                count ++;
                c++;
            }
            datalayer.destroy();
            request.setAttribute("inizioprogetto", startdate);
            request.setAttribute("ncollaboratori", ncollaboratori);
            request.setAttribute("fotoCoordinatore", fotos); 
         }
         else{
             request.setAttribute("listaprogetti", pro);
             request.setAttribute("nontrovato","There are no projects with these parameters in the system..");
         }
        TemplateResult res = new TemplateResult(getServletContext());
        res.activate("project_list.html",request, response);
    }
    
    
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {
        try {
            action_searchProject(request, response);
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
