package it.univaq.f4i.iw.framework.data;

/**
 *
 * @author Giuseppe Della Penna
 */
public interface DataLayer extends AutoCloseable {

    void init() throws DataLayerException;

    void destroy() throws DataLayerException;
}
