import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;


public class UserInterface {

    private static ApplicationDatabase appsDb = new ApplicationDatabase();
    private static JobsDatabase jobsDb = new JobsDatabase();
    private static UserCredentialsDatabase usersDb = new UserCredentialsDatabase();
    private static FirmDatabase firmsDb = new FirmDatabase();



    private static UserCredentials currentUser;
    private static LocalDate sessionDate;
    private static String applicantUserType = "Applicant";
    private static String interviewerUserType = "Interviewer";
    private static String hrUserType = "HR";

    protected static String applicationsDbPath = "Applications.bin";
    protected static String jobsDbPath = "Jobs.bin";
    protected static String usersDbPath = "Users.bin";
    protected static String firmDbPath = "Firms.bin";

    private static LocalDate setDate(){
        // get the current date
        System.out.print("What year is it? ");
        int year = (int) InputFormatting.inputWrapper("int", null); // todo
        System.out.print("What month is it? ");
        int month = (int) InputFormatting.inputWrapper("int", null); // todo
        System.out.print("What day is it? ");
        int day = (int) InputFormatting.inputWrapper("int", null); // todo
        return LocalDate.of(year, month, day);
    }

    public static LocalDate getDate(){
        return sessionDate;
    }

    private static UserCredentials getUser(){
        System.out.println("If you are already registered, please enter your username. Otherwise, please type <register>: ");
        String userName;
        String userInput = (String) InputFormatting.inputWrapper("string", null);

        // register the user if they entered register
        if (userInput.equals("register")){
            System.out.println("Please fill up the form below");
            System.out.println("Username: ");
            userName = (String) InputFormatting.inputWrapper("string", null);
            if (usersDb.userExists(userName)) {
                System.out.println("That user already exists, try a different username.");
                return getUser();
            }
            System.out.println("Password: ");
            String password = (String) InputFormatting.inputWrapper("string", null);

            System.out.println("Account type (" + applicantUserType +
                    ", "+ interviewerUserType +
                    " or " + hrUserType +"): ");
            String accountType = (String) InputFormatting.inputWrapper("string", null);
            if (accountType.equals(applicantUserType)){
                usersDb.addUser(userName, password, accountType, sessionDate);
            } else {
                System.out.println("Please enter your firm name: ");

                String firmName = (String) InputFormatting.inputWrapper("string", null);
                if (firmsDb.getFirmByFirmName(firmName) == null){
                    firmsDb.addFirm(firmName);
                }
                long firmId = firmsDb.getFirmByFirmName(firmName).getFirmId();
                usersDb.addUser(userName, password, accountType, firmId);
            }

            System.out.println("Sign up successful. Please login with your account");
            return getUser();
        } else {
            userName = userInput;
        }

        System.out.println("Password: ");
        String password = (String) InputFormatting.inputWrapper("string", null);

        UserCredentials targetUser = usersDb.getUserByCredentials(userName, password);
        if (targetUser == null){
            System.out.println("Incorrect user name & password combination");
            // incorrect password. redirect them to the signing page again
            return getUser();
        } else {
            return  targetUser;
        }
    }


    private static void displayUserNotifications(UserCredentials user){
        // checks for user notifications...
        return;
    }

    public static ApplicationDatabase getAppsDb() {
        return appsDb;
    }

    public static JobsDatabase getJobsDb() {
        return jobsDb;
    }

    public static UserCredentialsDatabase getUsersDb() {
        return usersDb;
    }

    public static UserCredentials getCurrentUser() {
        return currentUser;
    }

    protected static void saveAll() throws IOException {
        appsDb.saveDatabase(applicationsDbPath);
        jobsDb.saveDatabase(jobsDbPath);
        usersDb.saveDatabase(usersDbPath);
        firmsDb.saveDatabase(firmDbPath);
    }

    protected static void readAll() throws IOException, ClassNotFoundException {
        appsDb.readDatabase(applicationsDbPath);
        jobsDb.readDatabase(jobsDbPath);
        usersDb.readDatabase(usersDbPath);
        firmsDb.readDatabase(firmDbPath);
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {

        while (true) {
            // a methods that gets the date from the user
            sessionDate = setDate();
            readAll();

            jobsDb.updateDb(sessionDate);
            appsDb.updateDb(sessionDate);
            // a method that handles sign ups & log ins
            currentUser = getUser();

            System.out.println(currentUser);
            // polymorphism => all Command Handler objects are of time CommandHandler
            CommandHandler commandHandler;


            // set the value of CommandHandler based on the user type
            if (currentUser.getUserType().equals(applicantUserType)){
                commandHandler = new ApplicantCommandHandler(currentUser);
                displayUserNotifications(currentUser);

            } else if (currentUser.getUserType().equals(interviewerUserType)){
                commandHandler = new InterviewerCommandHandler(currentUser);

            } else if (currentUser.getUserType().equals(hrUserType)){
                commandHandler = new HrCommandHandler();
            } else {
                System.out.println("Invalid user type");
                continue;
            }

            commandHandler.handleCommands();

            saveAll();




        }

    }


}
