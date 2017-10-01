package it.socialdevelop.data.model;

import it.univaq.f4i.iw.framework.data.DataLayerException;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Period;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Hello World Group
 */
public interface Task {

    int getKey();

    String getName();

    int getProjectKey();

    void setProjectKey(int project_key);

    Project getProject() throws DataLayerException;

    void setProject(Project project);

    void setName(String name);

    void setNumCollaborators(int num); //tramite questa si può anche modificare il numCollaboratori

    int getNumCollaborators();

    boolean isOpen();

    void setOpen(boolean isOpen);

    void setDescription(String description);

    String getDescription();

    //Timestamp getTimeInterval();
    //void setTimeInterval(Timestamp interval);
    GregorianCalendar getStartDate();

    void setStartDate(GregorianCalendar start);

    GregorianCalendar getEndDate();

    void setEndDate(GregorianCalendar end);

    void setType_key(int type_key);

    int getType_key();

    void setType(Type type);

    Type getTypeByTask() throws DataLayerException;

    Map<Skill, Integer> getSkillsByTask() throws DataLayerException;

    void setSkills(Map<Skill, Integer> skill_levelMin);

    void addSkill(Skill skill, int levelMin);

    void removeSkill(Skill skill);

    Map<Developer, Integer> getCollaborators() throws DataLayerException;

    void setCollaborators(List<Developer> listDev);
    //al momento del setCollaborators Integer è null

    void addCollaborator(Developer collaborator);
    //voto all'inizio è null

    void removeCollaborator(Developer collaborator);

    int getVote(Developer collaborator) throws DataLayerException;

    void setVote(Developer collaborator, int vote);

    boolean isFull();

    void setDirty(boolean dirty);

    boolean isDirty();

    void copyFrom(Task task) throws DataLayerException;

    void setKey(int key);

    void setCollaborators(Map<Developer, Integer> developers);

}
