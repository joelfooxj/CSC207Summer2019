package Control.CommandHandlers;

import Control.DateRange;
import Model.JobApplicationPackage.JobApplication;
import Model.JobApplicationPackage.JobApplicationDatabase;
import Model.JobPostingPackage.JobPosting;
import Model.JobPostingPackage.jobTags;
import Model.UserCredentialsPackage.UserCredentials;
import Model.requiredDocs;
import View.GUI;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class HrCommandHandler extends CommandHandler {

    /**
     * This class handles all high-level commands for HR users
     * This class should call methods from the database and attended classes
     */

    private final int JOBLIFESPAN = 30;
    private String username;
    private Long firmID;

    private List<String> allInterviewStages = Arrays.asList(
            "One-on-one",
            "Group",
            "Phone",
            "Technical");

    HrCommandHandler(UserCredentials hrUser) {

        this.username = hrUser.getUserName();
        this.firmID = hrUser.getFirmId();

    }

    public void handleCommands() {
        GUI.hrForm(this);
    }

    public List<String> getAllInterviewStages() {
        return this.allInterviewStages;
    }

    public String getUsername() {
        return this.username;
    }

    public Long getFirmID() {
        return this.firmID;
    }


    /**
     * This method sets the selected JobApplication to hired.
     * If the JobPosting associated with this JobApplication has filled its required
     * number of positions, it will be closed.
     *
     * @param appID: the ID associated to the JobApplication to be hired
     */
    public void hireApplicationID(String appID) {
        JobApplication inApp = sessionData.jobAppsDb.getItemByID(Long.parseLong(appID));
        JobPosting inJob = sessionData.jobPostingsDb.getItemByID(inApp.getJobID());
        inApp.hire(sessionDate);
        long numHired = inJob.getNumberOfPositions();
        HashMap<JobApplicationDatabase.jobAppFilterKeys, Object> query = new HashMap<>();
        query.put(JobApplicationDatabase.jobAppFilterKeys.JOB_ID, inJob.getJobId());
        for (JobApplication app : this.query.getJobAppsFilter(query).getFilteredJobApps()) {
            if (app.isSuccessful()) {
                numHired--;
            }
        }
        if (numHired == 0) {
            inJob.isFilled(sessionDate);
        }
    }

    /**
     * Reject application by Application ID
     *
     * @param appID id of the application to reject
     */
    public void rejectApplicationID(String appID) {
        sessionData.jobAppsDb.getApplicationByApplicationID(Long.parseLong(appID)).reject(sessionDate);
    }

    public void createJob(
            String title,
            String details,
            Long firmID,
            Long numLabour,
            List<jobTags> hashTags,
            List<String> interviewStages,
            String location,
            List<String> skills,
            List<requiredDocs> docs
    ) {
        DateRange newRange = new DateRange(sessionDate, sessionDate.plusDays(this.JOBLIFESPAN));
        sessionData.jobPostingsDb.addItem(new JobPosting(title, details, sessionData.firmsDb.getItemByID(firmID), numLabour, location, newRange,
                interviewStages, hashTags, skills, docs, sessionData.jobPostingsDb.getCurrID()));
    }

    /**
     * Assign interviewer to application based on IDs
     *
     * @param application  id of application to assign interviewer to
     * @param interviewer  id of user to assign as the interviewer for the application
     */
    public void assignInterviewer(Long application, Long interviewer) {
        sessionData.jobAppsDb.getItemByID(application).setUpInterview(interviewer);
    }


}
