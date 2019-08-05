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
    public Filter filter = new Filter(sessionData);


    public static void setSessionData(SessionData sessionData) {
        CommandHandler.sessionData = sessionData;
    }

    public static void setSessionDate(LocalDate sessionDate){
        CommandHandler.sessionDate = sessionDate;
    }

    public List<UserCredentials> filterUserCredentials(HashMap<Model.UserCredentialsDatabase.filterKeys, String> filter){
        return sessionData.usersDb.filter(filter);
    }

    public List<JobApplication> filterJobApplication(HashMap<Model.JobApplicationDatabase.filterKeys, Long> filter) {
        return sessionData.jobAppsDb.filter(filter);
    }

    public List<JobPosting> filterJobPosting(HashMap<Model.JobPostingDatabase.jobFilters, Long> filter) {
        return sessionData.jobPostingsDb.filterJobPostings(filter);
    }



    public abstract void handleCommands();
}
