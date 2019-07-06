import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public abstract class CommandHandler {

    protected ApplicationDatabase appsDb;
    protected JobsDatabase jobsDb;
    protected UserCredentialsDatabase usersDb;
    protected UserCredentials currentUser;
    protected Scanner sc = new Scanner(System.in);
    protected String applicationsDbPath = "Applications.bin";
    protected String jobsDbPath = "Jobs.bin";
    protected String usersDbPath = "Users.bin";

    public CommandHandler(ApplicationDatabase appsDb, JobsDatabase jobsDb, UserCredentials currentUser) {
        this.appsDb = appsDb;
        this.jobsDb = jobsDb;
        this.currentUser = currentUser;

        try {
            this.readAll();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    abstract void handleCommands();

    protected void saveAll() throws IOException {
        appsDb.saveDatabase(this.applicationsDbPath);
        jobsDb.saveDatabase(this.jobsDbPath);
        usersDb.saveDatabase(this.usersDbPath);
    }

    protected void readAll() throws IOException, ClassNotFoundException {
        appsDb.readDatabase(this.applicationsDbPath);
        jobsDb.readDatabase(this.jobsDbPath);
        usersDb.readDatabase(this.usersDbPath);
    }


}
