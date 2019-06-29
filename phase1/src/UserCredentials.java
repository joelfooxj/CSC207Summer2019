public class UserCredentials {
  private String userName;
  private String password;
  private String userType;
  private long firmId;

  // for the HR & interviwers
  public UserCredentials(String userName, String password, String userType, long firmId) {
    this.userName = userName;
    this.password = password;
    this.userType = userType;
    this.firmId = firmId;
  }

  // for the applicants
  public UserCredentials(String userName, String password, String userType) {
    this.userName = userName;
    this.password = password;
    this.userType = userType;
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
}
