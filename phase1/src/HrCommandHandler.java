import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class HrCommandHandler implements CommandHandler {

    protected int JOBLIFESPAN = 30;


    private void handleJobCreation(){
        System.out.println("Enter the job title: ");
        String jobTitle = (String) InputFormatting.inputWrapper(
                "string",
                true,
                null); // todo
        if (jobTitle == null) {return;}
        System.out.println("Enter the job description");
        String jobDescription = (String) InputFormatting.inputWrapper("string", true,null); // todo
        if (jobDescription == null) {return;}
        LocalDate expiryDate = UserInterface.getDate().plusDays(JOBLIFESPAN);
        long firmId = UserInterface.getCurrentUser().getFirmId();
        UserInterface.getJobsDb().addJob(jobTitle, jobDescription, firmId, UserInterface.getDate(), expiryDate);
        System.out.println("Job post created successfully");
    }

    private void handleApplicantInfo(){

        // get the applicant ID (username)
        UserInterface.getUsersDb().printApplicants();
        System.out.println("Please choose an applicant: ");
        //UserInterface.getAppsDb().printApplicationsByFirmID(UserInterface.getCurrentUser().getFirmId());

        Long targetApplicant = (Long) InputFormatting.inputWrapper("long", true,null); // todo
        if (targetApplicant == null) {return;}
        // Prompt the HR user an option
        System.out.println("Please choose an option: ");
        System.out.println("[1] View jobs applied for");
        System.out.println("[2] View Resume");
        System.out.println("[3] View Cover letter");
        String userCommand = (String) InputFormatting.inputWrapper("string", true, null); // todo
        if (userCommand == null) {return;}
        long firmId = UserInterface.getCurrentUser().getFirmId();
        if (userCommand.equals("1")){

            UserInterface.getAppsDb().printApplicationsByApplicantID(targetApplicant, firmId);
            return;
        }

        // the HR User wants to either get the resume or cover letter of
        // the target applicant.
        // The target applicant may have different Resumes and cover letter
        // prompt the HR all the applications by this individual
        System.out.println("Please select an application: ");

        UserInterface.getAppsDb().printApplicationsByApplicantID(targetApplicant, firmId);

        long targetApplicationId = (long) InputFormatting.inputWrapper("long",false, null); // todo
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
        long firmId = UserInterface.getCurrentUser().getFirmId();
        UserInterface.getJobsDb().printJobsByFirmId(firmId);
        Long jobId = (Long) InputFormatting.inputWrapper("long",true, null); // todo
        if (jobId == null) {return;}
        UserInterface.getAppsDb().printApplicationsByJobID(jobId);
    }

    private void handleIntervieweeMatching(){
        System.out.println("Please select a job: ");
        long firmId = UserInterface.getCurrentUser().getFirmId();
        UserInterface.getJobsDb().printJobsByFirmId(firmId);
        Long jobId = (Long) InputFormatting.inputWrapper("long", true,null); // todo
        if (jobId == null) {return;}
        System.out.println("Please select an application to interview");
        UserInterface.getAppsDb().printOpenApplicationsByJobID(jobId);
        Long applicationId = (Long) InputFormatting.inputWrapper("long", true, null); // todo
        if (applicationId == null) {return;}
        Application targetApplication = (Application) UserInterface.getAppsDb().getItemByID(applicationId);
        System.out.println("Please select an interviwer: ");
        UserInterface.getUsersDb().printInterviewersByFirmID(firmId);
        Long targetInterviewerId = (Long) InputFormatting.inputWrapper("long", true, null); // todo
        if (targetInterviewerId == null) {return;}
        targetApplication.setUpInterview(targetInterviewerId);
    }


    public void handleCommands(){

        HashMap<String, Runnable> mainHrCommands = new HashMap<>();
        mainHrCommands.put("1", () -> this.handleJobCreation());
        mainHrCommands.put("2", () -> this.handleApplicantInfo());
        mainHrCommands.put("3", () -> this.handleApplicantsPerJobCommand());
        mainHrCommands.put("4", () -> this.handleIntervieweeMatching());
        mainHrCommands.put("Exit", () -> {});

        String command = "";
        while (!command.equals("Exit")){

            System.out.println("[1] Create a new job post");
            System.out.println("[2] View applicants information");
            System.out.println("[3] View applicants for a particular job");
            System.out.println("[4] Set up an interview");
            System.out.println("[Exit] to exit to login.");

            command = (String) InputFormatting.inputWrapper("string", false, new ArrayList<>(mainHrCommands.keySet()));
            mainHrCommands.get(command).run();
        }

    }
}
