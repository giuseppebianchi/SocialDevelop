package it.socialdevelop.controller;

import it.socialdevelop.data.impl.DeveloperImpl;
import it.socialdevelop.data.impl.TaskImpl;
import it.univaq.f4i.iw.framework.data.DataLayerException;
import it.univaq.f4i.iw.framework.result.FailureResult;
import it.univaq.f4i.iw.framework.result.TemplateManagerException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.servlet.http.Part;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import it.socialdevelop.data.model.Admin;
import it.socialdevelop.data.model.Developer;
import it.socialdevelop.data.model.Project;
import it.socialdevelop.data.model.SocialDevelopDataLayer;
import it.univaq.f4i.iw.framework.security.SecurityLayer;
import java.time.Instant;
import java.util.GregorianCalendar;
import java.util.Iterator;
import org.apache.tomcat.util.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Hello World Group
 */
public class UpdateProfileSubmit extends SocialDevelopBaseController {

    private void action_error(HttpServletRequest request, HttpServletResponse response) {
        if (request.getAttribute("exception") != null) {
            (new FailureResult(getServletContext())).activate((Exception) request.getAttribute("exception"), request, response);
        }
    }

    private void conferma_upd(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, NamingException, NoSuchAlgorithmException, Exception {
        HttpSession s = request.getSession(true);
        String u = (String) s.getAttribute("previous_url");
        if (s.getAttribute("userid") != null && ((int) s.getAttribute("userid")) > 0) {
            if (s.getAttribute("previous_url") != null && (u.equals("/SocialDevelop/settings/profile") || u.equals("/SocialDevelop/settings/profile?updated_profile=1"))) {

                SocialDevelopDataLayer datalayer = (SocialDevelopDataLayer) request.getAttribute("datalayer");
                Developer dev = datalayer.getDeveloper((Integer) s.getAttribute("userid"));
                  //controllo se chi sta modificando il progetto è veramente il propretario
                int userid = (int) s.getAttribute("userid");
                // this parses the json
                String JSONData = request.getParameter("data");
                JSONObject o = new JSONObject(JSONData);
                int profile_id = o.getInt("profile_id");
                if(userid != profile_id){
                    response.sendRedirect("/SocialDevelop");
                }else{
                        String profile_username = o.getString("profile_username");
                        String profile_photo = o.getString("profile_photo");
                        String profile_name = o.getString("profile_name");
                        String profile_surname = o.getString("profile_surname");
                        String profile_headline = o.getString("profile_headline");
                        String profile_biography = o.getString("profile_biography");
                        String profile_resume = o.getString("profile_resume");
                        String profile_email = o.getString("profile_email");
                        
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
                            try (OutputStream stream = new FileOutputStream(file.getParent() + "/webapps/SocialDevelop/uploads/images/" + filename)) {
                                stream.write(data);
                            }
                            d.setPicture(filename);
                            request.getSession().setAttribute("profile_picture", filename);
                        } else {
                            d.setPicture(dev.getPicture());
                        }
                        //memorizziamo il task
                        d.setKey(profile_id);
                        d.setUsername(profile_username);
                        d.setName(profile_name);
                        d.setSurname(profile_surname);
                        d.setHeadline(profile_headline);
                        d.setBiography(profile_biography);
                        d.setResume(profile_resume);
                        d.setMail(profile_email);
                        

                        datalayer.storeDeveloper(d);
                        //ora recuperiamo le info suulle skill e le memorizziamo

                        try {
                            //JSONObject skills = o.getJSONObject("skills");

                            JSONArray removed_skills = o.getJSONArray("removed_skills");

                            for(int i = 0; i < removed_skills.length(); i++){
                                int removed_skill_key = removed_skills.getInt(i);
                                datalayer.deleteSkillHasDeveloper(removed_skill_key, profile_id);
                            }

                         } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try{
                            JSONObject skills = o.getJSONObject("skills");
                            Iterator<String> temp = skills.keys();
                            while (temp.hasNext()) {
                                String sk_key = temp.next();
                                    JSONObject sk = skills.getJSONObject(sk_key);
                                    int skill_key = sk.getInt("key");
                                    int level = sk.getInt("level");
                                    datalayer.storeSkillHasDeveloper(skill_key, profile_id, level);
                                }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    datalayer.destroy();
                    //response.sendRedirect("developers/" + dev.getKey());
                    response.sendRedirect("/SocialDevelop/settings/profile?updated_profile=1");
                }
            } else {
                response.sendRedirect("UpdateProfile");
            }
            s.removeAttribute("previous_url");
        } else {
            response.sendRedirect("/SocialDevelop");
        }
    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {
        try {
            conferma_upd(request, response);
        } catch (IOException ex) {
            request.setAttribute("exception", ex);
            action_error(request, response);
        } catch (TemplateManagerException ex) {
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
