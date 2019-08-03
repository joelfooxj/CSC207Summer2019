package Control;

import Model.*;
import View.GUI;

import java.io.IOException;
import java.time.LocalDate;

public class HyreSession {

    private SessionData sessionData = new SessionData();
    private CommandHandlerFactory commandHandlerFactory = new CommandHandlerFactory();
    private UserCredentials currentUser;
    private LocalDate sessionDate;
    private SaveFilesHandler saveFilesHandler = new SaveFilesHandler(sessionData);

    private void loadProgramData() throws IOException, ClassNotFoundException {
        boolean response = GUI.yesNoForm("Do you want to import the default test setup? (y/n): ");
        if (response) {
            this.saveFilesHandler.loadTestFiles();
        } else {
            this.saveFilesHandler.loadUserSavedFiles();
        }
    }

    private void saveSessionData() throws IOException {
        boolean willOverwrite = GUI.yesNoForm("Do you want to overwrite the default test setup? (y/n): ");
        if (!willOverwrite) {
            this.saveFilesHandler.saveToUserData();
        } else {
            this.saveFilesHandler.overrideTestData();
        }
    }

    private void updateDataStatus() {
        sessionData.jobsDb.updateDb(sessionDate);
    }

    public void launchSession() throws IOException, ClassNotFoundException {
        loadProgramData();
        while (true) {
            sessionDate = GUI.setDateForm();
            currentUser = GUI.loginForm();
            updateDataStatus();
            CommandHandler commandHandler = commandHandlerFactory.getCommandHandler(currentUser);
            // handle commands...
            saveSessionData();

            boolean exitProgramRequested = GUI.yesNoForm("Do you want to exit the program?");
            if (exitProgramRequested) {
                return;

            }
        }
    }
}
