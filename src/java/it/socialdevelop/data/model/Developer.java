/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.socialdevelop.data.model;

import it.univaq.f4i.iw.framework.data.DataLayerException;
import java.io.File;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Hello World Group
 */
public interface Developer {
    
    int getKey();
    
    void setUsername(String username);
    
    String getUsername();
    
    void setName(String name);
    
    String getName();
    
    void setSurname(String surname);
    
    String getSurname();
    
    void setMail(String mail);
    
    String getMail();
    
    void setPwd(String pwd);
    
    String getPwd();
    
    void setBirthDate(GregorianCalendar birthdate);
    
    GregorianCalendar getBirthDate();
    
    void setBiography(String biography);
    
    String getBiography();
      
    void setCurriculum(int curriculum);
    
    void setCurriculum(String curriculum);
    
    int getCurriculumFile();
    
    int getFoto();
    
    void setFoto(int foto);
    
    void setFotoFile(Files file);
    
    Files getFotoFile() throws DataLayerException;
    
    String getCurriculumString();
    
    void setSkills(Map<Skill, Integer> skills);
    
    Map<Skill, Integer> getSkillsByDeveloper() throws DataLayerException;

    //per recuperare i voti relativi ai task a cui 
    //il developer ha partecipato, basta un array assoc
    //con Task-Voto, perchè è possibile risalire alle info
    //del coordinatore che ha lasciato la valutazione attraverso il task
    //stesso che ha un unico coordinatore
    Map<Task, Integer> getTasksByDeveloper() throws DataLayerException; 
    //"valutazione" va bene come Integer???
    
    void addSkill(Skill skill, int level);
    
    void removeSkill(Skill skill);
    
    void modSkillLevel(Skill skill, int level);
    
    //List<Project> getProjects ();
    
    void setDirty(boolean dirty);

    boolean isDirty();
    
    void copyFrom(Developer developer) throws DataLayerException;
    
    void setPicture(String picture);
    
    String getPicture();
    
}
