package pt.isec.pd.server.rest.managers;

import pt.isec.pd.server.rest.dataacess.VersionsDataAccess;

import java.sql.Connection;

public class VersionsManager {
    private VersionsDataAccess versionsDataAccess;

    public VersionsManager(Connection connection) {
        this.versionsDataAccess = new VersionsDataAccess(connection);
    }

    public boolean updateDatabaseVersion() {
        return versionsDataAccess.updateVersion();
    }

    public int getDatabaseVersion() {
        return versionsDataAccess.getDatabaseVersion();
    }
}
