/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.socialdevelop.data.model;

import java.util.GregorianCalendar;

/**
 *
 * @author Hello World Group
 */
public interface CurriculumPdf {
    
    int getKey();
    
    String getName();
    
    void setName(String name);
    
    int getSize();
    
    void setSize(int size);
    
    String getLocalfile();
    
    void setLocalfile(String localfile);
    
    GregorianCalendar getUpdated();
    
    void setUpdated(GregorianCalendar updated);
    
    String getDigest();
    
    void setDigest(String digest);
    
    void setDirty(boolean dirty);

    boolean isDirty();
}
