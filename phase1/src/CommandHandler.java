import java.util.ArrayList;
import java.util.Scanner;

public abstract class CommandHandler {

    protected ApplicationsDatabase appsDb;
    protected JobsDatabase jobsDb;
    protected UserCredentials currentUser;
    protected Scanner sc = new Scanner(System.in);

    public CommandHandler(ApplicationsDatabase appsDb, JobsDatabase jobsDb, UserCredentials currentUser) {
        this.appsDb = appsDb;
        this.jobsDb = jobsDb;
        this.currentUser = currentUser;
    }

    abstract void printCommandList();
    abstract void handleCommand(String commandId);

    public boolean isValidCommand(String userInput, ArrayList <String> validCommand ){
        return true;
    }
}
