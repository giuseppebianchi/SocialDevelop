package it.socialdevelop.controller;

import it.univaq.f4i.iw.framework.data.DataLayerException;
import it.univaq.f4i.iw.framework.result.FailureResult;
import it.univaq.f4i.iw.framework.result.TemplateManagerException;
import it.univaq.f4i.iw.framework.result.TemplateResult;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import it.socialdevelop.data.model.Admin;
import it.socialdevelop.data.model.Developer;
import it.socialdevelop.data.model.Project;
import it.socialdevelop.data.model.Skill;
import it.socialdevelop.data.model.SocialDevelopDataLayer;
import it.socialdevelop.data.model.Task;

/**
 *
 * @author Hello World Group
 */
public class DeveloperForTask extends SocialDevelopBaseController {

    private void action_error(HttpServletRequest request, HttpServletResponse response) {
        if (request.getAttribute("exception") != null) {
            (new FailureResult(getServletContext())).activate((Exception) request.getAttribute("exception"), request, response);
        }
    }

    private void action_developTask(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, TemplateManagerException, SQLException, NamingException, DataLayerException {
        HttpSession s = request.getSession(true);
        request.setAttribute("page_title", "Suggested Developers");
        request.setAttribute("page_subtitle", "find your collaborators!");
        if (s.getAttribute("userid") != null && ((int) s.getAttribute("userid")) > 0) {
            String u = (String) s.getAttribute("previous_url");
            request.setAttribute("logout", "Logout");

            SocialDevelopDataLayer datalayer = (SocialDevelopDataLayer) request.getAttribute("datalayer");
            Admin admin = datalayer.getAdmin((int) s.getAttribute("userid"));
            if (admin != null) {
                request.setAttribute("admin", "admin");
            }
            Project p = datalayer.getProject(Integer.parseInt(request.getParameter("n")));
            if (p.getCoordinatorKey() == (int) s.getAttribute("userid")) {
                List<Task> tasks = datalayer.getTasks(Integer.parseInt(request.getParameter("n")));
                List<List<Developer>> devs = new ArrayList<List<Developer>>();
                HashMap<Integer, Integer> votes = new HashMap<Integer, Integer>();
                HashMap<Integer, Integer> projects = new HashMap<Integer, Integer>();

                for (Task task : tasks) {
                    List<Developer> devTask = new ArrayList<Developer>();
                    Map<Skill, Integer> skills = datalayer.getSkillsByTask(task.getKey());
                    for (Map.Entry<Skill, Integer> entry : skills.entrySet()) {
                        List<Developer> devSkill = datalayer.getDevelopersBySkillNoLevel(entry.getKey().getKey(), entry.getValue());
                        for (Developer dev : devSkill) {
                            int photo_key = dev.getFoto();
                            if (photo_key > 0) {
                                dev.setFotoFile(datalayer.getFile(photo_key));
                            }
                            int sprojects = datalayer.getProjectCollaborators(dev.getKey()).size() + datalayer.getProjectsByCoordinator(dev.getKey()).size();
                            projects.put(dev.getKey(), sprojects);
                            List<Integer> vote = new ArrayList<Integer>(datalayer.getTasksByDeveloper(dev.getKey()).values());
                            int vote2 = 0;
                            if (votes.size() > 0) {
                                int count2 = 0;

                                for (int vote1 : vote) {
                                    if (vote1 >= 0) {
                                        count2++;
                                        vote2 = vote2 + vote1;
                                    }
                                }
                                if (count2 != 0) {
                                    vote2 = vote2 / count2;
                                }
                            }
                            votes.put(dev.getKey(), vote2);
                            boolean flag = false;
                            List<Developer> taskCollaborators = new ArrayList<Developer>(datalayer.getCollaboratorsByTask(task.getKey()).keySet());
                            for (Developer collaborator : taskCollaborators) {
                                if (collaborator.equals(dev)) {
                                    flag = true;
                                }
                            }
                            if (!devTask.contains(dev) && dev.getKey() != ((int) s.getAttribute("userid")) && !flag) {

                                devTask.add(dev);
                            }

                        }
                    }
                    devs.add(devTask);
                }
                request.setAttribute("votes", votes);
                request.setAttribute("projects", projects);
                request.setAttribute("tasks", tasks);
                request.setAttribute("devs", devs);
                datalayer.destroy();
                TemplateResult res = new TemplateResult(getServletContext());
                res.activate("developer_for_task.html", request, response);
            } else {
                response.sendRedirect("index");
            }
        } else {
            response.sendRedirect("index");
        }
    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {
        try {
            action_developTask(request, response);
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
