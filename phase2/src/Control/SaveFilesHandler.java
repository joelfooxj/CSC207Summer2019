package Control;

import java.io.IOException;

class SaveFilesHandler {
    private String applicationsDbPath = "Applications.bin";
    private String jobsDbPath = "Jobs.bin";
    private String usersDbPath = "Users.bin";
    private String firmDbPath = "Firms.bin";
    private SessionData sessionData;
    private String pathToSource = "";


    SaveFilesHandler(SessionData sessionData) {
        this.sessionData = sessionData;
    }

    /**
     * changes the links to the saved files
     * any read/write operation will occur in the user's save files
     */
    private void setUserSavedData() {
        this.applicationsDbPath = pathToSource + "Applications.bin";
        this.jobsDbPath = pathToSource + "Jobs.bin";
        this.usersDbPath = pathToSource + "Users.bin";
        this.firmDbPath = pathToSource + "Firms.bin";
    }

    /**
     * changes the links to the saved files
     * any read/write operation will occur in the test files
     */
    private void setTestData() {
        this.applicationsDbPath = pathToSource + "TestApplications.bin";
        this.jobsDbPath = this.pathToSource + "TestJobs.bin";
        this.usersDbPath = this.pathToSource + "TestUsers.bin";
        this.firmDbPath = this.pathToSource + "TestFirms.bin";
    }

    /**
     * saves all the data from the current session
     *
     * @throws IOException if file doesn't exist
     */
    private void saveAll() throws IOException {
        sessionData.jobAppsDb.saveDatabase(applicationsDbPath);
        sessionData.jobPostingsDb.saveDatabase(jobsDbPath);
        sessionData.usersDb.saveDatabase(usersDbPath);
        sessionData.firmsDb.saveDatabase(firmDbPath);
    }

    /**
     * loads saved data
     *
     * @throws IOException            if file doesn't exist
     * @throws ClassNotFoundException if class is not found
     */
    private void readAll() throws IOException, ClassNotFoundException {
        sessionData.jobAppsDb.readDatabase(applicationsDbPath);
        sessionData.jobPostingsDb.readDatabase(jobsDbPath);
        sessionData.usersDb.readDatabase(usersDbPath);
        sessionData.firmsDb.readDatabase(firmDbPath);
    }


    /**
     * Loads saved test data
     *
     * @throws ClassNotFoundException if class is not found
     * @throws IOException            if file doesn't exist
     */
    void loadTestFiles() throws ClassNotFoundException, IOException {
        setTestData();
        try {
            readAll();
        } catch (IOException ex) {
            saveAll();
        }
    }

    /**
     * Loads the test files data
     *
     * @throws IOException if file is not found
     */
    void loadUserSavedFiles() throws IOException {
        setUserSavedData();
        try {
            readAll();
        } catch (IOException | ClassNotFoundException ex) {
            saveAll();
        }
    }


    /**
     * Overrides the test files data with the current session's data
     *
     * @throws IOException if file is not found
     */
    void overrideTestData() throws IOException {
        setTestData();
        saveAll();
    }

    /**
     * Overrides the user's saved data with the current session's data
     *
     * @throws IOException if file is not found
     */
    void saveToUserData() throws IOException {
        setUserSavedData();
        saveAll();
    }
}
