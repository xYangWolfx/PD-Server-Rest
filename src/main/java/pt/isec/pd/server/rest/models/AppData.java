package pt.isec.pd.server.rest.models;

import java.io.Serializable;
import java.util.List;

public class AppData implements Serializable {
    private static final long serialVersionUID = 1L;
    public List<Event> events;
    public List<User> users;
    public List<RegistrationCode> registrationCodes;
    private int databaseVersion;
    private User user;
    public AppData() {

    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<RegistrationCode> getRegistrationCodes() {
        return registrationCodes;
    }

    public void setRegistrationCodes(List<RegistrationCode> registrationCodes) {
        this.registrationCodes = registrationCodes;
    }

    @Override
    public String toString() {
        return "AppData{" +
                "events=" + events +
                ", users=" + users +
                ", registrationCodes=" + registrationCodes +
                '}';
    }

    public String toStringPersonalizado(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("AppData Version ["+ databaseVersion + "]{\n");
        stringBuilder.append("\tEvents{\n");
        for (Event event : events) {
            boolean haveCode = false;
            stringBuilder.append("\t\t");
            stringBuilder.append(event.toString());
            for (RegistrationCode registrationCode : registrationCodes) {
                if(registrationCode.getEventId() == event.getId()){
                    stringBuilder.append("\t"+registrationCode.toString());
                    haveCode = true;
                }
            }
            if(!haveCode){
                stringBuilder.append("\tRegistration: Don't have any code");
            }
            stringBuilder.append("\n\t\t\tUsers registered for this event{\n");
            for (User user : event.getUsers()) {
                stringBuilder.append("\t\t\t\t");
                stringBuilder.append(user.toString());
                stringBuilder.append("\n");
            }
            stringBuilder.append("\t\t\t}\n");
        }
        stringBuilder.append("\t}\n}");

        stringBuilder.append("\tUsers{\n");
        for (User user : users) {
            stringBuilder.append("\t\t");
            stringBuilder.append(user.toString());
            if(!user.getEvents().isEmpty()){
                stringBuilder.append("\n\t\t\tLogged events{\n");
                for (Event event : user.getEvents()) {
                    stringBuilder.append("\t\t\t\t");
                    stringBuilder.append(event.toString());
                    stringBuilder.append("\n");
                }
                stringBuilder.append("\t\t\t}\n");
            }else{
                stringBuilder.append("\n\t\t\tUser did not register for any events\n");
            }

        }

        stringBuilder.append("\t}\n}\n");
        return stringBuilder.toString();
    }

    public int getDatabaseVersion() {
        return databaseVersion;
    }

    public void setDatabaseVersion(int databaseVersion) {
        this.databaseVersion = databaseVersion;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
