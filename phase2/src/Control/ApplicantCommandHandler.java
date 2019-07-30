package Control;

import Model.JobApplication;
import Model.JobPosting;
import Model.UserCredentials;
import View.GUI;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;
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
        List<String> inboxMessages = user.getInbox();
        if (!inboxMessages.isEmpty()){ GUI.messageBox("Messages", String.join("\n", inboxMessages)); }
        GUI.applicantForm(this);
    }

    public List<String> getLocationList(){
        // get all open jobs, construct set of locations
        HashSet<String> locationSet = new HashSet<>();
        for (JobPosting job: this.getOpenUnappliedJobs()){
            locationSet.add(job.getLocation());
        }
        return List.copyOf(locationSet);
    }

    // todo: encapsulate these getters and setters into its own class and send to GUI
    private JobApplication getApplication(String applicationID){
        return HyreLauncher.getAppsDb().getApplicationByApplicationID(Long.parseLong(applicationID));
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

    /**
     * This methods returns a list of JobPostings that are open and unapplied
     * for by this applicant
     * @return list of open & unapplied JobPosting
     */
    private List<JobPosting> getOpenUnappliedJobs(){
        List<JobPosting> openJobs = new ArrayList<>();
        if (HyreLauncher.getJobsDb().isEmpty()){
            return openJobs;
        }
        for (Long jobID: HyreLauncher.getJobsDb().getOpenJobIDs()){
            boolean appliedForFlag = false;
            if (!this.getAllApplications().isEmpty()){
                for (JobApplication app: this.getAllApplications()){
                    if (app.getJobID() == jobID){
                        appliedForFlag = true;
                    }
                }
            }
            if (!appliedForFlag){
                openJobs.add(HyreLauncher.getJobsDb().getJobPostingByID(jobID));
            }
        }
        return openJobs;
    }

    /**
     * This methods return
     * @param tags: list of hashtags of JobPosting
     * @param location: location of JobPosting
     * @return
     */
    private List<JobPosting> getFilteredJobs(List<String> tags, String location){
        List<JobPosting> retJobList = new ArrayList<>();
        for (JobPosting job: this.getOpenUnappliedJobs()){
            if(job.containsHashTags(tags) && job.getLocation().equals(location)){
                retJobList.add(job);
            }
        }
        return retJobList;
    }

    /**
     * This method returns a single string containing all jobs that are open
     * and have not already been applied for by this applicant.
     * @return String openJobs
     */
    public String getFilteredJobsPrintout(List<String> tags, String location){
        if (this.getOpenUnappliedJobs().isEmpty()) {
            return "There are no open job postings.";
        } else {
            StringBuilder openJobsPrintout = new StringBuilder();
            for (JobPosting job: this.getFilteredJobs(tags, location)){
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
    public List<String> getFilteredJobsList(List<String> tags, String location){
        List<String> jobList = new ArrayList<>();
        if(!this.getOpenUnappliedJobs().isEmpty()) {
            for (JobPosting job : this.getFilteredJobs(tags, location)) {
                jobList.add(job.getJobId().toString());
            }
        }
        return jobList;
    }

    // todo: update addApplication() with less parameters
    public void applyForJobs(List<String> jobIDs){
        for (String jobID: jobIDs){
            long inputFirmID = HyreLauncher.getJobsDb().getItemByID(Long.parseLong(jobID)).getFirmId();
            HyreLauncher.getAppsDb().addApplication(
                    this.applicantID,
                    Long.parseLong(jobID),
                    inputFirmID,
                    HyreLauncher.getDate());
        }
    }

    /**
     * This method is just a shorthand
     * @return A list of applications associated with this Applicant
     */
    private List<JobApplication> getAllApplications(){
        return HyreLauncher.getAppsDb().getApplicationsByApplicantID(this.applicantID);
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

    /**
     * This method calculates the days between the current session and the last closed application
     * @return minDaysBetween
     */
    public String getMinDays(){
        long minDaysBetween = 0;
        for (JobApplication app:this.getAllApplications()) {
            if(!app.isOpen()){
                long daysBetween = ChronoUnit.DAYS.between(app.getClosedDate(), HyreLauncher.getDate());
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
            else if (ChronoUnit.DAYS.between(HyreLauncher.getDate(), app.getClosedDate()) < 30){return; }
        }
        for (JobApplication app:this.getAllApplications()){
            app.setCV(null);
            app.setCoverLetter(null);
        }
    }
}
