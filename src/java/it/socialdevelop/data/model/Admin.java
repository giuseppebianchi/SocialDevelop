/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.socialdevelop.data.model;

import it.univaq.f4i.iw.framework.data.DataLayerException;

/**
 *
 * @author Hello World Group
 */

public interface Admin {
    
    int getKey();
    
    void setKey(int key);
    
    void setDeveloperKey(int developer);
    
    int getDevelperKey();
    
    void setDirty(boolean dirty);

    boolean isDirty();
    
    void copyFrom(Admin admin) throws DataLayerException;
}
