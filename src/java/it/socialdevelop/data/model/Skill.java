package it.socialdevelop.data.model;

import it.univaq.f4i.iw.framework.data.DataLayerException;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Hello World Group
 */
public interface Skill {

    int getKey();

    void setName(String name);

    String getName();

    void setDirty(boolean dirty);

    boolean isDirty();

    void setType_key(int type_key);

    int getType_key();

    Type getType() throws DataLayerException;

    void setType(Type t);

    void copyFrom(Skill skill) throws DataLayerException;

}
