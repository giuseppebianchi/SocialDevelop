package it.socialdevelop.controller;

import it.univaq.f4i.iw.framework.data.DataLayerException;
import it.univaq.f4i.iw.framework.result.FailureResult;
import it.univaq.f4i.iw.framework.result.TemplateManagerException;
import it.univaq.f4i.iw.framework.result.TemplateResult;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import it.socialdevelop.data.model.Admin;
import it.socialdevelop.data.model.Developer;
import it.socialdevelop.data.model.Skill;
import it.socialdevelop.data.model.SocialDevelopDataLayer;
import it.socialdevelop.data.model.Task;
import it.socialdevelop.data.model.Type;

/**
 *
 * @author Hello World Group
 */
public class BackEndSkill extends SocialDevelopBaseController {

    private void action_error(HttpServletRequest request, HttpServletResponse response) {
        if (request.getAttribute("exception") != null) {
            (new FailureResult(getServletContext())).activate((Exception) request.getAttribute("exception"), request, response);
        }
    }

    private void action_backends(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, TemplateManagerException, SQLException, NamingException, DataLayerException {

        HttpSession s = request.getSession(true);
        if (s.getAttribute("userid") != null && ((int) s.getAttribute("userid")) > 0) {
            SocialDevelopDataLayer datalayer = (SocialDevelopDataLayer) request.getAttribute("datalayer");
            Developer dev = datalayer.getDeveloper((int) s.getAttribute("userid"));
            Admin admin = datalayer.getAdmin(dev.getKey());
            if (admin != null) {
                request.setAttribute("admin", "admin");
                request.setAttribute("page_title", "SKILL BACKEND");
                request.setAttribute("page_subtitle", "Manage the Skills");
                List<Skill> skills = datalayer.getSkills();
                List<Skill> skills_ok = new ArrayList();
                if (skills != null) {
                    for (Skill skill : skills) {
                        
                        //riempiamo skills_ok con le skill cancellabili
                        Map<Developer, Integer> devs = datalayer.getDevelopersBySkill(skill.getKey());
                        if (devs.size() == 0) {
                            List<Task> tasks = datalayer.getTasksBySkill(skill.getKey());
                            if (tasks.size() == 0) {
                                skills_ok.add(skill);
                            }
                        }

                    }
                    request.setAttribute("skillsD", skills_ok);
                }
                request.setAttribute("skills", skills);
                List<Type> types = datalayer.getTypes();
                if (types != null) {
                    request.setAttribute("types", types);
                }

                request.setAttribute("logout", "Logout");

                datalayer.destroy();
                String act_url = request.getRequestURI();
                s.setAttribute("previous_url", act_url);
                TemplateResult res = new TemplateResult(getServletContext());
                res.activate("backend_skills.html", request, response);

            } else {
                s.removeAttribute("previous_url");
                response.sendRedirect("index");
            }
        } else {
            s.removeAttribute("previous_url");
            response.sendRedirect("index");
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            action_backends(request, response);
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
}
