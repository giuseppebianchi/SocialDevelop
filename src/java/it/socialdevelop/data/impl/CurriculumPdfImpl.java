/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.socialdevelop.data.impl;

import java.util.GregorianCalendar;
import it.socialdevelop.data.model.CurriculumPdf;
import it.socialdevelop.data.model.SocialDevelopDataLayer;

/**
 *
 * @author Hello World Group
 */
public class CurriculumPdfImpl implements CurriculumPdf{
    private int key;
    private String name;
    private int size;
    private String localfile;
    private GregorianCalendar updated;
    private String digest;
    protected SocialDevelopDataLayer ownerdatalayer;
    private boolean dirty;
    
     public CurriculumPdfImpl(SocialDevelopDataLayer ownerdatalayer) {
        this.ownerdatalayer = ownerdatalayer;
        key = 0;
        name = "";
        size = 0;
        localfile = "";
        updated = null;
        digest = "";
        dirty = false;
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

    /**
     * @return the localfile
     */
    @Override
    public String getLocalfile() {
        return localfile;
    }

    /**
     * @param localfile the localfile to set
     */
    @Override
    public void setLocalfile(String localfile) {
        this.localfile = localfile;
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
     
    @Override
    public int getKey(){
        return key;
    }
}
