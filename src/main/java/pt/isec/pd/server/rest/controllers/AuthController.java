package pt.isec.pd.server.rest.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pt.isec.pd.server.rest.managers.UsersManager;
import pt.isec.pd.server.rest.models.User;
import pt.isec.pd.server.rest.security.TokenService;
import pt.isec.pd.server.rest.utils.DbConnections;

@RestController
public class AuthController {
    private final TokenService tokenService;

    public AuthController(TokenService tokenService)
    {
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public String login(Authentication authentication)
    {
        return tokenService.generateToken(authentication);
    }

    @PostMapping("/register")
    public ResponseEntity registerUser(@RequestParam(value = "email") String email,
                                       @RequestParam(value = "password") String password,
                                       @RequestParam(value = "name") String name,
                                       @RequestParam(value = "idNumber") int idNumber,
                                       @RequestParam(value = "nif") int nif) {
        DatabaseController databaseController = new DatabaseController("tp_db", "TP");
        DbConnections.handleDbConnections(databaseController, "tp_db");
        UsersManager usersManager = new UsersManager(databaseController.getConnection());

        User user = new User(email, password, name, idNumber, nif);

        if (usersManager.createUser(user)) {
            return ResponseEntity.ok(HttpStatus.CREATED);
        }

        return ResponseEntity.badRequest().body(HttpStatus.BAD_REQUEST);
    }
}
