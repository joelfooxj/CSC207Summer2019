package CommandHandlers;

import Databases.JobApplication;
import Databases.UserCredentials;
import GuiForms.*;
import java.util.ArrayList;
import java.util.List;

public class InterviewerCommandHandler implements CommandHandler{
    private Long interviewerID;
    private String username;


    public InterviewerCommandHandler(UserCredentials user){
        this.interviewerID = user.getUserID();
        this.username = user.getUserName();
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
        return HyreLauncher.getAppsDb().getApplicationByInterviewerID(this.interviewerID);
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
                app.removeInterviewer(this.interviewerID);
                return;
            }
        }
    }
}