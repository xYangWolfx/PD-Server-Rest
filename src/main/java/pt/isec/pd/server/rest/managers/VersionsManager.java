package pt.isec.pd.server.rest.managers;

import pt.isec.pd.server.rest.dataacess.VersionsDataAccess;
import pt.isec.pd.server.rest.rmi.handler.DatabaseVersionChangeHandler;

import java.sql.Connection;

public class VersionsManager {
    private VersionsDataAccess versionsDataAccess;
    private DatabaseVersionChangeHandler changeHandler;

    public VersionsManager(Connection connection) {
        this.versionsDataAccess = new VersionsDataAccess(connection);
    }

    public VersionsManager(Connection connection, DatabaseVersionChangeHandler changeHandler) {
        this.versionsDataAccess = new VersionsDataAccess(connection);
        this.changeHandler = changeHandler;
    }

    public boolean updateDatabaseVersion() {
        if (versionsDataAccess.updateVersion()) {
            changeHandler.notifyListeners();
            return true;
        }
        return false;
    }

    public int getDatabaseVersion() {
        return versionsDataAccess.getDatabaseVersion();
    }
}
