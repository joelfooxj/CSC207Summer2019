import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class HrCommandHandler extends CommandHandler {

    protected UserCredentialsDatabase usersDb;
    protected LocalDate sessionDate;
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
        String jobTitle = sc.nextLine();
        System.out.println("Enter the job description");
        String jobDescription = sc.nextLine();
        this.jobsDb.addJob(jobTitle, jobDescription, currentUser.getFirmId(), this.sessionDate);
        System.out.println("Job post created successfully");
    }

    private void handleApplicantInfo(){

        // get the applicant ID (username)
        System.out.println("Please choose an applicant: ");
        appsDb.printApplicationsByFirmID(this.currentUser.getFirmId());

        String targetApplicant = sc.nextLine();

        // Prompt the HR user an option
        System.out.println("Please choose an option: ");
        System.out.println("[1] View jobs applied for");
        System.out.println("[2] View Resume");
        System.out.println("[3] View Cover letter");
        String userCommand = sc.nextLine();

        if (userCommand.equals("1")){
            appsDb.printApplicationsByApplicantID(targetApplicant, this.currentUser.getFirmId());
            return;
        }

        // the HR User wants to either get the resume or cover letter of
        // the target applicant.
        // The target applicant may have different Resumes and cover letter
        // prompt the HR all the applications by this individual
        System.out.println("Please select an application: ");

        appsDb.printApplicationsByApplicantID(targetApplicant, this.currentUser.getFirmId());

        Application targetApplication = (Application) this.appsDb.getItemByID(sc.nextLong());

        // get the resume or cover letter of the chose application
        if(userCommand.equals("2")){
            System.out.println(targetApplication.getResume());
        } else {
            System.out.println(targetApplication.getCoverLetter());
        }
    }

    private void handleApplicantsPerJobCommand(){
        System.out.println("Please select a job: ");
        this.jobsDb.printJobsByFirmId(this.currentUser.getFirmId());
        //jobsDb.getJobsByFirmId();
        long jobId = sc.nextLong();
        this.appsDb.printApplicationsByJobID(jobId);
    }

    private void handleIntervieweeMatching(){
        System.out.println("Please select a job: ");
        this.jobsDb.printJobsByFirmId(this.currentUser.getFirmId());
        //jobsDb.getJobsByFirmId();
        long jobId = sc.nextLong();
        System.out.println("Please select an application to interview");
        this.appsDb.printOpenApplicationsByJobID(jobId);
        Application targetApplication = (Application) this.appsDb.getItemByID(sc.nextLong());

        System.out.println("Please select an interviwer: ");
        this.usersDb.printInterviewersByFirmID(this.currentUser.getFirmId());
        String targetInterviewerId = sc.nextLine();
        targetApplication.setUpInterview(targetInterviewerId);
    }


    void handleCommands(String commandId){

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

        try {
            this.saveAll();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
