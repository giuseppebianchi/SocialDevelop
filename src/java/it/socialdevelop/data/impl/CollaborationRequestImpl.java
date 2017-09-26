/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.socialdevelop.data.impl;

import it.univaq.f4i.iw.framework.data.DataLayerException;
import java.util.GregorianCalendar;
import it.socialdevelop.data.model.CollaborationRequest;
import it.socialdevelop.data.model.Developer;
import it.socialdevelop.data.model.SocialDevelopDataLayer;
import it.socialdevelop.data.model.Task;

/**
 *
 * @author Hello World Group
 */


public class CollaborationRequestImpl implements CollaborationRequest{
    
    private Developer coordinator;
    private int coordinator_key;
    private Task task;
    private int task_key;
    private Developer collaborator;
    private int collaborator_key;
    private GregorianCalendar date;
    private int state; //0 in attesa, 1 confermata, 2 completata
    private int sender_key; //chiave sender
    private Developer sender;
    private int vote;
    
    protected SocialDevelopDataLayer ownerdatalayer;
    protected boolean dirty;
    
    public CollaborationRequestImpl(SocialDevelopDataLayer ownerdatalayer) {
        this.ownerdatalayer = ownerdatalayer;
        coordinator = null;
        coordinator_key = 0;
        task = null;
        sender_key = 0;
        sender = null;
        task_key = 0;
        collaborator = null;
        collaborator_key = 0;
        date = null;
        state = 0;
        vote = 0;
        dirty = false;
    }
    
    @Override
    public int getSender_key(){
        return sender_key;
    }
    
    @Override 
    public void setSender_key(int sender_key){
        this.sender_key = sender_key;
        this.dirty = true;
    }
    
    @Override
    public Developer getSender() throws DataLayerException{
        if(sender == null){
            sender = ownerdatalayer.getDeveloper(sender_key);
        }
        return sender;
    }
    
    @Override
    public void setSender(Developer sender){
        this.sender = sender;
        this.sender_key = sender.getKey();
    }
    
    
    @Override
    public Developer getCoordinatorRequest() throws DataLayerException{
        if(coordinator == null){
            coordinator = ownerdatalayer.getCoordinatorByTask(this.task_key);
        }
        return coordinator;
    }

    @Override
    public void setCoordinatorRequest(Developer coordinator) {
        this.coordinator = coordinator;
        this.coordinator_key = coordinator.getKey();
        this.dirty = true;
    }

    @Override
    public void setCoordinatorKey(int coordinator_key){
        this.coordinator_key = coordinator_key;
        this.coordinator = null;
        this.dirty = true;
    }
    
    @Override
    public int getCoordinatorKey(){
        return coordinator_key;
    }
    
    @Override
    public Task getTaskByRequest() throws DataLayerException {
        if(task == null){
            task = ownerdatalayer.getTask(task_key);
        }
        return task;
    }
    
    @Override
    public void setTaskKey(int task_key){
        this.task_key = task_key;
        this.task = null;
        this.dirty = true;
    }
    
    @Override
    public int getTaskKey(){
        return task_key;
    }

    @Override
    public void setTaskRequest(Task task) {
        this.task = task;
        this.task_key = task.getKey();
        this.dirty = true;
    }

    @Override
    public Developer getCollaboratorRequest() throws DataLayerException {
        if(collaborator == null){
            collaborator = ownerdatalayer.getDeveloper(this.collaborator_key);
        }
        return collaborator;
    }

    @Override
    public void setCollaboratorRequest(Developer collaborator) {
        this.collaborator = collaborator;
        this.collaborator_key = collaborator.getKey();
        this.dirty = true;
    }
    
    
    @Override
    public void setCollaboratorKey(int collaborator_key){
        this.collaborator_key = collaborator_key;
        this.collaborator = null;
        this.dirty = true;
    }
    
    @Override
    public int getCollaboratorKey(){
        return collaborator_key;
    }

    @Override
    public void setVote(int vote){
        this.vote = vote;
        this.dirty = true;
    }
    
    @Override
    public int getVote(){
        return vote;
    }
    
    @Override
    public GregorianCalendar getDate() {
        return date;
    }

    @Override
    public void setDate(GregorianCalendar data) {
        this.date = data;
        this.dirty = true;
    }

    @Override
    public int getState() {
        return state;
    }

    @Override
    public void setState(int state) {
        this.state = state;
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
    
    @Override
    public void copyFrom(CollaborationRequest request) throws DataLayerException {
        task_key = request.getTaskKey();
        collaborator_key = request.getCollaboratorKey();
        vote = request.getVote();
        date = request.getDate();
        sender_key = request.getSender_key();
        state = request.getState();
        coordinator_key = request.getCoordinatorKey();
        this.dirty = true;
    }
        
}
