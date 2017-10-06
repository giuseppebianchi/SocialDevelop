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
import it.socialdevelop.data.model.Project;
import it.socialdevelop.data.model.SocialDevelopDataLayer;
import java.util.Iterator;

/**
 *
 * @author Hello World Group
 */
public class InsertTask extends SocialDevelopBaseController {

    private void action_error(HttpServletRequest request, HttpServletResponse response) {
        if (request.getAttribute("exception") != null) {
            (new FailureResult(getServletContext())).activate((Exception) request.getAttribute("exception"), request, response);
        }
    }

    private void action_updatetasksave(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, TemplateManagerException, SQLException, NamingException, DataLayerException {
        //recuperare coordinatore dalla sessione!
        HttpSession s = request.getSession(true);
         
        if (s.getAttribute("userid") != null && ((int) s.getAttribute("userid")) > 0) {
                //controllo se chi sta modificando il progetto è veramente il propretario
            int userid = (int) s.getAttribute("userid");
             // this parses the json
            String JSONData = request.getParameter("data");
            JSONObject o = new JSONObject(JSONData);
            int project_key = o.getInt("task_project_key");
            SocialDevelopDataLayer datalayer = (SocialDevelopDataLayer) request.getAttribute("datalayer");
            Project project = datalayer.getProject(project_key);
            int coordinator_key = project.getCoordinatorKey();
            
            if (userid == coordinator_key) {  

                    String task_name = o.getString("task_name");
                    int task_type = o.getInt("task_type");
                    int task_developers = o.getInt("task_developers");
                    int task_open = o.getInt("task_open");
                    String task_start = o.getString("task_start");
                    String task_end = o.getString("task_end");
                    String task_description = o.getString("task_description");
                    //memorizziamo il task
                    TaskImpl t = new TaskImpl(datalayer);

                    t.setName(task_name);
                    t.setType_key(task_type);
                    
                    if(task_open == 1){
                        t.setOpen(true);
                    }else{
                        t.setOpen(false);
                    }
                    
                    t.setNumCollaborators(task_developers);
                    t.setProjectKey(project_key);
                    
                    String start = task_start;
                    String[] parts1 = start.split("-");
                    GregorianCalendar gc = new GregorianCalendar();
                    gc.setLenient(false);
                    gc.set(GregorianCalendar.YEAR, Integer.valueOf(parts1[0]));
                    gc.set(GregorianCalendar.MONTH, Integer.valueOf(parts1[1])-1);
                    gc.set(GregorianCalendar.DATE, Integer.valueOf(parts1[2]));
                    t.setStartDate(gc);

                    String end = task_end;
                    String[] parts2 = end.split("-");
                    GregorianCalendar gc1 = new GregorianCalendar();
                    gc1.setLenient(false);
                    gc1.set(GregorianCalendar.YEAR, Integer.valueOf(parts2[0]));
                    gc1.set(GregorianCalendar.MONTH, Integer.valueOf(parts2[1])-1);
                    gc1.set(GregorianCalendar.DATE, Integer.valueOf(parts2[2]));
                    t.setEndDate(gc1);
                    
                    t.setDescription(task_description);
                    
                    int task_key = datalayer.storeTask(t);
                    //ora recuperiamo le info sui task e le memorizziamo
                    
                    try{
                        JSONObject skills = o.getJSONObject("skills");
                        Iterator<String> temp = skills.keys();
                        while (temp.hasNext()) {
                            String sk_key = temp.next();
                                JSONObject sk = skills.getJSONObject(sk_key);
                                int skill_key = sk.getInt("key");
                                int level = sk.getInt("level");
                                datalayer.storeTaskHasSkill(task_key, skill_key, level);
                            }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    
                    datalayer.destroy();
                    String act_url = request.getRequestURI();
                    s.setAttribute("previous_url", act_url);
                    response.sendRedirect("/SocialDevelop/projects/tasks/settings/" + task_key + "?created_task=1");
                    
            } else {
                response.sendRedirect("/SocialDevelop");
            }
        } else {
            response.sendRedirect("/SocialDevelop");
        }
    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {
        try {
            action_updatetasksave(request, response);
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
