package Model;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;

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

  public void removeUserByID(Long id) {
    super.removeItemByID(id);
    //super.saveChanges(super.getData(),"Model.UserCredentials.bin");
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

  public Long getUserID(UserCredentials user) {
    for (Long i = 0L; i < super.getCurrID(); i++) {
      if (super.getItemByID(i) == user) {
        return i;
      }
    }
    return null;
  }

  public void printInterviewersByFirmID(long firmId) {
    for (Long i = 0L; i < super.getCurrID(); i++) {
      UserCredentials item = super.getItemByID(i);
        if (item != null && item.getUserType().equals("Interviewer") && item.getFirmId() == firmId) {
          System.out.println("[" + i.toString() + "] " + item.toString() + "\n");
      }
    }
  }

  public void printApplicants() {
    StringBuilder ret = new StringBuilder();
    for (Long i = 0L; i < super.getCurrID(); i++) {
      if (getItemByID(i) != null && getItemByID(i).getUserType().equals(HyreLauncher.getApplicantUserType())) {
        ret.append("\n[" + i.toString() + "] " + getItemByID(i).toString());
      }
    }
    System.out.println(ret);
  }

  public ArrayList<UserCredentials> getUsersByUsername(String username) {
    ArrayList<UserCredentials> users = new ArrayList<>();
    for (UserCredentials user : this) {
      if (user.getUserName().contains(username)) {
        users.add(user);
      }
    }
    return users;
  }
}
