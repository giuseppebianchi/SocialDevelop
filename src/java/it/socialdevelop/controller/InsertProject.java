package it.socialdevelop.controller;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import it.socialdevelop.data.model.SocialDevelopDataLayer;
import java.util.Iterator;

/**
 *
 * @author Hello World Group
 */
public class InsertProject extends SocialDevelopBaseController {

    private void action_error(HttpServletRequest request, HttpServletResponse response) {
        if (request.getAttribute("exception") != null) {
            (new FailureResult(getServletContext())).activate((Exception) request.getAttribute("exception"), request, response);
        }
    }

    private void action_insertproject(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, TemplateManagerException, SQLException, NamingException, DataLayerException {
        //recuperare coordinatore dalla sessione!
        HttpSession s = request.getSession(true);
         
        if (s.getAttribute("userid") != null && ((int) s.getAttribute("userid")) > 0) {
            String u = (String) s.getAttribute("previous_url");
            if (s.getAttribute("previous_url") != null && u.equals("/socialdevelop/projects/new")) {
                if (true) {
                    SocialDevelopDataLayer datalayer = (SocialDevelopDataLayer) request.getAttribute("datalayer");
                    
                    // this parses the json
                    String JSONData = request.getParameter("data");
                    JSONObject o = new JSONObject(JSONData);  
                   
                    String project_name = o.getString("project_name");
                    String project_category = o.getString("project_category");
                    String project_location = o.getString("project_location");
                    String project_company = o.getString("project_company");
                    String project_description = o.getString("project_description");
                    int userid = (int) s.getAttribute("userid");
                    //memorizziamo il progetto
                    ProjectImpl p = new ProjectImpl(datalayer);
                    p.setCoordinatorKey(userid);
                    p.setName(project_name);
                    p.setCategory(project_category);
                    p.setLocation(project_location);
                    p.setCompany(project_company);
                    p.setDescription(project_description);
                    int project_key = datalayer.storeProject(p);
                    //ora recuperiamo le info sui task e le memorizziamo
                    
                    try {
                        JSONObject tasks = o.getJSONObject("tasks");
                        Iterator<String> temp = tasks.keys();
                        while (temp.hasNext()) {
                            String key = temp.next();
                            JSONObject task = tasks.getJSONObject(key);
                            
                            TaskImpl t = new TaskImpl(datalayer);
                            t.setName(task.getString("name"));
                            t.setType_key(task.getInt("typev"));
                            t.setProjectKey(project_key);
                            
                            String start = task.getString("tstart");
                            String[] parts1 = start.split("-");
                            GregorianCalendar gc = new GregorianCalendar();
                            gc.setLenient(false);
                            gc.set(GregorianCalendar.YEAR, Integer.valueOf(parts1[0]));
                            gc.set(GregorianCalendar.MONTH, Integer.valueOf(parts1[1])-1);
                            gc.set(GregorianCalendar.DATE, Integer.valueOf(parts1[2]));
                            t.setStartDate(gc);
 
                            String end = task.getString("tend");
                            String[] parts2 = end.split("-");
                            GregorianCalendar gc1 = new GregorianCalendar();
                            gc1.setLenient(false);
                            gc1.set(GregorianCalendar.YEAR, Integer.valueOf(parts2[0]));
                            gc1.set(GregorianCalendar.MONTH, Integer.valueOf(parts2[1])-1);
                            gc1.set(GregorianCalendar.DATE, Integer.valueOf(parts2[2]));
                            t.setEndDate(gc1);
                            
                            t.setDescription(task.getString("dec"));
                            t.setNumCollaborators(task.getInt("devs"));
                            
                            int task_key = datalayer.storeTask(t);
                            
                            JSONArray skills = task.getJSONArray("skills");
                            
                            for (int i=0; i < skills.length(); i++) {
                                JSONObject sk = skills.getJSONObject(i);
                                int skill_key = sk.getInt("key");
                                int level = sk.getInt("level");
                                datalayer.storeTaskHasSkill(task_key, skill_key, level);
                            }
                            
                            
                            }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    
                    datalayer.destroy();
                    String act_url = request.getRequestURI();
                    s.setAttribute("previous_url", act_url);
                    response.sendRedirect("/socialdevelop/projects/suggestions/" + project_key + "?created_project=1");
                    
                } else {
                    response.sendRedirect("/socialdevelop");
                }
            } else {
                response.sendRedirect("/socialdevelop");
            }
        } else {
            response.sendRedirect("/socialdevelop");
        }
    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {
        try {
            action_insertproject(request, response);
        } catch (IOException ex) {
            request.setAttribute("exception", ex);
            action_error(request, response);
        } catch (DataLayerException ex) {
            request.setAttribute("exception", ex);
            action_error(request, response);
        } catch (SQLException ex) {
            request.setAttribute("exception", ex);
            action_error(request, response);
        } catch (NamingException ex) {
            request.setAttribute("exception", ex);
            action_error(request, response);
        } catch (TemplateManagerException ex) {
            request.setAttribute("excpetion", ex);
            action_error(request, response);
        }
    }
}
