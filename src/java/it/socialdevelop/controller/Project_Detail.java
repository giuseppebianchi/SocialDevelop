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
import java.util.Date;
import java.util.GregorianCalendar;
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
import it.socialdevelop.data.model.Message;
import it.socialdevelop.data.model.Project;
import it.socialdevelop.data.model.Skill;
import it.socialdevelop.data.model.SocialDevelopDataLayer;
import it.socialdevelop.data.model.Task;
import it.socialdevelop.data.model.Type;

/**
 *
 * @author Hello World Group
 */

public class Project_Detail extends SocialDevelopBaseController {
    
    private void action_error(HttpServletRequest request, HttpServletResponse response) {
        if (request.getAttribute("exception") != null) {
            (new FailureResult(getServletContext())).activate((Exception) request.getAttribute("exception"), request, response);
        }
    }
    
    private String getImg(HttpServletRequest request, HttpServletResponse response, Developer dev) throws IOException, SQLException, DataLayerException, NamingException {
        StreamResult result = new StreamResult(getServletContext());
        
         SocialDevelopDataLayer datalayer = (SocialDevelopDataLayer) request.getAttribute("datalayer");
         if(dev.getFoto() != 0){
            Files foto_profilo = datalayer.getFile(dev.getFoto());
            return "uploaded-images/"+foto_profilo.getLocalFile();
         }else{
            return "uploaded-images/foto_profilo_default.png";             
         }
        
    }
    
    private void action_project(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, TemplateManagerException, SQLException, NamingException, DataLayerException {    
                
                SocialDevelopDataLayer datalayer = (SocialDevelopDataLayer) request.getAttribute("datalayer");           
                HttpSession s = request.getSession(true);
                int key = Integer.parseInt(request.getParameter("n")); //project_key
                Project project = datalayer.getProject(key);
                int coordinator_key = project.getCoordinatorKey();
                if (s.getAttribute("userid") != null && ((int) s.getAttribute("userid"))>0){
                    request.setAttribute("logout", "Logout");
                    if(coordinator_key == (int) s.getAttribute("userid")){
                        request.setAttribute("userid", coordinator_key);
                    }
                    Admin admin = datalayer.getAdmin((int) s.getAttribute("userid"));
                    if(admin!=null){
                        request.setAttribute("admin", "admin");
                    }
                    
                }
                request.setAttribute("page_title", "Project" + " " + project.getName());
                request.setAttribute("page_subtitle", "Check project info");
                request.setAttribute("projectname", project.getName());
                request.setAttribute("projectdescr", project.getDescription());
                request.setAttribute("projectkey", key);
                List <Task> tasks = datalayer.getTasks(project.getKey()); //lista task del progetto
                List <Task> tasksEnded = new ArrayList();  //lista di quelli terminati
                List<Type> tasks_types = new ArrayList<>(); //lista dei tipi di ogni task
                int nProjectCollaborators = 0; //numero collaboratori totali task
                ArrayList skills = new ArrayList();
                request.setAttribute("tasks", tasks);
                boolean flag = false; //serve per controllare se l'utente loggato (se c'è) è tra i collaboratori del progetto
                for (Task task : tasks){
                    if(!task.isOpen()){
                        tasksEnded.add(task);
                    }
                    Type type = datalayer.getType(task.getType_key());
                    tasks_types.add(type);
                     if (s.getAttribute("userid") != null && ((int) s.getAttribute("userid"))>0) {
                        //se l'utente è loggato controlliamo se è un collaboratore del progetto.
                        //se lo è rendiamo visibili i messaggi privati e inoltre rendiamo visibile
                        //il form di inserimento del messaggio
                        request.setAttribute("isLogged", "isLogged");
                        int dev_key = (int) s.getAttribute("userid");
                        Map<Developer, Integer> collaborators = datalayer.getCollaboratorsByTask(task.getKey());
                        for(Map.Entry<Developer, Integer> m : collaborators.entrySet()){
                           if(m.getKey().getKey() == dev_key || dev_key == coordinator_key){
                               
                               flag = true;
                               request.setAttribute("userid", dev_key);
                           }
                            
                        }
                        if(dev_key == coordinator_key){
                                request.setAttribute("isCoordinator", 1);
                            }
                     }
                    nProjectCollaborators += task.getNumCollaborators();
                    GregorianCalendar start = task.getStartDate();
                    GregorianCalendar end = task.getEndDate();
                    Date startDate = new Date();
                    Date endDate = new Date();
                    if(startDate!=null){
                        startDate = start.getTime();
                    }
                    if(endDate!=null){
                        endDate = end.getTime();
                    }
                    if(startDate!=null && endDate!=null){
                        long startTime = startDate.getTime();
                        long endTime = endDate.getTime();
                        long diffTime = endTime - startTime;
                        long diffDays = diffTime / (1000 * 60 * 60 * 24);
                        request.setAttribute("daysleft",diffDays);
                    }
                    Map <Skill, Integer> skillsList = datalayer.getSkillsByTask(task.getKey());
                    skills.add(skillsList);
                    
                }
                request.setAttribute("nProjectCollaborators", nProjectCollaborators);
                request.setAttribute("skills", skills);
                request.setAttribute("tasks_types", tasks_types);
                double percProg = Math.round(((double)tasksEnded.size() / (double)tasks.size())*100) ;
                request.setAttribute("percProg", percProg);
                List <Message> messages = new ArrayList();
                if(flag){
                    messages = datalayer.getMessages(project.getKey());
                }
                else{
                    messages = datalayer.getPublicMessages(project.getKey());
                }
               
                request.setAttribute("messages", messages);
                List<String> foto_msg = new ArrayList();
                
                List<Developer> by = new ArrayList();
                for (Message message : messages){
                    Developer dev2 = message.getDeveloper();
                    String foto = getImg(request, response, dev2);
                    foto_msg.add(foto);
                    by.add(dev2);      
                }   
                request.setAttribute("by", by);
                request.setAttribute("foto_msg", foto_msg);
                Developer coordinator=datalayer.getDeveloper(project.getCoordinatorKey());
                request.setAttribute("coordinator", coordinator);
                
                String foto2 = getImg(request, response, coordinator);
                request.setAttribute("coordinatorpic", foto2);
                datalayer.destroy();
                String act_url = request.getRequestURI();
                s.setAttribute("previous_url", act_url);
                TemplateResult res = new TemplateResult(getServletContext());
                res.activate("project_detail.html",request, response);  
    
    }
    
    
    
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try{
            action_project(request,response);
        }
        catch (IOException ex) {
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


