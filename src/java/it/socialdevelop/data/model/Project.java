package it.socialdevelop.data.model;

import it.univaq.f4i.iw.framework.data.DataLayerException;
import java.util.List;

/**
 *
 * @author Hello World Group
 */
public interface Project {

    int getKey();

    void setName(String name);

    String getName();
    
    void setCategory(String category);
    
    String getCategory();

    String getDescription();

    void setDescription(String description);
    
    String getLocation();

    void setLocation(String location);
    
    String getCompany();

    void setCompany(String company);

    void setCoordinator(Developer coordinator);

    Developer getCoordinator() throws DataLayerException;

    void setCoordinatorKey(int coordinator_key);

    int getCoordinatorKey();

    List<Task> getTasks() throws DataLayerException;

    void setTasks(List<Task> tasks);

    void addTask(Task task);

    void removeTask(Task task);

    List<Message> getMessages() throws DataLayerException;

    //List<Message> getPublicMessages() throws DataLayerException;
    void addMessage(Message message);

    void removeMessage(Message message);

    void setDirty(boolean dirty);

    boolean isDirty();

    void copyFrom(Project project) throws DataLayerException;

    void setKey(int key);

    void setPicture(String picture);

    String getPicture();
    
    String getTextDescription();

}
