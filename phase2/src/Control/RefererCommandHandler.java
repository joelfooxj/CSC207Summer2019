package Control;

import Model.JobApplication;
import Model.JobApplicationDatabase;
import Model.UserCredentials;
import Model.UserCredentialsDatabase;
import View.GUI;

import java.util.ArrayList;
import java.util.List;

public class RefererCommandHandler implements CommandHandler {

    private UserCredentials user;

    RefererCommandHandler(UserCredentials refUser){
        this.user = refUser;
        GUI.refererForm(this);
    }

    public void addReferenceLetter(JobApplication app, String referenceLetter) {
        app.addReferenceLetter(referenceLetter);
    }

    public ArrayList<UserCredentials> findUser(UserCredentialsDatabase userDB, String search) {
        return userDB.getUsersByUsername(search);
    }

    public List<JobApplication> findApplication(JobApplicationDatabase appsDB, UserCredentials user) {
        return appsDB.getApplicationsByApplicantID(user.getUserID());
    }
}
