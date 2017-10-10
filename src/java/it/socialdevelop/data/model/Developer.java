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
    
    void setKey(int key);
    
    void setUsername(String username);

    String getUsername();

    void setName(String name);

    String getName();

    void setSurname(String surname);

    String getSurname();

    void setMail(String mail);

    String getMail();

    void setPassword(String password);

    String getPassword();

    void setBiography(String biography);

    String getBiography();
    
    void setSkills(Map<Skill, Integer> skills);

    Map<Skill, Integer> getSkillsByDeveloper() throws DataLayerException;

    
    Map<Task, Integer> getTasksByDeveloper() throws DataLayerException;

    void addSkill(Skill skill, int level);

    void removeSkill(Skill skill);

    void modSkillLevel(Skill skill, int level);

    //List<Project> getProjects ();
    void setDirty(boolean dirty);

    boolean isDirty();

    void copyFrom(Developer developer) throws DataLayerException;

    void setPicture(String picture);

    String getPicture();
    
    void setHeadline(String picture);

    String getHeadline();
    
    void setResume(String resume);
    
    String getResume();
    
    String getTextBio();
    
    String getTextResume();

}
