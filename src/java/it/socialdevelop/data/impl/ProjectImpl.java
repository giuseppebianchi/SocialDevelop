package it.socialdevelop.data.impl;

import org.jsoup.Jsoup;
import it.univaq.f4i.iw.framework.data.DataLayerException;
import it.socialdevelop.data.model.Developer;
import it.socialdevelop.data.model.Project;
import it.socialdevelop.data.model.Task;
import java.util.List;
import it.socialdevelop.data.model.Message;
import it.socialdevelop.data.model.SocialDevelopDataLayer;

/**
 *
 * @author Hello World Group
 */
public class ProjectImpl implements Project {

    private int key;
    private Developer coordinator;
    private int coordinator_key;
    private String name;
    private String category;
    private String location;
    private String company;
    private String description;
    private String picture;
    private List<Task> tasks;
    private List<Message> messages;

    protected SocialDevelopDataLayer ownerdatalayer;
    protected boolean dirty;

    public ProjectImpl(SocialDevelopDataLayer ownerdatalayer) {
        this.ownerdatalayer = ownerdatalayer;
        key = 0;
        coordinator = null;
        coordinator_key = 0;
        name = "";
        category = "";
        location = "";
        company = "";
        description = "";
        picture = "pro-img-1.jpg";
        tasks = null;
        messages = null;
        dirty = false;
    }

    @Override
    public int getKey() {
        return key;
    }

    @Override
    public void setName(String name) {
        this.name = name;
        this.dirty = true;
    }

    @Override
    public String getName() {
        return name;
    }
    @Override
    public void setCategory(String category) {
        this.category = category;
        this.dirty = true;
    }

    @Override
    public String getCategory() {
        return category;
    }

    @Override
    public void setCoordinatorKey(int coordinator_key) {
        this.coordinator_key = coordinator_key;
        this.coordinator = null;
        this.dirty = true;
    }

    @Override
    public int getCoordinatorKey() {
        return coordinator_key;
    }
    
    @Override
    public void setLocation(String location) {
        this.location = location;
        this.dirty = true;
    }

    @Override
    public String getLocation() {
        return location;
    }
    
    @Override
    public void setCompany(String company) {
        this.company = company;
        this.dirty = true;
    }

    @Override
    public String getCompany() {
        return company;
    }
    
    @Override
    public void setDescription(String description) {
        this.description = description;
        this.dirty = true;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setCoordinator(Developer coordinator) {
        this.coordinator = coordinator;
        this.coordinator_key = coordinator.getKey();
        this.dirty = true;
    }

    @Override
    public Developer getCoordinator() throws DataLayerException {
        //notare come il coordinatore in relazione venga caricato solo su richiesta
        if (coordinator == null && coordinator_key > 0) {
            coordinator = ownerdatalayer.getDeveloper(coordinator_key);
        }
        //attenzione: il coordinatore caricato viene lagato all'oggetto in modo da non 
        //dover venir ricaricato alle richieste successive, tuttavia, questo
        //puo' rende i dati potenzialmente disallineati: se il coordinatore viene modificato
        //nel DB, qui rimarrà la sua "vecchia" versione

        return coordinator;
    }

    @Override
    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
        this.dirty = true;
    }

    @Override
    public List<Task> getTasks() throws DataLayerException {
        if (tasks == null) {
            tasks = ownerdatalayer.getTasks(this.key);
        }
        return tasks;
    }

    @Override
    public void addTask(Task task) {
        this.tasks.add(task);
        this.dirty = true;
    }

    @Override
    public void removeTask(Task task) {
        this.tasks.remove(task);
        this.dirty = true;
    }

    @Override
    public List<Message> getMessages() throws DataLayerException {
        if (messages == null) {
            messages = ownerdatalayer.getMessages(this.key);
        }
        return messages;
    }

    @Override
    public void addMessage(Message message) {
        this.messages.add(message);
        this.dirty = true;
    }

    @Override
    public void removeMessage(Message message) {
        this.messages.remove(message);
        this.dirty = true;
    }

    @Override
    public void copyFrom(Project project) throws DataLayerException {
        key = project.getKey();
        coordinator_key = project.getCoordinator().getKey();
        description = project.getDescription();
        name = project.getName();
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
        this.dirty = true;
    }

    @Override
    public void setPicture(String picture) {
        this.picture = picture;
        this.dirty = true;
    }

    @Override
    public String getPicture() {
        return picture;
    }

    @Override
    public String getTextDescription() {
        return Jsoup.parse(this.description).text();
    }

}
