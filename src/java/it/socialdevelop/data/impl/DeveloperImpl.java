package it.socialdevelop.data.impl;

import it.univaq.f4i.iw.framework.data.DataLayerException;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.Objects;
import it.socialdevelop.data.model.Developer;
import it.socialdevelop.data.model.Files;
import it.socialdevelop.data.model.Skill;
import it.socialdevelop.data.model.SocialDevelopDataLayer;
import it.socialdevelop.data.model.Task;
import org.jsoup.Jsoup;

/**
 *
 * @author Hello World Group
 */
public class DeveloperImpl implements Developer {

    private int key;
    private String name;
    private String surname;
    private String username;
    private String picture;
    private String headline;
    private String password;
    private String mail;
    private String biography;
    private String resume;
    private Map<Skill, Integer> skills;
    protected SocialDevelopDataLayer ownerdatalayer;
    protected boolean dirty;

    public DeveloperImpl(SocialDevelopDataLayer ownerdatalayer) {
        this.ownerdatalayer = ownerdatalayer;
        key = 0;
        name = "";
        surname = "";
        username = "";
        picture = "";
        headline = "";
        password = "";
        mail = "";
        biography = "";
        resume = "";
        skills = null;
        dirty = false;
    }

    @Override
    public int getKey() {
        return key;
    }

    @Override
    public void setUsername(String username) {
        this.username = username;
        this.dirty = true;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public void setName(String name) {
        this.name = name;
        this.dirty = true;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setSurname(String surname) {
        this.surname = surname;
        this.dirty = true;
    }

    @Override
    public String getSurname() {
        return surname;
    }

    @Override
    public void setMail(String mail) {
        this.mail = mail;
        this.dirty = true;
    }

    @Override
    public String getMail() {
        return mail;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
        this.dirty = true;
    }

    @Override
    public String getPassword() {
        return password;
    }

    

    @Override
    public void setBiography(String biography) {
        this.biography = biography;
        this.dirty = true;
    }

    @Override
    public String getBiography() {
        return biography;
    }

    

    @Override
    public void setSkills(Map<Skill, Integer> skills) {
        this.skills = skills;
        this.dirty = true;
    }

    @Override
    public Map<Skill, Integer> getSkillsByDeveloper() throws DataLayerException {
        if (skills == null) {
            skills = ownerdatalayer.getSkillsByDeveloper(this.key);
        }
        return skills;
    }

    @Override
    public void addSkill(Skill skill, int level) {
        this.skills.put(skill, level);
        this.dirty = true;
    }

    @Override
    public void removeSkill(Skill skill) {
        this.skills.remove(skill);
        this.dirty = true;
    }

    @Override
    public void modSkillLevel(Skill skill, int level) {
        this.skills.put(skill, level);
        this.dirty = true;
    }

    @Override
    public Map<Task, Integer> getTasksByDeveloper() throws DataLayerException {
        return ownerdatalayer.getTasksByDeveloper(this.key);
    }

    @Override
    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    @Override
    public boolean isDirty() {
        return dirty;
    }

    @Override
    public void setKey(int key) {
        this.key = key;
    }

    

    @Override
    public void copyFrom(Developer developer) throws DataLayerException {
        key = developer.getKey();
        name = developer.getName();
        surname = developer.getSurname();
        username = developer.getUsername();
        headline = developer.getHeadline();
        mail = developer.getMail();
        password = developer.getPassword();
        biography = developer.getBiography();
        resume = developer.getResume();
        picture = developer.getPicture();
        this.dirty = true;
    }

    

    @Override
    public boolean equals(Object o) {

        if (o == this) {
            return true;
        }
        if (!(o instanceof Developer)) {
            return false;
        }

        Developer dev = (Developer) o;

        return dev.getKey() == key && dev.getName().equals(name)
                && dev.getSurname().equals(surname) && dev.getUsername().equals(username) && dev.getPassword().equals(password)
                && dev.getMail().equals(mail) && dev.getPicture().equals(picture) && dev.getBiography().equals(biography);

    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + this.key;
        hash = 67 * hash + Objects.hashCode(this.name);
        hash = 67 * hash + Objects.hashCode(this.surname);
        hash = 67 * hash + Objects.hashCode(this.username);
        hash = 67 * hash + Objects.hashCode(this.password);
        hash = 67 * hash + Objects.hashCode(this.mail);
        hash = 67 * hash + Objects.hashCode(this.picture);
        hash = 67 * hash + Objects.hashCode(this.biography);
        hash = 67 * hash + Objects.hashCode(this.headline);
        hash = 67 * hash + Objects.hashCode(this.resume);
        return hash;
    }

    @Override
    public void setPicture(String picture) {
        this.picture = picture;
        this.dirty = true;
    }

    @Override
    public String getPicture() {
        return picture;
    }
    
    @Override
    public void setHeadline(String headline) {
        this.headline = headline;
        this.dirty = true;
    }

    @Override
    public String getHeadline() {
        return headline;
    }
    
    @Override
    public void setResume(String resume) {
        this.resume = resume;
        this.dirty = true;
    }

    @Override
    public String getResume() {
        return resume;
    }
    
    @Override
    public String getTextBio() {
        return Jsoup.parse(this.biography).text();
    }
    
    @Override
    public String getTextResume() {
        return Jsoup.parse(this.resume).text();
    }

}
