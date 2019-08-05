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
  private Long firmId;
  private Long userId;
  private LocalDate creationDate;
  private List<String> inbox = new ArrayList<>();

  public enum userTypes{
    HR,
    APPLICANT,
    INTERVIEWER,
    REFERER
  }

  // this constructor is only for the HR & interviewers
  /**
   *
   * @param userName: the login username of the HR/ interviewer (applicant does NOT use this constructor)
   * @param password: the password of the account
   * @param type: the type of the user. It is either HR or interviewer
   * @param firmId: the firm ID of the company that that user works in
   * @param userId: the unique ID of this user (this is NOT the log in username)
   */
  public UserCredentials(String userName, String password, userTypes type, long firmId, long userId) {
    this.userName = userName;
    this.password = password;
    this.userType = type;
    this.firmId = firmId;
    this.userId = userId;
  }

  // this constructor is only for the applicants
  /**
   *
   * @param userName: the login username of the applicant (HR& interviewer do NOT use this constructor)
   * @param password: the password of this account
   * @param type: the type of the user, which is applicant
   * @param creationDate: the creation date of this applicant account
   * @param userId: the unique ID of this applicant (this is NOT the log in username)
   */
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

  public Long getFirmId(){
    return firmId;
  }

  @Override
  public String toString() {
    return "Username: " + this.userName;
  }

  public LocalDate getCreationDate() {
    return this.creationDate;
  }


  public Long getUserID() {
    return this.userId;
  }

  public List<String> getInbox(){
    return this.inbox;
  }


  @Override
  public void update(Observable o, Object arg) {
    List argument = (ArrayList) arg;
    if (argument.get(0).equals("hire") && argument.get(1).equals(this.userId)){
      inbox.add("You get a message from "+argument.get(2)+" You are hired by our company. You are welcome!");
    }
    else{
      inbox.add("You get a message from "+argument.get(2)+" Congratulations! You are rejected");
    }
  }

}
