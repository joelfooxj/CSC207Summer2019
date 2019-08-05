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

    public LocalDate getSessionDate(){
        return this.sessionDate;
    }

    private void updateDataStatus() {
        sessionData.jobPostingsDb.updateDb(sessionDate);
    }

    public SessionData getSessionData() {
        return sessionData;
    }

    public UserCredentials addUser(String userName, String password,
                                   UserCredentials.userTypes accountType, String firmName) {
        if (firmName.equals("")) {
            return this.sessionData.usersDb.addUser(userName, password, accountType, sessionDate);
        } else {
            Firm firm = this.sessionData.firmsDb.getFirmByFirmName(firmName);
            if (firm == null) {
                this.sessionData.firmsDb.addFirm(firmName);
                firm = this.sessionData.firmsDb.getFirmByFirmName(firmName);
            }
            return this.sessionData.usersDb.addUser(userName, password, accountType, firm.getFirmId());
        }
    }

    public void launchSession() throws IOException, ClassNotFoundException {
        loadProgramData();
        while (true) {

            sessionDate = GUI.setDateForm();
            currentUser = GUI.loginForm(this);
            updateDataStatus();
            CommandHandler commandHandler = commandHandlerFactory.getCommandHandler(currentUser);

            commandHandler.setSessionData(sessionData);
            commandHandler.setSessionDate(sessionDate);
            commandHandler.handleCommands();


            saveSessionData();

            boolean exitProgramRequested = GUI.yesNoForm("Do you want to exit the program?");
            if (exitProgramRequested) {
                return;

            }
        }
    }
}
