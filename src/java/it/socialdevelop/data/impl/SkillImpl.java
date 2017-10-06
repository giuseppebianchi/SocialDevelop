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
public class SkillImpl implements Skill {

    private int key;
    private String name;
    private int type_key;
    private Type type;
    protected SocialDevelopDataLayer ownerdatalayer;
    protected boolean dirty;

    public SkillImpl(SocialDevelopDataLayer ownerdatalayer) {
        this.ownerdatalayer = ownerdatalayer;
        key = 0;
        type_key = 0;
        type = null;
        name = "";
        dirty = false;
    }

    @Override
    public int getKey() {
        return key;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
        this.dirty = true;
    }

    @Override
    public void setType_key(int type_key) {
        this.type_key = type_key;
        this.dirty = true;
    }

    @Override
    public int getType_key() {
        return type_key;
    }

    @Override
    public void setType(Type t) {
        this.type = t;
        this.type_key = t.getKey();
        this.dirty = true;
    }

    @Override
    public Type getType() throws DataLayerException {
        if (type == null) {
            type = ownerdatalayer.getTypeBySkill(this.key);
        }
        return type;
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
        name = skill.getName();
        this.dirty = true;
    }
}
