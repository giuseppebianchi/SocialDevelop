package it.socialdevelop.controller;

import it.univaq.f4i.iw.framework.data.DataLayerException;
import it.univaq.f4i.iw.framework.result.FailureResult;
import it.univaq.f4i.iw.framework.result.StreamResult;
import it.univaq.f4i.iw.framework.result.TemplateManagerException;
import it.univaq.f4i.iw.framework.result.TemplateResult;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Calendar;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import it.socialdevelop.data.model.Admin;
import it.socialdevelop.data.model.Developer;
import it.socialdevelop.data.model.Files;
import it.socialdevelop.data.model.Skill;
import it.socialdevelop.data.model.SocialDevelopDataLayer;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Hello World Group
 */
public class SettingsProfile extends SocialDevelopBaseController {

    private void action_error(HttpServletRequest request, HttpServletResponse response) {
        if (request.getAttribute("exception") != null) {
            (new FailureResult(getServletContext())).activate((Exception) request.getAttribute("exception"), request, response);
        }
    }

    

    private void action_updprofile(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, TemplateManagerException, SQLException, NamingException, DataLayerException {
        HttpSession s = request.getSession(true);
        request.setAttribute("request", request);
        request.setAttribute("bootstrap", 1);
        request.setAttribute("page_title", "Update Profile");
        request.setAttribute("page_subtitle", "update your profile");
        if (s.getAttribute("userid") != null && ((int) s.getAttribute("userid")) > 0) {
            SocialDevelopDataLayer datalayer = (SocialDevelopDataLayer) request.getAttribute("datalayer");
            Admin admin = datalayer.getAdmin((int) s.getAttribute("userid"));
            if (admin != null) {
                request.setAttribute("isAdmin", 1);
            }
            Developer dev = datalayer.getDeveloper((int) s.getAttribute("userid"));
            Map<Skill, Integer> skillsList = datalayer.getSkillsByDeveloper(dev.getKey());
            List<Skill> skills = datalayer.getSkills();
            //Get difference between years
            request.setAttribute("biography", dev.getBiography());
            request.setAttribute("name", dev.getName());
            request.setAttribute("auth_user", s.getAttribute("userid"));
            request.setAttribute("surname", dev.getSurname());
            request.setAttribute("username", dev.getUsername());
            request.setAttribute("mail", dev.getMail());
            request.setAttribute("headline", dev.getHeadline());
            request.setAttribute("resume", dev.getResume());
            request.setAttribute("fullname", dev.getName() + " " + dev.getSurname());
            request.setAttribute("logout", "Logout");
            request.setAttribute("skills", skills);
            request.setAttribute("skillsList", skillsList);
            
            if(request.getParameterMap().containsKey("updated_profile") && (Integer.parseInt(request.getParameter("updated_profile")) == 1)) { 
                request.setAttribute("updated_profile", 1);
            }
            //request.setAttribute("datalayer", datalayer);
            String act_url = request.getRequestURI();
            s.setAttribute("previous_url", act_url);
            datalayer.destroy();
            TemplateResult res = new TemplateResult(getServletContext());
            res.activate("settings_profile.ftl.html", request, response);
        } else {
            response.sendRedirect("/SocialDevelop");
        }

    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {

        try {
            action_updprofile(request, response);
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
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
