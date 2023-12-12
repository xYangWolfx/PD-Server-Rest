package pt.isec.pd.server.rest.managers;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseController {
    private DatabaseManager databaseManager;
    private String dbDirectory;

    public DatabaseController(String dbDirectory, String dbFileName) {
        this.databaseManager = new DatabaseManager();
        this.dbDirectory = dbDirectory + "/" + dbFileName + ".db";
    }

    public void connect() throws SQLException {
        databaseManager.connect(dbDirectory);
    }

    public void disconnect() throws SQLException {
        databaseManager.disconnect();
    }

    public void createDB(String directory) {
        try {
            File file = new File(System.getProperty("user.dir") + "/" + directory);

            if (!file.exists()){
                file.mkdir();
            }

            databaseManager.connect(dbDirectory);
            databaseManager.createTables();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean dbExists() {
        return new File(System.getProperty("user.dir") + "/" + dbDirectory).exists();
    }

    public Connection getConnection() {
        return databaseManager.getConnection();
    }
}
