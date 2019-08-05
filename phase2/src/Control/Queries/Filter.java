package Control.Queries;

import Control.SessionData;
import Model.*;

import java.util.HashMap;



public class Filter {

    private SessionData sessionData;

    public Filter(SessionData sessionData){
        this.sessionData = sessionData;
    }

    public JobApplicationQuery getJobAppsFilter(HashMap<JobApplicationDatabase.jobAppFilterKeys, Object> filter){
        return new JobApplicationQuery(sessionData.jobAppsDb.filter(filter));
    }

    public JobPostQuery getJobPostsFilter(HashMap<JobPostingDatabase.jobPostingFilters, Object> filter){
        return new JobPostQuery(sessionData.jobPostingsDb.filterJobPostings(filter));
    }

    public UserQuery getUsersFilter(HashMap<UserCredentialsDatabase.usersFilterKeys, Object> filter){
        return new UserQuery(sessionData.usersDb.filter(filter));
    }

}
