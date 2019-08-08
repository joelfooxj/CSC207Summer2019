package Control.CommandHandlers;

import Model.JobApplicationPackage.JobApplication;
import View.GUI;

public class RefererCommandHandler extends CommandHandler {

    /**
     * This class handles all high-level commands for Referee users
     * This class should call methods from the database and attended classes
     */

    @Override
    public void handleCommands() {
        GUI.refererForm(this);
    }

    public void addReferenceLetter(Long appID, String referenceLetter) {
        JobApplication app = super.sessionData.jobAppsDb.getItemByID(appID);
        app.addReferenceLetter(referenceLetter);
    }

}
