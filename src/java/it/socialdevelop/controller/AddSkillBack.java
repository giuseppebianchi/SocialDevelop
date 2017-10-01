package it.socialdevelop.controller;

import it.univaq.f4i.iw.framework.data.DataLayerException;
import it.univaq.f4i.iw.framework.result.FailureResult;
import it.univaq.f4i.iw.framework.result.TemplateManagerException;
import java.io.IOException;
import static java.lang.Integer.parseInt;
import java.sql.SQLException;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import it.socialdevelop.data.impl.SkillImpl;
import it.socialdevelop.data.model.Admin;
import it.socialdevelop.data.model.Developer;
import it.socialdevelop.data.model.Skill;
import it.socialdevelop.data.model.SocialDevelopDataLayer;

/**
 *
 * @author Hello World Group
 */
public class AddSkillBack extends SocialDevelopBaseController {

    private void action_error(HttpServletRequest request, HttpServletResponse response) {
        if (request.getAttribute("exception") != null) {
            (new FailureResult(getServletContext())).activate((Exception) request.getAttribute("exception"), request, response);
        }
    }

    private void action_addSkillB(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, TemplateManagerException, SQLException, NamingException, DataLayerException {

        HttpSession s = request.getSession(true);
        String u = (String) s.getAttribute("previous_url");
        if (s.getAttribute("userid") != null && ((int) s.getAttribute("userid")) > 0) {
            if (s.getAttribute("previous_url") != null && ((String) s.getAttribute("previous_url")).equals("/SocialDevelop/BackEndSkill")) {

                SocialDevelopDataLayer datalayer = (SocialDevelopDataLayer) request.getAttribute("datalayer");
                Developer dev = datalayer.getDeveloper((int) s.getAttribute("userid"));
                Admin admin = datalayer.getAdmin(dev.getKey());
                if (admin != null) {

                    Skill skill = new SkillImpl(datalayer);
                    skill.setName(request.getParameter("skill-name"));

                    skill.setType_key(parseInt(request.getParameter("typeS")));

                    if (!(request.getParameter("skill-father") == "")) {

                        Skill skillFather = datalayer.getSkill(parseInt(request.getParameter("skill-father")));
                        if (skill.getType_key() == skillFather.getType_key()) {
                            skill.setParent(datalayer.getSkill(parseInt(request.getParameter("skill-father"))));
                        }
                    }

                    datalayer.storeSkill(skill);

                    datalayer.destroy();
                    s.removeAttribute("previous_url");
                    response.sendRedirect(u.split("/")[2]);

                } else {
                    s.removeAttribute("previous_url");
                    response.sendRedirect("index");
                }

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
            action_addSkillB(request, response);
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
