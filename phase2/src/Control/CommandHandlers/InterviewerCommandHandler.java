package Control.CommandHandlers;

import Model.UserCredentialsPackage.UserCredentials;
import View.*;

public class InterviewerCommandHandler extends CommandHandler {
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