package it.socialdevelop.controller;

import it.univaq.f4i.iw.framework.result.FailureResult;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import it.socialdevelop.data.model.SocialDevelopDataLayer;
import it.socialdevelop.data.model.Task;
import it.socialdevelop.mailer.Mailer;

/**
 *
 * @author Hello World Group
 */
public class acceptProposal1 extends SocialDevelopBaseController {

    private void action_error(HttpServletRequest request, HttpServletResponse response) {
        if (request.getAttribute("exception") != null) {
            (new FailureResult(getServletContext())).activate((Exception) request.getAttribute("exception"), request, response);
        }
    }

    private void acceptproposal(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, NamingException, NoSuchAlgorithmException, Exception {

        HttpSession s = request.getSession(true);
        if (s.getAttribute("userid") != null && ((int) s.getAttribute("userid")) > 0) {
            int user_key = (int) s.getAttribute("userid");
            SocialDevelopDataLayer datalayer = (SocialDevelopDataLayer) request.getAttribute("datalayer");
            int developer_key = 0;
            if (request.getParameter("developer_key") != null) {
                developer_key = Integer.parseInt(request.getParameter("developer_key"));
            }
            int task_key = Integer.parseInt(request.getParameter("task_key"));
            int state = Integer.parseInt(request.getParameter("state"));

            if (developer_key == 0) {
                //riposta a proposta
                int sender_key = Integer.parseInt(request.getParameter("sender"));
                datalayer.storeTaskHasDeveloper(task_key, user_key, state, -1, sender_key);

                //invio mail
                String obj = "Stato dell'invito aggiornato";
                if (state == 1) {
                    //proposta accettata
                    String txt = datalayer.getDeveloper(user_key).getUsername() + " ha accettato la tua proposta di collaborazione"
                            + " al task " + datalayer.getTask(task_key).getName() + "!";
                    Mailer m1 = new Mailer(datalayer.getDeveloper(sender_key).getMail(), obj, txt);
                    m1.sendEmail();
                } else {
                    //proposta rifiutata
                    String txt = datalayer.getDeveloper(user_key).getUsername() + " ha rifiutato la tua proposta di collaborazione"
                            + " al task " + datalayer.getTask(task_key).getName() + ".";
                    Mailer m1 = new Mailer(datalayer.getDeveloper(sender_key).getMail(), obj, txt);
                    m1.sendEmail();
                }
            } else {
                //risposta a domanda
                datalayer.storeTaskHasDeveloper(task_key, developer_key, state, -1, developer_key);
                //invio mail
                String obj = "Stato della domanda aggiornato";
                if (state == 1) {
                    //domanda accettata
                    String txt = "La tua domanda di partecipazione al task " + datalayer.getTask(task_key).getName() + " è stata accettata.";
                    Mailer m1 = new Mailer(datalayer.getDeveloper(developer_key).getMail(), obj, txt);
                    m1.sendEmail();
                } else {
                    //domanda rifiutata
                    String txt = "La tua domanda di partecipazione al task " + datalayer.getTask(task_key).getName() + " è stata rifiutata.";
                    Mailer m1 = new Mailer(datalayer.getDeveloper(developer_key).getMail(), obj, txt);
                    m1.sendEmail();
                }

            }

            if (state == 1) {
                Task task = datalayer.getTask(task_key);
                if (task.getNumCollaborators() != 0) {
                    int n = task.getNumCollaborators() - 1;
                    task.setNumCollaborators(n);
                    datalayer.storeTask(task);
                }
            }

            datalayer.destroy();
            response.setContentType("text/plain");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();

            try {
                out.println("Accepted");
            } finally {
                out.close();
            }
        }

    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {
        try {
            acceptproposal(request, response);
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
