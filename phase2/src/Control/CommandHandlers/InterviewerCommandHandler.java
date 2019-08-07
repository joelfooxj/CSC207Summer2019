package Control.CommandHandlers;

import Model.JobApplication;
import Model.JobApplicationDatabase;
import Model.UserCredentials;
import View.*;

import java.util.HashMap;
import java.util.List;

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

    /**
     * returns a list of applications assigned to this interviewer
     *
     * @return List of JobApplications
     */
    private List<JobApplication> getAssignedApplications() {
        HashMap<JobApplicationDatabase.jobAppFilterKeys, Object> requirement = new HashMap<>();
        requirement.put(JobApplicationDatabase.jobAppFilterKeys.INTERVIEWER_ID, this.interviewerID);
        return sessionData.jobAppsDb.filterJobApps(requirement);
    }

    /**
     * If application is already recommended, will do nothing
     * Interviewers can only recommend each application once per session.
     *
     * @param ApplicationID: ID of the application to be recommended.
     */
    public void recommendApplication(Long ApplicationID) {
        for (JobApplication app : this.getAssignedApplications()) {
            if (app.getApplicationID().equals(ApplicationID)) {
                app.recommend();
                return;
            }
        }
    }

    public void rejectApplication(Long ApplicationID) {
        for (JobApplication app : this.getAssignedApplications()) {
            if (app.getApplicationID().equals(ApplicationID)) {
                app.reject(sessionDate);
                return;
            }
        }
    }
}