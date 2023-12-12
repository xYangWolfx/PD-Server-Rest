package pt.isec.pd.server.rest.models;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {
  private static final long serialVersionUID = 1L;

  private int id;
  private String email;
  private String password;
  private String name;
  private int idNumber;
  private int nif;
  
  private boolean isAdmin;
  
  private List<Event> events;
  public List<Event> getEvents() {
    return events;
  }

  public void setEvents(List<Event> events) {
    this.events = events;
  }
  
  public User() {}

  public User(String email, String password, String name, int idNumber, int nif) {
    this.email = email;
    this.password = password;
    this.name = name;
    this.idNumber = idNumber;
    this.nif = nif;
    this.events = new ArrayList<>();
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }


  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }


  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }


  public int getIdNumber() {
    return idNumber;
  }

  public void setIdNumber(int idNumber) {
    this.idNumber = idNumber;
  }

  public int getNif() {
    return nif;
  }

  public void setNif(int nif) {
    this.nif = nif;
  }

  @Override
  public String toString() {
    return "[" +
            "id=" + id +
            ", email='" + email + '\'' +
            ", password='" + password + '\'' +
            ", name='" + name + '\'' +
            ", idNumber=" + idNumber +
            ", nif=" + nif +
            ']';
  }

  public boolean isAdmin() {
    return isAdmin;
  }

  public void setAdmin(boolean admin) {
    isAdmin = admin;
  }
}
