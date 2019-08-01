package Model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class UserCredentials implements Serializable, Observer {
  private String userName;
  private String password;
  private userTypes userType;
  private long firmId;
  private long userId;
  private LocalDate creationDate;
  private List<String> inbox = new ArrayList<>();

  public enum userTypes{
    HR,
    APPLICANT,
    INTERVIEWER,
    REFERER
  }

  // for the HR & interviwers
  public UserCredentials(String userName, String password, userTypes type, long firmId, long userId) {
    this.userName = userName;
    this.password = password;
    this.userType = type;
    this.firmId = firmId;
    this.userId = userId;
  }

  // for the applicants
  public UserCredentials(String userName, String password, userTypes type, LocalDate creationDate, long userId) {
    this.userName = userName;
    this.password = password;
    this.userType = type;
    this.creationDate = creationDate;
    this.userId = userId;
  }

  public String getUserName() {
    return this.userName;
  }

  public String getPassword() {
    return this.password;
  }

  public userTypes getUserType() {
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
    List argument = (ArrayList) arg;
    if (argument.get(0).equals("hire") && argument.get(1).equals(this.userId)){
      inbox.add("You get a new message from " + o + " : You are hired by our company. You are welcome!");
    }
    else{
      inbox.add("You get a new message from " + o + " : Congratulations! You are rejected");
    }
  }

}
