package pt.isec.pd.server.rest.managers;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private final String databaseURL = "jdbc:sqlite:" + System.getProperty("user.dir");
    private File databaseFile = new File(System.getProperty("user.dir") + "/TP.db");
    private Connection connection;
    private static final String CREATE_USERS_TABLE = "CREATE TABLE Users ("
            + "id INTEGER,"
            + "email TEXT UNIQUE,"
            + "password TEXT,"
            + "name TEXT,"
            + "id_number INTEGER,"
            + "nif INTEGER,"
            + "CONSTRAINT User_PK PRIMARY KEY (id)"
            + ")";
    private static final String CREATE_EVENTS_TABLE = "CREATE TABLE Events ("
            + "id INTEGER,"
            + "name TEXT,"
            + "date TEXT,"
            + "start_hour TEXT,"
            + "end_hour TEXT,"
            + "place TEXT,"
            + "CONSTRAINT Events_PK PRIMARY KEY (id)"
            + ")";
    public static final String CREATE_VERSIONS_TABLE = "CREATE TABLE Versions ("
            + "id INTEGER,"
            + "version INTEGER DEFAULT (1),"
            + "\"timestamp\" INTEGER,"
            + "CONSTRAINT Versions_PK PRIMARY KEY (id)"
            + ")";
    public static final String CREATE_REGISTRATIONCODE_TABLE = "CREATE TABLE RegistrationCode ("
            + "id INTEGER,"
            + "event_id INTEGER,"
            + "code TEXT,"
            + "duration INTEGER," //TimeUnit.MINUTES.toMillis(30)
            + "creation TEXT," //date.getTime()
            + "active INTEGER," //0 -> false / 1 -> true
            + "CONSTRAINT RegistrationCode_PK PRIMARY KEY (id),"
            + "FOREIGN KEY (event_id) REFERENCES Events(id)"
            + ")";
    public static final String CREATE_USERS_EVENTS_TABLE = "CREATE TABLE UsersEvents ("
            + "user_id INTEGER,"
            + "event_id INTEGER,"
            + "CONSTRAINT UsersEvents_PK PRIMARY KEY (user_id,event_id),"
            + "CONSTRAINT Events_FK FOREIGN KEY (event_id) REFERENCES Events(id),"
            + "CONSTRAINT Users_FK FOREIGN KEY (user_id) REFERENCES Users(id)"
            + ")";

    public void connect(String dbDirectory) throws SQLException {
        connection = DriverManager.getConnection(databaseURL + "/" + dbDirectory);
    }

    public void disconnect() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

    public void createTables() throws SQLException {
        Statement statement = connection.createStatement();

        statement.executeUpdate(CREATE_VERSIONS_TABLE);
        statement.executeUpdate(CREATE_USERS_TABLE);
        statement.executeUpdate(CREATE_EVENTS_TABLE);
        statement.executeUpdate(CREATE_REGISTRATIONCODE_TABLE);
        statement.executeUpdate(CREATE_USERS_EVENTS_TABLE);

        statement.executeUpdate("INSERT INTO Users (email, password) VALUES ('admin', 'admin')");
        statement.executeUpdate("INSERT INTO Users (email, password,name,id_number,nif) VALUES ('leonardo', '123','Leonardo',1,123456789)");
        statement.executeUpdate("INSERT INTO Versions (version, timestamp) VALUES (0, strftime('%s','now'))");


        statement.close();
    }

    public Connection getConnection() {
        return connection;
    }
}
