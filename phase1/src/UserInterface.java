import java.time.LocalDate;
import java.util.Scanner;


public class UserInterface{

    private static Scanner sc = new Scanner(System.in);
    private static ApplicationsDatabase appsDb = new ApplicationsDatabase();
    private static JobsDatabase jobsDb = new JobsDatabase();
    private static UserCredentialsDatabase usersDb = new UserCredentialsDatabase();
    private static String applicantUserType = "Applicant";
    private static String interviewerUserType = "Interviewer";
    private static String hrUserType = "HR";

    private static LocalDate getDate(){
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

    private static UserCredentials getUser(){
        System.out.print("If you are already registered, please enter your username. Otherwise, please type <register>: ");
        String userName;
        String userInput = sc.nextLine();

        // register the user if they entered register
        if (userInput.equals("Register")){
            System.out.print("Please fill up the form below");
            System.out.print("Username: ");
            userName = sc.nextLine();
            System.out.print("Password: ");
            String password = sc.nextLine();

            System.out.print("Account type (" + applicantUserType +
                    ", "+ interviewerUserType +
                    " or " + hrUserType +"): ");
            String accountType = sc.nextLine();

            usersDb.addUser(userName, password, accountType);
            System.out.println("Sign up successful. Please login with your account");
            return getUser();
        } else {
            userName = userInput;
        }

        System.out.print("Password: ");
        String password = sc.nextLine();


        UserCredentials targetUser = getUserByCredentials(userName, password);
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
            LocalDate sessionDate = getDate();
            UserCredentials user = getUser();
            CommandHandler commandHandler;
            if (user.getUserType().equals(applicantUserType)){
                commandHandler = new ApplicantCommandHandler(appsDb, jobsDb, usersDb);
                displayUserNotifications(user);
            } else if (user.getUserType().equals(interviewerUserType)){
                commandHandler = new EmployerCommandHandler(appsDb, jobsDb, usersDb);
            } else if (user.getUserType().equals(hrUserType)){
                commandHandler = new hrCommandHandler(appsDb, jobsDb, usersDb);
            }

            String command = "";
            while (!command.equals("Exit")){
                commandHandler.printCommandList();
                command = sc.nextLine();
                commandHandler.handleCommand(userInput);
            }

        }

    }


}