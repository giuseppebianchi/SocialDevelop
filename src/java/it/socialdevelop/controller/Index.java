package it.socialdevelop.controller;

import it.univaq.f4i.iw.framework.data.DataLayerException;
import it.univaq.f4i.iw.framework.result.FailureResult;
import it.univaq.f4i.iw.framework.result.TemplateManagerException;
import it.univaq.f4i.iw.framework.result.TemplateResult;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import it.socialdevelop.data.model.Developer;
import it.socialdevelop.data.model.SocialDevelopDataLayer;

/**
 *
 * @author Hello World Group
 */
public class Index extends SocialDevelopBaseController {

    private void action_error(HttpServletRequest request, HttpServletResponse response) {
        if (request.getAttribute("exception") != null) {
            (new FailureResult(getServletContext())).activate((Exception) request.getAttribute("exception"), request, response);
        }
    }

    private void action_home(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, TemplateManagerException, DataLayerException {

        HttpSession s = request.getSession(true);
        Map data = new HashMap();
        if (s.getAttribute("userid") != null && ((int) s.getAttribute("userid")) > 0) {
            //response.sendRedirect("MyProfile");
            Map user = new HashMap();
            SocialDevelopDataLayer datalayer = (SocialDevelopDataLayer) request.getAttribute("datalayer");
            Developer dev = datalayer.getDeveloper((int) s.getAttribute("userid"));
            data.put("auth_user", s.getAttribute("userid"));
            data.put("foto", s.getAttribute("foto"));
            data.put("fullname", s.getAttribute("fullname"));
            data.put("menu_active", "home");
            datalayer.destroy();
        }
        s.getAttribute("name");
        data.put("page_title", "Social Develop - Homepage");
        data.put("request", request);
        String problem = (String) s.getAttribute("problem");
        if (s.getAttribute("problem") != null) {
            if (problem.equals("login_all")) {
                data.put("error", "please, Compile the fields");
            } else if (problem.equals("login_pwd")) {
                data.put("error", "Password is not correct");
            } else if (problem.equals("login_user")) {
                data.put("error", "Username/mail is not correct");
            } else if (problem.equals("reg_all")) {
                data.put("error_register", "Please, compile the fields");
            } else if (problem.equals("reg_pwd")) {
                data.put("error_register", "Passwords do not match");
            } else if (problem.equals("reg_username")) {
                data.put("error_register", "Username already exists");
            } else if (problem.equals("reg_email")) {
                data.put("error_register", "Email already exists");
            }
            s.removeAttribute("problem");
        }

        String u = (String) s.getAttribute("previous_url");
        if (s.getAttribute("previous_url") != null && (u.equals("/SocialDevelop/signup"))) {
            data.put("active", "register");
        } else {
            data.put("active", "login");
        }
        String act_url = request.getRequestURI();
        s.setAttribute("previous_url", act_url);
        TemplateResult res = new TemplateResult(getServletContext());
        res.activate("home.ftl.html", data, response);
    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {

        try {
            action_home(request, response);
        } catch (IOException ex) {
            request.setAttribute("exception", ex);
            action_error(request, response);
        } catch (TemplateManagerException ex) {
            request.setAttribute("exception", ex);
            action_error(request, response);
        } catch (DataLayerException ex) {
            Logger.getLogger(Index.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
