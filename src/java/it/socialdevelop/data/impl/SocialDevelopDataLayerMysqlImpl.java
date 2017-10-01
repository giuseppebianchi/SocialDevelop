package it.socialdevelop.data.impl;

import it.univaq.f4i.iw.framework.data.DataLayerException;
import it.univaq.f4i.iw.framework.data.DataLayerMysqlImpl;
import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.naming.NamingException;
import javax.servlet.http.Part;
import javax.sql.DataSource;
import it.socialdevelop.data.model.Admin;
import it.socialdevelop.data.model.CollaborationRequest;
import it.socialdevelop.data.model.Developer;
import it.socialdevelop.data.model.Files;
import it.socialdevelop.data.model.Message;
import it.socialdevelop.data.model.Project;
import it.socialdevelop.data.model.Skill;
import it.socialdevelop.data.model.SocialDevelopDataLayer;
import it.socialdevelop.data.model.Task;
import it.socialdevelop.data.model.Type;

/**
 *
 * @author Hello World Group
 */
public class SocialDevelopDataLayerMysqlImpl extends DataLayerMysqlImpl implements SocialDevelopDataLayer {

    //NB: sRequestByID --> ID è inteso come coppia collaborator_key-task_key)
    private PreparedStatement sProjectByID, sTaskByID, sProjects, sDeveloperByID, sSkillByID, sRequestByID, sSkillsParentList;
    private PreparedStatement sProjectsByFilter, sSkillsByTask, sSkillsByDeveloper, sSkillsByType, sQuestionsByCoordinatorID;
    private PreparedStatement sCollaboratorsByTask, sVoteByTaskandDeveloper, sTypeByTask, sMessagesByProject, sDeveloperByMessage;
    private PreparedStatement sDeveloperBySkillWithLevel, sDeveloperBySkill, sTasksByProject, sTaskByRequest, scTaskByProjectID;
    private PreparedStatement sParentBySkill, sMessageByID, sCollaboratorsByProjectID, sProjectsByDeveloperID;
    private PreparedStatement sProjectsByDeveloperIDandDate, sInvitesByCoordinatorID, sProposalsByCollaboratorID;
    private PreparedStatement sOffertsByDeveloperID, sCoordinatorByTask, sTasksByDeveloper, sChildBySkill, sPublicMessagesByProject;
    private PreparedStatement sTypeByID, iProject, uProject, dProject, sSkillByName, sTypes, sTypeByName;
    private PreparedStatement iDeveloper, uDeveloper, dDeveloper, sDeveloperByUsername, sDeveloperByMail;
    private PreparedStatement iSkill, uSkill, dSkill, sSkills;
    private PreparedStatement iTask, uTask, dTask;
    private PreparedStatement iMessage, uMessage, dMessage;
    private PreparedStatement iType, uType, dType, sFileByID;
    private PreparedStatement iRequest, uRequest, dRequest, iImg, sAdmin, sTasksBySkill;
    private PreparedStatement iTaskHasSkill, dTaskHasSkill, uTaskHasSkill, sTaskHasSkill, sCollaboratorRequestsByTask;
    private PreparedStatement iTaskHasDeveloper, dTaskHasDeveloper, uTaskHasDeveloper, sTaskHasDeveloper, dTasksFromProject;
    private PreparedStatement iSkillHasDeveloper, dSkillHasDeveloper, uSkillHasDeveloper, sSkillHasDeveloper;
    private PreparedStatement sProjectByTask, sCurrentTasksByDeveloper, sEndedTasksByDeveloper, sProjectsByCoordinator;
    private PreparedStatement sDateOfTaskByProject, sEndDateOfTaskByProject, sTypeBySkill, dSkillsFromTask, sDeveloperByUsernameLike, sProjectsLimit, sDeveloperBySkillWithLevelLimit;

    public SocialDevelopDataLayerMysqlImpl(DataSource datasource) throws SQLException, NamingException {
        super(datasource);
    }

    @Override
    public void init() throws DataLayerException {
        try {
            super.init();

            //precompiliamo tutte le query utilizzate
            sAdmin = connection.prepareStatement("SELECT * FROM admin WHERE developer_ID=?");

            sProjectByID = connection.prepareStatement("SELECT * FROM project WHERE ID=?");

            sProjectsByCoordinator = connection.prepareStatement("SELECT ID from project WHERE coordinator_ID=?");

            sSkills = connection.prepareStatement("SELECT * FROM skill");

            sTaskByID = connection.prepareStatement("SELECT * FROM task WHERE ID=?");

            scTaskByProjectID = connection.prepareStatement("SELECT COUNT(*) AS n FROM task WHERE project_ID=?");

            sTypeByID = connection.prepareStatement("SELECT * FROM type WHERE ID=?");

            sTypeBySkill = connection.prepareStatement("SELECT type_ID FROM skill WHERE ID=? ");

            sTypes = connection.prepareStatement("SELECT ID FROM type");

            sSkillsByType = connection.prepareStatement("SELECT ID FROM skill WHERE type_ID=?");

            sDeveloperByID = connection.prepareStatement("SELECT * FROM developer WHERE ID=?");

            sDeveloperByUsername = connection.prepareStatement("SELECT developer.ID FROM developer WHERE username=?");

            sDeveloperByUsernameLike = connection.prepareStatement("SELECT developer.ID FROM developer WHERE username LIKE ?");

            sDeveloperByMail = connection.prepareStatement("SELECT developer.ID FROM developer WHERE mail=?");

            sMessagesByProject = connection.prepareStatement("SELECT ID FROM message WHERE project_ID=? ORDER BY type");

            sMessageByID = connection.prepareStatement("SELECT * FROM message WHERE ID=?");

            sDeveloperByMessage = connection.prepareStatement("SELECT m.developer_ID FROM message AS m WHERE m.ID=?");

            sPublicMessagesByProject = connection.prepareStatement("SELECT ID FROM message WHERE project_ID=? and private=0 ORDER BY type");

            sProjects = connection.prepareStatement("SELECT ID FROM project");

            sProjectsLimit = connection.prepareStatement("SELECT ID FROM project LIMIT ?,6");

            sRequestByID = connection.prepareStatement("SELECT thd.*,p.coordinator_ID FROM (SELECT task_has_developer.* FROM task_has_developer WHERE developer_ID=? AND task_ID=?)AS thd \n"
                    + "INNER JOIN task AS t ON (t.ID=thd.task_ID) INNER JOIN project AS p ON (t.project_ID=p.ID)");

            sCoordinatorByTask = connection.prepareStatement("SELECT p.coordinator_ID FROM (SELECT task.* FROM task WHERE task.ID=?) AS t INNER JOIN project AS p "
                    + "ON (p.ID = t.project_ID)");

            sSkillByID = connection.prepareStatement("SELECT * FROM skill WHERE ID=?");

            sSkillsParentList = connection.prepareStatement("SELECT ID FROM skill WHERE parent_ID IS NULL");

            sProjectsByFilter = connection.prepareStatement("SELECT ID FROM project WHERE "
                    + "(name LIKE ? or description LIKE ?)");
            sSkillsByTask = connection.prepareStatement("SELECT skill.ID,task_has_skill.level_min FROM skill INNER JOIN task_has_skill ON"
                    + "(skill.ID = task_has_skill.skill_ID) WHERE task_has_skill.task_ID=?");
            sSkillsByDeveloper = connection.prepareStatement("SELECT skill_ID, level FROM skill_has_developer WHERE developer_ID=?");

            sSkillByName = connection.prepareStatement("SELECT ID FROM skill WHERE name=?");

            sTypeByName = connection.prepareStatement("SELECT ID FROM type WHERE type=?");

            sTasksByDeveloper = connection.prepareStatement("SELECT task_ID,vote FROM task_has_developer WHERE developer_ID=? AND state>0");

            sTasksBySkill = connection.prepareStatement("SELECT task_ID FROM task_has_skill WHERE skill_ID=?");

            sCurrentTasksByDeveloper = connection.prepareStatement("SELECT task_ID,vote FROM task_has_developer WHERE developer_ID=? AND state=1");

            sEndedTasksByDeveloper = connection.prepareStatement("SELECT task_ID,vote FROM task_has_developer WHERE developer_ID=? AND state=2");

            //sRequestByTask = connection.prepareStatement("SELECT * FROM task_has_developer WHERE task_ID=?");
            sCollaboratorsByTask = connection.prepareStatement("SELECT * FROM task_has_developer WHERE task_ID=? AND state>=1");

            sCollaboratorRequestsByTask = connection.prepareStatement("SELECT developer_ID FROM task_has_developer WHERE task_ID=? AND state=0");

            sVoteByTaskandDeveloper = connection.prepareStatement("SELECT vote FROM task_has_developer WHERE task_ID=? "
                    + "AND developer_ID=?");
            sTypeByTask = connection.prepareStatement("SELECT type_ID FROM task WHERE ID=?");
            sDeveloperBySkillWithLevel = connection.prepareStatement("SELECT developer.*, skill_has_developer.level FROM developer INNER JOIN "
                    + "skill_has_developer ON (developer.ID = skill_has_developer.developer_ID)"
                    + "WHERE skill_has_developer.skill_ID=? AND skill_has_developer.level>=?");

            sDeveloperBySkillWithLevelLimit = connection.prepareStatement("SELECT developer.*, skill_has_developer.level FROM developer INNER JOIN "
                    + "skill_has_developer ON (developer.ID = skill_has_developer.developer_ID)"
                    + "WHERE skill_has_developer.skill_ID=? AND skill_has_developer.level>=? LIMIT ?,6");

            sDeveloperBySkill = connection.prepareStatement("SELECT developer.*, skill_has_developer.level FROM developer INNER JOIN skill_has_developer "
                    + "ON(developer.ID = skill_has_developer.developer_ID) WHERE skill_has_developer.skill_ID=?");
            sTasksByProject = connection.prepareStatement("SELECT task.ID FROM task WHERE project_ID=?");

            sProjectByTask = connection.prepareStatement("SELECT task.project_ID FROM task WHERE ID=?");

            sTaskByRequest = connection.prepareStatement("SELECT task.ID FROM ((SELECT task_has_skill.* FROM task_has_skill WHERE task_has_skill.collaborator_ID=?) AS ths"
                    + "INNER JOIN task AS t ON (ths.task_ID = t.ID) INNER JOIN project AS p ON (t.project_ID = p.ID)) WHERE p.coordinator_ID=?)");

            sParentBySkill = connection.prepareStatement("SELECT parent_ID FROM skill WHERE ID=?");
            sChildBySkill = connection.prepareStatement("SELECT ID FROM skill WHERE parent_ID=?");
            sCollaboratorsByProjectID = connection.prepareStatement("SELECT thd.developer_ID FROM (SELECT * FROM task WHERE project_ID=?) "
                    + "AS t INNER JOIN task_has_developer AS thd ON (thd.task_ID = t.ID)");

            //seleziona i progetti ai quali il developer ha partecipato
            sProjectsByDeveloperID = connection.prepareStatement("SELECT t.project_ID FROM (SELECT task_has_developer.* FROM task_has_developer WHERE"
                    + " task_has_developer.developer_ID=?) AS thd INNER JOIN task AS t ON (thd.task_ID= t.ID)");
            sProjectsByDeveloperIDandDate = connection.prepareStatement("SELECT t.project_ID FROM (SELECT task_has_developer.* FROM task_has_developer WHERE"
                    + " task_has_developer.developer_ID=? AND task_has_developer.date=?) AS thd INNER JOIN task AS t ON (thd.task_ID= t.ID)");

            //seleziona inviti di partecipazione inviati da un coordinatore(pannello degli inviti)
            sInvitesByCoordinatorID = connection.prepareStatement("SELECT thd.* FROM  task_has_developer AS thd WHERE sender=? AND developer_ID<>? ORDER BY thd.task_ID");

            //seleziona proposte che un collaboratore riceve da un coordinatore(pannello delle proposte)
            sProposalsByCollaboratorID = connection.prepareStatement("SELECT thd.* FROM task_has_developer AS thd WHERE "
                    + "thd.developer_ID=? AND thd.sender<>? AND state=0");

            //seleziona proposte che un coordinatore riceve da un collaboratore(pannello delle domande)
            /*SELECT thd.task_ID, thd.developer_ID FROM task_has_developer AS thd WHERE "
                                        + "thd.sender=1 INNER JOIN task AS t ON (thd.task_ID = t.ID) INNER JOIN project AS p ON (t.project_ID = p.ID) WHERE p.coordinator_ID=?");  
             */
            sQuestionsByCoordinatorID = connection.prepareStatement("SELECT thd.task_ID,thd.developer_ID FROM(((SELECT project.ID FROM project WHERE project.coordinator_ID=?) AS p "
                    + "INNER JOIN task AS t ON (p.ID=t.project_ID))INNER JOIN task_has_developer AS thd ON (thd.task_ID=t.ID)) WHERE thd.sender<>? AND thd.state=0");

            //seleziona le skill per le quali il developer risulta idoneo a partecipare(pannello delle offerte)
            /*sOffertsByDeveloperID = connection.prepareStatement("SELECT thd.*,p.coordinator_ID FROM (SELECT task_has_developer.* FROM task_has_developer WHERE\n" +
                            "task_has_developer.developer_ID=? AND task_has_developer.sender=0) AS thd INNER JOIN task AS t ON (thd.task_ID = t.ID) INNER JOIN project AS p\n" +
                            "ON(p.ID=t.project_ID)");
             */
            sOffertsByDeveloperID = connection.prepareStatement("SELECT task_ID FROM (SELECT skill_ID FROM skill_has_developer WHERE developer_ID=?) AS shd "
                    + "INNER JOIN task_has_skill AS ths ON (shd.skill_ID = ths.skill_ID) WHERE task_ID NOT IN (SELECT task_ID FROM task_has_developer WHERE developer_ID=?) GROUP BY task_ID");
            iProject = connection.prepareStatement("INSERT INTO project (name,description,coordinator_ID) VALUES(?,?,?)", Statement.RETURN_GENERATED_KEYS);
            uProject = connection.prepareStatement("UPDATE project SET name=?,description=?,coordinator_ID=? WHERE ID=?");
            dProject = connection.prepareStatement("DELETE FROM project WHERE ID=?");

            iSkill = connection.prepareStatement("INSERT INTO skill (name,parent_ID,type_ID) VALUES(?,?,?)", Statement.RETURN_GENERATED_KEYS);
            uSkill = connection.prepareStatement("UPDATE skill SET name=?,parent_ID=? WHERE ID=?");
            dSkill = connection.prepareStatement("DELETE FROM skill WHERE ID=?");

            iDeveloper = connection.prepareStatement("INSERT INTO developer (name,surname,username,mail,pwd,birthdate,biography,curriculumString) VALUES(?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            uDeveloper = connection.prepareStatement("UPDATE developer SET username=?,mail=?,pwd=?,biography=?,curriculumString=?,curriculum_pdf_ID=?,photo_ID=? WHERE ID=?");
            dDeveloper = connection.prepareStatement("DELETE FROM developer WHERE ID=?");

            iTask = connection.prepareStatement("INSERT INTO task (name,numCollaborators,start,end,description,open,project_ID, type_ID) VALUES(?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            uTask = connection.prepareStatement("UPDATE task SET name=?,numCollaborators=?,start=?,end=?,description=?,open=?,project_ID=? WHERE ID=?");
            dTask = connection.prepareStatement("DELETE FROM task WHERE ID=?");
            dTasksFromProject = connection.prepareStatement("DELETE FROM task WHERE project_ID=?");

            iMessage = connection.prepareStatement("INSERT INTO message (private,text,type,project_ID, developer_ID) VALUES(?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            uMessage = connection.prepareStatement("UPDATE message SET private=?,text=?,type=?,project_ID=? WHERE ID=?");
            dMessage = connection.prepareStatement("DELETE FROM message WHERE ID=?");

            iType = connection.prepareStatement("INSERT INTO type (type) VALUES(?)", Statement.RETURN_GENERATED_KEYS);
            uType = connection.prepareStatement("UPDATE type SET type=? WHERE ID=?");
            dType = connection.prepareStatement("DELETE FROM type WHERE ID=?");

            //iRequest = connection.prepareStatement("INSERT INTO request (task_ID,developer_ID,state,date,vote) VALUES(?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            //uRequest = connection.prepareStatement("UPDATE request SET task_ID=?,developer_ID=?,state=?,date=?,vote=? WHERE task_ID=? AND developer_ID=?");
            //dRequest = connection.prepareStatement("DELETE FROM request WHERE task_ID=? AND developer_ID=?");
            iTaskHasSkill = connection.prepareStatement("INSERT INTO task_has_skill (task_ID,skill_ID,level_min) VALUES(?,?,?)", Statement.RETURN_GENERATED_KEYS);
            dTaskHasSkill = connection.prepareStatement("DELETE FROM task_has_skill WHERE task_id=? AND skill_ID=?");
            dSkillsFromTask = connection.prepareStatement("DELETE FROM task_has_skill WHERE task_ID=?");
            uTaskHasSkill = connection.prepareStatement("UPDATE task_has_skill SET task_ID=?,skill_ID=?,type_ID=?,level_min=? WHERE task_ID=? AND skill_ID=? AND type_ID=? ");
            sTaskHasSkill = connection.prepareStatement("SELECT * FROM task_has_skill WHERE task_ID=? AND skill_ID=?");

            iTaskHasDeveloper = connection.prepareStatement("INSERT INTO task_has_developer (task_ID,developer_ID,state,date,vote,sender) VALUES(?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            dTaskHasDeveloper = connection.prepareStatement("DELETE FROM task_has_developer WHERE task_id=? AND developer_ID=?");
            uTaskHasDeveloper = connection.prepareStatement("UPDATE task_has_developer SET state=?,vote=? WHERE task_ID=? AND developer_ID=? AND sender=?");
            sTaskHasDeveloper = connection.prepareStatement("SELECT * FROM task_has_developer WHERE task_ID=? AND developer_ID=? AND sender=?");

            iSkillHasDeveloper = connection.prepareStatement("INSERT INTO skill_has_developer (skill_ID,developer_ID,level) VALUES(?,?,?)", Statement.RETURN_GENERATED_KEYS);
            dSkillHasDeveloper = connection.prepareStatement("DELETE FROM skill_has_developer WHERE skill_id=? AND developer_ID=?");
            uSkillHasDeveloper = connection.prepareStatement("UPDATE skill_has_developer SET skill_ID=?,developer_ID=?,level=? WHERE skill_ID=? AND developer_ID=?");
            sSkillHasDeveloper = connection.prepareStatement("SELECT * FROM skill_has_developer WHERE skill_id=? AND developer_ID=?");

            iImg = connection.prepareStatement("INSERT INTO files (name,size,localfile,digest,type) VALUES (?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            sFileByID = connection.prepareStatement("SELECT * FROM files WHERE ID=?");
            sDateOfTaskByProject = connection.prepareStatement("SELECT start FROM task AS t INNER JOIN project as p ON t.project_ID = p.ID WHERE p.ID = ? ORDER BY start LIMIT 1");
            sEndDateOfTaskByProject = connection.prepareStatement("SELECT MAX(end) AS maxend FROM task AS t INNER JOIN project as p ON t.project_ID = p.ID WHERE p.ID = ? LIMIT 1");

        } catch (SQLException ex) {
            throw new DataLayerException("Error initializing newspaper data layer", ex);
        }
    }

    //metodi "factory" che permettono di creare
    //e inizializzare opportune implementazioni
    //delle interfacce del modello dati, nascondendo
    //all'utente tutti i particolari
    //factory methods to create and initialize
    //suitable implementations of the data model interfaces,
    //hiding all the implementation details
    @Override
    public Project createProject() {
        return new ProjectImpl(this);
    }

    public Project createProject(ResultSet rs) throws DataLayerException {
        try {
            ProjectImpl a = new ProjectImpl(this);
            a.setKey(rs.getInt("ID"));
            a.setName(rs.getString("name"));
            a.setDescription(rs.getString("description"));
            a.setCoordinatorKey(rs.getInt("coordinator_ID"));
            //a.setTasks(getTasks(a.getKey()));
            //a.setCoordinator(getDeveloper(a.getCoordinatorKey()));
            return a;
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to create project object form ResultSet", ex);
        }
    }

    @Override
    public Task createTask() {
        return new TaskImpl(this);
    }

    public Task createTask(ResultSet rs) throws DataLayerException {
        try {
            TaskImpl a = new TaskImpl(this);
            a.setKey(rs.getInt("ID"));
            a.setName(rs.getString("name"));
            Timestamp ts = rs.getTimestamp("start");
            GregorianCalendar start = new GregorianCalendar();
            start.setTime(ts);
            a.setStartDate(start);

            Timestamp ts2 = rs.getTimestamp("end");
            GregorianCalendar end = new GregorianCalendar();
            end.setTime(ts2);
            a.setEndDate(end);

            a.setOpen(rs.getBoolean("open"));
            a.setNumCollaborators(rs.getInt("numCollaborators"));
            a.setDescription(rs.getString("description"));
            a.setProjectKey(rs.getInt("project_ID"));
            a.setType_key(rs.getInt("type_ID"));
            return a;
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to create task object form ResultSet", ex);
        }
    }

    @Override
    public Skill createSkill() {
        return new SkillImpl(this);
    }

    public Skill createSkill(ResultSet rs) throws DataLayerException {
        try {
            SkillImpl a = new SkillImpl(this);
            a.setKey(rs.getInt("ID"));
            a.setName(rs.getString("name"));
            if (rs.getObject("parent_ID") != null && !rs.wasNull()) {
                a.setParentKey(rs.getInt("parent_ID"));
            } else {
                a.setParentKey(-1);
            }
            a.setType_key(rs.getInt("type_ID"));

            return a;
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to create skill object form ResultSet", ex);
        }
    }

    @Override
    public Developer createDeveloper() {
        return new DeveloperImpl(this);
    }

    public Developer createDeveloper(ResultSet rs) throws DataLayerException {
        try {
            DeveloperImpl a = new DeveloperImpl(this);
            a.setKey(rs.getInt("ID"));
            a.setName(rs.getString("name"));
            a.setSurname(rs.getString("surname"));
            a.setUsername(rs.getString("username"));
            a.setMail(rs.getString("mail"));
            a.setPwd(rs.getString("pwd"));
            a.setBiography(rs.getString("biography"));
            java.sql.Date date;
            date = rs.getDate("birthdate");
            if (date != null) {
                GregorianCalendar birthdate = new GregorianCalendar();
                birthdate.setTime(date);
                a.setBirthDate(birthdate);

            } else {
                a.setBirthDate(null);
            }
            String curriculumStr = rs.getString("curriculumString");
            int curriculumFile = rs.getInt("curriculum_pdf_ID");
            if (curriculumStr != null && !curriculumStr.equals("")) {
                a.setCurriculum(curriculumStr);
            } else {
                a.setCurriculum(curriculumFile);
            }
            a.setFoto(rs.getInt("photo_ID"));
            return a;
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to create developer object form ResultSet", ex);
        }
    }

    @Override
    public Files createFiles() {
        return new FilesImpl(this);
    }

    public Files createFiles(ResultSet rs) throws DataLayerException {
        try {
            FilesImpl a = new FilesImpl(this);
            a.setKey(rs.getInt("ID"));
            a.setName(rs.getString("name"));
            a.setDigest(rs.getString("digest"));
            a.setLocalFile(rs.getString("localfile"));
            a.setType(rs.getString("type"));
            a.setSize(rs.getInt("size"));
            java.sql.Date date;
            date = rs.getDate("updated");
            if (date != null) {
                GregorianCalendar birthdate = new GregorianCalendar();
                birthdate.setTime(date);
                a.setUpdated(birthdate);

            } else {
                a.setUpdated(null);
            }
            return a;
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to create file object form ResultSet", ex);
        }
    }

    @Override
    public Type createType() {
        return new TypeImpl(this);
    }

    public Type createType(ResultSet rs) throws DataLayerException {
        try {
            TypeImpl a = new TypeImpl(this);
            a.setKey(rs.getInt("ID"));
            a.setType(rs.getString("type"));
            return a;
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to create type object form ResultSet", ex);
        }
    }

    @Override
    public Message createMessage() {
        return new MessageImpl(this);
    }

    public Message createMessage(ResultSet rs) throws DataLayerException {
        try {
            MessageImpl a = new MessageImpl(this);
            a.setKey(rs.getInt("ID"));
            a.setText(rs.getString("text"));
            a.setPrivate(rs.getBoolean("private"));
            a.setType(rs.getString("type"));
            a.setProjectKey(rs.getInt("project_ID"));
            a.setDeveloperKey(rs.getInt("developer_ID"));
            return a;
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to create message object form ResultSet", ex);
        }
    }

    @Override
    public CollaborationRequest createCollaborationRequest() {
        return new CollaborationRequestImpl(this);
    }

    public CollaborationRequest createCollaborationRequest(ResultSet rs) throws DataLayerException {
        try {
            CollaborationRequestImpl a = new CollaborationRequestImpl(this);
            GregorianCalendar requestDate = new GregorianCalendar();
            java.sql.Date date;
            date = rs.getDate("date");
            requestDate.setTime(date);
            a.setDate(requestDate);
            a.setState(rs.getInt("state"));
            a.setCollaboratorKey(rs.getInt("developer_ID"));
            a.setTaskKey(rs.getInt(("task_ID")));
            a.setSender_key(rs.getInt("sender"));
            return a;
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to create collaborationRequest object form ResultSet", ex);
        }
    }

    @Override
    public Project getProject(int project_key) throws DataLayerException {
        try {
            sProjectByID.setInt(1, project_key); //setta primo parametro query a project_key
            try (ResultSet rs = sProjectByID.executeQuery()) {
                if (rs.next()) {
                    //notare come utilizziamo il costrutture
                    //"helper" della classe AuthorImpl
                    //per creare rapidamente un'istanza a
                    //partire dal record corrente
                    return createProject(rs);
                }
            }
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to load project by ID", ex);
        }
        return null;
    }

    @Override
    public int getNumberTaskByProjectID(int project_key) throws DataLayerException {
        try {
            scTaskByProjectID.setInt(1, project_key); //setta primo parametro query a project_key
            try (ResultSet rs = scTaskByProjectID.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt('n');
                }
            }
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to load number of task by project key", ex);
        }
        return 0;
    }

    @Override
    public Files getFile(int file) throws DataLayerException {
        try {
            sFileByID.setInt(1, file); //setta primo parametro query a project_key
            try (ResultSet rs = sFileByID.executeQuery()) {
                if (rs.next()) {
                    //notare come utilizziamo il costrutture
                    //"helper" della classe AuthorImpl
                    //per creare rapidamente un'istanza a
                    //partire dal record corrente
                    return createFiles(rs);
                }
            }
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to load file by ID", ex);
        }
        return null;
    }

    @Override
    public List<Project> getProjects() throws DataLayerException {
        List<Project> result = new ArrayList();

        try (ResultSet rs = sProjects.executeQuery()) {
            while (rs.next()) {
                result.add((Project) getProject(rs.getInt("ID")));

            }
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to load projects", ex);
        }
        return result; //restituisce in result tutti gli oggetti Project esistenti
    }

    @Override
    public List<Project> getProjectsByCoordinator(int coordinator_key) throws DataLayerException {
        List<Project> result = new ArrayList();
        try {
            sProjectsByCoordinator.setInt(1, coordinator_key);

            try (ResultSet rs = sProjectsByCoordinator.executeQuery()) {
                while (rs.next()) {
                    result.add((Project) getProject(rs.getInt("ID")));
                }
            }
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to load projects by coordinator key", ex);
        }
        return result; //restituisce in result tutti gli oggetti Project di cui il coordinatore è quello indicato
    }

    @Override
    public List<Project> getProjects(String filter) throws DataLayerException {
        List<Project> result = new ArrayList();
        try {
            sProjectsByFilter.setString(1, "%" + filter + "%");
            sProjectsByFilter.setString(2, "%" + filter + "%");

            try (ResultSet rs = sProjectsByFilter.executeQuery()) {
                while (rs.next()) {
                    result.add((Project) getProject(rs.getInt("ID")));
                }
            }
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to load projects", ex);
        }
        return result; //restituisce in result tutti gli oggetti Project esistenti
    }

    @Override
    public List<Task> getTasks(int project_key) throws DataLayerException {
        List<Task> result = new ArrayList();
        try {
            sTasksByProject.setInt(1, project_key);
            try (ResultSet rs = sTasksByProject.executeQuery()) {
                while (rs.next()) {
                    result.add((Task) getTask(rs.getInt("ID")));

                }
            }
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to load tasks by project", ex);
        }
        return result;
    }

    @Override
    public Message getMessage(int message_key) throws DataLayerException {
        try {
            sMessageByID.setInt(1, message_key);
            try (ResultSet rs = sMessageByID.executeQuery()) {
                if (rs.next()) {
                    return createMessage(rs);
                }
            }
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to load message by ID", ex);
        }
        return null;
    }

    @Override
    public Developer getDeveloperByMessage(int message_key) throws DataLayerException {
        try {
            sDeveloperByMessage.setInt(1, message_key);
            try (ResultSet rs = sDeveloperByMessage.executeQuery()) {
                if (rs.next()) {
                    return createDeveloper(rs);
                }
            }
        } catch (SQLException ex) {
            throw new DataLayerException("unable to load developer by message key", ex);
        }
        return null;
    }

    @Override
    public List<Message> getMessages(int project_key) throws DataLayerException {
        List<Message> result = new ArrayList();
        try {
            sMessagesByProject.setInt(1, project_key);
            try (ResultSet rs = sMessagesByProject.executeQuery()) {
                while (rs.next()) {
                    result.add((Message) getMessage(rs.getInt("ID")));
                }
            }
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to load messages", ex);
        }
        return result; //restituisce in result tutti gli oggetti Project esistenti
    }

    @Override
    public Skill getSkill(int skill_key) throws DataLayerException {
        try {
            sSkillByID.setInt(1, skill_key);
            try (ResultSet rs = sSkillByID.executeQuery()) {
                if (rs.next()) {
                    return createSkill(rs);
                }
            }
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to load skill by ID", ex);
        }
        return null;
    }

    @Override
    public int getSkillByName(String name) throws DataLayerException {
        try {
            sSkillByName.setString(1, name);
            try (ResultSet rs = sSkillByName.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("ID");
                }
            }
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to load skill by name", ex);
        }
        return 0;
    }

    @Override
    public int getTypeByName(String name) throws DataLayerException {
        try {
            sTypeByName.setString(1, name);
            try (ResultSet rs = sTypeByName.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("ID");
                }
            }
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to load type by name", ex);
        }
        return 0;
    }

    @Override
    public List<Skill> getSkillsByType(int type_key) throws DataLayerException {
        List<Skill> result = new ArrayList();
        try {
            sSkillsByType.setInt(1, type_key);
            try (ResultSet rs = sSkillsByType.executeQuery()) {
                while (rs.next()) {
                    result.add((Skill) getSkill(rs.getInt("ID")));
                }
            }
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to load skills by type key", ex);
        }
        return result;
    }

    @Override
    public Type getType(int type_key) throws DataLayerException {
        try {
            sTypeByID.setInt(1, type_key);
            try (ResultSet rs = sTypeByID.executeQuery()) {
                if (rs.next()) {
                    return createType(rs);
                }
            }
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to load type by ID", ex);
        }
        return null;
    }

    @Override
    public Task getTask(int task_key) throws DataLayerException {
        try {
            sTaskByID.setInt(1, task_key);
            try (ResultSet rs = sTaskByID.executeQuery()) {
                if (rs.next()) {
                    return createTask(rs);
                }
            }
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to load task by ID", ex);
        }
        return null;
    }

    @Override
    public Type getTypeByTask(int task_key) throws DataLayerException {
        try {
            sTypeByTask.setInt(1, task_key);
            try (ResultSet rs = sTypeByTask.executeQuery()) {
                if (rs.next()) {
                    try {
                        sTypeByID.setInt(1, rs.getInt("type_ID"));
                        try (ResultSet rs1 = sTypeByID.executeQuery()) {
                            if (rs1.next()) {
                                return createType(rs1);
                            }
                        }
                    } catch (SQLException ex) {
                        throw new DataLayerException("Unable to load typeByTask", ex);
                    }
                }
            }
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to load typeByTask", ex);
        }
        return null;
    }

    @Override
    public Map<Skill, Integer> getSkillsByTask(int task_key) throws DataLayerException {
        Map<Skill, Integer> result = new HashMap<>();
        try {
            sSkillsByTask.setInt(1, task_key);
            try (ResultSet rs = sSkillsByTask.executeQuery()) {
                while (rs.next()) {
                    result.put((Skill) getSkill(rs.getInt("ID")), rs.getInt("level_min"));
                }
            }
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to load skillsByTask", ex);
        }
        return result;
    }

    @Override
    public Map<Task, Integer> getTasksByDeveloper(int developer_key) throws DataLayerException {
        Map<Task, Integer> result = new HashMap<>();
        try {
            sTasksByDeveloper.setInt(1, developer_key);
            try (ResultSet rs = sTasksByDeveloper.executeQuery()) {
                while (rs.next()) {
                    result.put((Task) getTask(rs.getInt("task_ID")), rs.getInt("vote"));
                }
            }
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to load taskByDev", ex);
        }
        return result;
    }

    @Override
    public Map<Task, Integer> getCurrentTasksByDeveloper(int developer_key) throws DataLayerException {
        Map<Task, Integer> result = new HashMap<>();
        try {
            sCurrentTasksByDeveloper.setInt(1, developer_key);
            try (ResultSet rs = sCurrentTasksByDeveloper.executeQuery()) {
                while (rs.next()) {
                    result.put((Task) getTask(rs.getInt("task_ID")), rs.getInt("vote"));
                }
            }
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to load taskByDev", ex);
        }
        return result;
    }

    @Override
    public Map<Task, Integer> getEndedTasksByDeveloper(int developer_key) throws DataLayerException {
        Map<Task, Integer> result = new HashMap<>();
        try {
            sEndedTasksByDeveloper.setInt(1, developer_key);
            try (ResultSet rs = sEndedTasksByDeveloper.executeQuery()) {
                while (rs.next()) {
                    result.put((Task) getTask(rs.getInt("task_ID")), rs.getInt("vote"));
                }
            }
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to load taskByDev", ex);
        }
        return result;
    }

    @Override
    public Developer getDeveloper(int developer_key) throws DataLayerException {
        try {
            sDeveloperByID.setInt(1, developer_key);
            try (ResultSet rs = sDeveloperByID.executeQuery()) {
                if (rs.next()) {
                    return createDeveloper(rs);
                }
            }
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to load developer", ex);
        }
        return null;
    }

    @Override
    public int getDeveloperByUsername(String username) throws DataLayerException {
        try {
            sDeveloperByUsername.setString(1, username);
            try (ResultSet rs = sDeveloperByUsername.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("ID");
                }
            }
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to load developer", ex);
        }
        return 0;
    }

    @Override
    public int getDeveloperByMail(String mail) throws DataLayerException {
        try {
            sDeveloperByMail.setString(1, mail);
            try (ResultSet rs = sDeveloperByMail.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("ID");
                }
            }
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to load developer", ex);
        }
        return 0;
    }

    @Override
    public List<Type> getTypes() throws DataLayerException {
        List<Type> result = new ArrayList();
        try (ResultSet rs = sTypes.executeQuery()) {
            while (rs.next()) {
                result.add((Type) getType(rs.getInt("ID")));
            }
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to load types", ex);
        }
        return result;
    }

    @Override
    public List<Skill> getSkills() throws DataLayerException {
        List<Skill> result = new ArrayList();
        try {
            try (ResultSet rs = sSkills.executeQuery()) {
                while (rs.next()) {
                    result.add((Skill) getSkill(rs.getInt("ID")));
                }
            }
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to load skills", ex);
        }
        return result;
    }

    @Override
    public Map<Developer, Integer> getCollaboratorsByTask(int task_key) throws DataLayerException {
        Map<Developer, Integer> result = new HashMap<>();
        try {
            sCollaboratorsByTask.setInt(1, task_key);
            try (ResultSet rs = sCollaboratorsByTask.executeQuery()) {
                while (rs.next()) {
                    result.put((Developer) getDeveloper(rs.getInt("developer_ID")), rs.getInt("vote"));
                }
            }
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to load collaboratorByTask", ex);
        }
        return result;
    }

    @Override
    public List<Developer> getCollaboratorRequestsByTask(int task_key) throws DataLayerException {
        List<Developer> result = new ArrayList();
        try {
            sCollaboratorRequestsByTask.setInt(1, task_key);
            try (ResultSet rs = sCollaboratorRequestsByTask.executeQuery()) {
                while (rs.next()) {
                    result.add((Developer) getDeveloper(rs.getInt("developer_ID")));
                }
            }
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to load collaboratorRequestsByTask", ex);
        }
        return result;
    }

    @Override
    public Map<Developer, Integer> getDevelopersBySkill(int skill_key) throws DataLayerException {
        Map<Developer, Integer> result = new HashMap<>();
        try {
            sDeveloperBySkill.setInt(1, skill_key);
            try (ResultSet rs = sDeveloperBySkill.executeQuery()) {
                while (rs.next()) {
                    result.put((Developer) getDeveloper(rs.getInt("ID")), rs.getInt("level"));
                }
            }
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to load developerBySkill", ex);
        }
        return result;
    }

    @Override
    public Map<Developer, Integer> getDevelopersBySkill(int skill_key, int level) throws DataLayerException {
        Map<Developer, Integer> result = new HashMap<>();
        try {
            sDeveloperBySkillWithLevel.setInt(1, skill_key);
            sDeveloperBySkillWithLevel.setInt(2, level);

            try (ResultSet rs = sDeveloperBySkillWithLevel.executeQuery()) {
                while (rs.next()) {
                    result.put((Developer) getDeveloper(rs.getInt("ID")), rs.getInt("level"));
                }
            }
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to load developerBySkill", ex);
        }
        return result;
    }

    @Override
    public Map<Skill, Integer> getSkillsByDeveloper(int developer_key) throws DataLayerException {
        Map<Skill, Integer> result = new HashMap<>();
        try {
            sSkillsByDeveloper.setInt(1, developer_key);

            try (ResultSet rs = sSkillsByDeveloper.executeQuery()) {
                while (rs.next()) {
                    result.put((Skill) getSkill(rs.getInt("skill_ID")), rs.getInt("level"));
                }
            }
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to load skillByDeveloper", ex);
        }
        return result;
    }

    @Override
    public Skill getParent(int skill_key) throws DataLayerException {
        try {
            sParentBySkill.setInt(1, skill_key);
            try (ResultSet rs = sParentBySkill.executeQuery()) {
                if (rs.next()) {
                    return (Skill) getSkill(rs.getInt("parent_ID"));
                }
            }
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to load skill parent", ex);
        }
        return null;

    }

    @Override
    public Type getTypeBySkill(int skill_key) throws DataLayerException {
        try {
            sTypeBySkill.setInt(1, skill_key);
            try (ResultSet rs = sTypeBySkill.executeQuery()) {
                if (rs.next()) {
                    return (Type) getType(rs.getInt("type_ID"));
                }
            }
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to load skill child", ex);
        }
        return null;

    }

    @Override
    public List<Skill> getChild(int skill_key) throws DataLayerException {
        List<Skill> result = new ArrayList();
        try {
            sChildBySkill.setInt(1, skill_key);
            try (ResultSet rs = sChildBySkill.executeQuery()) {
                while (rs.next()) {
                    result.add((Skill) getSkill(rs.getInt("ID")));
                }
            }

        } catch (SQLException ex) {
            throw new DataLayerException("Unable to load skill child", ex);
        }
        return result;
    }

    @Override
    public List<Skill> getSkillsParentList() throws DataLayerException {
        List<Skill> result = new ArrayList();
        try {
            try (ResultSet rs = sSkillsParentList.executeQuery()) {
                while (rs.next()) {
                    result.add((Skill) getSkill(rs.getInt("ID")));
                }
            }

        } catch (SQLException ex) {
            throw new DataLayerException("Unable to load skills", ex);
        }
        return result;
    }

    @Override
    public List<Developer> getProjectCollaborators(int project_key) throws DataLayerException {
        List<Developer> result = new ArrayList();
        try {
            sCollaboratorsByProjectID.setInt(1, project_key);
            try (ResultSet rs = sCollaboratorsByProjectID.executeQuery()) {
                while (rs.next()) {
                    result.add((Developer) getDeveloper(rs.getInt("developer_ID")));
                }
            }
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to load project collaborators by project_key", ex);
        }
        return result;
    }

    @Override
    public List<Project> getDeveloperProjects(int developer_key) throws DataLayerException {
        List<Project> result = new ArrayList();
        try {
            sProjectsByDeveloperID.setInt(1, developer_key);
            try (ResultSet rs = sProjectsByDeveloperID.executeQuery()) {
                while (rs.next()) {
                    result.add((Project) getProject(rs.getInt("project_ID")));
                }
            }
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to load projects by developer", ex);
        }
        return result;
    }

    @Override
    public List<Project> getDeveloperProjects(int developer_key, GregorianCalendar data) throws DataLayerException {
        List<Project> result = new ArrayList();
        try {
            sProjectsByDeveloperIDandDate.setInt(1, developer_key);
            Date sqldate = new Date(data.getTimeInMillis());
            sProjectsByDeveloperIDandDate.setDate(2, sqldate);
            try (ResultSet rs = sProjectsByDeveloperIDandDate.executeQuery()) {
                while (rs.next()) {
                    result.add((Project) getProject(rs.getInt("project_ID")));
                }
            }
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to load projects by developer and date", ex);
        }
        return result;
    }

    @Override
    public Project getProjectByTask(int task_key) throws DataLayerException {
        try {
            sProjectByTask.setInt(1, task_key);
            try (ResultSet rs = sProjectByTask.executeQuery()) {
                if (rs.next()) {
                    return (Project) getProject(rs.getInt("project_ID"));
                }
            }
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to load projects by developer and date", ex);
        }
        return null;
    }

    @Override
    public List<Task> getOffertsByDeveloper(int developer_key) throws DataLayerException {
        List<Task> result = new ArrayList();
        try {
            sOffertsByDeveloperID.setInt(1, developer_key);
            sOffertsByDeveloperID.setInt(2, developer_key);
            try (ResultSet rs = sOffertsByDeveloperID.executeQuery()) {
                while (rs.next()) {
                    result.add(getTask(rs.getInt("task_ID")));
                }

            }
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to load projects by developer and date", ex);
        }
        return result;

    }

    @Override
    public List<Message> getPublicMessages(int project_key) throws DataLayerException {
        List<Message> result = new ArrayList();
        try {
            sPublicMessagesByProject.setInt(1, project_key);
            try (ResultSet rs = sPublicMessagesByProject.executeQuery()) {
                while (rs.next()) {
                    result.add((Message) getMessage(rs.getInt("ID")));
                }
            }
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to public message by project", ex);
        }
        return result;
    }

    @Override
    public CollaborationRequest getCollaborationRequest(int collaborator_key, int task_key) throws DataLayerException {
        try {
            sRequestByID.setInt(1, collaborator_key);
            sRequestByID.setInt(2, task_key);
            try (ResultSet rs = sRequestByID.executeQuery()) {
                if (rs.next()) {
                    //notare come utilizziamo il costrutture
                    //"helper" della classe AuthorImpl
                    //per creare rapidamente un'istanza a
                    //partire dal record corrente
                    return createCollaborationRequest(rs);
                }
            }
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to load collaboration request by ID", ex);
        }
        return null;
    }

    @Override
    public List<CollaborationRequest> getInvitesByCoordinator(int coordinator_key) throws DataLayerException {
        List<CollaborationRequest> result = new ArrayList();
        try {
            sInvitesByCoordinatorID.setInt(1, coordinator_key);
            sInvitesByCoordinatorID.setInt(2, coordinator_key);
            try (ResultSet rs = sInvitesByCoordinatorID.executeQuery()) {
                while (rs.next()) {
                    result.add((CollaborationRequest) getCollaborationRequest(rs.getInt("developer_ID"), rs.getInt("task_ID")));
                }
            }
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to load invetes by project coordinator key ", ex);
        }
        return result;
    }

    @Override
    public List<CollaborationRequest> getProposalsByCollaborator(int collaborator_key) throws DataLayerException {
        List<CollaborationRequest> result = new ArrayList();
        try {
            sProposalsByCollaboratorID.setInt(1, collaborator_key);
            sProposalsByCollaboratorID.setInt(2, collaborator_key);
            try (ResultSet rs = sProposalsByCollaboratorID.executeQuery()) {
                while (rs.next()) {
                    result.add((CollaborationRequest) getCollaborationRequest(collaborator_key, rs.getInt("task_ID")));
                }
            }
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to load requests by collaborator key ", ex);
        }
        return result;

    }

    @Override
    public Developer getCoordinatorByTask(int task_key) throws DataLayerException {
        try {
            sCoordinatorByTask.setInt(1, task_key);
            try (ResultSet rs = sCoordinatorByTask.executeQuery()) {
                if (rs.next()) {
                    return (Developer) getDeveloper(rs.getInt("coordinator_ID"));
                }
            }
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to load coordiantor by task key ", ex);
        }
        return null;
    }

    @Override
    public List<CollaborationRequest> getQuestionsByCoordinator(int coordinator_key) throws DataLayerException {
        List<CollaborationRequest> result = new ArrayList();
        try {
            sQuestionsByCoordinatorID.setInt(1, coordinator_key);
            sQuestionsByCoordinatorID.setInt(2, coordinator_key);
            try (ResultSet rs = sQuestionsByCoordinatorID.executeQuery()) {
                while (rs.next()) {
                    result.add((CollaborationRequest) getCollaborationRequest(rs.getInt("developer_ID"), rs.getInt("task_ID")));
                }
            }
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to load questions by coordinator key ", ex);
        }
        return result;
    }

    @Override
    public int getVote(int task_key, int developer_key) throws DataLayerException {
        try {
            sVoteByTaskandDeveloper.setInt(1, task_key);
            sVoteByTaskandDeveloper.setInt(2, developer_key);
            try (ResultSet rs = sVoteByTaskandDeveloper.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("vote");
                }
            }
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to load vote by developer key and task key ", ex);
        }
        return -1;
    }

    @Override
    public Task getTaskByRequest(int collaborator_key, int coordinator_key) throws DataLayerException {

        try {
            sTaskByRequest.setInt(1, collaborator_key);
            sTaskByRequest.setInt(2, coordinator_key);
            try (ResultSet rs = sTaskByRequest.executeQuery()) {
                if (rs.next()) {
                    return (Task) getTask(rs.getInt("ID"));
                }
            }
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to load task by request ", ex);
        }
        return null;
    }

    @Override
    public int storeProject(Project project) throws DataLayerException {
        int key = project.getKey();
        try {
            if (key > 0) { //update
                //non facciamo nulla se l'oggetto non ha subito modifiche
                //do not store the object if it was not modified
                if (!project.isDirty()) {
                    return key;
                }
                uProject.setString(1, project.getName());
                uProject.setString(2, project.getDescription());
                if (project.getCoordinator() != null) {
                    uProject.setInt(3, project.getCoordinator().getKey());
                } else {
                    uProject.setNull(3, java.sql.Types.INTEGER);
                }
                uProject.setInt(4, project.getKey());
                uProject.executeUpdate();
            } else { //insert
                iProject.setString(1, project.getName());
                iProject.setString(2, project.getDescription());
                if (project.getCoordinator() != null) {
                    iProject.setInt(3, project.getCoordinator().getKey());
                } else {
                    iProject.setNull(3, java.sql.Types.INTEGER);
                }

                if (iProject.executeUpdate() == 1) {
                    //per leggere la chiave generata dal database
                    //per il record appena inserito, usiamo il metodo
                    //getGeneratedKeys sullo statement.
                    try (ResultSet keys = iProject.getGeneratedKeys()) {
                        //il valore restituito è un ResultSet con un record
                        //per ciascuna chiave generata (uno solo nel nostro caso)

                        if (keys.next()) {
                            //i campi del record sono le componenti della chiave
                            //(nel nostro caso, un solo intero)
                            key = keys.getInt(1);
                        }
                    }
                }
            }
            //restituiamo l'oggetto appena inserito RICARICATO
            //dal database tramite le API del modello. In tal
            //modo terremo conto di ogni modifica apportata
            //durante la fase di inserimento

            if (key > 0) {
                project.copyFrom(getProject(key));
            }
            project.setDirty(false);
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to store article", ex);
        }
        return key;
    }

    @Override
    public void deleteProject(Project project) throws DataLayerException {
        int key = project.getKey();
        try {
            dProject.setInt(1, key);
            dProject.executeUpdate();
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to store article", ex);
        }
    }

    @Override
    public void storeDeveloper(Developer developer) throws DataLayerException {
        int key = developer.getKey();
        try {

            if (key > 0) { //update
                //non facciamo nulla se l'oggetto non ha subito modifiche
                //do not store the object if it was not modified
                if (!developer.isDirty()) {
                    return;
                }
                uDeveloper.setString(1, developer.getUsername());
                uDeveloper.setString(2, developer.getMail());
                uDeveloper.setString(3, developer.getPwd());
                uDeveloper.setString(4, developer.getBiography());
                uDeveloper.setString(5, developer.getCurriculumString());
                if (developer.getCurriculumFile() != 0) {
                    uDeveloper.setInt(6, developer.getCurriculumFile());
                } else {
                    uDeveloper.setNull(6, java.sql.Types.INTEGER);
                }
                if (developer.getFoto() != 0) {
                    uDeveloper.setInt(7, developer.getFoto());
                } else {
                    uDeveloper.setNull(7, java.sql.Types.INTEGER);
                }
                uDeveloper.setInt(8, developer.getKey());
                uDeveloper.executeUpdate();
            } else { //insert
                iDeveloper.setString(1, developer.getName());
                iDeveloper.setString(2, developer.getSurname());
                iDeveloper.setString(3, developer.getUsername());
                iDeveloper.setString(4, developer.getMail());
                iDeveloper.setString(5, developer.getPwd());
                if (developer.getBirthDate() != null) {
                    Date sqldate = new Date(developer.getBirthDate().getTimeInMillis());
                    iDeveloper.setDate(6, sqldate);
                } else {
                    iDeveloper.setDate(6, null);
                }
                iDeveloper.setString(7, developer.getBiography());
                iDeveloper.setString(8, developer.getCurriculumString());

                if (iDeveloper.executeUpdate() == 1) {
                    //per leggere la chiave generata dal database
                    //per il record appena inserito, usiamo il metodo
                    //getGeneratedKeys sullo statement.
                    try (ResultSet keys = iDeveloper.getGeneratedKeys()) {
                        //il valore restituito è un ResultSet con un record
                        //per ciascuna chiave generata (uno solo nel nostro caso)

                        if (keys.next()) {
                            //i campi del record sono le componenti della chiave
                            //(nel nostro caso, un solo intero)
                            key = keys.getInt(1);
                        }
                    }
                }
            }
            //restituiamo l'oggetto appena inserito RICARICATO
            //dal database tramite le API del modello. In tal
            //modo terremo conto di ogni modifica apportata
            //durante la fase di inserimento

            if (key > 0) {
                developer.copyFrom(getDeveloper(key));
            }
            developer.setDirty(false);
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to store developer", ex);
        }
    }

    @Override
    public void deleteDeveloper(Developer developer) throws DataLayerException {
        int key = developer.getKey();
        try {
            dDeveloper.setInt(1, key);
            dDeveloper.executeUpdate();
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to store article", ex);
        }
    }

    @Override
    public int storeTask(Task task) throws DataLayerException {
        int key = task.getKey();
        try {
            if (key > 0) { //update
                //non facciamo nulla se l'oggetto non ha subito modifiche
                //do not store the object if it was not modified
                if (!task.isDirty()) {
                    return key;
                }
                uTask.setString(1, task.getName());
                uTask.setInt(2, task.getNumCollaborators());
                Date sqldate1 = new Date(task.getStartDate().getTimeInMillis());
                uTask.setDate(3, sqldate1);
                Date sqldate2 = new Date(task.getEndDate().getTimeInMillis());
                uTask.setDate(4, sqldate2);
                uTask.setString(5, task.getDescription());
                uTask.setBoolean(6, task.isOpen());

                if (task.getProject() != null) {
                    uTask.setInt(7, task.getProject().getKey());
                } else {
                    uTask.setNull(7, java.sql.Types.INTEGER);
                }
                /* if(task.getTypeByTask() != null){
                    uTask.setInt(8, task.getType_key());
                }else{
                    uTask.setNull(8, java.sql.Types.INTEGER);
                }*/
                uTask.setInt(8, task.getKey());
                uTask.executeUpdate();
            } else { //insert
                iTask.setString(1, task.getName());
                iTask.setInt(2, task.getNumCollaborators());
                Date sqldate1 = new Date(task.getStartDate().getTimeInMillis());
                iTask.setDate(3, sqldate1);
                Date sqldate2 = new Date(task.getEndDate().getTimeInMillis());
                iTask.setDate(4, sqldate2);
                iTask.setString(5, task.getDescription());
                iTask.setBoolean(6, task.isOpen());
                if (task.getProject() != null) {
                    iTask.setInt(7, task.getProject().getKey());
                } else {
                    iTask.setNull(7, java.sql.Types.INTEGER);
                }
                iTask.setInt(8, task.getType_key());

                if (iTask.executeUpdate() == 1) {
                    try (ResultSet keys = iTask.getGeneratedKeys()) {

                        if (keys.next()) {
                            key = keys.getInt(1);
                        }
                    }
                }
            }

            if (key > 0) {
                task.copyFrom(getTask(key));
            }
            task.setDirty(false);
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to store task", ex);
        }
        return key;
    }

    @Override
    public void deleteTask(Task task) throws DataLayerException {
        int key = task.getKey();
        try {
            dTask.setInt(1, key);
            dTask.executeUpdate();
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to store article", ex);
        }
    }

    @Override
    public void deleteTasksFromProject(int project_key) throws DataLayerException {

        try {
            dTasksFromProject.setInt(1, project_key);
            dTasksFromProject.executeUpdate();
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to store article", ex);
        }
    }

    @Override
    public void storeSkill(Skill skill) throws DataLayerException {
        int key = skill.getKey();
        try {
            if (key > 0) { //update
                //non facciamo nulla se l'oggetto non ha subito modifiche
                //do not store the object if it was not modified
                if (!skill.isDirty()) {
                    return;
                }
                uSkill.setString(1, skill.getName());
                if (skill.getParent() != null) {
                    uSkill.setInt(2, skill.getParent().getKey());
                } else {
                    uSkill.setNull(2, java.sql.Types.INTEGER);
                }
                uSkill.setInt(3, skill.getKey());
                uSkill.executeUpdate();
            } else { //insert
                iSkill.setString(1, skill.getName());
                if (skill.getParent() != null) {
                    iSkill.setInt(2, skill.getParent().getKey());
                } else {
                    iSkill.setNull(2, java.sql.Types.INTEGER);
                    skill.setParentKey(-1);
                }
                iSkill.setInt(3, skill.getType_key());
                if (iSkill.executeUpdate() == 1) {
                    try (ResultSet keys = iSkill.getGeneratedKeys()) {
                        if (keys.next()) {
                            key = keys.getInt(1);
                        }
                    }
                }
            }

            if (key > 0) {
                //getSkill(key).setParentKey(-1);
                skill.copyFrom(getSkill(key));
            }
            skill.setDirty(false);
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to store article", ex);
        }
    }

    @Override
    public void deleteSkill(Skill skill) throws DataLayerException {
        int key = skill.getKey();
        try {
            dSkill.setInt(1, key);
            dSkill.executeUpdate();
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to store article", ex);
        }
    }

    @Override
    public void storeMessage(Message message) throws DataLayerException {
        int key = message.getKey();
        try {
            if (key > 0) { //update
                //non facciamo nulla se l'oggetto non ha subito modifiche
                //do not store the object if it was not modified
                if (!message.isDirty()) {
                    return;
                }
                uMessage.setBoolean(1, message.isPrivate());
                uMessage.setString(2, message.getText());
                uMessage.setString(3, message.getType());
                if (message.getProject() != null) {
                    uMessage.setInt(4, message.getProject().getKey());
                } else {
                    uMessage.setNull(4, java.sql.Types.INTEGER);
                }
                uMessage.setInt(5, message.getKey());

                uMessage.executeUpdate();
            } else { //insert
                iMessage.setBoolean(1, message.isPrivate());
                iMessage.setString(2, message.getText());
                iMessage.setString(3, message.getType());
                if (message.getProject() != null) {
                    iMessage.setInt(4, message.getProject().getKey());
                } else {
                    iMessage.setNull(4, java.sql.Types.INTEGER);
                }
                if (message.getDeveloper() != null) {
                    iMessage.setInt(5, message.getDeveloper().getKey());
                } else {
                    iMessage.setNull(5, java.sql.Types.INTEGER);
                }
                if (iMessage.executeUpdate() == 1) {
                    try (ResultSet keys = iMessage.getGeneratedKeys()) {
                        if (keys.next()) {
                            key = keys.getInt(1);
                        }
                    }
                }
            }

            if (key > 0) {
                message.copyFrom(getMessage(key));
            }
            message.setDirty(false);
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to store article", ex);
        }
    }

    @Override
    public void deleteMessage(Message message) throws DataLayerException {
        int key = message.getKey();
        try {
            dMessage.setInt(1, key);
            dMessage.executeUpdate();

        } catch (SQLException ex) {
            throw new DataLayerException("Unable to store article", ex);
        }
    }

    @Override
    public void storeType(Type type) throws DataLayerException {
        int key = type.getKey();
        try {
            if (key > 0) { //update
                //non facciamo nulla se l'oggetto non ha subito modifiche
                //do not store the object if it was not modified
                if (!type.isDirty()) {
                    return;
                }
                uType.setString(1, type.getType());
                uType.setInt(2, type.getKey());

                uType.executeUpdate();
            } else { //insert
                iType.setString(1, type.getType());
                if (iType.executeUpdate() == 1) {
                    try (ResultSet keys = iType.getGeneratedKeys()) {
                        if (keys.next()) {
                            key = keys.getInt(1);
                        }
                    }
                }
            }

            if (key > 0) {
                type.copyFrom(getType(key));
            }
            type.setDirty(false);
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to store type", ex);
        }
    }

    @Override
    public int deleteType(Type type) throws DataLayerException {
        int key = type.getKey();
        try {
            dType.setInt(1, key);
            if (dType.executeUpdate() == 1) {
                return 1;
            }

        } catch (SQLException ex) {
            throw new DataLayerException("Unable to delete type", ex);
        }
        return 0;
    }

    @Override
    //task_has_developer
    public int storeTaskHasDeveloper(int task_ID, int developer_ID, int state, int vote, int sender) throws DataLayerException {
        boolean flag = true;

        java.sql.Date date1 = new Date(Calendar.getInstance().getTimeInMillis());
        try {
            sTaskHasDeveloper.setInt(1, task_ID);
            sTaskHasDeveloper.setInt(2, developer_ID);
            sTaskHasDeveloper.setInt(3, sender);
            try (ResultSet rs = sTaskHasDeveloper.executeQuery()) {
                if (!rs.next()) {
                    try {
                        sTaskByID.setInt(1, task_ID);
                        try (ResultSet rs1 = sTaskByID.executeQuery()) {
                            if (!rs1.next()) {
                                flag = false;
                            }
                        }
                    } catch (SQLException ex) {
                        throw new DataLayerException("Unable to select request", ex);
                    }
                    if (flag) {
                        try {
                            sDeveloperByID.setInt(1, developer_ID);
                            try (ResultSet rs2 = sDeveloperByID.executeQuery()) {
                                if (!rs2.next()) {
                                    flag = false;
                                }
                            }
                        } catch (SQLException ex) {
                            throw new DataLayerException("Unable to select request", ex);
                        }
                        try {
                            iTaskHasDeveloper.setInt(1, task_ID);
                            iTaskHasDeveloper.setInt(2, developer_ID);
                            iTaskHasDeveloper.setInt(3, state);
                            iTaskHasDeveloper.setDate(4, date1);
                            iTaskHasDeveloper.setInt(5, vote);
                            iTaskHasDeveloper.setInt(6, sender);
                            if (iTaskHasDeveloper.executeUpdate() == 1) {
                                return vote;
                            }

                        } catch (SQLException ex) {
                            throw new DataLayerException("Unable to insert task_has_dev", ex);
                        }

                    }
                } else {
                    try {

                        uTaskHasDeveloper.setInt(1, state);
                        uTaskHasDeveloper.setInt(2, vote);
                        uTaskHasDeveloper.setInt(3, task_ID);
                        uTaskHasDeveloper.setInt(4, developer_ID);
                        uTaskHasDeveloper.setInt(5, sender);
                        if (uTaskHasDeveloper.executeUpdate() == 1) {
                            return vote;
                        }
                    } catch (SQLException ex) {
                        throw new DataLayerException("Unable to insert task_has_dev", ex);
                    }

                }
            } catch (SQLException ex) {
                throw new DataLayerException("Unable to store request", ex);
            }
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to store request", ex);
        }
        return -1;
    }

    @Override
    public void deleteRequest(CollaborationRequest request) throws DataLayerException {
        int task_key = request.getTaskKey();
        int collaborator_key = request.getCollaboratorKey();
        try {
            dRequest.setInt(1, task_key);
            dRequest.setInt(2, collaborator_key);
            dRequest.executeUpdate();
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to delete request", ex);
        }
    }

    @Override
    public void storeTaskHasSkill(int task_key, int skill_key, int level_min) throws DataLayerException {
        try {
            sTaskHasSkill.setInt(1, task_key);
            sTaskHasSkill.setInt(2, skill_key);
            try (ResultSet rs = sTaskHasSkill.executeQuery()) {
                if (rs.next()) {
                    //update
                    return;
                }
            }
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to load taskHasSkill", ex);
        }
        try {
            iTaskHasSkill.setInt(1, task_key);
            iTaskHasSkill.setInt(2, skill_key);
            iTaskHasSkill.setInt(3, level_min);
            iTaskHasSkill.executeUpdate();
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to insert taskHasSkill", ex);
        }
    }

    /*@Override
    public void storeTaskHasSkill(int task_ID, int skill_ID, int level_min) throws DataLayerException {
        boolean flag = true;
        try {
            sTaskHasSkill.setInt(1, task_ID);
            sTaskHasSkill.setInt(2, skill_ID);
            try(ResultSet rs = sTaskHasSkill.executeQuery()){
                if(!rs.next()){
                    try {
                        sTaskByID.setInt(1, task_ID);
                    try(ResultSet rs1 = sTaskByID.executeQuery()){
                        if(!rs1.next()){
                            flag = false;
                        }
                }
            }catch (SQLException ex) {
                throw new DataLayerException("Unable to delete request", ex);
            }
            if(flag){
                try {
                    sSkillByID.setInt(1, skill_ID);
                    try(ResultSet rs2 = sSkillByID.executeQuery()){
                        if(!rs2.next()){
                            flag = false;
                        }
                    }
                }catch (SQLException ex) {
                    throw new DataLayerException("Unable to delete request", ex);
                }
                if(flag){
                    try {
                        //sTypeByID.setInt(1, type_ID);
                        try(ResultSet rs3 = sTypeByID.executeQuery()){
                            if(!rs3.next()){
                                flag = false;
                            }
                        }
                        }catch (SQLException ex) {
                            throw new DataLayerException("Unable to delete request", ex);
                        }
                        //continua qui
                    try{
                        iTaskHasSkill.setInt(1, task_ID);
                        iTaskHasSkill.setInt(2, skill_ID);
                        iTaskHasSkill.setInt(3, level_min);
                        iTaskHasSkill.executeUpdate();  

                    }catch (SQLException ex) {
                        throw new DataLayerException("Unable to insert task_has_skill", ex);
                        }

                    }
                }
            }else{
                    try{
                        uTaskHasSkill.setInt(1, task_ID);
                        uTaskHasSkill.setInt(2, skill_ID);
                        //uTaskHasSkill.setInt(3, type_ID);
                        uTaskHasSkill.setInt(4, level_min);
                        uTaskHasSkill.setInt(5, task_ID);
                        uTaskHasSkill.setInt(6, skill_ID);
                        //uTaskHasSkill.setInt(7, type_ID);                        
                        uTaskHasSkill.executeUpdate(); 
                    }catch (SQLException ex) {
                        throw new DataLayerException("Unable to insert task_has_dev", ex);
                    }
                }   
        }catch (SQLException ex) {
            throw new DataLayerException("Unable to select request", ex);
        } 
    }catch (SQLException ex){
        throw new DataLayerException("Unable to store request", ex);
        }
    }    */
    @Override
    public int storeFile(Part file_to_upload, File uploaded_file, String sdigest) throws DataLayerException {
        int key = 0;
        try {
            iImg.setString(1, file_to_upload.getSubmittedFileName());
            iImg.setLong(2, file_to_upload.getSize());
            iImg.setString(3, uploaded_file.getName());
            iImg.setString(4, sdigest);
            iImg.setString(5, file_to_upload.getContentType());
            //iImg.executeUpdate();
            if (iImg.executeUpdate() == 1) {
                try (ResultSet keys = iImg.getGeneratedKeys()) {
                    if (keys.next()) {
                        key = keys.getInt(1);
                    }
                }
            }
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to store img", ex);
        }
        return key;
    }

    @Override
    public int storeSkillHasDeveloper(int skill_ID, int developer_ID, int level) throws DataLayerException {
        boolean flag = true;
        try {
            sSkillHasDeveloper.setInt(1, skill_ID);
            sSkillHasDeveloper.setInt(2, developer_ID);
            try (ResultSet rs = sSkillHasDeveloper.executeQuery()) {
                if (rs.next()) {
                    /*uSkillHasDeveloper.setInt(1, skill_ID);
                    uSkillHasDeveloper.setInt(2, developer_ID);
                    uSkillHasDeveloper.setInt(3, level);
                    uSkillHasDeveloper.setInt(4, skill_ID);
                    uSkillHasDeveloper.setInt(5, developer_ID);                    
                    uSkillHasDeveloper.executeUpdate();*/
                    return 0;
                } else {
                    try {
                        sSkillByID.setInt(1, skill_ID);
                        try (ResultSet rs1 = sSkillByID.executeQuery()) {
                            if (!rs1.next()) {
                                flag = false;
                            }
                        }
                    } catch (SQLException ex) {
                        throw new DataLayerException("Unable to delete request", ex);
                    }
                    if (flag) {
                        try {
                            sDeveloperByID.setInt(1, developer_ID);
                            try (ResultSet rs2 = sDeveloperByID.executeQuery()) {
                                if (!rs2.next()) {
                                    flag = false;
                                }
                            }
                        } catch (SQLException ex) {
                            throw new DataLayerException("Unable to delete request", ex);
                        }
                        //continua qui
                        try {
                            iSkillHasDeveloper.setInt(1, skill_ID);
                            iSkillHasDeveloper.setInt(2, developer_ID);
                            iSkillHasDeveloper.setInt(3, level);
                            if (iSkillHasDeveloper.executeUpdate() == 1) {
                                return 1;
                            }

                        } catch (SQLException ex) {
                            throw new DataLayerException("Unable to insert skill_has_developer", ex);
                        }

                    }
                }
            } catch (SQLException ex) {
                throw new DataLayerException("Unable to store request", ex);
            }
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to store request", ex);
        }
        return 0;
    }

    @Override
    public int deleteTaskHasDeveloper(int task_id, int developer_id) throws DataLayerException {
        try {
            dTaskHasDeveloper.setInt(1, task_id);
            dTaskHasDeveloper.setInt(2, developer_id);
            if (dTaskHasDeveloper.executeUpdate() == 1) {
                return 1;
            }
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to delete", ex);
        }
        return 0;
    }

    @Override
    public int deleteSkillHasDeveloper(int task_id, int developer_id) throws DataLayerException {
        try {
            dSkillHasDeveloper.setInt(1, task_id);
            dSkillHasDeveloper.setInt(2, developer_id);
            if (dSkillHasDeveloper.executeUpdate() == 1) {
                return 1;
            }
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to delete", ex);
        }
        return 0;
    }

    @Override
    public void deleteTaskHasSkill(int task_id, int skill_id) throws DataLayerException {
        try {
            dTaskHasSkill.setInt(1, task_id);
            dTaskHasSkill.setInt(2, skill_id);

            dTaskHasSkill.executeUpdate();

        } catch (SQLException ex) {
            throw new DataLayerException("Unable to delete", ex);
        }
    }

    @Override
    public void deleteSkillsFromTask(int task_id) throws DataLayerException {
        try {
            dSkillsFromTask.setInt(1, task_id);
            dSkillsFromTask.executeUpdate();

        } catch (SQLException ex) {
            throw new DataLayerException("Unable to delete", ex);
        }
    }

    @Override
    public Date getEndDateOfTaskByProject(int project_key) throws DataLayerException {
        try {

            sEndDateOfTaskByProject.setInt(1, project_key);
            try (ResultSet rs = sEndDateOfTaskByProject.executeQuery()) {
                if (rs.next()) {
                    java.sql.Date date;
                    date = rs.getDate("maxend");
                    return date;
                }
            }
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to load maxend by ID", ex);
        }
        return null;
    }

    @Override
    public Date getDateOfTaskByProject(int project_key) throws DataLayerException {
        try {

            sDateOfTaskByProject.setInt(1, project_key);
            try (ResultSet rs = sDateOfTaskByProject.executeQuery()) {
                if (rs.next()) {
                    java.sql.Date date;
                    date = rs.getDate("start");
                    return date;
                }
            }
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to load skill by ID", ex);
        }
        return null;
    }

    @Override
    public List<Integer> getDeveloperByUsernameLike(String username) throws DataLayerException {
        List<Integer> result = new ArrayList();
        try {
            sDeveloperByUsernameLike.setString(1, "%" + username + "%");
            try (ResultSet rs = sDeveloperByUsernameLike.executeQuery()) {
                while (rs.next()) {
                    result.add(rs.getInt("ID"));
                }
            }
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to load developer", ex);
        }
        return result;
    }

    @Override
    public List<Developer> getDevelopersBySkillNoLevel(int skill_key, int level) throws DataLayerException {
        List<Developer> result = new ArrayList<>();
        try {
            sDeveloperBySkillWithLevel.setInt(1, skill_key);
            sDeveloperBySkillWithLevel.setInt(2, level);

            try (ResultSet rs = sDeveloperBySkillWithLevel.executeQuery()) {
                while (rs.next()) {
                    result.add((Developer) getDeveloper(rs.getInt("ID")));
                }
            }
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to load developerBySkill", ex);
        }
        return result;
    }

    @Override
    public Map<Developer, Integer> getDevelopersBySkillLimit(int skill_key, int level, int n) throws DataLayerException {
        Map<Developer, Integer> result = new HashMap<>();
        try {
            sDeveloperBySkillWithLevelLimit.setInt(1, skill_key);
            sDeveloperBySkillWithLevelLimit.setInt(2, level);
            sDeveloperBySkillWithLevelLimit.setInt(3, n);
            try (ResultSet rs = sDeveloperBySkillWithLevelLimit.executeQuery()) {
                while (rs.next()) {
                    result.put((Developer) getDeveloper(rs.getInt("ID")), rs.getInt("level"));
                }
            }
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to load developerBySkill", ex);
        }
        return result;
    }

    @Override
    public List<Project> getProjectsLimit(int n) throws DataLayerException {
        List<Project> result = new ArrayList();
        try {
            sProjectsLimit.setInt(1, n);
            try (ResultSet rs = sProjectsLimit.executeQuery()) {
                while (rs.next()) {
                    result.add((Project) getProject(rs.getInt("ID")));

                }
            } catch (SQLException ex) {
                throw new DataLayerException("Unable to load projects", ex);
            }
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to load projects", ex);
        }
        return result; //restituisce in result tutti gli oggetti Project esistenti
    }

    @Override
    public Admin createAdmin() {
        return new AdminImpl(this);
    }

    public Admin createAdmin(ResultSet rs) throws DataLayerException {
        try {
            Admin a = new AdminImpl(this);
            a.setKey(rs.getInt("ID"));
            a.setDeveloperKey(rs.getInt("developer_ID"));
            return a;
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to create admin object form ResultSet", ex);
        }
    }

    @Override
    public Admin getAdmin(int developer_key) throws DataLayerException {
        try {
            sAdmin.setInt(1, developer_key);
            try (ResultSet rs = sAdmin.executeQuery()) {
                if (rs.next()) {
                    return createAdmin(rs);
                }
            }
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to load admin by ID", ex);
        }
        return null;
    }

    @Override
    public List<Task> getTasksBySkill(int skill_key) throws DataLayerException {
        List<Task> result = new ArrayList();
        try {
            sTasksBySkill.setInt(1, skill_key);
            try (ResultSet rs = sTasksBySkill.executeQuery()) {
                if (rs.next()) {
                    result.add((Task) getTask(rs.getInt("task_ID")));
                }
            }
        } catch (SQLException ex) {
            throw new DataLayerException("Unable to load admin by ID", ex);
        }
        return result;
    }

}
