package Control;

import Model.FirmPackage.FirmDatabase;
import Model.JobApplicationPackage.JobApplicationDatabase;
import Model.JobPostingPackage.JobPostingDatabase;
import Model.UserCredentialsPackage.UserCredentialsDatabase;

public class SessionData {
    /**
     * Contains all database objects.
     * Prevents data clump when accessing databases together.
     */
    public JobApplicationDatabase jobAppsDb = new JobApplicationDatabase();
    public JobPostingDatabase jobPostingsDb = new JobPostingDatabase();
    public UserCredentialsDatabase usersDb = new UserCredentialsDatabase();
    public FirmDatabase firmsDb = new FirmDatabase();

}
