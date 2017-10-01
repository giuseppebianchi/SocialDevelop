package it.socialdevelop.data.impl;

import it.univaq.f4i.iw.framework.data.DataLayerException;
import java.util.GregorianCalendar;
import java.util.HashMap;
import it.socialdevelop.data.model.Task;
import it.socialdevelop.data.model.Type;
import it.socialdevelop.data.model.Skill;
import java.util.Map;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import it.socialdevelop.data.model.Developer;
import it.socialdevelop.data.model.Project;
import it.socialdevelop.data.model.SocialDevelopDataLayer;

/**
 *
 * @author Hello World Group
 */
public class TaskImpl implements Task {

    private int key;
    private String name;
    //private Timestamp timeInterval;
    private GregorianCalendar start;
    private GregorianCalendar end;
    private boolean open;
    private int numCollaborators;
    private String description;
    private int type_key;
    private Type type;
    private Map<Skill, Integer> skills;
    private Map<Developer, Integer> collaborators;
    private Project project;
    private int project_key;

    protected SocialDevelopDataLayer ownerdatalayer;
    protected boolean dirty;

    public TaskImpl(SocialDevelopDataLayer ownerdatalayer) {
        this.ownerdatalayer = ownerdatalayer;
        key = 0;
        name = "";
        start = null;
        end = null;
        open = true;
        numCollaborators = 0;
        description = "";
        type = null;
        type_key = 0;
        skills = null;
        collaborators = null;
        dirty = false;
        project = null;
        project_key = 0;
    }

    @Override
    public int getKey() {
        return key;
    }

    @Override
    public void setNumCollaborators(int num) {
        this.numCollaborators = num;
        this.dirty = true;
    }

    @Override
    public int getNumCollaborators() {
        return numCollaborators;
    }

    @Override
    public void setProjectKey(int project_key) {
        this.project_key = project_key;
        this.project = null;
        this.dirty = true;
    }

    @Override
    public int getProjectKey() {
        return project_key;
    }

    @Override
    public void setProject(Project project) {
        this.project = project;
        this.project_key = project.getKey();
        this.dirty = true;
    }

    @Override
    public Project getProject() throws DataLayerException {
        //notare come il coordinatore in relazione venga caricato solo su richiesta
        if (project == null && project_key > 0) {
            project = ownerdatalayer.getProject(project_key);
        }
        //attenzione: il coordinatore caricato viene lagato all'oggetto in modo da non 
        //dover venir ricaricato alle richieste successive, tuttavia, questo
        //puo' rende i dati potenzialmente disallineati: se il coordinatore viene modificato
        //nel DB, qui rimarrà la sua "vecchia" versione

        return project;
    }

    @Override
    public void setOpen(boolean isOpen) {
        this.open = isOpen;
        this.dirty = true;
    }

    @Override
    public boolean isOpen() {
        return open;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
        this.dirty = true;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public GregorianCalendar getStartDate() {
        return start;
    }

    @Override
    public GregorianCalendar getEndDate() {
        return end;
    }

    @Override
    public void setStartDate(GregorianCalendar start) {
        this.start = start;
        this.dirty = true;
    }

    @Override
    public void setEndDate(GregorianCalendar end) {
        this.end = end;
        this.dirty = true;
    }

    @Override
    public Type getTypeByTask() throws DataLayerException {
        if (type == null) {
            type = ownerdatalayer.getTypeByTask(this.type_key);
        }
        return type;
    }

    @Override
    public void setType(Type type) {
        this.type = type;
        this.dirty = true;
    }

    @Override
    public void setType_key(int type_key) {
        this.type_key = type_key;
        this.dirty = true;
    }

    @Override
    public int getType_key() {
        return type_key;
    }

    @Override
    public Map<Skill, Integer> getSkillsByTask() throws DataLayerException {
        if (skills == null) {
            skills = ownerdatalayer.getSkillsByTask(this.key);
        }
        return skills;
    }

    @Override
    public void setSkills(Map<Skill, Integer> skill_levelMin) {
        this.skills = skill_levelMin;
        this.dirty = true;
    }

    @Override
    public void addSkill(Skill skill, int levelMin) {
        this.skills.put(skill, levelMin);
        this.dirty = true;
    }

    @Override
    public void removeSkill(Skill skill) {
        this.skills.remove(skill);
        this.dirty = true;
    }

    @Override
    public Map<Developer, Integer> getCollaborators() throws DataLayerException {
        if (collaborators == null) {
            collaborators = ownerdatalayer.getCollaboratorsByTask(this.key);
        }
        return collaborators;
    }

    @Override
    public void setCollaborators(List<Developer> developers) {
        Map<Developer, Integer> coll = new HashMap<>();
        for (Developer developer : developers) {
            coll.put(developer, null);
        }
        this.collaborators = coll;
        this.dirty = true;
    }

    @Override
    public void setCollaborators(Map<Developer, Integer> developers) {
        this.collaborators = developers;
        this.dirty = true;
    }

    @Override
    public void addCollaborator(Developer collaborator) {
        this.collaborators.put(collaborator, null);
        this.dirty = true;
    }

    @Override
    public void removeCollaborator(Developer developer) {
        this.collaborators.remove(developer);
        this.dirty = true;
    }

    @Override
    public int getVote(Developer developer) throws DataLayerException {
        if (collaborators.get(developer) == null) {
            collaborators.put(developer, ownerdatalayer.getVote(this.key, developer.getKey()));
        }
        return collaborators.get(developer);
    }

    @Override
    public void setVote(Developer developer, int vote) {
        this.collaborators.put(developer, vote);
        this.dirty = true;
    }

    @Override
    public boolean isFull() {
        boolean isFull = false;
        try {
            isFull = (numCollaborators == this.getCollaborators().size());
        } catch (DataLayerException ex) {
            Logger.getLogger(TaskImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return isFull;
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
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
        this.dirty = true;
    }

    @Override
    public void copyFrom(Task task) throws DataLayerException {
        key = task.getKey();
        project_key = task.getProject().getKey();
        name = task.getName();
        numCollaborators = task.getNumCollaborators();
        start = task.getStartDate();
        end = task.getEndDate();
        description = task.getDescription();
        open = task.isOpen();
        type_key = task.getType_key();
        this.dirty = true;
    }
}
