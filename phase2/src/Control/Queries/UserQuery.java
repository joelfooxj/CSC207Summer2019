package Control.Queries;

import Model.UserCredentials;

import java.util.ArrayList;
import java.util.List;

public class UserQuery {
    private List<UserCredentials> filteredUsers;
    UserQuery(List<UserCredentials> filteredUsers){
        this.filteredUsers = filteredUsers;
    }

    public List<String> getRepresentations(){
        List<String> ret = new ArrayList<>();
        for (UserCredentials user : this.filteredUsers) {
            ret.add(user.toString());
        }
        List<String> userReps = new ArrayList<>();
        for (UserCredentials user: filteredUsers){
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


    public String getRepresentation(){
        if (filteredUsers.size() != 1){
            return null;
        }
        return filteredUsers.get(0).toString();
    }

}
