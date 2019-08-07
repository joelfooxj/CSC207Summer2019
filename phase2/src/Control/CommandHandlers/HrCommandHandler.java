package Control.CommandHandlers;

import Control.DateRange;
import Model.*;
import View.GUI;

import java.util.*;

public class HrCommandHandler extends CommandHandler {

    private final int JOBLIFESPAN = 30;
    private String username;
    private String firmID;
    private List<String> allInterviewStages = Arrays.asList(
            "One-on-one",
            "Group",
            "Phone",
            "Technical");

    HrCommandHandler(UserCredentials hrUser) {

        this.username = hrUser.getUserName();
        this.firmID = String.valueOf(hrUser.getFirmId());

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

    public String getFirmID() {
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
        for (JobApplication app : filter.getJobAppsFilter(query).getFilteredJobApps()) {
            if (app.isSuccessful()) {
                numHired--;
            }
        }
        if (numHired == 0) {
            inJob.isFilled(sessionDate);
        }
    }

    public void rejectApplicationID(String appID) {
        sessionData.jobAppsDb.getApplicationByApplicationID(Long.parseLong(appID)).reject(sessionDate);
    }

    public void createJob(
            String title,
            String details,
            String firmID,
            Long numLabour,
            List<jobTags> hashTags,
            List<String> interviewStages,
            String location,
            List<String> skills,
            List<requiredDocs> docs
    ) {
        DateRange newRange = new DateRange(sessionDate, sessionDate.plusDays(this.JOBLIFESPAN));
        sessionData.jobPostingsDb.addItem(new JobPosting(title, details, sessionData.firmsDb.getItemByID(Long.parseLong(firmID)), numLabour, location, newRange,
                     interviewStages, hashTags, skills, docs, sessionData.jobPostingsDb.getCurrID()));
    }

    public void assignInterviewer(Long application, Long interviewer) {
        sessionData.jobAppsDb.getItemByID(application).setUpInterview(interviewer);
    }


}
