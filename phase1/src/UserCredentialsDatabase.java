import java.util.HashMap;
public class UserCredentialsDatabase extends AbstractDatabase {

  public UserCredentials getUserByID(Long id) {
    return (UserCredentials) super.getItemByID(id);
  }
  
  public UserCredentials getUserByCredentials(String userName, String password) {
    Long ids = super.getCurrID();
    for (Long i = 0L; i < ids; i++) {
      UserCredentials user = getUserByID(i);
      if (user != null) {
        if (user.getUserName() == userName && user.getPassword() == password) {
          return user;
        }
      }
    }
    return null;
  }

  public boolean userExists(String username) {
    Long ids = super.getCurrID();
    for (Long i = 0L; i < ids; i++) {
      UserCredentials user = getUserByID(i);
      if (user != null) {
        if (user.getUserName().equals(username)) {
          return true;
        }
      }
    }
    return false;
  }

  public void addUsers(String userName, String password, String accountType) {
    if (!userExists(userName)) {
      super.addItem(new UserCredentials(userName, password, accountType));
    }
  }

  public void removeUserByID(Long id) {
    super.removeItemByID(id);
  }
}
