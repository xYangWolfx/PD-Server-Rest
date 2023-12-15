package pt.isec.pd.server.rest.managers;

import pt.isec.pd.server.rest.dataacess.UsersDataAccess;
import pt.isec.pd.server.rest.models.User;

import java.sql.Connection;
import java.util.List;

public class UsersManager {
    private UsersDataAccess usersDataAccess;
    private VersionsManager versionsManager;

    public UsersManager(Connection connection) {
        this.usersDataAccess = new UsersDataAccess(connection);
        this.versionsManager = new VersionsManager(connection);
    }

    public int getIdNumber() {
        return usersDataAccess.getIdNumber();
    }

    public User getUserByEmail(String email) {
        return usersDataAccess.getUserByEmail(email);
    }

    public boolean createUser(User user) {
        if (usersDataAccess.createUser(user)) {
            return versionsManager.updateDatabaseVersion();
        }

        return false;
    }

    public boolean updateUser(User userByEmail, User user) {
        if (usersDataAccess.updateUser(userByEmail, user)) {
            return versionsManager.updateDatabaseVersion();
        }

        return false;
    }

    public List<User> getAllUsers(){
        return usersDataAccess.getAllUsers();
    }
}
