package Control.Queries;

import Control.SessionData;
import Model.*;

import java.util.HashMap;



public class Filter {

    private SessionData sessionData;

    public Filter(SessionData sessionData){
        this.sessionData = sessionData;
    }

    public JobApplicationQuery getJobApplicationsFilter(HashMap<JobApplicationDatabase.jobAppFilterKeys, Long> filter){
        return new JobApplicationQuery(sessionData.jobAppsDb.filter(filter));
    }

    public JobPostQuery getJobPostsFilter(HashMap<JobPostingDatabase.jobFilters, Long> filter){
        return new JobPostQuery(sessionData.jobPostingsDb.filterJobPostings(filter));
    }

    public UserQuery getUsersFilter(HashMap<UserCredentialsDatabase.filterKeys, String> filter){
        return new UserQuery(sessionData.usersDb.filter(filter));
    }

}
