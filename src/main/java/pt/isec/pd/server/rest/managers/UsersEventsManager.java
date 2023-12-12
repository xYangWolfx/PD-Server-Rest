package pt.isec.pd.server.rest.managers;

import pt.isec.pd.server.rest.dataacess.EventsDataAccess;
import pt.isec.pd.server.rest.dataacess.UsersDataAccess;
import pt.isec.pd.server.rest.dataacess.UsersEventsDataAccess;
import pt.isec.pd.server.rest.models.Event;
import pt.isec.pd.server.rest.models.User;
import pt.isec.pd.server.rest.rmi.handler.DatabaseVersionChangeHandler;

import java.sql.Connection;
import java.util.List;

public class UsersEventsManager {
    private UsersDataAccess usersDataAccess;
    private UsersEventsDataAccess usersEventsDataAccess;
    private EventsDataAccess eventsDataAccess;
    private VersionsManager versionsManager;

    public UsersEventsManager(Connection connection, DatabaseVersionChangeHandler changeHandler) {
        this.usersDataAccess = new UsersDataAccess(connection);
        this.usersEventsDataAccess = new UsersEventsDataAccess(connection);
        this.eventsDataAccess = new EventsDataAccess(connection);
        this.versionsManager = new VersionsManager(connection, changeHandler);
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

    public boolean addUserToEvent(int userId, int eventId) {
        usersEventsDataAccess.addUserToEvent(userId, eventId);
        return versionsManager.updateDatabaseVersion();
    }

    public boolean deleteUserFromEvent(int id, int eventId) {
        usersEventsDataAccess.deleteUserRegistration(id, eventId);
        return versionsManager.updateDatabaseVersion();
    }
}
