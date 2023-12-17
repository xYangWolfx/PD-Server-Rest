package pt.isec.pd.server.rest.managers;

import pt.isec.pd.server.rest.dataacess.EventsDataAccess;
import pt.isec.pd.server.rest.dataacess.RegistrationCodeDataAccess;
import pt.isec.pd.server.rest.models.Event;
import pt.isec.pd.server.rest.models.RegistrationCode;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

public class RegistrationCodeManager {
    private RegistrationCodeDataAccess registrationCodeDataAccess;
    private EventsManager eventsManager;
    private VersionsManager versionsManager;

    public RegistrationCodeManager(Connection connection) {
        this.registrationCodeDataAccess = new RegistrationCodeDataAccess(connection);
        this.eventsManager = new EventsManager(connection);
        this.versionsManager = new VersionsManager(connection);
    }

    public boolean createRegistrationCode(RegistrationCode registrationCode) {
        Random random = new Random();
        int code = random.nextInt(90000000) + 10000000;
        System.out.println("Número Aleatório: " + code);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        registrationCode.setCreation(LocalDateTime.now().format(formatter));
        registrationCode.setCode(String.valueOf(code));

        for (Event event : eventsManager.getAllEvents()) {
            if (event.getId() == registrationCode.getEventId()) {
                RegistrationCode codeTemp = registrationCodeDataAccess.getRegistrationCodeByEventId(registrationCode.getEventId());
                if (codeTemp != null) {
                    registrationCodeDataAccess.deleteRegistrationCode(codeTemp.getCode());
                    if (registrationCodeDataAccess.createRegistrationCode(registrationCode)){
                        versionsManager.updateDatabaseVersion();
                        return true;
                    }
                } else {
                    if (registrationCodeDataAccess.createRegistrationCode(registrationCode)) {
                        versionsManager.updateDatabaseVersion();
                        return true;
                    }
                }
            }
        }

        return false;
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
