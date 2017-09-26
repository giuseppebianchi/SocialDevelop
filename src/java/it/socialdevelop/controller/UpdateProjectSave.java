/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.socialdevelop.controller;

import it.univaq.f4i.iw.framework.data.DataLayerException;
import it.univaq.f4i.iw.framework.result.FailureResult;
import it.univaq.f4i.iw.framework.result.TemplateManagerException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.GregorianCalendar;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import it.socialdevelop.data.impl.ProjectImpl;
import it.socialdevelop.data.impl.TaskImpl;
import it.socialdevelop.data.model.Admin;
import it.socialdevelop.data.model.SocialDevelopDataLayer;
import it.socialdevelop.data.model.Task;

/**
 *
 * @author Hello World Group
 */
public class UpdateProjectSave extends SocialDevelopBaseController {
    
    private void action_error(HttpServletRequest request, HttpServletResponse response) {
        if (request.getAttribute("exception") != null) {
            (new FailureResult(getServletContext())).activate((Exception) request.getAttribute("exception"), request, response);
        }
    }
    
     private void action_updateproject(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, TemplateManagerException, SQLException, NamingException, DataLayerException {
        //recuperare coordinatore dalla sessione!
        HttpSession s = request.getSession(true);   
        if (s.getAttribute("userid") != null && ((int) s.getAttribute("userid"))>0) {
                if(!request.getParameter("tasks").equals("")){
                SocialDevelopDataLayer datalayer = (SocialDevelopDataLayer) request.getAttribute("datalayer");
                
                String project_name = request.getParameter("project_name");
                String project_descr = request.getParameter("project_descr");
                int userid = (int) s.getAttribute("userid");
                //memorizziamo il progetto
                ProjectImpl p = new ProjectImpl(datalayer);
                p.setCoordinatorKey(userid);
                p.setName(project_name);
                p.setDescription(project_descr);
                String pk = request.getParameter("n");
                p.setKey(Integer.parseInt(pk));
                s.removeAttribute("projectKey");
                String tasks = request.getParameter("tasks");
                
                int project_key = datalayer.storeProject(p);
                //ora recuperiamo le info sui task e le memorizziamo
                
                String tasks_keys = request.getParameter("tasks_keys");
                String [] task_key = tasks_keys.split(";");
                String [] task = tasks.split("@");
                //datalayer.deleteTasksFromProject(project_key);
                int i = 0;
                for(String t : task){
                    String [] thistask = t.split("#");
                    TaskImpl current = new TaskImpl(datalayer);
                    current.setName(thistask[0]);
                    //int i;
                    //for(i=0;i<task_key.length;i++){
                        /*if(thistask[0].equals(task_key[i].split(",")[0])){
                            current.setKey(Integer.parseInt(task_key[i].split(",")[1]));
                        }*/
                        if(i<task_key.length){
                        Task temp = datalayer.getTask(Integer.parseInt(task_key[i].split(",")[1]));
                        if(temp!=null){
                            current.setKey(Integer.parseInt(task_key[i].split(",")[1]));
                        //}
                        
                    }}
                        i++;
                    
                    current.setProjectKey(project_key);
                    String start = thistask[1];
                    GregorianCalendar gc = new GregorianCalendar();
                    gc.setLenient(false);
                    gc.set(GregorianCalendar.YEAR, Integer.valueOf(start.split("/")[2]));
                    gc.set(GregorianCalendar.MONTH, Integer.valueOf(start.split("/")[1])-1); 
                    gc.set(GregorianCalendar.DATE, Integer.valueOf(start.split("/")[0]));
                    current.setStartDate(gc);

                    String end = thistask[2];
                    GregorianCalendar gc1 = new GregorianCalendar();
                    gc.setLenient(false);
                    gc.set(GregorianCalendar.YEAR, Integer.valueOf(end.split("/")[2]));
                    gc.set(GregorianCalendar.MONTH, Integer.valueOf(end.split("/")[1])-1);
                    gc.set(GregorianCalendar.DATE, Integer.valueOf(end.split("/")[0]));
                    current.setEndDate(gc1);

                    current.setDescription(thistask[3]);
                    current.setNumCollaborators(Integer.parseInt(thistask[4]));
                    current.setType_key(datalayer.getTypeByName(thistask[6]));
                    if(thistask[7].equals("Open")){
                        current.setOpen(true);
                    }else{
                        current.setOpen(false);
                    }
                    
                    int task_key2 = datalayer.storeTask(current);
                    datalayer.deleteSkillsFromTask(task_key2);
                    String [] skills = thistask[5].split(";");
                    for(String skl : skills){
                        String [] split = skl.split("\\(");
                        String n = split[0].trim();
                        int l = Integer.parseInt(split[1].split("\\)")[0]);
                        int skill_key = datalayer.getSkillByName(n);
                        datalayer.storeTaskHasSkill(task_key2, skill_key, l);
                    }
                }
                datalayer.destroy();
                response.sendRedirect("MyProjects");
                }else{
                    response.sendRedirect("UpdateProject?n="+request.getParameter("n"));
                }
            }else{
                response.sendRedirect("MyProjects");
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
