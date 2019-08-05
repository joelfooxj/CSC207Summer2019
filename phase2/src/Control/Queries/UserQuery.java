package Control.Queries;

import Model.UserCredentials;

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


}
