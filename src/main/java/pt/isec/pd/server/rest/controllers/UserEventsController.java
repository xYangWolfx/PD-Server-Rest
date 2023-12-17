package pt.isec.pd.server.rest.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pt.isec.pd.server.rest.managers.RegistrationCodeManager;
import pt.isec.pd.server.rest.managers.UsersEventsManager;
import pt.isec.pd.server.rest.models.Event;
import pt.isec.pd.server.rest.models.RegistrationCode;
import pt.isec.pd.server.rest.models.User;
import pt.isec.pd.server.rest.utils.ClientConnection;
import pt.isec.pd.server.rest.utils.DbConnections;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class UserEventsController {
    @GetMapping("/getEventsByUser")
    public ResponseEntity getRegisteredEventsFromUser(Authentication authentication,
                                                      @RequestParam(value = "email") String email,
                                                      @RequestParam(value = "date", required = false) LocalDate startDate,
                                                      @RequestParam(value = "name", required = false) String name,
                                                      @RequestParam(value = "location", required = false) String location,
                                                      @RequestParam(value = "startHour", required = false) String startHour,
                                                      @RequestParam(value = "endHour", required = false) String endHour) {
        if (ClientConnection.isAdmin(authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not have permission to access this resource.");
        }

        DatabaseController databaseController = new DatabaseController("tp_db", "TP");
        DbConnections.handleDbConnections(databaseController, "tp_db");
        UsersEventsManager usersEventsManager = new UsersEventsManager(databaseController.getConnection());

        List<Event> eventList = usersEventsManager.getRegisteredEventsFromUser(email);

        if (!eventList.isEmpty()) {
            // Apply filters if provided
            if (startDate != null || name != null || location != null || startHour != null || endHour != null) {
                eventList = filterEvents(eventList, startDate, name, location, startHour, endHour);
            }

            return ResponseEntity.status(HttpStatus.OK).body(eventList);
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(eventList);
    }

    @GetMapping("/getUsersByEvent")
    public ResponseEntity getUsersRegisteredForEvent(Authentication authentication,
                                     @RequestParam int eventId) {
        if (!ClientConnection.isAdmin(authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not have permission to access this resource.");
        }

        DatabaseController databaseController = new DatabaseController("tp_db", "TP");
        DbConnections.handleDbConnections(databaseController, "tp_db");
        UsersEventsManager usersEventsManager = new UsersEventsManager(databaseController.getConnection());

        List<User> userList = usersEventsManager.getUsersRegisteredForEvent(eventId);

        if (!userList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(userList);
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(userList);
    }

    @PostMapping("/registerUserForEvent")
    public ResponseEntity registerUserForEvent(Authentication authentication,
                                               @RequestBody Map<String, Object> requestBody) {
        if (ClientConnection.isAdmin(authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not have permission to access this resource.");
        }

        DatabaseController databaseController = new DatabaseController("tp_db", "TP");
        DbConnections.handleDbConnections(databaseController, "tp_db");
        UsersEventsManager usersEventsManager = new UsersEventsManager(databaseController.getConnection());

        String email = (String) requestBody.get("email");
        String code = (String) requestBody.get("code");

        if (usersEventsManager.addUserToEvent(email, code)) {
            return ResponseEntity.status(HttpStatus.OK).body("Registration successfull");
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("An error ocurred while trying to register on event");
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
