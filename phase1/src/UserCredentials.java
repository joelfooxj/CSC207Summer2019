public class UserCredentials {
  private String userName;
  private String password;
  private String userType;

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
}
