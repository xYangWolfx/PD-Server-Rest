package pt.isec.pd.server.rest.dataacess;


import pt.isec.pd.server.rest.models.RegistrationCode;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RegistrationCodeDataAccess {
    private Connection connection;

    public RegistrationCodeDataAccess(Connection connection) {
        this.connection = connection;
    }

    public void createRegistrationCode(RegistrationCode registrationCode) {
        String insertSQL = "INSERT INTO RegistrationCode (event_id, code, duration, creation, active) VALUES (?, ?, ?, ?, ?)";

        try {
            deleteRegistrationCodeByEventId(registrationCode.getEventId());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
            preparedStatement.setInt(1, registrationCode.getEventId());
            preparedStatement.setString(2, registrationCode.getCode());
            preparedStatement.setInt(3, registrationCode.getDuration());
            preparedStatement.setString(4, registrationCode.getCreation());
            preparedStatement.setInt(5, 1);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteRegistrationCodeByEventId(int eventId) {
        String updateSQL = "UPDATE RegistrationCode SET active = 0 WHERE event_id = ? AND active = 1";

        try (PreparedStatement preparedStatement = connection.prepareStatement(updateSQL)){
            preparedStatement.setInt(1, eventId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public RegistrationCode getRegistrationCodeByEventId(int eventId) {
        String selectSQL = "SELECT * FROM RegistrationCode WHERE event_id = ? AND active = 1";

        try (PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
            preparedStatement.setInt(1, eventId);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                RegistrationCode registrationCode = new RegistrationCode();

                registrationCode.setId(resultSet.getInt("id"));
                registrationCode.setEventId(resultSet.getInt("event_id"));
                registrationCode.setCode(resultSet.getString("code"));
                registrationCode.setDuration(resultSet.getInt("duration"));
                registrationCode.setCreation(resultSet.getString("creation"));
                registrationCode.setActive(resultSet.getInt("active"));

                return registrationCode;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    public int getEventIdByCode(String code) {
        String selectSQL = "SELECT event_id FROM RegistrationCode WHERE code = ? AND active = 1";

        try (PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
            preparedStatement.setString(1, code);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("event_id");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // Se não encontrar nenhum registro ativo com o código fornecido, você pode retornar um valor padrão ou lançar uma exceção, dependendo do seu requisito.
        // Neste exemplo, estou retornando -1, mas você pode ajustar conforme necessário.
        return -1;
    }


    public void deleteRegistrationCode(String code) {
        String updateSQL = "UPDATE RegistrationCode SET active = 0 WHERE code = ? AND active = 1";

        try (PreparedStatement preparedStatement = connection.prepareStatement(updateSQL)){
            preparedStatement.setString(1, code);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public List<RegistrationCode> getAllActiveRegistrationCodes() {
        String selectAllActiveSQL = "SELECT * FROM RegistrationCode WHERE active = 1";
        List<RegistrationCode> registrationCodes = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(selectAllActiveSQL)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                RegistrationCode registrationCode = new RegistrationCode();

                registrationCode.setId(resultSet.getInt("id"));
                registrationCode.setEventId(resultSet.getInt("event_id"));
                registrationCode.setCode(resultSet.getString("code"));
                registrationCode.setDuration(resultSet.getInt("duration"));
                registrationCode.setCreation(resultSet.getString("creation"));
                registrationCode.setActive(resultSet.getInt("active"));

                registrationCodes.add(registrationCode);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return registrationCodes;
    }



}
