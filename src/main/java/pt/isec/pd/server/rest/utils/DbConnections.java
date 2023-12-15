package pt.isec.pd.server.rest.utils;

import pt.isec.pd.server.rest.controllers.DatabaseController;

import java.sql.SQLException;

public class DbConnections {
    public static void handleDbConnections(DatabaseController databaseController, String dbDirectory) {
        if (databaseController.dbExists()) {
            try {
                databaseController.connect();
            } catch (SQLException e) {
                System.out.println("Erro de coneção à BD!");
                throw new RuntimeException(e);
            }
        } else {
            databaseController.createDB(dbDirectory);
        }
    }
}
