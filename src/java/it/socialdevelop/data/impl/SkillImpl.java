/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.socialdevelop.data.impl;

import it.univaq.f4i.iw.framework.data.DataLayerException;
import java.util.List;
import it.socialdevelop.data.model.Skill;
import it.socialdevelop.data.model.SocialDevelopDataLayer;
import it.socialdevelop.data.model.Type;

/**
 *
 * @author Hello World Group
 */
public class SkillImpl implements Skill{
    
    private int key;
    private String name;
    private int parent_id; //nel caso in cui non ha padre, come parent_id avrà il suo stesso id
    private Skill parent;
    private int type_key;
    private Type type;
    private List<Skill> child; //serve?? PESANTE!
    protected SocialDevelopDataLayer ownerdatalayer;
    protected boolean dirty;
    
    public SkillImpl(SocialDevelopDataLayer ownerdatalayer) {
        this.ownerdatalayer = ownerdatalayer;
        key = 0;
        type_key = 0;
        type = null;
        name = "";
        parent_id = 0;
        parent = null;
        child = null;
        dirty = false;
    }
    
    @Override
    public int getKey(){
        return key;
    }
    
    @Override
    public String getName(){
        return name;
    }
    
    @Override
    public void setName(String name){
        this.name = name;
        this.dirty = true;
    }
    
    @Override
    public void setType_key(int type_key){
        this.type_key = type_key;
        this.dirty = true;
    }
    
    @Override
    public int getType_key(){
        return type_key;
    }
    
    @Override
    public void setType(Type t){
        this.type = t;
        this.type_key = t.getKey();
        this.dirty = true;
    }
    
    
    @Override
    public Type getType() throws DataLayerException{
        if(type == null){
            type = ownerdatalayer.getTypeBySkill(this.key);
        }
        return type;
    }
    
    @Override
    public List<Skill> getChild() throws DataLayerException{
        if(child == null){
            child = ownerdatalayer.getChild(this.key);
        }
        return child;
    }
    
    @Override
    public int getParentKey() throws DataLayerException{
        if(parent_id == 0){
            parent_id = ownerdatalayer.getParent(this.key).getKey();
        }
        return parent_id;
    }
    
    @Override
    public void setParentKey(int skill_id){
        this.parent_id = skill_id;
        this.dirty = true;
    }
    
    
    @Override
    public void setParent(Skill parent) {
        this.parent = parent;
        this.parent_id = parent.getKey();
        this.dirty = true;
    }
    
    @Override
    public Skill getParent() throws DataLayerException{
        //notare come il coordinatore in relazione venga caricato solo su richiesta
        if (parent == null && parent_id > 0) {
            parent = ownerdatalayer.getSkill(parent_id);
        }
        //attenzione: il coordinatore caricato viene lagato all'oggetto in modo da non 
        //dover venir ricaricato alle richieste successive, tuttavia, questo
        //puo' rende i dati potenzialmente disallineati: se il coordinatore viene modificato
        //nel DB, qui rimarrà la sua "vecchia" versione
       
        return parent;
    }
    
    
    @Override
    public void setChild(List<Skill> child){
        this.child = child;
        this.dirty = true;
    }
    
    @Override
    public void addChild(Skill child){
        this.child.add(child);
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
    public void copyFrom(Skill skill) throws DataLayerException {
        key = skill.getKey();
        type_key = skill.getType_key();
        parent_id = skill.getParentKey();
        name = skill.getName();
        this.dirty = true;
    }
}   
