import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;


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
        // input will loop until a valid date is entered.
        while (true) {
            try {
                System.out.print("What year is it? ");
                int year = (int) InputFormatting.inputWrapper("int", false, null);
                System.out.print("What month is it? ");
                int month = (int) InputFormatting.inputWrapper("int", false, null);
                System.out.print("What day is it? ");
                int day = (int) InputFormatting.inputWrapper("int", false,  null);
                return LocalDate.of(year, month, day);
            } catch (DateTimeException dte) {
                System.out.println("You have entered an invalid date, please enter again.");
            }
        }
    }

    public static LocalDate getDate(){
        return sessionDate;
    }

    private static UserCredentials getUser(){
        System.out.println("If you are already registered, please enter your username. Otherwise, please type <register>: ");
        String userName;
        String userInput = (String) InputFormatting.inputWrapper("string", false, null);

        // register the user if they entered register
        if (userInput.equals("register")){
            System.out.println("Please fill up the form below");
            System.out.println("Username: ");
            userName = (String) InputFormatting.inputWrapper("string", false, null);
            if (usersDb.userExists(userName)) {
                System.out.println("That user already exists, try a different username.");
                return getUser();
            }
            System.out.println("Password: ");
            String password = (String) InputFormatting.inputWrapper("string", false, null);

            System.out.println("Account type (" + applicantUserType +
                    ", "+ interviewerUserType +
                    " or " + hrUserType +"): ");
            String accountType = (String) InputFormatting.inputWrapper("string",false,null);
            if (accountType.equals(applicantUserType)){
                usersDb.addUser(userName, password, accountType, sessionDate);
            } else {
                System.out.println("Please enter your firm name: ");

                String firmName = (String) InputFormatting.inputWrapper("string", false, null);
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
        String password = (String) InputFormatting.inputWrapper("string",false, null);

        UserCredentials targetUser = usersDb.getUserByCredentials(userName, password);
        if (targetUser == null){
            System.out.println("Incorrect user name & password combination");
            // incorrect password. redirect them to the signing page again
            return getUser();
        } else {
            return  targetUser;
        }
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

    public static String getApplicantUserType() { return applicantUserType; }

    public static LocalDate getSessionDate() { return sessionDate; }

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
        String fileSeparator = System.getProperty("file.separator");
        String pathToSource = "phase1" + fileSeparator + "src" + fileSeparator;
        System.out.print("Do you want to import the default test setup? (y/n): ");
        String response = (String) InputFormatting.inputWrapper("string", false, Arrays.asList("y", "n"));
        if (response.equals("y")) {
            applicationsDbPath =  pathToSource + "TestApplications.bin";
            jobsDbPath = pathToSource + "TestJobs.bin";
            usersDbPath = pathToSource + "TestUsers.bin";
            firmDbPath = pathToSource + "TestFirms.bin";
        } else {
            applicationsDbPath = pathToSource + "Applications.bin";
            jobsDbPath = pathToSource + "Jobs.bin";
            usersDbPath = pathToSource + "Users.bin";
            firmDbPath = pathToSource + "Firms.bin";
        }
        try {
            readAll();
        } catch (IOException ex) {
            saveAll();
        }
        if (response.equals("y")) {
            System.out.print("Do you want to overwrite the default test setup? (y/n): ");
            response = (String) InputFormatting.inputWrapper("string",false, Arrays.asList("y", "n"));
            if (response.equals("n")) {
                applicationsDbPath = pathToSource + "Applications.bin";
                jobsDbPath = pathToSource + "Jobs.bin";
                usersDbPath = pathToSource + "Users.bin";
                firmDbPath = pathToSource + "Firms.bin";
            }
        }
        while (true) {
            // a methods that gets the date from the user
            sessionDate = setDate();

            jobsDb.updateDb(sessionDate);
            //appsDb.updateDb(sessionDate);
            // a method that handles sign ups & log ins
            currentUser = getUser();

            System.out.println(currentUser);
            // polymorphism => all Command Handler objects are of time CommandHandler
            CommandHandler commandHandler;


            // set the value of CommandHandler based on the user type
            if (currentUser.getUserType().equals(applicantUserType)){
                commandHandler = new ApplicantCommandHandler(currentUser);

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
            System.out.println("Do you want to exit the program? (y/n): ");
            List<String> exitOptions = Arrays.asList("y", "n");
            String answer = (String) InputFormatting.inputWrapper("string", false,  exitOptions);
            if (answer.equals("y")) {
                return;
            }
        }
    }
}
