/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.socialdevelop.data.impl;

import it.univaq.f4i.iw.framework.data.DataLayerException;
import java.util.GregorianCalendar;
import it.socialdevelop.data.model.Developer;
import it.socialdevelop.data.model.SocialDevelopDataLayer;
import it.socialdevelop.data.model.Files;

/**
 *
 * @author Hello World Group
 */
public class FilesImpl implements Files {
    private int key;
    private int size;
    private String name;
    private String localFile;
    private GregorianCalendar updated;
    private String digest;
    private String type;
    
    protected SocialDevelopDataLayer ownerdatalayer;
    private boolean dirty;
    
    public FilesImpl(SocialDevelopDataLayer ownerdatalayer) {
        this.ownerdatalayer = ownerdatalayer;
        key = 0;
        size = 0;
        name = "";
        localFile = "";
        updated = null;
        digest = "";
        type = "";
        dirty = false;
    }

    /**
     * @return the key
     */

    @Override
    public int getKey() {
        return key;
    }

    /**
     * @param key the key to set
     */
   
    public void setKey(int key) {
        this.key = key;
        this.dirty = true;
    }

    /**
     * @return the name
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    @Override
    public void setName(String name) {
        this.name = name;
        this.dirty = true;
    }

    /**
     * @return the localFile
     */
    @Override
    public String getLocalFile() {
        return localFile;
    }

    /**
     * @param localFile the localFile to set
     */
    @Override
    public void setLocalFile(String localFile) {
        this.localFile = localFile;
        this.dirty = true;
    }

    /**
     * @return the updated
     */
    @Override
    public GregorianCalendar getUpdated() {
        return updated;
    }

    /**
     * @param updated the updated to set
     */
    @Override
    public void setUpdated(GregorianCalendar updated) {
        this.updated = updated;
        this.dirty = true;
    }

    /**
     * @return the digest
     */
    @Override
    public String getDigest() {
        return digest;
    }

    /**
     * @param digest the digest to set
     */
    @Override
    public void setDigest(String digest) {
        this.digest = digest;
        this.dirty = true;
    }

    /**
     * @return the type
     */
    @Override
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    @Override
    public void setType(String type) {
        this.type = type;
        this.dirty = true;
    }

    /**
     * @return the dirty
     */
    @Override
    public boolean isDirty() {
        return dirty;
    }

    /**
     * @param dirty the dirty to set
     */
    @Override
    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }
    
    @Override
    public void copyFrom(Files file) throws DataLayerException {
        key = file.getKey();
        name = file.getName();
        type = file.getType();
        digest = file.getDigest();
        localFile = file.getLocalFile();
        this.dirty = true;
    }

    /**
     * @return the size
     */
    @Override
    public int getSize() {
        return size;
    }

    /**
     * @param size the size to set
     */
    @Override
    public void setSize(int size) {
        this.size = size;
        this.dirty = true;        
    }
}
