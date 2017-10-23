package it.socialdevelop.controller;

import it.univaq.f4i.iw.framework.data.DataLayerException;
import it.univaq.f4i.iw.framework.result.FailureResult;
import it.univaq.f4i.iw.framework.result.TemplateManagerException;
import java.io.IOException;
import java.sql.SQLException;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import it.socialdevelop.data.impl.MessageImpl;
import it.socialdevelop.data.model.SocialDevelopDataLayer;
import java.io.PrintWriter;

/**
 *
 * @author Hello World Group
 */
public class MessagesManager extends SocialDevelopBaseController {

    private void action_error(HttpServletRequest request, HttpServletResponse response) {
        if (request.getAttribute("exception") != null) {
            (new FailureResult(getServletContext())).activate((Exception) request.getAttribute("exception"), request, response);
        }
    }

    private void action_postmsg(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, TemplateManagerException, SQLException, NamingException, DataLayerException {
        HttpSession s = request.getSession(true);
        String u = (String) s.getAttribute("previous_url");
        if (s.getAttribute("userid") != null && ((int) s.getAttribute("userid")) > 0) {
            if (/*s.getAttribute("previous_url") != null*/ true) {
                SocialDevelopDataLayer datalayer = (SocialDevelopDataLayer) request.getAttribute("datalayer");
                int user_key = (int) s.getAttribute("userid");
                if((request.getParameter("q") != null) && (Integer.parseInt(request.getParameter("q")) == 1)){ 
                    //rimuovi messaggio
                }else{ //aggiungi
                    MessageImpl msg = new MessageImpl(datalayer);
                    msg.setDeveloperKey(user_key);
                    msg.setText(request.getParameter("text"));
                    int type = Integer.parseInt(request.getParameter("type"));
                    int p = Integer.parseInt(request.getParameter("private"));
                    switch(type){
                        case 1: msg.setType("Discussion");break;
                        case 2: msg.setType("Ad");break;
                        case 3: msg.setType("Request");break;
                        default: msg.setType("Discussion");
                    }
                    int project_key = Integer.parseInt(request.getParameter("project_key"));
                    msg.setProjectKey(project_key);
                    boolean isPrivate = true;
                    if (p == 0) {
                        isPrivate = false;
                    }
                    msg.setPrivate(isPrivate);
                    datalayer.storeMessage(msg); 
                }

                datalayer.destroy();
                s.removeAttribute("previous_url");
                response.setContentType("text/plain");
                response.setCharacterEncoding("UTF-8");
                PrintWriter out = response.getWriter();
                try {
                    out.println("1");
                } finally {
                    out.close();
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
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {
        try {
            action_postmsg(request, response);
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
