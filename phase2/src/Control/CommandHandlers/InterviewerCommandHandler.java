package Control.CommandHandlers;

import Model.UserCredentialsPackage.UserCredentials;
import View.GUI;

public class InterviewerCommandHandler extends CommandHandler {
    /**
     * This class handles all high-level commands for Interviewers
     * This class should call methods from the database and attended classes
     */

    private Long interviewerID;
    private String username;

    InterviewerCommandHandler(UserCredentials user) {
        this.interviewerID = user.getUserID();
        this.username = user.getUserName();

    }

    public void handleCommands() {
        GUI.interviewerForm(this);
    }

    public String getUsername() {
        return this.username;
    }

    public Long getInterviewerID() {
        return this.interviewerID;
    }

    public void recommendApplication(Long applicationID) {
        sessionData.jobAppsDb.getItemByID(applicationID).recommend();
    }

    public void rejectApplication(Long applicationID) {
        sessionData.jobAppsDb.getItemByID(applicationID).reject(sessionDate);
    }
}