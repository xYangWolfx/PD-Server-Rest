package pt.isec.pd.server.rest.dataacess;

import pt.isec.pd.server.rest.models.Event;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UsersEventsDataAccess {
    private UsersDataAccess usersDataAccess;
    private EventsDataAccess eventsDataAccess;
    private Connection connection;

    public UsersEventsDataAccess(Connection connection) {
        this.connection = connection;
        this.eventsDataAccess = new EventsDataAccess(connection);
        this.usersDataAccess = new UsersDataAccess(connection);
    }

    public void registerUserForEvent(String email, String eventName) {
        String insertSQL = "INSERT INTO UsersEvents (user_id, event_id) VALUES (?, ?)";

        int userId = usersDataAccess.getUserIdByEmail(email);
        int eventId = eventsDataAccess.getEventIdByName(eventName);

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
            preparedStatement.setLong(1, userId);
            preparedStatement.setLong(2, eventId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteUserRegistration(int userId, int eventId) {
        String deleteSQL = "DELETE FROM UsersEvents WHERE user_id = ? AND event_id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL)) {
            preparedStatement.setLong(1, userId);
            preparedStatement.setLong(2, eventId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //Return a list of user_id that are registered in a specific event
    public List<Integer> getUsersRegisteredForEvent(int eventId) {
        List<Integer> registeredUserIds = new ArrayList<>();
        String selectSQL = "SELECT user_id FROM UsersEvents WHERE event_id = ?";


        try (PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
            preparedStatement.setLong(1, eventId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    registeredUserIds.add(resultSet.getInt("user_id"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return registeredUserIds;
    }

    //Returns a list of events where a user is registered
    public List<Event> getEventsForUser(String userEmail) {
        int userId = usersDataAccess.getUserIdByEmail(userEmail);

        if (userId != -1) {
            List<Integer> eventIds = getEventIdsForUser(userId);

            List<Event> events = new ArrayList<>();
            for (int eventId : eventIds) {
                Event event = eventsDataAccess.findEventById(eventId);
                if (event != null) {
                    events.add(event);
                }
            }

            return events;
        } else {
            return Collections.emptyList();
        }
    }

    public List<Integer> getEventIdsForUser(int userId) {
        List<Integer> eventIds = new ArrayList<>();
        String selectSQL = "SELECT event_id FROM UsersEvents WHERE user_id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
            preparedStatement.setInt(1, userId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    eventIds.add(resultSet.getInt("event_id"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return eventIds;
    }

    public boolean hasRegistrations(int eventId) {
        String countRegistrationsSQL = "SELECT COUNT(*) FROM UsersEvents WHERE event_id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(countRegistrationsSQL)) {
            preparedStatement.setLong(1, eventId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to check for registrations.", e);
        }

        return false;
    }


    public void addUserToEvent(int userId, int eventId) {
        String insertSQL = "INSERT INTO UsersEvents (user_id, event_id) VALUES (?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, eventId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
