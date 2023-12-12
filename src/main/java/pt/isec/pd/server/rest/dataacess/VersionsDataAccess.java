package pt.isec.pd.server.rest.dataacess;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class VersionsDataAccess {
    private Connection connection;

    public VersionsDataAccess(Connection connection) {
        this.connection = connection;
    }

    public boolean updateVersion() {
        String updateSQL = "UPDATE Versions SET version = ?, timestamp = strftime('%s','now') WHERE id = 1";

        try (PreparedStatement preparedStatement = connection.prepareStatement(updateSQL)) {
            int version = getDatabaseVersion();

            preparedStatement.setInt(1, version + 1);

            int updatedRows = preparedStatement.executeUpdate();

            if (updatedRows == 0) {
                System.out.println("Error while trying to update the database version!");
                return false;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error while trying to update the database version!", e);
        }

        return true;
    }

    public int getDatabaseVersion() {
        String selectSQL = "SELECT version FROM Versions WHERE id = 1";

        try (PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("version");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }
}
