package Control;

import Model.FirmDatabase;
import Model.JobApplicationDatabase;
import Model.JobPostingDatabase;
import Model.UserCredentialsDatabase;

public class SessionData {
    public JobApplicationDatabase appsDb = new JobApplicationDatabase();
    public JobPostingDatabase jobsDb = new JobPostingDatabase();
    public UserCredentialsDatabase usersDb = new UserCredentialsDatabase();
    public FirmDatabase firmsDb = new FirmDatabase();

}
