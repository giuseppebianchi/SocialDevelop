package it.socialdevelop.data.model;

import it.univaq.f4i.iw.framework.data.DataLayerException;

/**
 *
 * @author Hello World Group
 */
public interface Type {

    int getKey();

    void setType(String type);

    String getType();

    void setDirty(boolean dirty);

    boolean isDirty();

    void copyFrom(Type type) throws DataLayerException;
}
