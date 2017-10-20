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
import it.socialdevelop.data.model.Task;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import org.json.JSONObject;

/**
 *
 * @author Hello World Group
 */
public class DevelopersFilter extends SocialDevelopBaseController {

    private void action_error(HttpServletRequest request, HttpServletResponse response) {
        if (request.getAttribute("exception") != null) {
            (new FailureResult(getServletContext())).activate((Exception) request.getAttribute("exception"), request, response);
        }
    }

    private void action_developers(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, TemplateManagerException, SQLException, NamingException, DataLayerException {
        HttpSession s = request.getSession(true);
        request.setAttribute("request", request);
        request.setAttribute("page_title", "Search Developers");
        request.setAttribute("page_subtitle", "who are you looking for?");
        SocialDevelopDataLayer datalayer = (SocialDevelopDataLayer) request.getAttribute("datalayer");
        if (s.getAttribute("userid") != null && ((int) s.getAttribute("userid")) > 0) {
            request.setAttribute("logout", "Logout");
            request.setAttribute("auth_user", s.getAttribute("userid"));
            request.setAttribute("foto", s.getAttribute("foto"));
            request.setAttribute("fullname", s.getAttribute("fullname"));
            Admin admin = datalayer.getAdmin((int) s.getAttribute("userid"));
            if (admin != null) {
                request.setAttribute("admin", "admin");
            }
        }
 
        // this parses the json
        String JSONData = request.getParameter("data");
        JSONObject o = new JSONObject(JSONData);
        Map<Integer, Developer> dev_list = new HashMap<Integer, Developer>(); 
        Map<Integer, Developer> dev_temp = new HashMap<Integer, Developer>(); 
        List<Map<String, Integer>> query = new ArrayList<Map<String, Integer>>();
        try {
            Iterator<String> temp = o.keys();
            while (temp.hasNext()) {
                //per ogni skill
                String key = temp.next();
                JSONObject skill = o.getJSONObject(key);
                int skill_id = skill.getInt("key");
                String skill_name = skill.getString("name");
                int level = skill.getInt("level");
                //aggiungo il name e il level alla query da visualizzare
                Map<String, Integer> sk = new HashMap<String, Integer>();
                sk.put(skill_name, level);
                query.add(sk);
                
                List<Developer> ds = datalayer.getDevelopersBySkillNoLevel(skill_id, level);
                if(key.equals("0")){
                    //aggiungo tutti i risultati in dev_list
                    for(int i = 0; i<ds.size(); i++){
                       dev_list.put(ds.get(i).getKey(), ds.get(i));
                    }
                }else{
                   //se gli sviluppatori si trovano gia in dev_list li aggiungo a temp
                    for(int i = 0; i<ds.size(); i++){
                        if(dev_list.get(ds.get(i).getKey()) != null){
                            dev_temp.put(ds.get(i).getKey(), ds.get(i));
                        }
                    }
                    //svuoto dev list e la riempio con temp
                    dev_list.clear();
                    for (Entry<Integer, Developer> d : dev_temp.entrySet()){
                        //iterate over the pairs
                        dev_list.put(d.getKey(), d.getValue());
                    }
                }
                
                
                //int skill = temp.getValue();
                //JSONObject task = tasks.getJSONObject(key);
            }
        } catch (Exception ex){
            
        } 
        //Map<Developer, Integer> devs = datalayer.getDevelopersBySkill();
        request.setAttribute("filter", 1);
        request.setAttribute("query", query);
        //List<Developer> devs = datalayer.getDevelopers();
        List<Developer> devs = new ArrayList<Developer>(dev_list.values());
        request.setAttribute("devs", devs);
        int[] votes = new int[devs.size()];
        
        for (int i=0; i < devs.size(); i++) {
            int j = 0;
            Map<Task, Integer> tasks = datalayer.getTasksByDeveloper(devs.get(i).getKey());
            
            for (Entry<Task,Integer> t : tasks.entrySet()){
                Task task = t.getKey();    
                if (task.isCompleted()) {
                    votes[i] += t.getValue();
                    j++;
                }
            }
            if(j != 0){
               votes[i] = (int) votes[i]/j;   
            }
                      
        }
        
        
        List<Skill> skills = datalayer.getSkills();
        request.setAttribute("skills", skills);
        
        request.setAttribute("votes", votes);
        
        datalayer.destroy();
        TemplateResult res = new TemplateResult(getServletContext());
        res.activate("developers.ftl.html", request, response);
    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {
        try {
            action_developers(request, response);
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
