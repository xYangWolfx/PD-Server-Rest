package pt.isec.pd.server.rest.managers;

import pt.isec.pd.server.rest.dataacess.EventsDataAccess;
import pt.isec.pd.server.rest.dataacess.UsersDataAccess;
import pt.isec.pd.server.rest.dataacess.UsersEventsDataAccess;
import pt.isec.pd.server.rest.models.Event;
import pt.isec.pd.server.rest.models.RegistrationCode;
import pt.isec.pd.server.rest.models.User;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class UsersEventsManager {
    private UsersDataAccess usersDataAccess;
    private UsersEventsDataAccess usersEventsDataAccess;
    private EventsDataAccess eventsDataAccess;
    private VersionsManager versionsManager;
    private RegistrationCodeManager registrationCodeManager;

    public UsersEventsManager(Connection connection) {
        this.usersDataAccess = new UsersDataAccess(connection);
        this.usersEventsDataAccess = new UsersEventsDataAccess(connection);
        this.eventsDataAccess = new EventsDataAccess(connection);
        this.versionsManager = new VersionsManager(connection);
    }

    public boolean deleteUserRegistration(String email, String eventName) {
        int userId = usersDataAccess.getUserIdByEmail(email);
        int eventId = eventsDataAccess.getEventIdByName(eventName);

        usersEventsDataAccess.deleteUserRegistration(userId, eventId);
        return versionsManager.updateDatabaseVersion();
    }

    public List<Event> getRegisteredEventsFromUser(String email) {
        return usersEventsDataAccess.getEventsForUser(email);
    }


    public List<User> getUsersRegisteredForEvent(int eventId) {
        List<Integer> registeredUserIds = usersEventsDataAccess.getUsersRegisteredForEvent(eventId);
        return usersDataAccess.getUsersByIds(registeredUserIds);
    }

    public boolean addUserToEvent(String email, String code) {
        int idEvent = registrationCodeManager.getEventIdByCode(code);
        RegistrationCode registrationCode = registrationCodeManager.getRegistrationCodeByEventId(idEvent);

        if(idEvent == -1){
            return false;
        }else{
            Event event = eventsDataAccess.findEventById(idEvent);
            LocalDateTime currentDateTime = LocalDateTime.now();

            LocalDateTime eventStartDateTime = LocalDateTime.parse(event.getDate() + " " + event.getStartHour(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            LocalDateTime eventEndDateTime = LocalDateTime.parse(event.getDate() + " " + event.getEndHour(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            //Se a hora atual esta entre os eventos
            if(currentDateTime.isAfter(eventStartDateTime) && currentDateTime.isBefore(eventEndDateTime)){
                LocalDateTime codeCreationDateTime = LocalDateTime.parse(event.getDate() + " " + registrationCode.getCreation(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                LocalDateTime codeExpirationDateTime = codeCreationDateTime.plusMinutes(registrationCode.getDuration());

                //Se a hora a atual estiver antes da data de limite
                if(currentDateTime.isBefore(codeExpirationDateTime)){
                    System.out.println("O codigo ainda nao expirou");

                    //Se o user ainda não se registou a presença
                    User user = usersDataAccess.getUserByEmail(email);
                    user.setEvents(usersEventsDataAccess.getEventsForUser(user.getEmail()));
                    if(user.getEvents() != null){
                        for (Event e : user.getEvents()) {
                            System.out.println(e.getId() + " " + idEvent);
                            if(e.getId() == idEvent){
                                return false;
                            }
                        }
                    }
                    usersEventsDataAccess.addUserToEvent(usersDataAccess.getUserByEmail(email).getId(),idEvent);
                    versionsManager.updateDatabaseVersion();
                    return true;
                }else{
                    return false;
                }
            }else{
                return false;
            }
        }
    }

    public boolean deleteUserFromEvent(int id, int eventId) {
        usersEventsDataAccess.deleteUserRegistration(id, eventId);
        return versionsManager.updateDatabaseVersion();
    }
}
