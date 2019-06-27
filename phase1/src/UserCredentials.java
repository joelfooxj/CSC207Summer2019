public class UserCredentials {
  private String userName;
  private String password;
  private String accountType;

  public UserCredentials(String userName, String password, String accountType) {
    this.userName = userName;
    this.password = password;
    this.accountType = accountType;
  }

  public String getUserName() {
    return this.userName;
  }

  public String getPassword() {
    return this.password;
  }
}
