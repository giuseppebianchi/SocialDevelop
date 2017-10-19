package it.socialdevelop.controller;

import it.univaq.f4i.iw.framework.data.DataLayerException;
import it.univaq.f4i.iw.framework.result.FailureResult;
import it.univaq.f4i.iw.framework.result.TemplateManagerException;
import it.univaq.f4i.iw.framework.result.TemplateResult;
import java.io.IOException;
import java.sql.SQLException;
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
import it.socialdevelop.data.model.Task;
import it.socialdevelop.data.model.Type;
import java.util.ArrayList;
import java.util.Map;

/**
 *
 * @author Hello World Group
 */
public class AdminTypes extends SocialDevelopBaseController {

    private void action_error(HttpServletRequest request, HttpServletResponse response) {
        if (request.getAttribute("exception") != null) {
            (new FailureResult(getServletContext())).activate((Exception) request.getAttribute("exception"), request, response);
        }
    }

    private void action_backendt(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, TemplateManagerException, SQLException, NamingException, DataLayerException {
        request.setAttribute("request", request);
        HttpSession s = request.getSession(true);
        if (s.getAttribute("userid") != null && ((int) s.getAttribute("userid")) > 0) {
            SocialDevelopDataLayer datalayer = (SocialDevelopDataLayer) request.getAttribute("datalayer");
            Developer dev = datalayer.getDeveloper((int) s.getAttribute("userid"));
            Admin admin = datalayer.getAdmin(dev.getKey());
            if (admin != null) {
                request.setAttribute("admin", "admin");
                request.setAttribute("page_title", "Admin Area - Skills");
                request.setAttribute("page_subtitle", "Manage the Skills");
                //recuperiamo sviluppatore a cui appartiene il pannello
                int dev_key = dev.getKey();
                request.setAttribute("username", dev.getUsername());
                //request.setAttribute("fullname", dev.getName() + " " + dev.getSurname());
                request.setAttribute("auth_user", s.getAttribute("userid"));
                request.setAttribute("foto", s.getAttribute("foto"));
                request.setAttribute("fullname", s.getAttribute("fullname"));
                request.setAttribute("bio", dev.getBiography());
                request.setAttribute("mail", dev.getMail());
                request.setAttribute("logout", "Logout");

                List<Type> types = datalayer.getTypes();
                if (types != null) {
                    request.setAttribute("types", types);
                }
                List<Integer> types_ok = new ArrayList();
                if (types != null) {
                    for (Type type : types) {
                        //riempiamo type_ok con le type cancellabili
                        List<Skill> skills = datalayer.getSkillsByType(type.getKey());
                        if (skills.size() == 0) {
                            types_ok.add(1);
                        }else{
                            types_ok.add(0);
                        }

                    }
                    request.setAttribute("typesD", types_ok);
                }

                datalayer.destroy();
                String act_url = request.getRequestURI();
                s.setAttribute("previous_url", act_url);
                TemplateResult res = new TemplateResult(getServletContext());
                res.activate("admin_types.ftl.html", request, response);
            } else {
                s.removeAttribute("previous_url");
                response.sendRedirect("/SocialDevelop");
            }
        } else {
            s.removeAttribute("previous_url");
            response.sendRedirect("/socialDevelop");
        }

    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            action_backendt(request, response);
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
