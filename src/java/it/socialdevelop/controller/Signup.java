package it.socialdevelop.controller;

import it.univaq.f4i.iw.framework.data.DataLayerException;
import it.univaq.f4i.iw.framework.result.FailureResult;
import it.univaq.f4i.iw.framework.result.TemplateManagerException;
import it.univaq.f4i.iw.framework.result.TemplateResult;
import it.univaq.f4i.iw.framework.security.SecurityLayer;
import java.io.IOException;
import java.sql.SQLException;
import java.util.GregorianCalendar;
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
public class Signup extends SocialDevelopBaseController {

    private void action_error(HttpServletRequest request, HttpServletResponse response) {
        if (request.getAttribute("exception") != null) {
            (new FailureResult(getServletContext())).activate((Exception) request.getAttribute("exception"), request, response);
        }
    }

    private void action_registrati(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, TemplateManagerException, DataLayerException, SQLException, NamingException {
        HttpSession s = request.getSession(true);
        String u = (String) s.getAttribute("previous_url");
        if (s.getAttribute("previous_url") != null && (u.equals("/SocialDevelop/index") || u.equals("/socialdevelop/MakeLoginReg"))) {

            String name = request.getParameter("first_name");
            String surname = request.getParameter("second_name");
            String username = request.getParameter("username1");
            String mail = request.getParameter("mail");
            String pwd = request.getParameter("pwd1");
            String pwd2 = request.getParameter("pwd2");
            String bday = request.getParameter("birthdate");
            SocialDevelopDataLayer datalayer = (SocialDevelopDataLayer) request.getAttribute("datalayer");
            DeveloperImpl dev = new DeveloperImpl(datalayer);
            if (name != null && !name.equals("") && surname != null && !surname.equals("") && username != null && !username.equals("")
                    && mail != null && !mail.equals("") && pwd != null && !pwd.equals("") && pwd2 != null && !pwd2.equals("")
                    && bday != null && !bday.equals("")) {
                int dev_check = datalayer.getDeveloperByMail(mail);
                int dev_check2 = datalayer.getDeveloperByUsername(username);
                if (dev_check == 0) {
                    if (dev_check2 == 0) {
                        if (pwd.equals(pwd2)) {
                            dev.setName(name);
                            dev.setSurname(surname);
                            dev.setUsername(username);
                            dev.setMail(mail);
                            dev.setPwd(pwd);
                            GregorianCalendar gc = new GregorianCalendar();
                            gc.setLenient(false);
                            gc.set(GregorianCalendar.YEAR, Integer.valueOf(bday.split("/")[2]));
                            gc.set(GregorianCalendar.MONTH, Integer.valueOf(bday.split("/")[1]) - 1); //parte da 0
                            gc.set(GregorianCalendar.DATE, Integer.valueOf(bday.split("/")[0]));
                            dev.setBirthDate(gc);
                            datalayer.storeDeveloper(dev);
                            int key = datalayer.getDeveloperByUsername(username);
                            request.setAttribute("userid", key);
                            request.setAttribute("page_title", username + ", ");
                            request.setAttribute("page_subtitle", "Complete your profile!");
                            request.setAttribute("username", dev.getUsername());
                            request.setAttribute("logout", "Logout");
                            //recupero skills che non hanno figli
                            List<Skill> skills = datalayer.getSkillsParentList();
                            //ora recuperiamo per ognuna di esse le skills figlie
                            if (skills != null) {
                                for (Skill skill : skills) {
                                    List<Skill> child = datalayer.getChild(skill.getKey());
                                    if (child != null) {
                                        skill.setChild(child);
                                    }
                                }
                            }
                            request.setAttribute("skills", skills);
                            datalayer.destroy();
                            HttpSession sess = SecurityLayer.createSession(request, dev.getUsername(), dev.getName(), dev.getPicture(), dev.getKey());
                            String act_url = request.getRequestURI();
                            sess.setAttribute("previous_url", act_url);
                            TemplateResult res = new TemplateResult(getServletContext());
                            res.activate("completa_registrazione.html", request, response);
                        } else {
                            //pwd non coincidono
                            //signup_error(request, response, "pwd");
                            s.setAttribute("problem", "reg_pwd");
                            response.sendRedirect("index");
                        }
                    } else {
                        //username esiste già
                        //signup_error(request, response, "username");
                        s.setAttribute("problem", "reg_username");
                        response.sendRedirect("index");
                    }
                } else {
                    //email esiste già
                    //signup_error(request, response, "email");
                    s.setAttribute("problem", "reg_email");
                    response.sendRedirect("index");
                }
            } else {
                s.setAttribute("problem", "reg_all");
                response.sendRedirect("index");
            }
        } else {
            response.sendRedirect("index");
        }

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
