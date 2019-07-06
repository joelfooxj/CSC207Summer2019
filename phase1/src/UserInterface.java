import java.time.LocalDate;
import java.util.Scanner;


public class UserInterface{

    private static ApplicationDatabase appsDb = new ApplicationDatabase();

    public static ApplicationDatabase getAppsDb() {
        return appsDb;
    }

    public static JobsDatabase getJobsDb() {
        return jobsDb;
    }

    public static UserCredentialsDatabase getUsersDb() {
        return usersDb;
    }

    private static JobsDatabase jobsDb = new JobsDatabase();
    private static UserCredentialsDatabase usersDb = new UserCredentialsDatabase();
    private static LocalDate sessionDate;
    private static Scanner sc = new Scanner(System.in);
    private static String applicantUserType = "Applicant";
    private static String interviewerUserType = "Interviewer";
    private static String hrUserType = "HR";

    private static LocalDate setDate(){
        // get the current date
        System.out.print("What year is it? ");
        int year = sc.nextInt();
        System.out.print("What month is it? ");
        int month = sc.nextInt();
        System.out.print("What day is it? ");
        int day = sc.nextInt();
        sc.nextLine();
        return LocalDate.of(year, month, day);
    }

    public static LocalDate getDate(){
        return sessionDate;
    }

    private static UserCredentials getUser(){
        System.out.println("If you are already registered, please enter your username. Otherwise, please type <register>: ");
        String userName;
        String userInput = sc.nextLine();

        // register the user if they entered register
        if (userInput.equals("register")){
            System.out.println("Please fill up the form below");
            System.out.println("Username: ");
            userName = sc.nextLine();
            System.out.println(usersDb.userExists(userName));
            if (usersDb.userExists(userName)) {
                System.out.println("That user already exists, try a different username.");
                return getUser();
            }
            System.out.println("Password: ");
            String password = sc.nextLine();

            System.out.println("Account type (" + applicantUserType +
                    ", "+ interviewerUserType +
                    " or " + hrUserType +"): ");
            String accountType = sc.nextLine();
            if (accountType.equals(applicantUserType)){
                usersDb.addUser(userName, password, accountType, sessionDate);
            } else {
                System.out.println("Please enter your firm ID: ");
                String firmId = sc.nextLine();
                usersDb.addUser(userName, password, accountType, Long.parseLong(firmId));
            }

            System.out.println("Sign up successful. Please login with your account");
            return getUser();
        } else {
            userName = userInput;
        }

        System.out.println("Password: ");
        String password = sc.nextLine();

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
    public static void main(String[] args) {

        while (true) {
            // a methods that gets the date from the user
            sessionDate = setDate();

            // a method that handles sign ups & log ins
            UserCredentials currentUser = getUser();
            System.out.println(currentUser);

            // polymorphism => all Command Handler objects are of time CommandHandler
            //CommandHandler commandHandler;

//            // set the value of CommandHandler based on the user type
//            if (currentUser.getUserType().equals(applicantUserType)){
//                commandHandler = new ApplicantCommandHandler(appsDb, jobsDb, currentUser);
//                displayUserNotifications(currentUser);
//
//            } else if (currentUser.getUserType().equals(interviewerUserType)){
//                commandHandler = new InterviewerCommandHandler(appsDb, jobsDb, usersDb, currentUser);
//
//            } else if (currentUser.getUserType().equals(hrUserType)){
//                commandHandler = new HrCommandHandler(appsDb, jobsDb, currentUser, usersDb, sessionDate);
//            } else {
//                System.out.println("Invalid user type");
//                continue;
//            }
//            jobsDb.updateDb(sessionDate);
//            appsDb.updateDb(sessionDate);
//
//            commandHandler.handleCommands();


        }

    }


}
