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

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

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
    public ResponseEntity getAllEvents(Authentication authentication,
                                       @RequestParam(value = "date", required = false) LocalDate startDate,
                                       @RequestParam(value = "name", required = false) String name,
                                       @RequestParam(value = "location", required = false) String location,
                                       @RequestParam(value = "startHour", required = false) String startHour,
                                       @RequestParam(value = "endHour", required = false) String endHour) {
        if (!ClientConnection.isAdmin(authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not have permission to access this resource.");
        }

        DatabaseController databaseController = new DatabaseController("tp_db", "TP");
        DbConnections.handleDbConnections(databaseController, "tp_db");
        EventsManager eventsManager = new EventsManager(databaseController.getConnection());

        List<Event> eventList = eventsManager.getAllEvents();

        if (eventList != null && !eventList.isEmpty()) {
            // Apply filters if provided
            if (startDate != null || name != null || location != null || startHour != null || endHour != null) {
                eventList = filterEvents(eventList, startDate, name, location, startHour, endHour);
            }
            return ResponseEntity.status(HttpStatus.OK).body(eventList);
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(eventList);
    }

    private List<Event> filterEvents(List<Event> events,
                                     LocalDate startDate,
                                     String name,
                                     String location,
                                     String startHour,
                                     String endHour) {
        // Implement logic to filter events based on the provided parameters
        // You can use streams and predicates to filter the list

        return events.stream()
                .filter(event -> startDate == null || event.getDate().equals(startDate.toString()))
                .filter(event -> name == null || event.getName().equals(name))
                .filter(event -> location == null || event.getPlace().equals(location))
                .filter(event -> startHour == null || event.getStartHour().equals(startHour))
                .filter(event -> endHour == null || event.getEndHour().equals(endHour))
                .collect(Collectors.toList());
    }
}
