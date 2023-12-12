package pt.isec.pd.server.rest.models;

import java.io.Serializable;
import java.util.List;

public class Event implements Serializable {
  private static final long serialVersionUID = 1L;

  private int id;
  private String name;
  private String date;
  private String startHour;
  private String endHour;
  private String place;

  private List<User> users;
  public List<User> getUsers() {
    return users;
  }

  public void setUser(List<User> users) {
    this.users = users;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public String getStartHour() {
    return startHour;
  }

  public void setStartHour(String startHour) {
    this.startHour = startHour;
  }

  public String getEndHour() {
    return endHour;
  }

  public void setEndHour(String endHour) {
    this.endHour = endHour;
  }

  public String getPlace() {
    return place;
  }

  public void setPlace(String place) {
    this.place = place;
  }

  @Override
  public String toString() {
    return "[" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", date='" + date + '\'' +
            ", startHour='" + startHour + '\'' +
            ", endHour='" + endHour + '\'' +
            ", place='" + place + '\'' +
            ']';
  }
}
