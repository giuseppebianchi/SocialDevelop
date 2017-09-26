/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.socialdevelop.data.model;

import it.univaq.f4i.iw.framework.data.DataLayerException;
import java.util.List;


/**
 *
 * @author Hello World Group
 */

public interface Project  {
    
    int getKey();
    
    void setName(String name);
    
    String getName();
        
    String getDescription();
    
    void setDescription(String description);
        
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
   
}