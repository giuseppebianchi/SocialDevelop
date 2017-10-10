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
import it.socialdevelop.data.model.Developer;
import it.socialdevelop.data.model.Skill;
import it.socialdevelop.data.model.SocialDevelopDataLayer;
import it.univaq.f4i.iw.framework.result.TemplateResult;
import java.util.List;

/**
 *
 * @author Hello World Group
 */
public class CompleteRegistration extends SocialDevelopBaseController {

    private void action_error(HttpServletRequest request, HttpServletResponse response) {
        if (request.getAttribute("exception") != null) {
            (new FailureResult(getServletContext())).activate((Exception) request.getAttribute("exception"), request, response);
        }
    }

    private String getDigest(Part file_to_upload, File uploaded_file) throws IOException, NoSuchAlgorithmException {
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

    private void completa_reg(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, NamingException, NoSuchAlgorithmException, Exception {
        HttpSession s = request.getSession(true);
        String u = (String) s.getAttribute("previous_url");
        if (s.getAttribute("userid") != null && ((int) s.getAttribute("userid")) > 0) {
            if (/*s.getAttribute("previous_url") != null && (u.equals("/SocialDevelop/signup/signup_submit"))*/true) {
                SocialDevelopDataLayer datalayer = (SocialDevelopDataLayer) request.getAttribute("datalayer");
                Developer dev = datalayer.getDeveloper((int) s.getAttribute("userid"));
                List<Skill> skills = datalayer.getSkills();
                request.setAttribute("request", request);
                request.setAttribute("bootstrap", 1);
                request.setAttribute("mail", dev.getMail());
                request.setAttribute("skills", skills);
                request.setAttribute("username", dev.getUsername());
                request.setAttribute("page_subtitle", "Complete your profile!");
                request.setAttribute("logout", "Logout");
                request.setAttribute("auth_user", s.getAttribute("userid"));
                request.setAttribute("foto", s.getAttribute("foto"));
                request.setAttribute("fullname", dev.getUsername());
                TemplateResult res = new TemplateResult(getServletContext());
                res.activate("complete_registration.ftl.html", request, response);

            } else {
                response.sendRedirect("/SocialDevelop");
            }
        } else {
            response.sendRedirect("/SocialDevelop");
        }
        response.sendRedirect("/SocialDevelop");
    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {
        try {
            completa_reg(request, response);
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
