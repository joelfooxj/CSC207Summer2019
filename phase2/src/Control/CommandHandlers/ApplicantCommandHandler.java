package Control.CommandHandlers;

import Model.JobApplicationPackage.JobApplication;
import Model.JobApplicationPackage.JobApplicationDatabase;
import Model.JobPostingPackage.JobPosting;
import Model.UserCredentialsPackage.UserCredentials;
import View.GUI;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;

public class ApplicantCommandHandler extends CommandHandler {
    /**
     * This class handles all high-level commands for Applicant users
     * This class should call methods from the database and attended classes
     */

    private long applicantID;
    private LocalDate creationDate;
    private String username;
    private UserCredentials currentUser;

    ApplicantCommandHandler(UserCredentials user) {
        this.applicantID = user.getUserID();
        this.creationDate = user.getCreationDate();
        this.username = user.getUserName();
        this.currentUser = user;
        List<String> inboxMessages = user.getInbox();
        if (!inboxMessages.isEmpty()) {
            GUI.messageBox("Messages", String.join("\n", inboxMessages));
        } else {
            GUI.messageBox("Messages", String.join("\n You Have no messages!", inboxMessages));
        }

    }

    @Override
    public void handleCommands() {
        deleteCVAndCoverLetter();
        GUI.applicantForm(this);
    }

    private JobApplication getApplication(String applicationID) {
        return sessionData.jobAppsDb.getApplicationByApplicationID(Long.parseLong(applicationID));
    }

    public void withdrawApplication(String applicationID) {
        this.getApplication(applicationID).setOpen(false, super.sessionDate);
    }

    public void setApplicationCV(String applicationID, String inCV) {
        this.getApplication(applicationID).setCV(inCV);
    }

    public void setApplicationCoverLetter(String applicationID, String inCoverLetter) {
        this.getApplication(applicationID).setCoverLetter(inCoverLetter);
    }

    public void applyForJobs(List<String> jobIDs) {
        for (String jobID : jobIDs) {
            JobPosting inJobPosting = sessionData.jobPostingsDb.getItemByID(Long.parseLong(jobID));
            JobApplication newJobApp = sessionData.jobAppsDb.addApplication(this.currentUser, inJobPosting);
            newJobApp.createInterviewProcess(sessionData.jobPostingsDb.getJobPostingByID(Long.parseLong(jobID)).getInterviewStages());
            newJobApp.addObserver(this.currentUser);
            sessionData.jobPostingsDb.getJobPostingByID(Long.parseLong(jobID)).addObserver(newJobApp);
        }
    }

    /**
     * This method is just a shorthand
     *
     * @return A list of applications associated with this Applicant
     */
    private List<JobApplication> getAllApplications() {
        HashMap<JobApplicationDatabase.jobAppFilterKeys, Object> requirement = new HashMap<>();
        requirement.put(JobApplicationDatabase.jobAppFilterKeys.APPLICANT_ID, this.applicantID);
        return sessionData.jobAppsDb.filterJobApps(requirement);
    }

    /**
     * This method calculates the days between the current session and the last closed application
     *
     * @return minDaysBetween
     */
    public String getMinDays() {
        long minDaysBetween = 0;
        for (JobApplication app : this.getAllApplications()) {
            if (!app.isOpen()) {
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

    public String getCreationDate() {
        return this.creationDate.toString();
    }

    public Long getApplicantID() {
        return this.applicantID;
    }

    public String getUsername() {
        return this.username;
    }

    /**
     * This method will set the cover letter and CV of all applications
     * to null if and only if all applications are closed AND at least 30 days
     * has passed since the last closed application.
     */
    private void deleteCVAndCoverLetter() {
        if (this.getAllApplications() == null) {
            return;
        }
        for (JobApplication app : this.getAllApplications()) {
            if (app.isOpen()) {
                return;
            } else if (ChronoUnit.DAYS.between(sessionDate, app.getClosedDate()) < 30) {
                return;
            }
        }
        for (JobApplication app : this.getAllApplications()) {
            app.setCV(null);
            app.setCoverLetter(null);
        }
    }
}
