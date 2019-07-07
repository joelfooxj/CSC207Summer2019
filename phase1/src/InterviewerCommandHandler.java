import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Arrays;

public class InterviewerCommandHandler implements CommandHandler{
    private Long interviewerID;
    private List<Application> assignedApps;
    private List<Application> recommendedApps;

    public InterviewerCommandHandler(UserCredentials user){
        this.interviewerID = user.getUserID();
        this.assignedApps = this.getApplications();
        this.recommendedApps = new ArrayList<>();
    }

    private List<Application> getApplications(){
        return UserInterface.getAppsDb().getApplicationByInterviewerID(this.interviewerID);
    }

    private void printInterviewees(){
        for (Application app:this.assignedApps){
            System.out.println(app);
        }
    }

    /**
     * If application is already recommended, will do nothing
     * Interviewers can only recommend each application once per session.
     * @param ApplicationID: ID of the application to be recommended.
     */
    private void recommendApplication(Long ApplicationID){
        for (Application app:this.assignedApps){
            if(app.getApplicationID() == ApplicationID && !this.recommendedApps.contains(app)){
                app.recommend();
                this.recommendedApps.add(app);
                return;
            }
        }
    }

    public void handleCommands(){
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
        menu.put("Exit", () -> System.out.println("Returning to login"));

        String commandInput = "";
        while(!commandInput.equals("Exit")){
            System.out.println("Select one of the following options:");
            System.out.println("[1] View all assigned interviewees.");
            System.out.println("[2] Recommend an application.");
            System.out.println("[Exit] To exit the program.");
            commandInput = (String) InputFormatting.inputWrapper(
                    "string",
                    new ArrayList<>(menu.keySet()));
            menu.get(commandInput).run();
        }
    }
}