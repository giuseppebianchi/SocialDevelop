package it.socialdevelop.controller;

import it.univaq.f4i.iw.framework.data.DataLayerException;
import it.univaq.f4i.iw.framework.result.FailureResult;
import it.univaq.f4i.iw.framework.result.TemplateManagerException;
import java.io.IOException;
import static java.lang.Integer.parseInt;
import java.sql.SQLException;
import static java.sql.Types.NULL;
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
import java.io.PrintWriter;

/**
 *
 * @author Hello World Group
 */
public class AdminRemoveSkill extends SocialDevelopBaseController {

    private void action_error(HttpServletRequest request, HttpServletResponse response) {
        if (request.getAttribute("exception") != null) {
            (new FailureResult(getServletContext())).activate((Exception) request.getAttribute("exception"), request, response);
        }
    }

    private void action_adminrmskill(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, TemplateManagerException, SQLException, NamingException, DataLayerException {

        HttpSession s = request.getSession(true);
        String u = (String) s.getAttribute("previous_url");
        if (s.getAttribute("userid") != null && ((int) s.getAttribute("userid")) > 0) {
            if (true) {

                SocialDevelopDataLayer datalayer = (SocialDevelopDataLayer) request.getAttribute("datalayer");
                Developer dev = datalayer.getDeveloper((int) s.getAttribute("userid"));
                Admin admin = datalayer.getAdmin(dev.getKey());
                if (admin != null && admin.getDevelperKey() > 0) {
                    Skill skill = datalayer.getSkill(parseInt(request.getParameter("skill_id")));
                    
                    int validation = 0;
                    //controlliamo se è utilizzata, se lo è non può essere rimossa
                    Map<Developer, Integer> devs = datalayer.getDevelopersBySkill(skill.getKey());
                    if (devs.size() == 0) {
                        List<Task> tasks = datalayer.getTasksBySkill(skill.getKey());
                        if (tasks.size() == 0) {
                            datalayer.deleteSkill(skill);
                            validation = 1;
                        }
                    }

                    datalayer.destroy();
                    s.removeAttribute("previous_url");
                    response.setContentType("text/plain");
                    response.setCharacterEncoding("UTF-8");
                    PrintWriter out = response.getWriter();
                    try {
                        out.println(validation);
                    } finally {
                        out.close();
                    }
                } else {
                    s.removeAttribute("previous_url");
                    response.sendRedirect("/SocialDevelop");
                }
            } else {
                s.removeAttribute("previous_url");
                response.sendRedirect("/SocialDevelop");
            }
        } else {
            s.removeAttribute("previous_url");
            response.sendRedirect("/SocialDevelop");
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            action_adminrmskill(request, response);
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
