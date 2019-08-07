package Control.CommandHandlers;

import Model.JobApplicationPackage.JobApplication;
import View.GUI;

public class RefererCommandHandler extends CommandHandler {

    @Override
    public void handleCommands() {
        GUI.refererForm(this);
    }

    public void addReferenceLetter(Long appID, String referenceLetter) {
        JobApplication app = super.sessionData.jobAppsDb.getItemByID(appID);
        app.addReferenceLetter(referenceLetter);
    }

}
