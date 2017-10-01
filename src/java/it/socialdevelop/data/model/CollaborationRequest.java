package it.socialdevelop.data.model;

import it.univaq.f4i.iw.framework.data.DataLayerException;
import java.util.GregorianCalendar;

/**
 *
 * @author Hello World Group
 */
public interface CollaborationRequest {

    Developer getCoordinatorRequest() throws DataLayerException;

    void setCoordinatorRequest(Developer coordinator);

    int getCoordinatorKey();

    void setCoordinatorKey(int coordinator_key);

    int getSender_key();

    void setSender_key(int sender_key);

    Developer getSender() throws DataLayerException;

    void setSender(Developer sender);

    Developer getCollaboratorRequest() throws DataLayerException;

    void setCollaboratorRequest(Developer collaborator);

    void setCollaboratorKey(int collaborator_key);

    int getCollaboratorKey();

    GregorianCalendar getDate();

    void setDate(GregorianCalendar date);

    int getState();

    void setState(int state);

    int getVote();

    void setVote(int vote);

    Task getTaskByRequest() throws DataLayerException;

    void setTaskRequest(Task task);

    void setTaskKey(int task_key);

    int getTaskKey();

    void setDirty(boolean dirty);

    boolean isDirty();

    void copyFrom(CollaborationRequest request) throws DataLayerException;

}
