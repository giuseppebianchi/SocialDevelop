/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.socialdevelop.controller;

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
import it.socialdevelop.data.model.SocialDevelopDataLayer;

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
        
     private String getDigest (Part file_to_upload, File uploaded_file) throws IOException, NoSuchAlgorithmException{
            InputStream is = file_to_upload.getInputStream(); 
            OutputStream os = new FileOutputStream(uploaded_file);
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] buffer = new byte[1024];
            int read;
            while ((read = is.read(buffer)) > 0) {
                //durante la copia, aggreghiamo i byte del file nel digest sha-1
                md.update(buffer, 0, read);
                os.write(buffer, 0, read);
            }
            //covertiamo il digest in una stringa
            byte[] digest = md.digest();
            String sdigest = "";
            for (byte b : digest) {
                sdigest += String.valueOf(b);
            }
            return sdigest;
     }
     
     private void conferma_upd(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, NamingException, NoSuchAlgorithmException, Exception {
        HttpSession s = request.getSession(true);
        String u = (String) s.getAttribute("previous_url");
        if(s.getAttribute("userid") != null && ((int) s.getAttribute("userid"))>0){
            if(s.getAttribute("previous_url") != null && u.equals("/socialdevelop/UpdateProfile")){
                
                SocialDevelopDataLayer datalayer = (SocialDevelopDataLayer) request.getAttribute("datalayer");
                
                String bio = request.getParameter("biography");

                String curriculum = request.getParameter("curriculum");

                Part foto_to_upload = request.getPart("foto-profilo");
                Part curriculum_to_upload = request.getPart("curriculum-pdf");

                Developer dev = datalayer.getDeveloper((Integer) s.getAttribute("userid"));

                File uploaded_foto;
                File uploaded_curriculum;
                int foto_key = 0;
                int curriculum_key = 0;

                if(foto_to_upload != null && foto_to_upload.getSize() > 0){
                    uploaded_foto = File.createTempFile("foto_profilo", "", new File(getServletContext().getInitParameter("uploaded-images.directory")));
                    String digest_foto = getDigest(foto_to_upload, uploaded_foto);
                    foto_key = datalayer.storeFile(foto_to_upload, uploaded_foto, digest_foto);
                    dev.setFoto(foto_key);
                }
                if(curriculum_to_upload != null && curriculum_to_upload.getSize() > 0){
                    uploaded_curriculum = File.createTempFile("curriculum", ".pdf", new File(getServletContext().getInitParameter("curriculums.directory")));
                    String digest_curriculum = getDigest(curriculum_to_upload, uploaded_curriculum);
                    curriculum_key = datalayer.storeFile(curriculum_to_upload, uploaded_curriculum, digest_curriculum);
                    dev.setCurriculum(curriculum_key);
                    dev.setCurriculum("");
                    curriculum = "";     
                }else{
                    dev.setCurriculum(0);
                    dev.setCurriculum(curriculum); 
                }
                dev.setBiography(bio);
                datalayer.storeDeveloper(dev);
                datalayer.destroy();
                response.sendRedirect("MyProfile");
            }else{
                response.sendRedirect("UpdateProfile");
            } 
            s.removeAttribute("previous_url");
        }else{
            response.sendRedirect("index");
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
