import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InterviewerCommandHandler implements CommandHandler{
    private Long interviewerID;
    private List<Application> recommendedApps;

    public InterviewerCommandHandler(UserCredentials user){
        this.interviewerID = user.getUserID();
        this.recommendedApps = new ArrayList<>();
    }

    private List<Application> getApplications(){
        return UserInterface.getAppsDb().getApplicationByInterviewerID(this.interviewerID);
    }

    private List<Long> getApplicationIDs(){
        List<Long> idList = new ArrayList<>();
        if (!this.getApplications().isEmpty()){
            for(Application app:this.getApplications()){
                idList.add(app.getApplicationID());
            }
        }
        return idList;
    }

    private void printInterviewees(){
        if (this.getApplications().isEmpty()) {return;}
        for (Application app:this.getApplications()){
            System.out.println(app);
        }
    }

    /**
     * If application is already recommended, will do nothing
     * Interviewers can only recommend each application once per session.
     * @param ApplicationID: ID of the application to be recommended.
     */
    private void recommendApplication(Long ApplicationID){
        if (this.getApplications().isEmpty()){
            System.out.println("There are no applications to recommend.");
            return;
        }
        for (Application app:this.getApplications()){
            if(app.getApplicationID() == ApplicationID && !this.recommendedApps.contains(app)){
                app.recommend();
                this.recommendedApps.add(app);
                app.setUpInterview(-1);
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
            Long inputApplicantID = (Long) InputFormatting.inputWrapper(
                    "long",
                    true,
                    new ArrayList<>(this.getApplicationIDs()));
            if (inputApplicantID != null) { recommendApplication(inputApplicantID); }
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
                    false,
                    new ArrayList<>(menu.keySet()));
            menu.get(commandInput).run();
        }
    }
}