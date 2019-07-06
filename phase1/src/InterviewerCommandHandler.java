import java.util.HashMap;
import java.util.List;
import java.util.Arrays;

public class InterviewerCommandHandler extends CommandHandler{
    private Long interviewerID;
    private List<Application> assignedApps;
    private List<Application> recommendedApps;

    // TODO: Add error check that prevents interviewer from recommending an application twice in the same session


    public InterviewerCommandHandler(ApplicationDatabase appsDb, JobsDatabase jobsDb, UserCredentials user){
        super(appsDb, jobsDb, user);
        this.interviewerID = user.getUserID();
        this.assignedApps = this.getApplications();
        while(true){
            printCommandList();
            String inputString = (String) InputFormatting.inputWrapper(
                    "string",
                    Arrays.asList("1", "2"));
            if (inputString.equals("Exit")){
                 break;
            } else { handleCommands(inputString); }
        }
    }

    private List<Application> getApplications(){
        return appsDb.getApplicationByInterviewerID(this.interviewerID);
    }

    public void printInterviewees(){
        for (Application app:this.assignedApps){
            System.out.println(app);
        }
    }

    public void recommendApplication(Long ApplicationID){
        for (Application app:this.assignedApps){
            if(app.getApplicationID() == ApplicationID && !this.recommendedApps.contains(app)){
                app.recommend();
                this.recommendedApps.add(app);
                return;
            }
        }
    }

    public void printCommandList(){
        System.out.println("Select one of the following options: \n");
        System.out.println("[1] View all assigned interviewees.\n");
        System.out.println("[2] Recommend an application.\n");
        System.out.println("[Exit] To exit the program.\n");
    }

    void handleCommands(String commandID){
        HashMap<String, Runnable> menu = new HashMap<>();
        menu.put("1", () -> {
            System.out.println("Here are your assigned interviewees: ");
            printInterviewees();
        });
        menu.put("2", () -> {
            System.out.println("Enter the applicant number to be recommended: ");
            Long inputApplicantID = (Long) InputFormatting.inputWrapper("long", null);
            recommendApplication(inputApplicantID);
        });
        menu.get(commandID).run();
    }
}