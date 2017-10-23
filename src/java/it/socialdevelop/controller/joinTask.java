package it.socialdevelop.controller;

import it.univaq.f4i.iw.framework.result.FailureResult;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Map;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import it.socialdevelop.data.model.Developer;
import it.socialdevelop.data.model.SocialDevelopDataLayer;
import it.socialdevelop.data.model.Task;
import it.socialdevelop.mailer.Mailer;

/**
 *
 * @author Hello World Group
 */
public class joinTask extends SocialDevelopBaseController {

    private void action_error(HttpServletRequest request, HttpServletResponse response) {
        if (request.getAttribute("exception") != null) {
            (new FailureResult(getServletContext())).activate((Exception) request.getAttribute("exception"), request, response);
        }
    }

    private void join(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, NamingException, NoSuchAlgorithmException, Exception {

        HttpSession s = request.getSession(true);
        if (s.getAttribute("userid") != null && ((int) s.getAttribute("userid")) > 0) {
            int user_key = (int) s.getAttribute("userid");
            SocialDevelopDataLayer datalayer = (SocialDevelopDataLayer) request.getAttribute("datalayer");
            boolean flag = false;
            int task_id = Integer.parseInt(request.getParameter("task_key"));
            Map<Developer, Integer> map = datalayer.getCollaboratorsByTask(task_id);
            //controlliamo se è già tra i collaboratori
            for (Developer dev : map.keySet()) {
                if (dev.getKey() == user_key) {
                    flag = true;
                    break;
                }
            }
            response.setContentType("text/plain");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            if (!flag) {
                //sender=1 --> inviata da collaboratore
                //stato=0 --> in attesa
                //voto=-1 --> non rilasciato
                datalayer.storeTaskHasDeveloper(task_id, user_key, 0, -1, user_key);
                
                String obj = "Richiesta di Collaborazione";
                String txt = "Hai ricevuto una richiesta di partecipazione al task " + datalayer.getTask(task_id).getName() + " da " + datalayer.getDeveloper(user_key).getUsername() + ".";
                Mailer m1 = new Mailer(datalayer.getDeveloper(user_key).getMail(), obj, txt);
                //m1.sendEmail();
                try {
                    out.println("Your request has been sended!");
                } finally {
                    out.close();
                }
            } else {
                datalayer.destroy();
                try {
                    out.println("Something went wrong...");
                } finally {
                    out.close();
                }
            }
        } else {
            response.sendRedirect("/socialDevelop");
        }
    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {
        try {
            join(request, response);
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
