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

/**
 *
 * @author Ingegneria del Web
 * @version
 */
public class SignupSubmit extends SocialDevelopBaseController {

    private void action_error(HttpServletRequest request, HttpServletResponse response) {
        if (request.getAttribute("exception") != null) {
            (new FailureResult(getServletContext())).activate((Exception) request.getAttribute("exception"), request, response);
        }
    }

    private void action_registration(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, TemplateManagerException, DataLayerException, SQLException, NamingException {
        HttpSession s = request.getSession(true);
        request.setAttribute("request", request);
        String u = (String) s.getAttribute("previous_url");
        if (/*s.getAttribute("userid") == null && ( u.equals("/SocialDevelop/") || u.equals("/SocialDevelop/home") || u.equals("/SocialDevelop/signup"))*/ true) {

            String username = request.getParameter("username1");
            String mail = request.getParameter("mail");
            String pwd = request.getParameter("pwd1");
            String pwd2 = request.getParameter("pwd2");
            SocialDevelopDataLayer datalayer = (SocialDevelopDataLayer) request.getAttribute("datalayer");
            DeveloperImpl dev = new DeveloperImpl(datalayer);
            if (username != null && !username.equals("")
                    && mail != null && !mail.equals("") && pwd != null && !pwd.equals("") && pwd2 != null && !pwd2.equals("")) {
                int dev_check = datalayer.getDeveloperByMail(mail);
                int dev_check2 = datalayer.getDeveloperByUsername(username);
                if (dev_check == 0) {
                    if (dev_check2 == 0) {
                        if (pwd.equals(pwd2)) {
                            dev.setUsername(username);
                            dev.setMail(mail);
                            dev.setPassword(pwd);
                            dev.setPicture("blog-image-4.png");
                            
                            datalayer.storeDeveloper(dev);
                            int key = datalayer.getDeveloperByUsername(username);
                            request.setAttribute("userid", key);
                            request.setAttribute("mail", mail);
                            request.setAttribute("username", dev.getUsername());
                            
                            datalayer.destroy();
                            String fullname = username;
                            
                            HttpSession sess = SecurityLayer.createSession(request, dev.getUsername(), fullname, dev.getPicture(), dev.getKey());
                            String act_url = request.getRequestURI();
                            sess.setAttribute("previous_url", act_url);
                            response.sendRedirect("/SocialDevelop/signup/complete_registration");
                        } else {
                            //pwd non coincidono
                            //signup_error(request, response, "pwd");
                            s.setAttribute("problem", "reg_pwd");
                            response.sendRedirect("/SocialDevelop/signup");
                        }
                    } else {
                        //username esiste già
                        //signup_error(request, response, "username");
                        s.setAttribute("problem", "reg_username");
                        response.sendRedirect("/SocialDevelop/signup");
                    }
                } else {
                    //email esiste già
                    //signup_error(request, response, "email");
                    s.setAttribute("problem", "reg_email");
                    response.sendRedirect("/SocialDevelop/signup");
                }
            } else {
                s.setAttribute("problem", "reg_all");
                response.sendRedirect("/SocialDevelop/signup");
            }
        } else {
            response.sendRedirect("/SocialDevelop/signup");
        }

    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {
        try {
            action_registration(request, response);
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
