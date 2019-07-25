package CommandHandlers;

import Databases.JobApplication;
import Databases.JobPosting;
import Databases.UserCredentials;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class ApplicantCommandHandler implements CommandHandler{
    /**
     * This class handles all high-level commands for Applicant users
     * This class should call methods from the database and attended classes
     */

    private long applicantID;
    private LocalDate creationDate;
    private String username;

    public ApplicantCommandHandler(UserCredentials user){
        this.applicantID = user.getUserID();
        this.creationDate = user.getCreationDate();
        this.username = user.getUserName();
        deleteCVAndCoverLetter();
    }

    private JobApplication getApplication(String applicationID){
        return UserInterface.getAppsDb().getApplicationByApplicationID(Long.parseLong(applicationID));
    }

    public void withdrawApplication(String applicationID){
        this.getApplication(applicationID).setOpen(false);
    }

    public String getApplicationDesc(String applicationID){
        return this.getApplication(applicationID).toString();
    }

    public String getApplicationCV(String applicationID){
        return this.getApplication(applicationID).getCV();
    }

    public void setApplicationCV(String applicationID, String inCV){
        this.getApplication(applicationID).setCV(inCV);
    }

    public String getApplicationCoverLetter(String applicationID){
        return this.getApplication(applicationID).getCoverLetter();
    }

    public void setApplicationCoverLetter(String applicationID, String inCoverLetter){
        this.getApplication(applicationID).setCoverLetter(inCoverLetter);
    }

    private List<JobPosting> getOpenUnappliedJobs(){
        List<JobPosting> openJobs = new ArrayList<>();
        if (UserInterface.getJobsDb().isEmpty()){
            return openJobs;
        }
        for (Long jobID: UserInterface.getJobsDb().getOpenJobIDs()){
            boolean appliedForFlag = false;
            if (!this.getAllApplications().isEmpty()){
                for (JobApplication app: this.getAllApplications()){
                    if (app.getJobID() == jobID){
                        appliedForFlag = true;
                    }
                }
            }
            if (!appliedForFlag){
                openJobs.add(UserInterface.getJobsDb().getJobPostingByID(jobID));
            }
        }
        return openJobs;
    }

    // todo: get jobsDB to re-write this.
    /**
     * This method returns a single string containing all jobs that are open
     * and have not already been applied for by this applicant.
     * @return String openJobs
     */
    public String getOpenJobsPrintout(){
        if (this.getOpenUnappliedJobs().isEmpty()) {
            return "There are no open job postings.";
        } else {
            StringBuilder openJobsPrintout = new StringBuilder();
            for (JobPosting job: this.getOpenUnappliedJobs()){
                openJobsPrintout.append(job);
                openJobsPrintout.append("\n");
            }
            return new String(openJobsPrintout);
        }
    }

    /**
     * This method returns a list of strings of JobIDs
     * @return jobIDs
     */
    public List<String> getOpenJobsList(){
        if(this.getOpenUnappliedJobs().isEmpty()){
            return null;
        } else {
            List<String> jobList = new ArrayList<>();
            for (JobPosting job: this.getOpenUnappliedJobs()){
                jobList.add(job.getJobId().toString());
            }
            return jobList;
        }
    }

    // todo: update addApplication() with less parameters
    public void applyForJobs(List<String> jobIDs){
        for (String jobID: jobIDs){
            long inputFirmID = UserInterface.getJobsDb().getItemByID(Long.parseLong(jobID)).getFirmid();
            UserInterface.getAppsDb().addApplication(
                    this.applicantID,
                    Long.parseLong(jobID),
                    inputFirmID,
                    UserInterface.getDate());
        }
    }

    /**
     * This method is just a shorthand
     * @return A list of applications associated with this Applicant
     */
    private List<JobApplication> getAllApplications(){
        return UserInterface.getAppsDb().getApplicationsByApplicantID(this.applicantID);
    }

    public List<Long> getAllOpenApplicationIDs(){
        if (!this.getAllApplications().isEmpty()){
            List<Long> retLongList = new ArrayList<>();
            for (JobApplication app:this.getAllApplications()){
                if (app.isOpen()){
                    retLongList.add(app.getApplicationID());
                }
            }
            return retLongList;
        } else {return null;}
    }

    public String getMinDays(){
        long minDaysBetween = 0;
        for (JobApplication app:this.getAllApplications()) {
            if(!app.isOpen()){
                long daysBetween = ChronoUnit.DAYS.between(app.getClosedDate(), UserInterface.getDate());
                if (minDaysBetween == 0) {
                    minDaysBetween = daysBetween;
                } else if (minDaysBetween >= daysBetween) {
                    minDaysBetween = daysBetween;
                }
            }
        }
        return String.valueOf(minDaysBetween);
    }

    public String getApplicationsPrintout(boolean isOpen){
        StringBuilder openApps = new StringBuilder();
        StringBuilder closedApps = new StringBuilder();
        if(!this.getAllApplications().isEmpty()){
            for (JobApplication app:this.getAllApplications()){
                if (app.isOpen()){
                    openApps.append(app);
                    openApps.append("\n");
                } else {
                    closedApps.append(app);
                    closedApps.append("\n");
                }
            }
            if (isOpen) { return new String(openApps); }
            else { return new String(closedApps); }
        } else {
            return "You have no open applications.";
        }
    }

    public String getCreationDate(){
        return this.creationDate.toString();
    }

    public String getApplicantID(){
        return String.valueOf(this.applicantID);
    }

    public String getUsername(){
        return this.username;
    }

    /**
     * This method will set the cover letter and CV of all applications
     * to null if and only if all applications are closed AND at least 30 days
     * has passed since the last closed application.
     */
    private void deleteCVAndCoverLetter(){
        if (this.getAllApplications().isEmpty()){ return; }
        for (JobApplication app:this.getAllApplications()){
            if (app.isOpen()) {return;}
            else if (ChronoUnit.DAYS.between(UserInterface.getDate(), app.getClosedDate()) < 30){return; }
        }
        for (JobApplication app:this.getAllApplications()){
            app.setCV(null);
            app.setCoverLetter(null);
        }
    }
}
