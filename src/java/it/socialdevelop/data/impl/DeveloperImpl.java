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
    private String pwd;
    private String mail;
    private GregorianCalendar birthdate;
    private String biography;
    private String curriculumString;
    private int curriculumFile;
    private int foto;
    private Files fotoFile;
    private Map<Skill, Integer> skills;
    protected SocialDevelopDataLayer ownerdatalayer;
    protected boolean dirty;

    public DeveloperImpl(SocialDevelopDataLayer ownerdatalayer) {
        this.ownerdatalayer = ownerdatalayer;
        key = 0;
        name = "";
        surname = "";
        username = "";
        picture = "blog-image-4.png";
        headline = "";
        pwd = "";
        mail = "";
        foto = 0;
        fotoFile = null;
        birthdate = null;
        biography = "";
        curriculumString = "";
        curriculumFile = 0;
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
    public void setPwd(String pwd) {
        this.pwd = pwd;
        this.dirty = true;
    }

    @Override
    public String getPwd() {
        return pwd;
    }

    @Override
    public void setBirthDate(GregorianCalendar birthdate) {
        this.birthdate = birthdate;
        this.dirty = true;
    }

    @Override
    public GregorianCalendar getBirthDate() {
        return birthdate;
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
    public void setCurriculum(int curriculum) {
        this.curriculumFile = curriculum;
        this.dirty = true;
    }

    @Override
    public void setCurriculum(String curriculum) {
        this.curriculumString = curriculum;
        this.dirty = true;
    }

    @Override
    public int getCurriculumFile() {
        return curriculumFile;
    }

    @Override
    public String getCurriculumString() {
        return curriculumString;
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

    protected void setKey(int key) {
        this.key = key;
    }

    @Override
    public void setFoto(int foto) {
        this.foto = foto;
    }

    @Override
    public int getFoto() {
        return foto;
    }

    @Override
    public void copyFrom(Developer developer) throws DataLayerException {
        key = developer.getKey();
        name = developer.getName();
        surname = developer.getSurname();
        username = developer.getUsername();
        mail = developer.getMail();
        pwd = developer.getPwd();
        birthdate = developer.getBirthDate();
        biography = developer.getBiography();
        curriculumFile = developer.getCurriculumFile();
        curriculumString = developer.getCurriculumString();
        foto = developer.getFoto();
        this.dirty = true;
    }

    /**
     * @return the fotoFile
     */
    @Override
    public Files getFotoFile() throws DataLayerException {
        if (foto > 0 && fotoFile == null) {
            this.fotoFile = ownerdatalayer.getFile(foto);
        }
        return fotoFile;
    }

    /**
     * @param fotoFile the fotoFile to set
     */
    @Override
    public void setFotoFile(Files fotoFile) {
        this.fotoFile = fotoFile;
        this.foto = fotoFile.getKey();
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
                && dev.getSurname().equals(surname) && dev.getUsername().equals(username) && dev.getPwd().equals(pwd)
                && dev.getMail().equals(mail) && dev.getBirthDate().equals(birthdate) && dev.getBiography().equals(biography)
                && dev.getCurriculumString().equals(curriculumString) && dev.getCurriculumFile() == curriculumFile && dev.getFoto() == foto;

    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + this.key;
        hash = 67 * hash + Objects.hashCode(this.name);
        hash = 67 * hash + Objects.hashCode(this.surname);
        hash = 67 * hash + Objects.hashCode(this.username);
        hash = 67 * hash + Objects.hashCode(this.pwd);
        hash = 67 * hash + Objects.hashCode(this.mail);
        hash = 67 * hash + Objects.hashCode(this.birthdate);
        hash = 67 * hash + Objects.hashCode(this.biography);
        hash = 67 * hash + Objects.hashCode(this.curriculumString);
        hash = 67 * hash + this.curriculumFile;
        hash = 67 * hash + this.foto;
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

}
