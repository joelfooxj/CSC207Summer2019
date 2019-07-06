import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.HashMap;

public class ApplicantCommandHandler extends CommandHandler{
    /**
     * This class handles all high-level commands for Applicant users
     * This class should call methods from the database and attended classes
     */

    private long applicantID;
    private LocalDate creationDate;
    private List<Application> allApps;
    private LocalDate sessionDate;

    public ApplicantCommandHandler(ApplicationDatabase appsDb, JobsDatabase jobsDb, UserCredentials user){
        super(appsDb, jobsDb, user);
        this.applicantID = user.getUserID();
        this.creationDate = user.getCreationDate();
        this.sessionDate = UserInterface.getDate();
        this.allApps = this.getApplications();
        deleteCVAndCoverLetter();
        while(true){
            printCommandList();
            String inputString = (String) InputFormatting.inputWrapper(
                    "string",
                    Arrays.asList("1", "2", "3", "4", "5"));
            if (inputString.equals("Exit")){
                break;
            } else { handleCommands(inputString); }
        }
    }

    /**
     * Idea:
     * 1. method to create and return the command-map
     * 2. method to print the command-map
     *      - pass in parameter s.t. if true, return string rep
     *      - else call internal method
     * 3. method to call runnable
     */

    public void printCommandList(){
        System.out.println("Select one of the following options: ");
        System.out.println("[1] View open job postings.");
        System.out.println("[2] Apply for job.");
        System.out.println("[3] View all of your open applications.");
        System.out.println("[4] View an option for an open application.");
        System.out.println("[5] View the history of this account.");
        System.out.println("[Exit] to exit the program.");
    }

    @Override
    public void handleCommands(String commandID){
        HashMap<String, Runnable> menu = new HashMap<>();
        menu.put("1", () -> {
            System.out.println("Here are the open jobs postings: ");
            this.viewJobPostings();
        });
        menu.put("2", () -> {
            System.out.println("Enter the ID of the Job to apply for: ");
            Long inputLong = (Long) InputFormatting.inputWrapper(
                    "long",null);
            this.applyForJob(inputLong);
        });
        menu.put("3", () -> {
            System.out.println("Here are all open applications: ");
            this.viewOpenApplications();
        });
        menu.put("4", () -> {
            System.out.println("Enter the Application ID to be viewed: ");
            Long inputLong = (Long) InputFormatting.inputWrapper(
                    "long",null);
            Application inputApp = this.getApplicationByApplicationID(inputLong, this.getOpenApplications());
            while(true){
                printApplicationCommands(inputApp);
                String inputString = (String) InputFormatting.inputWrapper(
                        "string",
                        Arrays.asList("1", "2", "3", "4"));
                if (inputString.equals("Exit")){
                    break;
                } else { applicationHandle(inputString, inputApp); }
            }
        });
        menu.put("5", () -> {
            System.out.println("Here is the history of this account: ");
            this.getHistory();
        });
        menu.get(commandID).run();
    }

    private void printApplicationCommands(Application inputApp){
        System.out.println("The current status of this interview is: " + inputApp.status());
        System.out.println("\nSelect one of the following options for this application: ");
        System.out.println("[1] Withdraw from this application.");
        System.out.println("[2] View/Set CV");
        System.out.println("[3] View/Set Cover Letter");
        System.out.println("[Exit] to exit to the main menu.");
    }

    private void applicationHandle(String appCommand, Application inputApp){
        HashMap<String, Runnable> appMenu = new HashMap<>();
        appMenu.put("1", () -> {
            inputApp.setOpen(false);
            System.out.println("You have withdrawn from the application.");
        });
        appMenu.put("2", () -> {
            System.out.println("Type [view] to view the CV for this application, " +
                    "\n or [new] to upload a new CV.");
            String inputString = (String) InputFormatting.inputWrapper(
                    "string",
                    Arrays.asList("view", "new"));
            if (inputString.equals("view")){
                System.out.println(inputApp.getCvPath());
            } else {
                System.out.println("Enter new CV path: ");
                String newCVPath = (String) InputFormatting.inputWrapper("string", null);
                inputApp.setCvPath(newCVPath);
            }
        });
        appMenu.put("3", () -> {
            System.out.println("Type [view] to view the cover letter for this application, " +
                    "\n or [new] to upload a new cover letter.");
            String inputString = (String) InputFormatting.inputWrapper(
                    "string",
                    Arrays.asList("view", "new"));
            if (inputString.equals("view")){
                System.out.println(inputApp.getClPath());
            } else {
                System.out.println("Enter new cover letter path: ");
                String newCLPath = (String) InputFormatting.inputWrapper("string", null);
                inputApp.setClPath(newCLPath);
            }
        });
        appMenu.get(appCommand).run();
    }

    private List<Application> getApplications(){
        return appsDb.getApplicationByApplicantID(this.applicantID);
    }

    private List<Application> getOpenApplications(){
        List<Application> openApps = new ArrayList<>();
        for (Application app:this.allApps){
            if (app.isOpen()){
                openApps.add(app);
            }
        }
        return openApps;
    }

    private List<Application> getClosedApplications(){
        List<Application> closedApps = new ArrayList<>();
        for (Application app:this.allApps){
            if (!app.isOpen()){
                closedApps.add(app);
            }
        }
        return closedApps;
    }

    private Application getApplicationByApplicationID(Long appID, List<Application> inApps){
        for (Application app:inApps){
            if (app.getApplicationID() == appID){
                return app;
            }
        }
        return null;
    }

    public void viewOpenApplications(){
        System.out.println("This is are your current Open applications: ");
        for (Application app:this.getOpenApplications()){
            System.out.println(app);
        }
    }

    /**
     * Should be able to view the jobID
     */
    public void viewJobPostings(){
        jobsDb.printJobPostings();
    }

    public void applyForJob(Long jobID){
        appsDb.addApplication(this.applicantID, jobID);
        //updates allApps
        this.allApps = this.getApplications();
    }

    public void getHistory(){
        System.out.println("The creation date of this account is: " + this.creationDate);
        System.out.println("These are your applications that are now closed: ");
        for (Application app:this.getClosedApplications()){
            System.out.println(app);
        }
        System.out.println("These are your applications that are open: ");
        for (Application app:this.getOpenApplications()){
            System.out.println(app);
        }
        long minDaysBetween = 0;
        for (Application app:this.getClosedApplications()) {
            long daysBetween = ChronoUnit.DAYS.between(app.getClosedDate(), sessionDate);
            if (minDaysBetween == 0) {
                minDaysBetween = daysBetween;
            } else if (minDaysBetween >= daysBetween) {
                minDaysBetween = daysBetween;
            }
        }
        System.out.println("It's been " + minDaysBetween + " since your last closed application.");
    }

    /**
     * This method will set the Cl and CV of all closed applications
     * to null after being closed for 30 days.
     */
    private void deleteCVAndCoverLetter(){
        for (Application app:this.getClosedApplications()){
            if (ChronoUnit.DAYS.between(sessionDate, app.getClosedDate()) > 30){
                app.setClPath(null);
                app.setCvPath(null);
            }
        }
    }

}
