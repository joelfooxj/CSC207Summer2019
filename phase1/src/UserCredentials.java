import java.time.LocalDate;

public class UserCredentials {
  private String userName;
  private String password;
  private String userType;
  private long firmId;
  private long applicantId;
  private LocalDate creationDate;

  // for the HR & interviwers
  public UserCredentials(String userName, String password, String userType, long firmId) {
    this.userName = userName;
    this.password = password;
    this.userType = userType;
    this.firmId = firmId;
  }

  // for the applicants
  public UserCredentials(String userName, String password, String userType, LocalDate creationDate, long applicantId) {
    this.userName = userName;
    this.password = password;
    this.userType = userType;
    this.creationDate = creationDate;
    this.applicantId = applicantId;
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

  //TODO: Use UserInterface.getDate() in constructor to set creation date
  public LocalDate getCreationDate() {
    return this.creationDate;
  }


  public long getApplicantID() {
    return this.applicantId;
  }

}
