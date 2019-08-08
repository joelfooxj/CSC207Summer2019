package Control;

import Control.CommandHandlers.CommandHandler;
import Control.CommandHandlers.CommandHandlerFactory;
import Model.FirmPackage.Firm;
import Model.UserCredentialsPackage.UserCredentials;
import View.GUI;

import java.io.IOException;
import java.time.LocalDate;

public class HyreSession {

    private SessionData sessionData = new SessionData();
    private CommandHandlerFactory commandHandlerFactory = new CommandHandlerFactory();
    private LocalDate sessionDate;
    private SaveFilesHandler saveFilesHandler = new SaveFilesHandler(sessionData);

    /**
     * Loads program data with the option of loading test files or user saved files
     *
     * @throws IOException
     * @throws ClassNotFoundException
     */

    private void loadProgramData() throws IOException, ClassNotFoundException {
        boolean response = GUI.yesNoForm("Do you want to import the default test setup? (y/n): ");
        if (response) {
            this.saveFilesHandler.loadTestFiles();
        } else {
            this.saveFilesHandler.loadUserSavedFiles();
        }
    }

    /**
     * Saves session data, with the option of overriding the test data
     *
     * @throws IOException
     */
    private void saveSessionData() throws IOException {
        boolean willOverwrite = GUI.yesNoForm("Do you want to overwrite the default test setup? (y/n): ");
        if (!willOverwrite) {
            this.saveFilesHandler.saveToUserData();
        } else {
            this.saveFilesHandler.overrideTestData();
        }
    }

    /**
     * Updates the session date of the Jobs Database
     */
    private void updateDataStatus() {
        sessionData.jobPostingsDb.updateDb(sessionDate);
    }

    public SessionData getSessionData() {
        return sessionData;
    }

    /**
     * Adds user to user credentials database and returns user credentials object
     *
     * @param userName
     * @param password
     * @param accountType
     * @param firmName
     * @return UserCredentials object
     */
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

    /**
     * Main program loop.
     * Launches session by getting date, current user and initializing command handler.
     * Allows exiting of program.
     *
     * @throws IOException
     * @throws ClassNotFoundException
     */
    void launchSession() throws IOException, ClassNotFoundException {
        loadProgramData();
        while (true) {

            sessionDate = GUI.setDateForm();
            UserCredentials currentUser = GUI.loginForm(this);
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
