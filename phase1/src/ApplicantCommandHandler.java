import javax.swing.*;
import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.HashMap;
import java.util.stream.Stream;

public class ApplicantCommandHandler implements CommandHandler{
    /**
     * This class handles all high-level commands for Applicant users
     * This class should call methods from the database and attended classes
     */

    private long applicantID;
    private LocalDate creationDate;

    public ApplicantCommandHandler(UserCredentials user){
        this.applicantID = user.getUserID();
        this.creationDate = user.getCreationDate();
        deleteCVAndCoverLetter();
    }

    /**
     * This method handles the main menu functionality of this commandHandler.
     *
     */
    public void handleCommands(){
        HashMap<String, Runnable> menu = new HashMap<>();
        menu.put("1", () -> {
            System.out.println("Here are the open jobs postings: ");
            if (UserInterface.getJobsDb().isEmpty()) {
                System.out.println("-> There are no job postings, returning to menu.");
            } else {UserInterface.getJobsDb().printOpenJobPostings();}
        });
        menu.put("2", () -> {
            System.out.println("Enter the ID of the Job to apply for: ");
            if (UserInterface.getJobsDb().isEmpty()) {
                System.out.println("-> There are no job postings, returning to menu.");
            } else {
                Long inputJobID = (Long) InputFormatting.inputWrapper(
                        "long",
                        true,
                        UserInterface.getJobsDb().getOpenJobIDs());
                if (inputJobID != null) {
                    if (!this.getAllApplications().isEmpty()){
                        // check if job has already been applied for
                        for (Application app:this.getAllApplications()){
                            if (app.getJobID() == inputJobID){
                                System.out.println("You had already applied for this job");
                                return;
                            }
                        }
                    }
                    Long inputFirmID = UserInterface.getJobsDb().getItemByID(inputJobID).getFirmid();
                    UserInterface.getAppsDb().addApplication(
                            this.applicantID,
                            inputJobID,
                            inputFirmID,
                            UserInterface.getDate());
                }
            }
        });
        menu.put("3", () -> {
            if (!this.getAllApplications().isEmpty()) {
                System.out.println("Here are all open applications: ");
                this.viewOpenApplications();
            }
        });
        menu.put("4", () -> {
            if (!this.getAllApplications().isEmpty()) {
                System.out.println("Enter the open Application ID to be viewed: ");
                Long inputApplicationID = (Long) InputFormatting.inputWrapper(
                        "long",
                        true,
                        this.getAllOpenApplicationIDs());
                if (inputApplicationID != null) {
                    this.singleAppHandle(UserInterface.getAppsDb().getApplicationByApplicationID(inputApplicationID));
                }
            }
        });
        menu.put("5", () -> {
            System.out.println("Here is the history of this account: ");
            this.getHistory();
        });
        menu.put("Exit", () -> System.out.println("Returning to login"));
        String inputCommand = "";
        while(!inputCommand.equals("Exit")){
            System.out.println("Select one of the following options: ");
            System.out.println("[1] View open job postings.");
            System.out.println("[2] Apply for job.");
            System.out.println("[3] View all of your open applications.");
            System.out.println("[4] View options for an open application.");
            System.out.println("[5] View the history of this account.");
            System.out.println("[Exit] to exit the program.");
            inputCommand = (String) InputFormatting.inputWrapper(
                    "string",
                    false,
                    new ArrayList<>(menu.keySet()));
            if(inputCommand != null) {
                menu.get(inputCommand).run();
            }
        }
    }

    /**
     * This method handles functionality for the Appliation sub-menu
     * @param inputApp: the Application that is to be read/mutated
     */
    private void singleAppHandle(Application inputApp){
        HashMap<String, Runnable> appMenu = new HashMap<>();
        appMenu.put("1", () -> {
            inputApp.setOpen(false);
            System.out.println("You have withdrawn from the application.\n");
            // Note: Once withdrawn, you cannot re-apply.
        });
        appMenu.put("2", () -> {
            System.out.println("Type [view] to view the CV for this application, " +
                    "\n or [new] to upload a new CV.");
            String inputString = (String) InputFormatting.inputWrapper(
                    "string",
                    true,
                    Arrays.asList("view", "new"));
            if (inputString != null){
                if (inputString.equals("view")){
                    System.out.println(inputApp.getCvPath());
                } else if (inputString.equals("new")) {
                    System.out.println("Enter new CV path: ");
                    String newCVPath = (String) InputFormatting.inputWrapper(
                            "string",
                            true,
                            null);
                    if(newCVPath != null) {
                        inputApp.setCvPath(newCVPath);
                    }
                }
            }
        });
        appMenu.put("3", () -> {
            System.out.println("Type [view] to view the cover letter for this application, " +
                    "\n or [new] to upload a new cover letter.");
            String inputString = (String) InputFormatting.inputWrapper(
                    "string",
                    true,
                    Arrays.asList("view", "new"));
            if (inputString != null){
                if (inputString.equals("view")){
                    System.out.println(inputApp.getClPath());
                } else {
                    System.out.println("Enter new cover letter path: ");
                    String newCLPath = (String) InputFormatting.inputWrapper(
                            "string",
                            true,
                            null);
                    if(newCLPath != null) {
                        inputApp.setClPath(newCLPath);
                    }
                }
            }
        });
        appMenu.put("Exit", () -> System.out.println("Returning to main menu\n"));
        String inputCommand = "";
        while(!(inputCommand.equals("Exit")||inputCommand.equals("1"))){
            System.out.println("\nThe current status of this interview is: " + inputApp.status());
            System.out.println("Select one of the following options for this application: ");
            System.out.println("[1] Withdraw from this application.");
            System.out.println("[2] View/Set CV");
            System.out.println("[3] View/Set Cover Letter");
            System.out.println("[Exit] to exit to the main menu.");
            inputCommand = (String) InputFormatting.inputWrapper(
                    "string",
                    false,
                    new ArrayList<>(appMenu.keySet()));
            appMenu.get(inputCommand).run();
        }
    }

    /**
     * This method is just a shorthand
     * @return A list of applications associated with this Applicant
     */
    private List<Application> getAllApplications(){
        return UserInterface.getAppsDb().getApplicationsByApplicantID(this.applicantID);
    }

    private List<Long> getAllOpenApplicationIDs(){
        if (!this.getAllApplications().isEmpty()){
            List<Long> retLongList = new ArrayList<>();
            for (Application app:this.getAllApplications()){
                if (app.isOpen()){
                    retLongList.add(app.getApplicationID());
                }
            }
            return retLongList;
        } else {return null;}
    }

    private void viewOpenApplications(){
        if (!this.getAllApplications().isEmpty()){
            for (Application app:this.getAllApplications()){
                if (app.isOpen()) {
                    System.out.println(app);
                    System.out.println("---");
                }
            }
        }
    }

    private void getHistory(){
        System.out.println("The creation date of this account is: " + this.creationDate);
        if (!this.getAllApplications().isEmpty()){
            System.out.println("These are your applications that are now closed: ");
            for (Application app:this.getAllApplications()){
                if(!app.isOpen()) {
                    System.out.println(app);
                }
            }
            System.out.println("These are your applications that are open: ");
            this.viewOpenApplications();
            long minDaysBetween = 0;
            for (Application app:this.getAllApplications()) {
                if(!app.isOpen()){
                    long daysBetween = ChronoUnit.DAYS.between(app.getClosedDate(), UserInterface.getDate());
                    if (minDaysBetween == 0) {
                        minDaysBetween = daysBetween;
                    } else if (minDaysBetween >= daysBetween) {
                        minDaysBetween = daysBetween;
                    }
                }
            }
            System.out.println("It's been " + minDaysBetween + " days since your last closed application.");
        }
    }

    /**
     * This method will set the cover letter and CV of all applications
     * to null if and only if all applications are closed AND at least 30 days
     * has passed since the last closed application.
     */
    private void deleteCVAndCoverLetter(){
        if (this.getAllApplications().isEmpty()){ return; }
        for (Application app:this.getAllApplications()){
            if (app.isOpen()) {return;}
            else if (ChronoUnit.DAYS.between(UserInterface.getDate(), app.getClosedDate()) < 30){return; }
        }
        for (Application app:this.getAllApplications()){
            app.setCvPath(null);
            app.setClPath(null);
        }
    }
}
