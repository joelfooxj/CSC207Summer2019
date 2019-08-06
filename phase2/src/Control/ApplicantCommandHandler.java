package Control;

import Model.*;
import View.GUI;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class ApplicantCommandHandler extends CommandHandler{
    /**
     * This class handles all high-level commands for Applicant users
     * This class should call methods from the database and attended classes
     */

    private long applicantID;
    private LocalDate creationDate;
    private String username;
    private UserCredentials currentUser;

    public ApplicantCommandHandler(UserCredentials user){
        this.applicantID = user.getUserID();
        this.creationDate = user.getCreationDate();
        this.username = user.getUserName();
        this.currentUser = user;
        List<String> inboxMessages = user.getInbox();
        if (!inboxMessages.isEmpty()){ GUI.messageBox("Messages", String.join("\n", inboxMessages)); }
        else {GUI.messageBox("no message received",String.join("\n", inboxMessages));}

    }

    @Override
    public void handleCommands() {
        deleteCVAndCoverLetter();
        GUI.applicantForm(this);
    }

    private JobApplication getApplication(String applicationID){
        return sessionData.jobAppsDb.getApplicationByApplicationID(Long.parseLong(applicationID));
    }

    public void withdrawApplication(String applicationID){
        this.getApplication(applicationID).setOpen(false, super.sessionDate);
    }
    public void setApplicationCV(String applicationID, String inCV){
        this.getApplication(applicationID).setCV(inCV);
    }
    public void setApplicationCoverLetter(String applicationID, String inCoverLetter){
        this.getApplication(applicationID).setCoverLetter(inCoverLetter);
    }

    /**
     * This methods returns a list of JobPostings that are:
     * 1. Open
     * 2. Unapplied For
     * 3. Are not expired
     * @return list of open & unapplied JobPosting
     */
    private List<JobPosting> getOpenUnappliedJobs(){
        List<JobPosting> openJobs = new ArrayList<>();
        if (sessionData.jobPostingsDb.isEmpty()){
            return openJobs;
        }
        for (Long jobID: sessionData.jobPostingsDb.getOpenPostingIds()){
            boolean appliedForFlag = false;
            if (!this.getAllApplications().isEmpty()){
                for (JobApplication app: this.getAllApplications()){
                    if (app.getJobID() == jobID){
                        appliedForFlag = true;
                    }
                }
            }
            JobPosting inJob = sessionData.jobPostingsDb.getJobPostingByID(jobID);
            if (!appliedForFlag && !inJob.isExpired(sessionDate)){
                openJobs.add(inJob);
            }
        }
        return openJobs;
    }

    /**
     * This method returns a list of jobPostings filtered based on tags and location
     * @param tags: list of hashtags of JobPosting
     * @param location: location of JobPosting
     * @return
     */
    private List<JobPosting> getFilteredJobs(HashSet<String> tags, String location){
        List<JobPosting> retJobList = new ArrayList<>();
        for (JobPosting job: this.getOpenUnappliedJobs()){
            if(job.containsAllHashTags(tags) && (job.getLocation().equals(location)||location.equals("All locations"))){
                retJobList.add(job);
            }
        }
        return retJobList;
    }



    // todo: update addApplication() with less parameters
    public void applyForJobs(List<String> jobIDs){
        for (String jobID: jobIDs){
            long inputFirmID = sessionData.jobPostingsDb.getItemByID(Long.parseLong(jobID)).getFirmId();
            List<requiredDocs> docsList = sessionData.jobPostingsDb.getItemByID(Long.parseLong(jobID)).getRequiredDocs();
            JobApplication newJobApp = sessionData.jobAppsDb.addApplication(
                    this.applicantID,
                    Long.parseLong(jobID),
                    inputFirmID,
                    sessionDate, docsList);

            newJobApp.addObserver(this.currentUser);
        }
    }

    /**
     * This method is just a shorthand
     * @return A list of applications associated with this Applicant
     */
    private List<JobApplication> getAllApplications(){
        //return sessionData.jobAppsDb.getApplicationsByApplicantID(this.applicantID);
        HashMap requirement = new HashMap();
        requirement.put(JobApplicationDatabase.jobAppFilterKeys.APPLICANT_ID, this.applicantID);
        return sessionData.jobAppsDb.filterJobApps(requirement);
    }

    /**
     * This method calculates the days between the current session and the last closed application
     * @return minDaysBetween
     */
    public String getMinDays(){
        long minDaysBetween = 0;
        for (JobApplication app:this.getAllApplications()) {
            if(!app.isOpen()){
                long daysBetween = ChronoUnit.DAYS.between(app.getClosedDate(), sessionDate);
                if (minDaysBetween == 0) {
                    minDaysBetween = daysBetween;
                } else if (minDaysBetween >= daysBetween) {
                    minDaysBetween = daysBetween;
                }
            }
        }
        return String.valueOf(minDaysBetween);
    }

    public String getCreationDate(){
        return this.creationDate.toString();
    }

    public Long getApplicantID(){
        return this.applicantID;
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
        if (this.getAllApplications() == null){ return; }
        for (JobApplication app:this.getAllApplications()){
            if (app.isOpen()) {return;}
            else if (ChronoUnit.DAYS.between(sessionDate, app.getClosedDate()) < 30){return; }
        }
        for (JobApplication app:this.getAllApplications()){
            app.setCV(null);
            app.setCoverLetter(null);
        }
    }
}
