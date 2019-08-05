package Control.Queries;

import Model.UserCredentials;

import java.util.ArrayList;
import java.util.List;

public class UserQuery {
    private List<UserCredentials> filteredUsers;
    UserQuery(List<UserCredentials> filteredUsers){
        this.filteredUsers = filteredUsers;
    }

    public List<String> getRepresentation(){
        if (filteredUsers.size() != 1){
            return null;
        }
        return null;//filteredUsers.get(0).toString();
    }

    public List<Long> getIDs() {
        ArrayList<Long> ids = new ArrayList<>();
        for (UserCredentials user : this.filteredUsers) {
            ids.add(user.getUserID());
        }
        return ids;
    }


}
