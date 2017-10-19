package it.socialdevelop.controller;

import it.univaq.f4i.iw.framework.result.FailureResult;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import static java.sql.Types.NULL;
import java.util.List;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import it.socialdevelop.data.model.Admin;
import it.socialdevelop.data.model.Developer;
import it.socialdevelop.data.model.Skill;
import it.socialdevelop.data.model.SocialDevelopDataLayer;
import it.socialdevelop.data.model.Type;
import static java.lang.Integer.parseInt;

/**
 *
 * @author Hello World Group
 */
public class AdminRemoveType extends SocialDevelopBaseController {

    private void action_error(HttpServletRequest request, HttpServletResponse response) {
        if (request.getAttribute("exception") != null) {
            (new FailureResult(getServletContext())).activate((Exception) request.getAttribute("exception"), request, response);
        }
    }

    private void rm_type(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, NamingException, NoSuchAlgorithmException, Exception {
        HttpSession s = request.getSession(true); 
        if (s.getAttribute("userid") != null && ((int) s.getAttribute("userid")) > 0) {
 
            SocialDevelopDataLayer datalayer = (SocialDevelopDataLayer) request.getAttribute("datalayer");
            Developer dev = datalayer.getDeveloper((int) s.getAttribute("userid"));
            Admin admin = datalayer.getAdmin(dev.getKey());
            if (admin != null && admin.getDevelperKey() > 0) {
                int validation = 0;
                int type_id = parseInt(request.getParameter("type_id"));
                Type type = datalayer.getType(type_id);

                List<Skill> skills = datalayer.getSkillsByType(type.getKey());

                if (skills.isEmpty()) {
                    datalayer.deleteType(type);
                    validation = 1;
                }

                

                datalayer.destroy();
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

    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {
        try {
            rm_type(request, response);
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
