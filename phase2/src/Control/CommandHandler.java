package Control;

import Model.JobApplication;
import Model.JobPosting;
import Model.UserCredentials;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

public abstract class CommandHandler {

    public static SessionData sessionData;
    public static LocalDate sessionDate;

    public static void setSessionData(SessionData sessionData) {
        CommandHandler.sessionData = sessionData;
    }

    public static void setSessionDate(LocalDate sessionDate){
        CommandHandler.sessionDate = sessionDate;
    }

    public List<UserCredentials> filterUserCredentials(HashMap<String, String> user){
        return null;
    }

    public List<JobApplication> filterJobApplication(HashMap<String, Long> user) {
        return null;
    }

    public List<JobPosting> filterJobPosting(HashMap<String, Long> user) {
        return null;
    }


    public abstract void handleCommands();
}
