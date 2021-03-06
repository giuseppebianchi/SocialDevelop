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
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.time.Instant;
import java.util.Iterator;
import javax.servlet.ServletContext;
import org.apache.tomcat.util.codec.binary.Base64;

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
                
                    
                    
                    // this parses the json 
                    String JSONData = request.getParameter("data");
                    SocialDevelopDataLayer datalayer = (SocialDevelopDataLayer) request.getAttribute("datalayer");
                    JSONObject o = new JSONObject(JSONData);  
                    
                    int profile_id = o.getInt("profile_id");
                    int userid = (int) s.getAttribute("userid");
                    
                    if (profile_id == userid) {
                        
                    String profile_name = o.getString("profile_name");
                    String profile_email = o.getString("profile_email");
                    String profile_username = o.getString("profile_username");
                    String profile_photo = o.getString("profile_photo");
                    String profile_surname = o.getString("profile_surname");
                    String profile_biography = o.getString("profile_biography");
                    String profile_headline = o.getString("profile_headline");
                    String profile_resume = o.getString("profile_resume");
                    
                    //dichiariamo d inizialmente in caso venga caricata una nuova picture
                    DeveloperImpl d = new DeveloperImpl(datalayer);

                    //salviamo la nuova picture
                    String filename;
                    if ((profile_photo != null || profile_photo != "") && (profile_photo.split(";base64,").length == 2)) {
                        byte[] data = Base64.decodeBase64(profile_photo.split(",")[1]);
                        String picture_ext = profile_photo.split(";")[0].replace("data:image/", "");
                        File file = new File("").getCanonicalFile();
                        String encoded_filename = profile_name + profile_surname + profile_username + profile_email;
                        filename = "dev_" + encoded_filename.hashCode() + "_" + Instant.now().getEpochSecond() + "." + picture_ext;
                        ServletContext context = getServletContext();
                        String path = context.getInitParameter("uploaded-images.directory");
                        try (OutputStream stream = new FileOutputStream(path + filename)) {
                            stream.write(data);
                        }
                        d.setPicture(filename);
                        request.getSession().setAttribute("profile_picture", filename);
                    } else {
                        d.setPicture("blog-image-4.png");
                    }

                    //memorizziamo il progetto
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
