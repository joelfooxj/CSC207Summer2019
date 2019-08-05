package Control;

import Model.JobApplication;
import Model.JobApplicationDatabase;
import Model.UserCredentials;
import View.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InterviewerCommandHandler extends CommandHandler{
    private Long interviewerID;
    private String username;


    public InterviewerCommandHandler(UserCredentials user){
        this.interviewerID = user.getUserID();
        this.username = user.getUserName();

    }

    public void handleCommands(){
        GUI.interviewerForm(this);
    }
    public String getUsername(){
        return this.username;
    }

    /**
     * returns a list of applications assigned to this interviewer
     * @return List of JobApplications
     */
    private List<JobApplication> getAssignedApplications(){
        HashMap requirement = new HashMap();
        requirement.put(JobApplicationDatabase.jobAppFilterKeys.INTERVIEWER_ID, this.interviewerID);
        return sessionData.jobAppsDb.filter(requirement);
        //return sessionData.jobAppsDb.getApplicationByInterviewerID(this.interviewerID);
    }

    /**
     * returns a list of ids of all the assigned job applications for this interviewer
     * @return list of ids
     */
    public List<String> getAssignedApplicationsIds(){
        List<String> idList = new ArrayList<>();
        if (!this.getAssignedApplications().isEmpty()){
            for(JobApplication app:this.getAssignedApplications()){
                idList.add(String.valueOf(app.getApplicationID()));
            }
        }
        return idList;
    }

    /**
     * gets a string representation of the assigned applications to this interviewer
     * @return a string representation of applications
     */
    public String getAssignedApplicationsRepresentation(){
        StringBuilder applicationText = new StringBuilder();
        if (this.getAssignedApplications().isEmpty()) {return "";}
        for (JobApplication app:this.getAssignedApplications()){
            applicationText.append(app);
            applicationText.append("\n");
        }
        return new String(applicationText);
    }

    /**
     * If application is already recommended, will do nothing
     * Interviewers can only recommend each application once per session.
     * @param ApplicationID: ID of the application to be recommended.
     */
    public void recommendApplication(Long ApplicationID){
        for (JobApplication app:this.getAssignedApplications()){
            if(app.getApplicationID() == ApplicationID){
                app.recommend();
                return;
            }
        }
    }

    public void rejectApplication(Long ApplicationID){
        for (JobApplication app:this.getAssignedApplications()){
            if(app.getApplicationID() == ApplicationID){
                app.reject(sessionDate);
                return;
            }
        }
    }
}