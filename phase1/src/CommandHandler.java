import java.util.Scanner;

public abstract class CommandHandler {

    protected ApplicationsDatabase appsDb;
    protected JobsDatabase jobsDb;
    protected UserCredentialsDatabase usersDb;
    protected Scanner sc = new Scanner(System.in);
    public CommandHandler(ApplicationsDatabase appsDb, JobsDatabase jobsDb, UserCredentialsDatabase usersDb) {
        this.appsDb = appsDb;
        this.jobsDb = jobsDb;
        this.usersDb = usersDb;
    }

    abstract void printCommandList();
    abstract void handleCommand(String commandId);
}
