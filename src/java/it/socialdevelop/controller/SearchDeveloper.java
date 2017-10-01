package it.socialdevelop.controller;

import it.univaq.f4i.iw.framework.data.DataLayerException;
import it.univaq.f4i.iw.framework.result.FailureResult;
import it.univaq.f4i.iw.framework.result.TemplateManagerException;
import it.univaq.f4i.iw.framework.result.TemplateResult;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import it.socialdevelop.data.model.Admin;
import it.socialdevelop.data.model.Developer;
import it.socialdevelop.data.model.Files;
import it.socialdevelop.data.model.Skill;
import it.socialdevelop.data.model.SocialDevelopDataLayer;

/**
 *
 * @author Hello World Group
 */
public class SearchDeveloper extends SocialDevelopBaseController {

    private void action_error(HttpServletRequest request, HttpServletResponse response) {
        if (request.getAttribute("exception") != null) {
            (new FailureResult(getServletContext())).activate((Exception) request.getAttribute("exception"), request, response);
        }
    }

    private void action_searchDeveloper(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, TemplateManagerException, SQLException, NamingException, DataLayerException {
        HttpSession s = request.getSession(true);
        request.setAttribute("page_title", "Search Developers");
        request.setAttribute("page_subtitle", "who are you looking for?");
        SocialDevelopDataLayer datalayer = (SocialDevelopDataLayer) request.getAttribute("datalayer");
        if (s.getAttribute("userid") != null && ((int) s.getAttribute("userid")) > 0) {
            request.setAttribute("logout", "Logout");
            Admin admin = datalayer.getAdmin((int) s.getAttribute("userid"));
            if (admin != null) {
                request.setAttribute("admin", "admin");
            }
        }

        List<Integer> developersID = datalayer.getDeveloperByUsernameLike(request.getParameter("username"));
        List<Developer> dev = new ArrayList<Developer>();
        for (int developerID : developersID) {
            dev.add(datalayer.getDeveloper(developerID));
        }
        if (dev.size() != 0) {
            request.setAttribute("listasviluppatori", dev);
            Files foto = null;
            String fotos[] = new String[dev.size()];
            int projects[] = new int[dev.size()];
            int vote[] = new int[dev.size()];
            int count = 0;
            for (Developer developer : dev) {
                int foto_key = (developer).getFoto();
                projects[count] = datalayer.getProjectCollaborators(developer.getKey()).size() + datalayer.getProjectsByCoordinator(developer.getKey()).size();
                List<Integer> votes = new ArrayList<Integer>(datalayer.getTasksByDeveloper(developer.getKey()).values());
                if (votes.size() != 0) {
                    int count2 = 0;
                    vote[count] = 0;
                    for (int vote1 : votes) {
                        if (vote1 >= 0) {
                            count2++;
                            vote[count] = vote[count] + vote1;
                        }
                    }
                    if (count2 != 0) {
                        vote[count] = vote[count] / count2;
                    }
                } else {
                    vote[count] = 0;
                }
                if (foto_key != 0) {
                    foto = datalayer.getFile(foto_key);
                    fotos[count] = "uploaded-images/" + foto.getLocalFile();
                } else {
                    fotos[count] = "uploaded-images/foto_profilo_default.png";
                }
                count++;
            }
            request.setAttribute("foto", fotos);
            request.setAttribute("progetto", projects);
            request.setAttribute("voto", vote);
        } else {
            request.setAttribute("listasviluppatori", dev);
            request.setAttribute("nontrovato", "There are no developers with these parameters in the system..");
        }
        List<Skill> skills = datalayer.getSkillsParentList();
        //ora recuperiamo per ognuna di esse le skills figlie
        if (skills != null) {
            for (Skill skill : skills) {
                List<Skill> child = datalayer.getChild(skill.getKey());
                if (child != null) {
                    skill.setChild(child);
                }
            }
        }
        request.setAttribute("skills", skills);
        datalayer.destroy();
        TemplateResult res = new TemplateResult(getServletContext());
        res.activate("developer_list.html", request, response);
    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {
        try {
            action_searchDeveloper(request, response);
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
