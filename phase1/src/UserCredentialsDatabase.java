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

  public void removeUserByID(Long id) {
    super.removeItemByID(id);
    //super.saveChanges(super.getData(),"UserCredentials.bin");
  }

  public void addUser(String userName, String password, String accountType) {
    super.addItem(new UserCredentials(userName, password, accountType));
  }

  public void addUser(String userName, String password, String accountType, long firmId) {
    super.addItem(new UserCredentials(userName, password, accountType, firmId));
  }

  public void printInterviewersByFirmID(long firmId) {
    for (Long i = 0L; i < super.getCurrID(); i++) {
      UserCredentials item = super.getItemByID(i);
        if (item != null && item.getUserType().equals("Interviewer") && item.getFirmId() == firmId) {
          System.out.println("[" + i.toString() + "] " + item.toString() + "\n");
      }
    }
  }
}
