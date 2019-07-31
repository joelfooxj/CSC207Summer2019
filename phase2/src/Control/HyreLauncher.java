package Control;

import Model.*;
import View.GUI;


import java.io.IOException;
import java.time.LocalDate;


public class HyreLauncher {

    private static JobApplicationDatabase appsDb = new JobApplicationDatabase();
    private static JobPostingDatabase jobsDb = new JobPostingDatabase();
    private static UserCredentialsDatabase usersDb = new UserCredentialsDatabase();
    private static FirmDatabase firmsDb = new FirmDatabase();
    private static CommandHandlerFactory commandHandlerFactory = new CommandHandlerFactory();
    private static UserCredentials currentUser;
    private static LocalDate sessionDate;
    private static SaveFilesHandler saveFilesHandler = new SaveFilesHandler();

    public static LocalDate getDate(){
        return sessionDate;
    }

    public static FirmDatabase getFirmsDb() {
        return firmsDb;
    }

    public static JobApplicationDatabase getAppsDb() {
        return appsDb;
    }

    public static JobPostingDatabase getJobsDb() {
        return jobsDb;
    }

    public static UserCredentialsDatabase getUsersDb() {
        return usersDb;
    }

    public static UserCredentials getCurrentUser() {
        return currentUser;
    }

    public static LocalDate getSessionDate() { return sessionDate; }






    public static void main(String[] args) throws IOException, ClassNotFoundException {

        boolean response = GUI.yesNoForm("Do you want to import the default test setup? (y/n): ");
        if (response) {
            saveFilesHandler.loadTestFiles();
        } else {
            saveFilesHandler.loadUserSavedFiles();
        }

        while (true) {
            sessionDate = GUI.setDateForm();
            jobsDb.updateDb(sessionDate);
            currentUser = GUI.loginForm();
            CommandHandler commandHandler = commandHandlerFactory.getCommandHandler(currentUser);

            // TODO: Is this necessary? Since the UI is a dropdown menu
//            if (commandHandler == null){
//                GUI.messageBox("Invalid user type");
//                continue;
//            }

            boolean willOverwrite = GUI.yesNoForm("Do you want to overwrite the default test setup? (y/n): ");
            if (!willOverwrite) {
                saveFilesHandler.saveToUserData();
            } else {
                saveFilesHandler.overrideTestData();
            }

            boolean exitProgramRequested = GUI.yesNoForm("Do you want to exit the program?");
            if (exitProgramRequested) {
                return;
            }
        }
    }
}




