package Control;

import Model.JobApplication;
import Model.UserCredentials;
import View.GUI;

public class RefererCommandHandler extends CommandHandler {

    private UserCredentials user;

    RefererCommandHandler(UserCredentials refUser){
        this.user = refUser;

    }

    @Override
    public void handleCommands() {
        GUI.refererForm(this);
    }

    public void addReferenceLetter(Long appID, String referenceLetter) {
        JobApplication app = super.sessionData.jobAppsDb.getItemByID(appID);
        app.addReferenceLetter(referenceLetter);
    }

}
