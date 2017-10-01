package it.socialdevelop.data.impl;

import it.univaq.f4i.iw.framework.data.DataLayerException;
import it.socialdevelop.data.model.Admin;
import it.socialdevelop.data.model.SocialDevelopDataLayer;

/**
 *
 * @author Hello World Group
 */
public class AdminImpl implements Admin {

    private int key;
    private int developerkey;
    protected SocialDevelopDataLayer ownerdatalayer;
    protected boolean dirty;

    public AdminImpl(SocialDevelopDataLayer ownerdatalayer) {
        this.ownerdatalayer = ownerdatalayer;
        key = 0;
        developerkey = 0;
        dirty = false;
    }

    @Override
    public int getKey() {
        return key;
    }

    @Override
    public void setDeveloperKey(int developerkey) {
        this.developerkey = developerkey;
    }

    @Override
    public int getDevelperKey() {
        return developerkey;
    }

    @Override
    public void copyFrom(Admin admin) throws DataLayerException {
        this.key = admin.getKey();
        this.developerkey = admin.getDevelperKey();
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
    public void setKey(int key) {
        this.key = key;
    }
}
