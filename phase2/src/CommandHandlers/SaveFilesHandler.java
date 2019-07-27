package CommandHandlers;

import java.io.IOException;

public class SaveFilesHandler {
    private String applicationsDbPath = "Applications.bin";
    private String jobsDbPath = "Jobs.bin";
    private String usersDbPath = "Users.bin";
    private String firmDbPath = "Firms.bin";

    //String fileSeparator = System.getProperty("file.separator");
    // String pathToSource = "phase2" + fileSeparator + "src" + fileSeparator;
    private String pathToSource = "";

    private void setUserSavedData(){
        this.applicationsDbPath = pathToSource + "Applications.bin";
        this.jobsDbPath = pathToSource + "Jobs.bin";
        this.usersDbPath = pathToSource + "Users.bin";
        this.firmDbPath = pathToSource + "Firms.bin";
    }

    private void setTestData(){
        this.applicationsDbPath =  pathToSource + "TestApplications.bin";
        this.jobsDbPath = this.pathToSource + "TestJobs.bin";
        this.usersDbPath = this.pathToSource + "TestUsers.bin";
        this.firmDbPath = this.pathToSource + "TestFirms.bin";
    }

    private void saveAll() throws IOException {
        UserInterface.getAppsDb().saveDatabase(applicationsDbPath);
        UserInterface.getJobsDb().saveDatabase(jobsDbPath);
        UserInterface.getUsersDb().saveDatabase(usersDbPath);
        UserInterface.getFirmsDb().saveDatabase(firmDbPath);
    }

    private void readAll() throws IOException, ClassNotFoundException {
        UserInterface.getAppsDb().readDatabase(applicationsDbPath);
        UserInterface.getJobsDb().readDatabase(jobsDbPath);
        UserInterface.getUsersDb().readDatabase(usersDbPath);
        UserInterface.getFirmsDb().readDatabase(firmDbPath);
    }

    public void loadTestFiles() throws ClassNotFoundException, IOException {
        setTestData();
        try {
            readAll();
        } catch (IOException ex) {
            saveAll();
        }
    }

    public void loadUserSavedFiles() throws IOException {
        setUserSavedData();
        try {
            readAll();
        } catch (IOException | ClassNotFoundException ex) {
            saveAll();
        }
    }

    public void overrideTestData() throws IOException {
        setTestData();
        saveAll();
    }

    public void saveToUserData() throws IOException {
        setUserSavedData();
        saveAll();
    }
}
