package Model;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import Control.HyreLauncher;
public class UserCredentialsDatabase extends TemplateDatabase<UserCredentials> {

  public UserCredentials getUserByID(Long id) {
    return super.getItemByID(id);
  } 

  public UserCredentials getUserByCredentials(String userName) {
    ArrayList<UserCredentials> userList = super.getListOfItems();
    for (UserCredentials user : userList) {
      if (user.getUserName().equals(userName)) {
        return user;
      }
    }
    return null;
  }


  public UserCredentials getUserByCredentials(String userName, String password) {
    UserCredentials user = this.getUserByCredentials(userName);
    if (user != null) {
      if (user.getPassword().equals(password)) {
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

  // for applicants
  public UserCredentials addUser(String userName,
                                 String password,
                                 UserCredentials.userTypes type,
                                 LocalDate creationDate) {
    UserCredentials newUser = new UserCredentials(userName, password, type, creationDate, super.getCurrID());
    super.addItem(newUser);
    return newUser;
  }

  // for interviewers and hr coordinators
  public UserCredentials addUser(String userName,
                                 String password, 
                                 UserCredentials.userTypes type,
                                 long firmId) {
    UserCredentials newUser = new UserCredentials(userName, password, type, firmId, super.getCurrID());
    super.addItem(newUser);
    return newUser;
  }

  public enum filterKeys {

  }

  public List<UserCredentials> filter(HashMap<filterKeys, Long> filtration){
    return null; // todo
  }
}
