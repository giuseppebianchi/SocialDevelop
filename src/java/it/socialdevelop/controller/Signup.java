package it.socialdevelop.controller;

import it.univaq.f4i.iw.framework.data.DataLayerException;
import it.univaq.f4i.iw.framework.result.FailureResult;
import it.univaq.f4i.iw.framework.result.TemplateManagerException;
import it.univaq.f4i.iw.framework.result.TemplateResult;
import it.univaq.f4i.iw.framework.security.SecurityLayer;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import it.socialdevelop.data.impl.DeveloperImpl;
import it.socialdevelop.data.model.Skill;
import it.socialdevelop.data.model.SocialDevelopDataLayer;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Ingegneria del Web
 * @version
 */
public class Signup extends SocialDevelopBaseController {

    private void action_error(HttpServletRequest request, HttpServletResponse response) {
        if (request.getAttribute("exception") != null) {
            (new FailureResult(getServletContext())).activate((Exception) request.getAttribute("exception"), request, response);
        }
    }

    private void action_registrati(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, TemplateManagerException, DataLayerException, SQLException, NamingException {
        HttpSession s = request.getSession(true);
        Map data = new HashMap();
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
            TemplateResult res = new TemplateResult(getServletContext());
            res.activate("signup.ftl.html", data, response);

    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {
        try {
            action_registrati(request, response);
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
            action_error(request, response);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    public String getServletInfo() {
        return "Short description";
    }
    // </editor-fold>
}
