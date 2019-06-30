import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.HashMap;

public class ApplicantCommandHandler extends CommandHandler{
    /**
     * This class handles all high-level commands for Applicant users
     * This class should call methods from the database and attended classes
     */

    // Common methods
    // listAvailableCommands()
    private long applicantID;
    private LocalDate creationDate;
    private ApplicationDatabase appsDb;
    private UserCredentialsDatabase usersDb;
    private List<Application> allApps;
    //TODO: set a global reference date here or something
    private LocalDate someDate = LocalDate.of(2019, 06, 10);

    public ApplicantCommandHandler(ApplicationDatabase appsDb, JobPostingDatabase jobsDb, UserCredentialsDatabase usersDb,
                                   UserCredentials user){

        super(appsDb, jobsDb, usersDb);
        this.applicantID = user.getApplicantID();
        this.creationDate = user.getCreationDate();
        this.allApps = this.getApplications();
        deleteCVAndCoverLetter();
    }

    @Override
    public void printCommandList(){

    }

    @Override
    public void handleCommand(String commandID){

    }

    private List<Application> getApplications(){
        return appsDb.getApplicationByApplicantID(this.applicantID);
    }

    private List<Application> getOpenApplications(){
        List<Application> openApps;
        for (Application app:this.allApps){
            if (app.isOpen()){
                openApps.add(app);
            }
        }
        return openApps;
    }

    private List<Application> getClosedApplications(){
        List<Application> closedApps;
        for (Application app:this.allApps){
            if (app.isClosed()){
                closedApps.add(app);
            }
        }
        return closedApps;
    }

    private Application getApplicationByApplicationID(Long appID, List<Application> inApps){
        for (Application app:inApps){
            if (app.getApplicationID() == appID){
                return app;
            }
        }
    }

    public void viewOpenApplications(){
        System.out.println("This is are your current Open applications: ");
        for (Application app:this.getOpenApplications()){
            System.out.println(app);
        }
    }

    /**
     * Should be able to view the jobID
     */
    public void viewJobPosting(){
        jobsDb.viewOpenJobs();
    }

    public void applyForJob(Long jobID){
        appsDb.addApplication(this.applicantID, jobID);
        this.getApplications();
    }

    public void withdrawFromJob(Long appID){
        List<Application> openApps = this.getOpenApplications();
        this.getApplicationByApplicationID(appID, openApps).setClosed();
    }

    //TODO: Needs common text parser/reader
    public void readApplicationCoverLetter(Long AppID){
        String clPath;
        clPath = this.getApplicationByApplicationID(AppID, this.allApps).getClPath();
        if (clPath != null){
            //parse and output txt file here
        }
    }

    //TODO: Needs common text parser/reader
    public void readApplicationCV(Long AppID){
        String cvPath;
        cvPath = this.getApplicationByApplicationID(AppID, this.allApps).getCvPath();
        if (cvPath != null){
            //parse and output txt file here
        }
    }

    public void setApplicationCV(Long AppID){
        String inCV = "some path here";
        this.getApplicationByApplicationID(AppID, this.allApps).setCvPath(inCV);
    }

    public void setApplicationCoverLetter(Long AppID){
        String inCL = "";
        this.getApplicationByApplicationID(AppID, this.allApps).setClPath(inCL);
    }

    public void getHistory{
        System.out.println("The creation date of this account is: " + this.creationDate);
        System.out.println("These are your applications that are now closed: ");
        for (Application app:this.getClosedApplications()){
            System.out.println(app);
        }

        System.out.println("These are your applications that are open: ");
        for (Application app:this.getOpenApplications()){
            System.out.println(app);
        }

        long minDaysBetween = 0;
        for (Application app:this.getClosedApplications()) {
            long daysBetween = ChronoUnit.DAYS.between(app.getClosedDate(), someDate);
            if (minDaysBetween == 0) {
                minDaysBetween = daysBetween;
            } else if (minDaysBetween >= daysBetween) {
                minDaysBetween = daysBetween;
            }
        }
        System.out.println("It's been " + minDaysBetween + " since your last closed application.");
    }

    public void deleteCVAndCoverLetter(){
        for (Application app:this.getClosedApplications()){
            if (ChronoUnit.DAYS.between(someDate, app.getClosedDate()) > 30){
                app.setClPath(null);
                app.setCvPath(null);
            }
        }
    }
}
