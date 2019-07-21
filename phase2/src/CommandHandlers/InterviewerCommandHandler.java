package CommandHandlers;

import Databases.Application;
import Databases.UserCredentials;
import GuiForms.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InterviewerCommandHandler implements CommandHandler{
    private Long interviewerID;
    private String username;
    private List<Application> recommendedApps;

    public InterviewerCommandHandler(UserCredentials user){
        this.interviewerID = user.getUserID();
        this.username = user.getUserName();
        this.recommendedApps = new ArrayList<>();
    }

    private List<Application> getApplications(){
        return UserInterface.getAppsDb().getApplicationByInterviewerID(this.interviewerID);
    }

    private List<String> getApplicationIDs(){
        List<String> idList = new ArrayList<>();
        if (!this.getApplications().isEmpty()){
            for(Application app:this.getApplications()){
                idList.add(String.valueOf(app.getApplicationID()));
            }
        }
        return idList;
    }

    private String applicationString(){
        StringBuilder applicationText = new StringBuilder();
        if (this.getApplications().isEmpty()) {return "";}
        for (Application app:this.getApplications()){
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
    private void recommendApplication(Long ApplicationID){
        for (Application app:this.getApplications()){
            if(app.getApplicationID() == ApplicationID && !this.recommendedApps.contains(app)){
                app.recommend();
                this.recommendedApps.add(app);
                app.setUpInterview(-1);
                return;
            }
        }
    }

    public void handleCommands(){
        List<String> retList;
        retList = GUI.interviewForm(this.applicationString(), this.getApplicationIDs(), this.username);
        for(String recommended:retList){
            this.recommendApplication(Long.parseLong(recommended));
        }
    }
}