package it.socialdevelop.data.impl;

import it.univaq.f4i.iw.framework.data.DataLayerException;
import it.socialdevelop.data.model.SocialDevelopDataLayer;
import it.socialdevelop.data.model.Type;

/**
 *
 * @author Hello World Group
 */
public class TypeImpl implements Type {

    private int key;
    private String type;
    protected SocialDevelopDataLayer ownerdatalayer;
    protected boolean dirty;

    public TypeImpl(SocialDevelopDataLayer ownerdatalayer) {
        this.ownerdatalayer = ownerdatalayer;
        key = 0;
        type = "";
        dirty = false;
    }

    @Override
    public int getKey() {
        return key;
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
    public void copyFrom(Type tipo) throws DataLayerException {
        key = tipo.getKey();
        type = tipo.getType();
        this.dirty = true;
    }
}
