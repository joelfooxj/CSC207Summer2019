import java.util.List;
import java.util.Arrays;

public class InterviewerCommandHandler extends CommandHandler {
    /**
     * This class handles interviewer functionality
     *
     * 1. see list of applications to be processed
     * 2. Enter recommendation for application
     */

    private ApplicationDatabase appsDb;
    private JobsDatabase jobsDb;
    private UserCredentialsDatabase usersDb;
    private String interviewerID;
    private List<Application> assignedApps;


    public InterviewerCommandHandler(ApplicationDatabase appsDb, JobsDatabase jobsDb, UserCredentialsDatabase usersDb,
                                     String interviewerID){
        super(appsDb, jobsDb, usersDb);
        this.interviewerID = interviewerID;
        this.assignedApps = this.getApplications();
        while(true){
            try {
                printCommandList();
                String inputString = (String) InputFormatting.inputWrapper(
                        "string",
                        Arrays.asList("1", "2"));
                handleCommand(inputString);
                break;
            } catch (EscapeLoopException e) {
                System.out.println("Returning to the main menu.");
            }
        }
    }

    private List<Application> getApplications(){
        return appsDb.getApplicationByIntervierID(this.interviewerID);
    }

    public void viewInterviewees(){
        for (Application app:this.assignedApps){
            System.out.println(app);
        }
    }

    public void recommendApplication(Long ApplicationID){
        for (Application app:this.assignedApps){
            if(app.getApplicationID() == ApplicationID){
                app.recommend();
            }
        }
    }

    @Override
    public void printCommandList(){
        System.out.println("Select one of the following options: \n");
        System.out.println("[1] View all assigned interviewees.\n");
        System.out.println("[2] Recommend an application.\n");
    }

    @Override
    public void handleCommand(String commandID){
        try {
            switch (commandID){
                case "1":
                    System.out.println("Here are your assigned interviewees: ");
                    viewInterviewees();
                    break;
                case "2":
                    System.out.println("Enter the applicant number to be recommended: ");
                    Long inputApplicantID = (Long) InputFormatting.inputWrapper("string", null);
                    recommendApplication(inputApplicantID);
                    break;
                default:
                    System.out.println("Not a valid input.");
                    break;
            }
        } catch (EscapeLoopException e){}
    }

}
