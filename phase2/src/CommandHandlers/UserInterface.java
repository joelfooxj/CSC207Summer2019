package CommandHandlers;

import Databases.*;
import GuiForms.GUI;


import java.io.IOException;
import java.time.LocalDate;


public class UserInterface{

    private static JobApplicationDatabase appsDb = new JobApplicationDatabase();
    private static JobsDatabase jobsDb = new JobsDatabase();
    private static UserCredentialsDatabase usersDb = new UserCredentialsDatabase();
    private static FirmDatabase firmsDb = new FirmDatabase();
    private static CommandHandlerFactory commandHandlerFactory = new CommandHandlerFactory();

    private static UserCredentials currentUser;
    private static LocalDate sessionDate;

    private static String applicantUserType = "Applicant";
    private static String interviewerUserType = "Interviewer";
    private static String hrUserType = "HR";

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

    public static JobsDatabase getJobsDb() {
        return jobsDb;
    }

    public static UserCredentialsDatabase getUsersDb() {
        return usersDb;
    }

    public static UserCredentials getCurrentUser() {
        return currentUser;
    }

    public static String getApplicantUserType() { return applicantUserType; }

    public static LocalDate getSessionDate() { return sessionDate; }
    public static String getInterviewerUserType() {
        return interviewerUserType;
    }

    public static String getHrUserType() {
        return hrUserType;
    }



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
                saveFilesHandler.loadUserSavedFiles();
            } else {
                saveFilesHandler.overrideTestData();
            }

            boolean answer = GUI.yesNoForm("Do you want to exit the program?");
            if (answer) {
                return;
            }
        }
    }
}




