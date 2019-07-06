import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class HrCommandHandler extends CommandHandler {

    protected UserCredentialsDatabase usersDb;
    protected LocalDate sessionDate;
    protected int JOBLIFESPAN = 30;

    public HrCommandHandler(ApplicationDatabase appsDb,
                            JobsDatabase jobsDb,
                            UserCredentials currentUser,
                            UserCredentialsDatabase usersDb,
                            LocalDate sessionDate){

        super(appsDb, jobsDb, currentUser);
        this.usersDb = usersDb;
        this.sessionDate = sessionDate;
    }



    private void handleJobCreation(){
        System.out.println("Enter the job title: ");
        String jobTitle = (String) InputFormatting.inputWrapper("string", null); // todo
        System.out.println("Enter the job description");
        String jobDescription = (String) InputFormatting.inputWrapper("string", null); // todo
        LocalDate expiryDate = UserInterface.getDate().plusDays(JOBLIFESPAN);
        UserInterface.getJobsDb().addJob(jobTitle, jobDescription, currentUser.getFirmId(), UserInterface.getDate(), expiryDate);
        System.out.println("Job post created successfully");
    }

    private void handleApplicantInfo(){

        // get the applicant ID (username)
        System.out.println("Please choose an applicant: ");

        UserInterface.getAppsDb().printApplicationsByFirmID(this.currentUser.getFirmId());

        Long targetApplicant = (long) InputFormatting.inputWrapper("long", null); // todo

        // Prompt the HR user an option
        System.out.println("Please choose an option: ");
        System.out.println("[1] View jobs applied for");
        System.out.println("[2] View Resume");
        System.out.println("[3] View Cover letter");
        String userCommand = (String) InputFormatting.inputWrapper("string", null); // todo

        if (userCommand.equals("1")){
            UserInterface.getAppsDb().printApplicationsByApplicantID(targetApplicant, this.currentUser.getFirmId());
            return;
        }

        // the HR User wants to either get the resume or cover letter of
        // the target applicant.
        // The target applicant may have different Resumes and cover letter
        // prompt the HR all the applications by this individual
        System.out.println("Please select an application: ");

        UserInterface.getAppsDb().printApplicationsByApplicantID(targetApplicant, this.currentUser.getFirmId());

        long targetApplicationId = (long) InputFormatting.inputWrapper("long", null); // todo
        Application targetApplication = (Application) UserInterface.getAppsDb().getItemByID(targetApplicationId);

        // get the resume or cover letter of the chose application
        if(userCommand.equals("2")){
            System.out.println(targetApplication.getResume());
        } else {
            System.out.println(targetApplication.getCoverLetter());
        }
    }

    private void handleApplicantsPerJobCommand(){
        System.out.println("Please select a job: ");
        UserInterface.getJobsDb().printJobsByFirmId(this.currentUser.getFirmId());
        long jobId = (long) InputFormatting.inputWrapper("long", null); // todo
        UserInterface.getAppsDb().printApplicationsByJobID(jobId);
    }

    private void handleIntervieweeMatching(){
        System.out.println("Please select a job: ");
        UserInterface.getJobsDb().printJobsByFirmId(this.currentUser.getFirmId());
        long jobId = (long) InputFormatting.inputWrapper("long", null); // todo
        System.out.println("Please select an application to interview");
        UserInterface.getAppsDb().printOpenApplicationsByJobID(jobId);
        long applicationId = (long) InputFormatting.inputWrapper("long", null); // todo

        Application targetApplication = (Application) UserInterface.getAppsDb().getItemByID(applicationId);

        System.out.println("Please select an interviwer: ");
        UserInterface.getUsersDb().printInterviewersByFirmID(this.currentUser.getFirmId());

        long targetInterviewerId = (long) InputFormatting.inputWrapper("long", null); // todo
        targetApplication.setUpInterview(targetInterviewerId);
    }


    void handleCommands(){

        HashMap<String, Runnable> mainHrCommands = new HashMap<>();
        mainHrCommands.put("1", () -> this.handleJobCreation());
        mainHrCommands.put("2", () -> this.handleApplicantInfo());
        mainHrCommands.put("3", () -> this.handleApplicantsPerJobCommand());
        mainHrCommands.put("4", () -> this.handleIntervieweeMatching());

        String command = "";
        while (!command.equals("Exit")){

            System.out.println("[1] Create a new job post");
            System.out.println("[2] View applicants information");
            System.out.println("[3] View applicants for a particular job");
            System.out.println("[4] View applicants for a particular job");

            command = (String) InputFormatting.inputWrapper("string", new ArrayList(mainHrCommands.keySet()));
            mainHrCommands.get(command).run();
        }

    }
}
