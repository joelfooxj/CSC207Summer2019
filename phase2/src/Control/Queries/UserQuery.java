package Control.Queries;

import Model.UserCredentialsPackage.UserCredentials;

import java.util.ArrayList;
import java.util.List;

public class UserQuery {

    /**
     * Class provides information from filtered lists of User Credentials to forms in the View Package
     */

    private List<UserCredentials> filteredUsers;

    UserQuery(List<UserCredentials> filteredUsers) {
        this.filteredUsers = filteredUsers;
    }

    public List<String> getRepresentations() {
        List<String> userReps = new ArrayList<>();
        for (UserCredentials user : filteredUsers) {
            userReps.add(user.toString());
        }
        return userReps;
    }

    public List<Long> getIDs() {
        ArrayList<Long> ids = new ArrayList<>();
        for (UserCredentials user : this.filteredUsers) {
            ids.add(user.getUserID());
        }
        return ids;
    }
}
