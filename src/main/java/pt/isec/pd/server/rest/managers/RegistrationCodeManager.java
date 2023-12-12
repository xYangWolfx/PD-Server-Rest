package pt.isec.pd.server.rest.managers;

import pt.isec.pd.server.rest.dataacess.EventsDataAccess;
import pt.isec.pd.server.rest.dataacess.RegistrationCodeDataAccess;
import pt.isec.pd.server.rest.models.RegistrationCode;
import pt.isec.pd.server.rest.rmi.handler.DatabaseVersionChangeHandler;

import java.sql.Connection;
import java.util.List;

public class RegistrationCodeManager {
    private RegistrationCodeDataAccess registrationCodeDataAccess;
    private EventsDataAccess eventsDataAccess;
    private VersionsManager versionsManager;

    public RegistrationCodeManager(Connection connection, DatabaseVersionChangeHandler changeHandler) {
        this.registrationCodeDataAccess = new RegistrationCodeDataAccess(connection);
        this.eventsDataAccess = new EventsDataAccess(connection);
        this.versionsManager = new VersionsManager(connection, changeHandler);
    }

    public void createRegistrationCode(RegistrationCode registrationCode) {
        registrationCodeDataAccess.createRegistrationCode(registrationCode);
        versionsManager.updateDatabaseVersion();
    }

    public RegistrationCode getRegistrationCodeByEventId(int eventId) {
        return  registrationCodeDataAccess.getRegistrationCodeByEventId(eventId);
    }


    public void deleteRegistrationCode(String code) {
        registrationCodeDataAccess.deleteRegistrationCode(code);
        versionsManager.updateDatabaseVersion();
    }

    public int getEventIdByCode(String code) {
        return registrationCodeDataAccess.getEventIdByCode(code);
    }

    public List<RegistrationCode> getAllRegistrationCode(){
        return registrationCodeDataAccess.getAllActiveRegistrationCodes();
    }
}
