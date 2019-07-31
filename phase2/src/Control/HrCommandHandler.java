package Control;

import Model.JobApplication;
import Model.JobPosting;
import Model.UserCredentials;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HrCommandHandler implements CommandHandler {

    protected int JOBLIFESPAN = 30;
    private UserCredentials thisUser;

    public HrCommandHandler(UserCredentials hrUser){
        this.thisUser = hrUser;
    }

    public String getUsername(){
        return this.thisUser.getUserName();
    }

    public String getFirmID(){
        return String.valueOf(this.thisUser.getFirmId());
    }

    private List<JobPosting> getOpenJobs(){
        List<JobPosting> openJobs;
        List<Long> allJobIDs = HyreLauncher.getJobsDb().getOpenJobIDs();
        if (!allJobIDs.isEmpty()){
            for(Long jobID:allJobIDs) {
                openJobs.add(HyreLauncher.getJobsDb().getJobPostingByID(jobID));
            }
        }
        return openJobs;
    }

    public List<String>





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
        LocalDate expiryDate = HyreLauncher.getDate().plusDays(JOBLIFESPAN);
        long firmId = HyreLauncher.getCurrentUser().getFirmId();
        DateRange jobDateRange = new DateRange(HyreLauncher.getDate(), expiryDate);
//        HyreLauncher.getJobsDb().addJob(jobTitle, jobDescription, firmId, jobDateRange);
        System.out.println("Job post created successfully");
    }

    private void handleApplicantInfo(){

        // get the applicant ID (username)
        HyreLauncher.getUsersDb().printApplicants();
        System.out.println("Please choose an applicant: ");
        //Control.HyreLauncher.getAppsDb().printApplicationsByFirmID(Control.HyreLauncher.getCurrentUser().getFirmId());

        Long targetApplicant = (Long) InputFormatting.inputWrapper("long", true,null); // todo
        if (targetApplicant == null) {return;}
        // Prompt the HR user an option
        System.out.println("Please choose an option: ");
        System.out.println("[1] View jobs applied for");
        System.out.println("[2] View Resume");
        System.out.println("[3] View Cover letter");
        String userCommand = (String) InputFormatting.inputWrapper("string", true, null); // todo
        if (userCommand == null) {return;}
        long firmId = HyreLauncher.getCurrentUser().getFirmId();
        if (userCommand.equals("1")){

            HyreLauncher.getAppsDb().printApplicationsByApplicantID(targetApplicant, firmId);
            return;
        }

        // the HR User wants to either get the resume or cover letter of
        // the target applicant.
        // The target applicant may have different Resumes and cover letter
        // prompt the HR all the applications by this individual
        System.out.println("Please select an application: ");

        HyreLauncher.getAppsDb().printApplicationsByApplicantID(targetApplicant, firmId);

        long targetApplicationId = (long) InputFormatting.inputWrapper("long",false, null); // todo
        JobApplication targetJobApplication = HyreLauncher.getAppsDb().getItemByID(targetApplicationId);

        // get the resume or cover letter of the chose application
        if(userCommand.equals("2")){
            System.out.println(targetJobApplication.getResume());
        } else {
            System.out.println(targetJobApplication.getCoverLetter());
        }
    }

    private void handleApplicantsPerJobCommand(){
        System.out.println("Please select a job: ");
        long firmId = HyreLauncher.getCurrentUser().getFirmId();
        HyreLauncher.getJobsDb().printJobsByFirmId(firmId);
        Long jobId = (Long) InputFormatting.inputWrapper("long",true, HyreLauncher.getJobsDb().getOpenJobIDs());
        if (jobId == null) {return;}
        HyreLauncher.getAppsDb().printOpenApplicationsByJobID(jobId);
        System.out.println("Select an application you would like to modify: ");
        Long appId = (Long) InputFormatting.inputWrapper("long", true, HyreLauncher.getAppsDb().getOpenAppIdsByJob(jobId));
        decideApplication(HyreLauncher.getAppsDb().getItemByID(appId));
    }

    public void decideApplication(JobApplication app) {
        HashMap<String, Runnable> decideCommands = new HashMap<>();
        decideCommands.put("1", () -> app.hire(HyreLauncher.getSessionDate()));
        decideCommands.put("2", () -> app.reject(HyreLauncher.getSessionDate()));
        decideCommands.put("3", () -> {});
        System.out.println(app);
        System.out.println("[1] Hire this application");
        System.out.println("[2] Reject this application");
        System.out.println("[3] Exit");
        String response = (String) InputFormatting.inputWrapper("string", false, new ArrayList<>(decideCommands.keySet()));
        decideCommands.get(response).run();
    }

    private void handleIntervieweeMatching(){
        System.out.println("Please select a job: ");
        long firmId = HyreLauncher.getCurrentUser().getFirmId();
        HyreLauncher.getJobsDb().printJobsByFirmId(firmId);
        Long jobId = (Long) InputFormatting.inputWrapper("long", true,null); // todo
        if (jobId == null) {return;}
        System.out.println("Please select an application to interview");
        HyreLauncher.getAppsDb().printOpenApplicationsByJobID(jobId);
        Long applicationId = (Long) InputFormatting.inputWrapper("long", true, null); // todo
        if (applicationId == null) {return;}
        JobApplication targetJobApplication = (JobApplication) HyreLauncher.getAppsDb().getItemByID(applicationId);
        System.out.println("Please select an interviwer: ");
        HyreLauncher.getUsersDb().printInterviewersByFirmID(firmId);
        Long targetInterviewerId = (Long) InputFormatting.inputWrapper("long", true, null); // todo
        if (targetInterviewerId == null) {return;}
        targetJobApplication.setUpInterview(targetInterviewerId);
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
