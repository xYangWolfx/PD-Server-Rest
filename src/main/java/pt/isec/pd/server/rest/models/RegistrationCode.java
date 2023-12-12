package pt.isec.pd.server.rest.models;


import java.io.Serializable;

public class RegistrationCode implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private int eventId;
    private String code;
    private int duration;
    private String creation;
    private int active;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }


    public String getCreation() {
        return creation;
    }

    public void setCreation(String creation) {
        this.creation = creation;
    }


    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "RegistrationCode[" +
                "id=" + id +
                ", eventId=" + eventId +
                ", code='" + code + '\'' +
                ", duration=" + duration +
                ", creation=" + creation +
                ", active=" + active +
                ']';
    }
}
