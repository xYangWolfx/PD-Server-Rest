package pt.isec.pd.server.rest.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import pt.isec.pd.server.rest.managers.EventsManager;
import pt.isec.pd.server.rest.models.Event;
import pt.isec.pd.server.rest.utils.ClientConnection;
import pt.isec.pd.server.rest.utils.DbConnections;

import java.util.List;

@RestController
public class EventsController {
    @PostMapping("/createEvent")
    public ResponseEntity createEvent(Authentication authentication,
                                      @RequestBody Event event) {
        if (!ClientConnection.isAdmin(authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not have permission to access this resource.");
        }

        DatabaseController databaseController = new DatabaseController("tp_db", "TP");
        DbConnections.handleDbConnections(databaseController, "tp_db");
        EventsManager eventsManager = new EventsManager(databaseController.getConnection());

        if (eventsManager.createEvent(event)) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Event created successfully");
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("An error ocurred while trying to create the event");
    }

    @DeleteMapping("/deleteEvent")
    public ResponseEntity deleteEvent(Authentication authentication,
                                      @RequestBody int eventId) {
        if (!ClientConnection.isAdmin(authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not have permission to access this resource.");
        }

        DatabaseController databaseController = new DatabaseController("tp_db", "TP");
        DbConnections.handleDbConnections(databaseController, "tp_db");
        EventsManager eventsManager = new EventsManager(databaseController.getConnection());

        if (eventsManager.deleteEvent(eventId)) {
            return ResponseEntity.status(HttpStatus.OK).body("Event deleted successfully");
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You can't delete this event because it has registrations");
    }

    @GetMapping("/getEvents")
    public ResponseEntity getAllEvents(Authentication authentication) {
        if (!ClientConnection.isAdmin(authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not have permission to access this resource.");
        }

        DatabaseController databaseController = new DatabaseController("tp_db", "TP");
        DbConnections.handleDbConnections(databaseController, "tp_db");
        EventsManager eventsManager = new EventsManager(databaseController.getConnection());

        List<Event> eventList = eventsManager.getAllEvents();

        if (eventList != null && !eventList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(eventList);
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(eventList);
    }
}
