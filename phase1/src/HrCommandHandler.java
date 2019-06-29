import java.time.LocalDate;
import java.util.ArrayList;

public class HrCommandHandler extends CommandHandler {

    protected UserCredentialsDatabase usersDb;
    protected LocalDate sessionDate;
    public HrCommandHandler(ApplicationsDatabase appsDb,
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
        this.jobsDb.addJob(jobTitle, jobDescription, this.sessionDate);
        System.out.println("Job post created successfully");
    }

    private void handleApplicantInfo(){

        // get the applicant ID (username)
        System.out.println("Please choose an applicant: ");
        appsDb.printApplicantsByFirmId(user.getFirmId());
        String targetApplicant = sc.nextLine();

        // Prompt the HR user an option
        System.out.println("Please choose an option: ");
        System.out.println("[1] View jobs applied for");
        System.out.println("[2] View Resume");
        System.out.println("[3] View Cover letter");
        String userCommand = sc.nextLine();

        if (userCommand.equals("1")){
            appsDb.printApplicationsByUserId(targetApplicant, user.getFirmId());
            return;
        }

        // the HR User wants to either get the resume or cover letter of
        // the target applicant.
        // The target applicant may have different Resumes and cover letter
        // prompt the HR all the applications by this individual
        System.out.println("Please select an application: ");
        appsDb.printApplicationsByUserId(targetApplicant, user.getFirmId());
        Application targetApplication = appsDb.getItemById(sc.nextLong());

        // get the resume or cover letter of the chose application
        if(userCommand.equals("2")){
            System.out.println(targetApplication.getResume());
        } else {
            System.out.println(targetApplication.getCoverLetter());
        }
    }


    void printCommandList(){
        System.out.println("[1] Create a new job post");
        System.out.println("[2] View applicants information");
        System.out.println("[3] View applicants for a particular job");

    }

    void handleCommand(String commandId){
        if (commandId.equals("1")){
            handleJobCreation();
        } else if (commandId.equals("2")){
            handleApplicantInfo();
        } else if(commandId.equals("3")){
            // TODO: not yet implemented

        }
    }
}
