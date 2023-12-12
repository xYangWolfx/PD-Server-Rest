package pt.isec.pd.server.rest.managers;



import pt.isec.pd.server.rest.dataacess.EventsDataAccess;
import pt.isec.pd.server.rest.dataacess.UsersEventsDataAccess;
import pt.isec.pd.server.rest.models.Event;
import pt.isec.pd.server.rest.rmi.handler.DatabaseVersionChangeHandler;

import java.sql.Connection;
import java.util.List;

public class EventsManager {
    private EventsDataAccess eventsDataAccess;
    private UsersEventsDataAccess usersEventsDataAccess;
    private VersionsManager versionsManager;

    public EventsManager(Connection connection, DatabaseVersionChangeHandler changeHandler) {
        this.eventsDataAccess = new EventsDataAccess(connection);
        this.usersEventsDataAccess = new UsersEventsDataAccess(connection);
        this.versionsManager = new VersionsManager(connection, changeHandler);
    }

    public boolean updateEvent(int eventId, Event event) {

        if (hasRegistrations(eventId)) {
            return false;
        } else {
            eventsDataAccess.updateEvent(eventId, event);
            return versionsManager.updateDatabaseVersion();
        }
    }

    public boolean deleteEvent(int eventId) {

        if (hasRegistrations(eventId)) {
            return false;
        } else {
            eventsDataAccess.deleteEvent(eventId);
            return versionsManager.updateDatabaseVersion();
        }
    }

    public List<Event> getAllEvents() {
        return eventsDataAccess.getAllEvents();
    }

    private boolean hasRegistrations(int eventId) {
        return usersEventsDataAccess.hasRegistrations(eventId);
    }


    public Event findEventById(int eventId) {
        return eventsDataAccess.findEventById(eventId);
    }
    public boolean createEvent(Event event) {
        eventsDataAccess.createEvent(event);
        return versionsManager.updateDatabaseVersion();
    }
}
