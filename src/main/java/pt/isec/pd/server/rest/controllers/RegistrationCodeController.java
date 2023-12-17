package pt.isec.pd.server.rest.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pt.isec.pd.server.rest.managers.RegistrationCodeManager;
import pt.isec.pd.server.rest.models.RegistrationCode;
import pt.isec.pd.server.rest.utils.ClientConnection;
import pt.isec.pd.server.rest.utils.DbConnections;

@RestController
public class RegistrationCodeController {
    @PostMapping("/createCode")
    public ResponseEntity createCode(Authentication authentication,
                                      @RequestBody RegistrationCode registrationCode) {
        if (!ClientConnection.isAdmin(authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not have permission to access this resource.");
        }

        DatabaseController databaseController = new DatabaseController("tp_db", "TP");
        DbConnections.handleDbConnections(databaseController, "tp_db");
        RegistrationCodeManager registrationCodeManager = new RegistrationCodeManager(databaseController.getConnection());

        if (registrationCodeManager.createRegistrationCode(registrationCode)) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Registration code created successfully");
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("An error ocurred while trying to create the registration code");
    }
}
