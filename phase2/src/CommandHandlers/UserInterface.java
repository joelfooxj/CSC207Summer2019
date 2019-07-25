package CommandHandlers;

import Databases.*;
import GuiForms.*;


import java.io.IOException;
import java.time.LocalDate;


public class UserInterface{

    private static JobApplicationDatabase appsDb = new JobApplicationDatabase();
    private static JobsDatabase jobsDb = new JobsDatabase();
    private static UserCredentialsDatabase usersDb = new UserCredentialsDatabase();
    private static FirmDatabase firmsDb = new FirmDatabase();

    private static UserCredentials currentUser;
    private static LocalDate sessionDate;
    private static String applicantUserType = "Applicant";
    private static String interviewerUserType = "Interviewer";
    private static String hrUserType = "HR";

    protected static String applicationsDbPath = "Applications.bin";
    protected static String jobsDbPath = "Jobs.bin";
    protected static String usersDbPath = "Users.bin";
    protected static String firmDbPath = "Firms.bin";

    public static LocalDate getDate(){
        return sessionDate;
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

    protected static void saveAll() throws IOException {
        appsDb.saveDatabase(applicationsDbPath);
        jobsDb.saveDatabase(jobsDbPath);
        usersDb.saveDatabase(usersDbPath);
        firmsDb.saveDatabase(firmDbPath);
    }

    protected static void readAll() throws IOException, ClassNotFoundException {
        appsDb.readDatabase(applicationsDbPath);
        jobsDb.readDatabase(jobsDbPath);
        usersDb.readDatabase(usersDbPath);
        firmsDb.readDatabase(firmDbPath);
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        String fileSeparator = System.getProperty("file.separator");
        // String pathToSource = "phase2" + fileSeparator + "src" + fileSeparator;
        String pathToSource = "";
        boolean response = GUI.yesNoForm("Do you want to import the default test setup? (y/n): ");
        if (response) {
            applicationsDbPath =  pathToSource + "TestApplications.bin";
            jobsDbPath = pathToSource + "TestJobs.bin";
            usersDbPath = pathToSource + "TestUsers.bin";
            firmDbPath = pathToSource + "TestFirms.bin";
        } else {
            applicationsDbPath = pathToSource + "Applications.bin";
            jobsDbPath = pathToSource + "Jobs.bin";
            usersDbPath = pathToSource + "Users.bin";
            firmDbPath = pathToSource + "Firms.bin";
        }
        try {
            readAll();
        } catch (IOException ex) {
            saveAll();
        }
        while (true) {
            sessionDate = GUI.setDateForm();
            jobsDb.updateDb(sessionDate);
            currentUser = GUI.loginForm();
            CommandHandler commandHandler = null;

            // set the value of CommandHandlers.CommandHandler based on the user type
            if (currentUser.getUserType().equals(applicantUserType)){
                commandHandler = new ApplicantCommandHandler(currentUser);

            } else if (currentUser.getUserType().equals(interviewerUserType)){
                commandHandler = new InterviewerCommandHandler(currentUser);

            } else if (currentUser.getUserType().equals(hrUserType)){
                commandHandler = new HrCommandHandler();
            }

            if (commandHandler == null){
                GUI.messageBox("Invalid user type");
                continue;
            }

            // commandHandler.handleCommands();

            boolean willOverwrite = GUI.yesNoForm("Do you want to overwrite the default test setup? (y/n): ");
            if (!willOverwrite) {
                applicationsDbPath = pathToSource + "Applications.bin";
                jobsDbPath = pathToSource + "Jobs.bin";
                usersDbPath = pathToSource + "Users.bin";
                firmDbPath = pathToSource + "Firms.bin";
            }
            saveAll();
            boolean answer = GUI.yesNoForm("Do you want to exit the program?");
            if (answer) {
                return;
            }
        }
    }
}
