package CommandHandlers;

import Databases.Application;
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

    private List<Application> getApplications(){
        return UserInterface.getAppsDb().getApplicationByInterviewerID(this.interviewerID);
    }

    public List<String> getApplicationIDs(){
        List<String> idList = new ArrayList<>();
        if (!this.getApplications().isEmpty()){
            for(Application app:this.getApplications()){
                idList.add(String.valueOf(app.getApplicationID()));
            }
        }
        return idList;
    }

    public String applicationPrintout(){
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
    public void recommendApplication(Long ApplicationID){
        for (Application app:this.getApplications()){
            if(app.getApplicationID() == ApplicationID){
                app.recommend();
                app.removeInterviewer(this.interviewerID);
                return;
            }
        }
    }
}