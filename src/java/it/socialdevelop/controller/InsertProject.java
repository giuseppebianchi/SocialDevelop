package it.socialdevelop.controller;

import it.univaq.f4i.iw.framework.data.DataLayerException;
import it.univaq.f4i.iw.framework.result.FailureResult;
import it.univaq.f4i.iw.framework.result.TemplateManagerException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.GregorianCalendar;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import it.socialdevelop.data.impl.ProjectImpl;
import it.socialdevelop.data.impl.TaskImpl;
import it.socialdevelop.data.model.SocialDevelopDataLayer;

/**
 *
 * @author Hello World Group
 */
public class InsertProject extends SocialDevelopBaseController {

    private void action_error(HttpServletRequest request, HttpServletResponse response) {
        if (request.getAttribute("exception") != null) {
            (new FailureResult(getServletContext())).activate((Exception) request.getAttribute("exception"), request, response);
        }
    }

    private void action_insertproject(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, TemplateManagerException, SQLException, NamingException, DataLayerException {
        //recuperare coordinatore dalla sessione!
        HttpSession s = request.getSession(true);
        if (s.getAttribute("userid") != null && ((int) s.getAttribute("userid")) > 0) {
            String u = (String) s.getAttribute("previous_url");
            if (s.getAttribute("previous_url") != null && u.equals("/SocialDevelop/CreateProject")) {
                if (!request.getParameter("tasks").equals("")) {
                    SocialDevelopDataLayer datalayer = (SocialDevelopDataLayer) request.getAttribute("datalayer");
                    String project_name = request.getParameter("project_name");
                    String project_descr = request.getParameter("project_descr");
                    int userid = (int) s.getAttribute("userid");
                    //memorizziamo il progetto
                    ProjectImpl p = new ProjectImpl(datalayer);
                    p.setCoordinatorKey(userid);
                    p.setName(project_name);
                    p.setDescription(project_descr);
                    int project_key = datalayer.storeProject(p);
                    //ora recuperiamo le info sui task e le memorizziamo
                    String tasks = request.getParameter("tasks");
                    String[] task = tasks.split("@");
                    for (String t : task) {
                        String[] thistask = t.split("#");
                        TaskImpl current = new TaskImpl(datalayer);
                        current.setName(thistask[0]);
                        current.setProjectKey(project_key);
                        String start = thistask[1];
                        GregorianCalendar gc = new GregorianCalendar();
                        gc.setLenient(false);
                        gc.set(GregorianCalendar.YEAR, Integer.valueOf(start.split("/")[2]));
                        gc.set(GregorianCalendar.MONTH, Integer.valueOf(start.split("/")[1]) - 1);
                        gc.set(GregorianCalendar.DATE, Integer.valueOf(start.split("/")[0]));
                        current.setStartDate(gc);

                        String end = thistask[2];
                        GregorianCalendar gc1 = new GregorianCalendar();
                        gc1.setLenient(false);
                        gc1.set(GregorianCalendar.YEAR, Integer.valueOf(end.split("/")[2]));
                        gc1.set(GregorianCalendar.MONTH, Integer.valueOf(end.split("/")[1]) - 1);
                        gc1.set(GregorianCalendar.DATE, Integer.valueOf(end.split("/")[0]));
                        current.setEndDate(gc1);

                        current.setDescription(thistask[3]);
                        current.setNumCollaborators(Integer.parseInt(thistask[4]));
                        current.setType_key(datalayer.getTypeByName(thistask[6]));
                        if (thistask[7].equals("Open")) {
                            current.setOpen(true);
                        } else {
                            current.setOpen(false);
                        }
                        int task_key = datalayer.storeTask(current);

                        String[] skills = thistask[5].split(";");
                        for (String skl : skills) {
                            String[] split = skl.split("\\(");
                            String n = split[0].trim();
                            int l = Integer.parseInt(split[1].split("\\)")[0]);
                            int skill_key = datalayer.getSkillByName(n);
                            datalayer.storeTaskHasSkill(task_key, skill_key, l);
                        }
                    }
                    datalayer.destroy();
                    String act_url = request.getRequestURI();
                    s.setAttribute("previous_url", act_url);
                    response.sendRedirect("DeveloperForTask?n=" + project_key);
                } else {
                    response.sendRedirect("CreateProject");
                }
            } else {
                response.sendRedirect("CreateProject");
            }
        } else {
            response.sendRedirect("index");
        }
    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {
        try {
            action_insertproject(request, response);
        } catch (IOException ex) {
            request.setAttribute("exception", ex);
            action_error(request, response);
        } catch (DataLayerException ex) {
            request.setAttribute("exception", ex);
            action_error(request, response);
        } catch (SQLException ex) {
            request.setAttribute("exception", ex);
            action_error(request, response);
        } catch (NamingException ex) {
            request.setAttribute("exception", ex);
            action_error(request, response);
        } catch (TemplateManagerException ex) {
            request.setAttribute("excpetion", ex);
            action_error(request, response);
        }
    }
}
