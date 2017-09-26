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
public interface Message {
    
    int getKey();
    
    void setText(String msg);
    
    String getText();
    
    void setProject(Project project);
    
    Project getProject() throws DataLayerException;
    
    void setProjectKey(int project_key);
    
    int getProjectKey();
    
    void setPrivate(boolean flag);
    
    boolean isPrivate();
    
    void setType(String type); //type in {annuncio,discussione,richiesta}
    
    String getType();
    
    void setDirty(boolean dirty);

    boolean isDirty();
    
     void copyFrom(Message message) throws DataLayerException;
     
      
    Developer getDeveloper() throws DataLayerException;
    
    void setDeveloper(Developer dev);
    
    int getDeveloperKey();
    
    void setDeveloperKey(int dev_key);
}
