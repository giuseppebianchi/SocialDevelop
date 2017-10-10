package it.socialdevelop.controller;

import it.univaq.f4i.iw.framework.data.DataLayerException;
import it.univaq.f4i.iw.framework.result.FailureResult;
import it.univaq.f4i.iw.framework.result.TemplateManagerException;
import it.univaq.f4i.iw.framework.security.SecurityLayer;
import java.io.IOException;
import java.sql.SQLException;
import javax.naming.NamingException;
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
public class Login extends SocialDevelopBaseController {

    private void action_error(HttpServletRequest request, HttpServletResponse response) {
        if (request.getAttribute("exception") != null) {
            (new FailureResult(getServletContext())).activate((Exception) request.getAttribute("exception"), request, response);
        }
    }

    private void action_login(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, TemplateManagerException, DataLayerException, SQLException, NamingException {
        HttpSession s = request.getSession(true);
        String u = (String) s.getAttribute("previous_url");

        if (s.getAttribute("previous_url") != null && (u.equals("/SocialDevelop") || u.equals("/SocialDevelop/") || u.equals("/SocialDevelop/home") || u.equals("/SocialDevelop/login"))) {
            String mail_username = request.getParameter("username");
            String password = request.getParameter("pwd");
            if (mail_username != null && !mail_username.equals("") && password != null && !password.equals("")) {
                SocialDevelopDataLayer datalayer = (SocialDevelopDataLayer) request.getAttribute("datalayer");
                int dev_key = 0;
                if (mail_username.contains("@")) {
                    dev_key = datalayer.getDeveloperByMail(mail_username);
                } else {
                    dev_key = datalayer.getDeveloperByUsername(mail_username);
                }
                Developer dev = datalayer.getDeveloper(dev_key);
                datalayer.destroy();
                
                String act_url = request.getRequestURI();
                s.setAttribute("previous_url", act_url);

                if (dev != null) {
                    if (dev.getPassword().equals(password)) {
                        String fullname = dev.getUsername();
                        if(!(dev.getName().equals("") && dev.getSurname().equals(""))){
                            fullname = dev.getName() + " " + dev.getSurname();
                        }
                        SecurityLayer.createSession(request, dev.getUsername(), fullname, dev.getPicture(), dev_key);
                        response.sendRedirect("/SocialDevelop");
                    } else {
                        //password errata
                        s.setAttribute("problem", "login_pwd");
                        response.sendRedirect("/SocialDevelop/signin");
                    }
                } else {
                    //mail o username errato
                    s.setAttribute("problem", "login_user");
                    response.sendRedirect("/SocialDevelop/signin");
                }
            } else {
                s.setAttribute("problem", "login_all");
                response.sendRedirect("/SocialDevelop/signin");
            }

        } else {
            response.sendRedirect("/SocialDevelop/signin");
        }

    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {
        try {
            action_login(request, response);
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
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    public String getServletInfo() {
        return "Short description";
    }
    // </editor-fold>
}
