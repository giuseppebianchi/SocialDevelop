/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.socialdevelop.data.model;

import it.univaq.f4i.iw.framework.data.DataLayerException;
import java.util.GregorianCalendar;

/**
 *
 * @author Hello World Group
 */
public interface Files {

    /**
     * @return the digest
     */
    String getDigest();

    /**
     * @return the key
     */
    int getKey();

    /**
     * @return the localFile
     */
    String getLocalFile();

    /**
     * @return the name
     */
    String getName();

    /**
     * @return the type
     */
    String getType();

    /**
     * @return the updated
     */
    GregorianCalendar getUpdated();

    /**
     * @return the dirty
     */
    boolean isDirty();

    /**
     * @param digest the digest to set
     */
    void setDigest(String digest);

    /**
     * @param dirty the dirty to set
     */
    void setDirty(boolean dirty);


    /**
     * @param localFile the localFile to set
     */
    void setLocalFile(String localFile);

    /**
     * @param name the name to set
     */
    void setName(String name);

    /**
     * @param type the type to set
     */
    void setType(String type);

    /**
     * @param updated the updated to set
     */
    void setUpdated(GregorianCalendar updated);
    
    void setSize(int size);
    
    int getSize();
    
    void copyFrom(Files files) throws DataLayerException;
}
