package pt.isec.pd.server.rest.utils;

import com.opencsv.CSVWriter;
import pt.isec.pd.server.rest.models.Event;
import pt.isec.pd.server.rest.models.User;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CSVGenerator {
    public void generateUserAttendanceCSV(String csvFilePath, User user, List<Event> events) {

        try {
            try (CSVWriter writer = new CSVWriter(new FileWriter(csvFilePath))) {
                String[] headerUser = { "Nome", "Número identificação", "Email" };
                writer.writeNext(headerUser);

                String[] userRow = { user.getName(), String.valueOf(user.getIdNumber()), user.getEmail() };
                writer.writeNext(userRow);

                writer.writeNext(new String[0]);

                String[] headerEvents = { "Designação", "Local", "Data", "Hora início" };
                writer.writeNext(headerEvents);

                for (Event event : events) {
                    String[] eventRow = {
                            event.getName(),
                            event.getPlace(),
                            event.getDate(),
                            event.getStartHour()
                    };

                    writer.writeNext(eventRow);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void generateEventAttendanceCSV(String csvFilePath, Event event, List<User> users) {
        try (CSVWriter csvWriter = new CSVWriter(new FileWriter(csvFilePath))) {
            String[] eventHeader = { "Designação", "Local", "Data", "Hora início" };
            String[] eventData = {
                    event.getName(),
                    event.getPlace(),
                    event.getDate(),
                    event.getStartHour()
            };

            csvWriter.writeNext(eventHeader);
            csvWriter.writeNext(eventData);

            csvWriter.writeNext(new String[0]);

            String[] headerUser = { "Nome", "Número identificação", "Email" };
            csvWriter.writeNext(headerUser);

            for (User user : users) {
                String[] userData = { user.getName(), String.valueOf(user.getIdNumber()), user.getEmail() };
                csvWriter.writeNext(userData);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
