package it.socialdevelop.controller;

import it.socialdevelop.data.impl.DeveloperImpl;
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
import it.socialdevelop.data.model.Developer;
import it.socialdevelop.data.model.SocialDevelopDataLayer;
import java.util.Iterator;

/**
 *
 * @author Hello World Group
 */
public class CompleteRegistrationSubmit  extends SocialDevelopBaseController {

    private void action_error(HttpServletRequest request, HttpServletResponse response) {
        if (request.getAttribute("exception") != null) {
            (new FailureResult(getServletContext())).activate((Exception) request.getAttribute("exception"), request, response);
        }
    }

    private void action_complete_registration(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, TemplateManagerException, SQLException, NamingException, DataLayerException {
        //recuperare coordinatore dalla sessione!
        HttpSession s = request.getSession(true);
         
        if (s.getAttribute("userid") != null && ((int) s.getAttribute("userid")) > 0) {
            String u = (String) s.getAttribute("previous_url");
            if (/*s.getAttribute("previous_url") != null && u.equals("/SocialDevelop/signup/signup_submit")*/ true) {
                
                    SocialDevelopDataLayer datalayer = (SocialDevelopDataLayer) request.getAttribute("datalayer");
                    
                    // this parses the json 
                    String JSONData = request.getParameter("data");
                    JSONObject o = new JSONObject(JSONData);  
                    
                    int profile_id = o.getInt("profile_id");
                    int userid = (int) s.getAttribute("userid");
                    
                    if (profile_id == userid) {
                        
                    String profile_name = o.getString("profile_name");
                    String profile_email = o.getString("profile_email");
                    String profile_username = o.getString("profile_username");
                    String profile_surname = o.getString("profile_surname");
                    String profile_biography = o.getString("profile_biography");
                    String profile_headline = o.getString("profile_headline");
                    String profile_resume = o.getString("profile_resume");
                    
                    //memorizziamo il progetto
                    Developer d = new DeveloperImpl(datalayer);
                    d.setKey(profile_id);
                    d.setName(profile_name);
                    d.setSurname(profile_surname);
                    d.setUsername(profile_username);
                    d.setMail(profile_email);
                    d.setHeadline(profile_headline);
                    d.setBiography(profile_biography);
                    d.setResume(profile_resume);
                    datalayer.storeDeveloper(d);
                    //ora recuperiamo le info sui task e le memorizziamo
                    
                    try {
                        
                        JSONObject skills = o.getJSONObject("skills");
                        Iterator<String> temp = skills.keys();
                        while (temp.hasNext()) {
                            String key = temp.next();
                            JSONObject skill = skills.getJSONObject(key);
                            int skill_key = skill.getInt("key");
                                int level = skill.getInt("level");
                            datalayer.storeSkillHasDeveloper(skill_key, profile_id, level);
                        }
                            
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    
                    datalayer.destroy();
                    String act_url = request.getRequestURI();
                    s.setAttribute("previous_url", act_url);
                    response.sendRedirect("/SocialDevelop/developers/" + profile_id);
                    
                } else {
                    response.sendRedirect("/SocialDevelop");
                }
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
            action_complete_registration(request, response);
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
