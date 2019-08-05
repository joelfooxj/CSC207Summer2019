package Control.Queries;

import Model.UserCredentials;

import java.util.List;

public class UserQuery {
    private List<UserCredentials> filteredUsers;
    UserQuery(List<UserCredentials> filteredUsers){
        this.filteredUsers = filteredUsers;
    }

    public List<String> getRepresentations(){
        if (filteredUsers.size() != 1){
            return null;
        }
        return null;//filteredUsers.get(0).toString();
    }


    public String getRepresentation(){
        if (filteredUsers.size() != 1){
            return null;
        }
        return filteredUsers.get(0).toString();
    }

}
