package Control;

import Model.FirmDatabase;
import Model.JobApplicationDatabase;
import Model.JobPostingDatabase;
import Model.UserCredentialsDatabase;

public class SessionData {
    public JobApplicationDatabase jobAppsDb = new JobApplicationDatabase();
    public JobPostingDatabase jobPostingsDb = new JobPostingDatabase();
    public UserCredentialsDatabase usersDb = new UserCredentialsDatabase();
    FirmDatabase firmsDb = new FirmDatabase();

}
