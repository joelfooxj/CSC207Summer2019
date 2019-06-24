import java.util.Scanner;

public abstract class CommandHandler {

    private ApplicationsDatabase appsDb;
    private JobsDatabase jobsDb;
    private UserCredentialsDatabase usersDb;

    public CommandHandler(ApplicationsDatabase appsDb, JobsDatabase jobsDb, UserCredentialsDatabase usersDb) {
        this.appsDb = appsDb;
        this.jobsDb = jobsDb;
        this.usersDb = usersDb;
    }

    abstract void printCommandList();
    abstract void handleCommand(String commandId);

}
