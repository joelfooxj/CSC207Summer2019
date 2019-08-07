package Model.UserCredentialsPackage;

import Model.TemplateDatabase;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class UserCredentialsDatabase extends TemplateDatabase<UserCredentials> {

    public UserCredentials getUserByCredentials(String userName, String password) {
        for (UserCredentials user : this) {
            if (user.getUserName().equals(userName) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    public boolean userExists(String username) {
        List<UserCredentials> userList = super.getListOfItems();
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

    // for interviewers and hr
    public UserCredentials addUser(String userName,
                                   String password,
                                   UserCredentials.userTypes type,
                                   long firmId) {
        UserCredentials newUser = new UserCredentials(userName, password, type, firmId, super.getCurrID());
        super.addItem(newUser);
        return newUser;
    }

    public enum usersFilterKeys {
        ACCOUNT_TYPE,
        FIRM_ID,
        USERNAME,
        REPR,
        USER_ID
    }

    public List<UserCredentials> filterUsers(HashMap<usersFilterKeys, Object> filtration) {
        List<UserCredentials> userCredentialsList = this.getListOfItems();
        if (filtration.containsKey(usersFilterKeys.ACCOUNT_TYPE)) {
            userCredentialsList = userCredentialsList.stream().filter(userCredentials -> userCredentials.getUserType().equals(
                    filtration.get(usersFilterKeys.ACCOUNT_TYPE))).collect(Collectors.toList());
        }
        if (filtration.containsKey(usersFilterKeys.FIRM_ID)) {
            userCredentialsList = userCredentialsList.stream().filter(userCredentials ->
                    userCredentials.getFirmId().toString().equals(filtration.get(usersFilterKeys.FIRM_ID))).collect(Collectors.toList());
        }
        if (filtration.containsKey(usersFilterKeys.USERNAME)) {
            userCredentialsList = userCredentialsList.stream().filter(userCredentials ->
                    userCredentials.getUserName().contains((String) filtration.get(usersFilterKeys.USERNAME))).collect(Collectors.toList());
        }
        if (filtration.containsKey(usersFilterKeys.REPR)) {
            userCredentialsList = userCredentialsList.stream().filter(userCredentials ->
                    userCredentials.toString().equals(filtration.get(usersFilterKeys.REPR))).collect(Collectors.toList());
        }
        if (filtration.containsKey(usersFilterKeys.USER_ID)) {
            userCredentialsList = userCredentialsList.stream().filter(userCredentials ->
                    userCredentials.getUserID().equals(filtration.get(usersFilterKeys.USER_ID))).collect(Collectors.toList());
        }
        return userCredentialsList;
    }
}
