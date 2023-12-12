package pt.isec.pd.server.rest.dataacess;


import pt.isec.pd.server.rest.models.Event;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EventsDataAccess {
    private Connection connection;

    public EventsDataAccess(Connection connection) {
        this.connection = connection;
    }

    public void createEvent(Event event) {
        String insertSQL = "INSERT INTO Events (name, date, start_hour, end_hour, place) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
            preparedStatement.setString(1, event.getName());
            preparedStatement.setString(2, event.getDate());
            preparedStatement.setString(3, event.getStartHour());
            preparedStatement.setString(4, event.getEndHour());
            preparedStatement.setString(5, event.getPlace());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateEvent(int eventId, Event updatedEvent) {
        String updateSQL = "UPDATE Events SET name = ?, date = ?, start_hour = ?, end_hour = ?, place = ? WHERE id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(updateSQL)) {
            preparedStatement.setString(1, updatedEvent.getName());
            preparedStatement.setString(2, updatedEvent.getDate());
            preparedStatement.setString(3, updatedEvent.getStartHour());
            preparedStatement.setString(4, updatedEvent.getEndHour());
            preparedStatement.setString(5, updatedEvent.getPlace());
            preparedStatement.setInt(6, eventId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void deleteEvent(int eventId) {
        String deleteSQL = "DELETE FROM Events WHERE id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL)) {
            preparedStatement.setInt(1, eventId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Event> getAllEvents() {
        String selectSQL = "SELECT * FROM Events";
        List<Event> events = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(selectSQL);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                Event event = new Event();
                event.setName(resultSet.getString("name"));
                event.setDate(resultSet.getString("date"));
                event.setStartHour(resultSet.getString("start_hour"));
                event.setEndHour(resultSet.getString("end_hour"));
                event.setPlace(resultSet.getString("place"));
                event.setId(resultSet.getInt("id"));
                events.add(event);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return events;

    }

    public Event findEventById(int eventId) {
        String selectSQL = "SELECT * FROM Events WHERE id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
            preparedStatement.setInt(1, eventId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Event event = new Event();
                    event.setId(resultSet.getInt("id"));
                    event.setName(resultSet.getString("name"));
                    event.setDate(resultSet.getString("date"));
                    event.setStartHour(resultSet.getString("start_hour"));
                    event.setEndHour(resultSet.getString("end_hour"));
                    event.setPlace(resultSet.getString("place"));
                    return event;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public Event findEventByName(String eventName) {
        String selectSQL = "SELECT * FROM Events WHERE name = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
            preparedStatement.setString(1, eventName);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Event event = new Event();
                    event.setId(resultSet.getInt("id"));
                    event.setName(resultSet.getString("name"));
                    event.setDate(resultSet.getString("date"));
                    event.setStartHour(resultSet.getString("start_hour"));
                    event.setEndHour(resultSet.getString("end_hour"));
                    event.setPlace(resultSet.getString("place"));
                    return event;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public int getEventIdByName(String eventName) {
        String selectSQL = "SELECT id FROM Events WHERE name = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
            preparedStatement.setString(1, eventName);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("id");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return -1;
    }


}
