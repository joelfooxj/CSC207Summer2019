package Control;

import Model.JobApplication;
import Model.UserCredentials;
import View.GUI;

public class RefererCommandHandler implements CommandHandler {

    private UserCredentials user;

    RefererCommandHandler(UserCredentials refUser){
        this.user = refUser;
        GUI.refererForm(this);
    }

    public void addReferenceLetter(JobApplication app, String referenceLetter) {
        app.addReferenceLetter(referenceLetter);
    }

}
