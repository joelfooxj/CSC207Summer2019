import java.util.HashMap;
import java.util.ArrayList;
public class UserCredentialsDatabase extends AbstractDatabase<UserCredentials> {

  public UserCredentials getUserByID(Long id) {
    return super.getItemByID(id);
  }
  
  public UserCredentials getUserByCredentials(String userName, String password) {
    ArrayList<UserCredentials> userList = super.getListOfItems();
    for (UserCredentials user : userList) {
      if (user.getUserName() == userName && user.getPassword() == password) {
        return user;
      }
    }
    return null;
  }

  public boolean userExists(String username) {
    ArrayList<UserCredentials> userList = super.getListOfItems();
    for (UserCredentials user : userList) {
      if (user.getUserName().equals(username)) {
        return true;
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
