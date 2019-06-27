import java.util.HashMap;
public class UserCredentialsDatabase extends AbstractDatabase {

  public UserCredentials getUserByID(Long id) {
    return (UserCredentials) super.getItemByID(id);
  }
  
  public UserCredentials getUserByCredentials(String userName, String password) {
    HashMap<Long, Object> data = super.getData();
    for (Long id : data.keySet()) {
      UserCredentials user = getUserByID(id);
      if (user.getUserName() == userName && user.getPassword() == password) {
        return user;
      }
    }
    return null;
  }

  public void addUsers(String userName, String password, String accountType) {
    super.addItem(new UserCredentials(userName, password, accountType));
  }

  public void removeUserByID(Long id) {
    super.removeItemByID(id);
  }
}
