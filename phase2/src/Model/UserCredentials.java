package Model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class UserCredentials implements Serializable, Observer {
  private String userName;
  private String password;
  private String userType;
  private long firmId;
  private long userId;
  private LocalDate creationDate;
  private List<String> inbox;

  // for the HR & interviwers
  public UserCredentials(String userName, String password, String userType, long firmId, long userId) {
    this.userName = userName;
    this.password = password;
    this.userType = userType;
    this.firmId = firmId;
    this.userId = userId;
  }

  // for the applicants
  public UserCredentials(String userName, String password, String userType, LocalDate creationDate, long userId) {
    this.userName = userName;
    this.password = password;
    this.userType = userType;
    this.creationDate = creationDate;
    this.userId = userId;
  }

  public String getUserName() {
    return this.userName;
  }

  public String getPassword() {
    return this.password;
  }

  public String getUserType() {
    return this.userType;
  }

  public long getFirmId(){
    return firmId;
  }

  @Override
  public String toString() {
    return "Username: " + this.userName;
  }

  public LocalDate getCreationDate() {
    return this.creationDate;
  }


  public long getUserID() {
    return this.userId;
  }

  public List<String> getInbox(){
    return this.inbox;
  }


  @Override
  public void update(Observable o, Object arg) {
    inbox.add("You get a new message from " + o + " : " + arg);
  }
}
