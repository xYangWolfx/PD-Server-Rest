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

import java.util.List;

@RestController
public class UserEventsController {
    @GetMapping("/getEventsByUser")
    public ResponseEntity getRegisteredEventsFromUser(Authentication authentication,
                                     @RequestParam String email) {
        if (ClientConnection.isAdmin(authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not have permission to access this resource.");
        }

        DatabaseController databaseController = new DatabaseController("tp_db", "TP");
        DbConnections.handleDbConnections(databaseController, "tp_db");
        UsersEventsManager usersEventsManager = new UsersEventsManager(databaseController.getConnection());

        List<Event> eventList = usersEventsManager.getRegisteredEventsFromUser(email);

        if (!eventList.isEmpty()) {
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
}
