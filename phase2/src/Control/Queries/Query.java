package Control.Queries;

import Control.SessionData;
import Model.JobApplicationPackage.JobApplicationDatabase;
import Model.JobPostingPackage.JobPostingDatabase;
import Model.UserCredentialsPackage.UserCredentialsDatabase;

import java.util.HashMap;


public class Query {
    /**
     * Class constructs different types of Queries by providing query HashMaps from databases
     */

    private SessionData sessionData;

    public Query(SessionData sessionData) {
        this.sessionData = sessionData;
    }

    public JobApplicationQuery getJobAppsFilter(HashMap<JobApplicationDatabase.jobAppFilterKeys, Object> filter) {
        return new JobApplicationQuery(sessionData.jobAppsDb.filterJobApps(filter));
    }

    public JobPostQuery getJobPostsFilter(HashMap<JobPostingDatabase.jobPostingFilters, Object> filter) {
        return new JobPostQuery(sessionData.jobPostingsDb.filterJobPostings(filter));
    }

    public UserQuery getUsersFilter(HashMap<UserCredentialsDatabase.usersFilterKeys, Object> filter) {
        return new UserQuery(sessionData.usersDb.filterUsers(filter));
    }

}
