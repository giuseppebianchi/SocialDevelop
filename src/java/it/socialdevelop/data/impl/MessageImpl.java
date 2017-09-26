/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.socialdevelop.data.impl;

import it.univaq.f4i.iw.framework.data.DataLayerException;
import it.socialdevelop.data.model.Developer;
import it.socialdevelop.data.model.Message;
import it.socialdevelop.data.model.Project;
import it.socialdevelop.data.model.SocialDevelopDataLayer;

/**
 *
 * @author Hello World Group
 */
public class MessageImpl implements Message{
    
    private int key;
    private String text;
    private boolean isPrivate;
    private String type;
    private Project project;
    private int project_key;
    private int developer_key;
    private Developer developer;
    protected SocialDevelopDataLayer ownerdatalayer;
    protected boolean dirty;
    
    public MessageImpl(SocialDevelopDataLayer ownerdatalayer) {
        this.ownerdatalayer = ownerdatalayer;
        key = 0;
        text = "";
        project = null;
        project_key = 0;
        isPrivate = false;
        type = "";
        developer = null;
        developer_key = 0;
        dirty = false;
    }

    
    @Override
    public int getKey() {
        return key;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public void setText(String text) {
        this.text = text;
        this.dirty = true;
    }
    
    @Override
    public void setProject(Project project){
        this.project = project;
        this.project_key = project.getKey();
        this.dirty = true;
    }
    
    @Override
    public Project getProject() throws DataLayerException{
        
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
    public void setProjectKey(int project_key){
        this.project = null;
        this.project_key = project_key;
        this.dirty = true;
    }
    
    @Override
    public int getProjectKey(){
        return project_key;
    }
    
    @Override 
    public Developer getDeveloper() throws DataLayerException{
        if(developer == null && developer_key > 0) {
            developer = ownerdatalayer.getDeveloper(developer_key);
        }
        return developer;
    }
    
    @Override
    public void setDeveloper(Developer developer){
        this.developer = developer;
        this.developer_key = developer.getKey();
        this.dirty = true;
    }
    
    @Override
    public void setDeveloperKey(int developer_key){
        this.developer = null;
        this.developer_key = developer_key;
        this.dirty = true;
    }
    
    @Override 
    public int getDeveloperKey(){
        return developer_key;
    }
    @Override
    public boolean isPrivate() {
        return isPrivate;
    }

    @Override
    public void setPrivate(boolean isPrivate) {
        this.isPrivate = isPrivate;
        this.dirty = true;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public void setType(String type) {
        this.type = type;
        this.dirty = true;
    }
    
    @Override
    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    @Override
    public boolean isDirty() {
        return dirty;
    }
    
    protected void setKey(int key) {
        this.key = key;
    }
    
    @Override
    public void copyFrom(Message message) throws DataLayerException {
        key = message.getKey();
        isPrivate = message.isPrivate();
        text = message.getText();
        type = message.getType();
        project_key = message.getProjectKey();
        developer_key = message.getDeveloperKey();
        this.dirty = true;
    }
}
