import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public abstract class CommandHandler {

    protected ApplicationDatabase appsDb;
    protected JobsDatabase jobsDb;
    protected UserCredentialsDatabase usersDb;
    protected UserCredentials currentUser;
    protected Scanner sc = new Scanner(System.in);

    public CommandHandler(ApplicationDatabase appsDb, JobsDatabase jobsDb, UserCredentials currentUser) {
        this.appsDb = appsDb;
        this.jobsDb = jobsDb;
        this.currentUser = currentUser;
    }

    abstract void printCommandList();
    abstract void handleCommand(String commandId);

    public boolean isValidCommand(String userInput, ArrayList <String> validCommand ){
        return true;
    }

    protected void saveAll() throws IOException {
        appsDb.saveChanges(appsDb, "Applications.bin");
        jobsDb.saveChanges(jobsDb, "Jobs.bin");
        usersDb.saveChanges(appsDb, "Users.bin");
    }
}
