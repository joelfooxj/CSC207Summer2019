package Control;

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

    private HashMap<String, requiredDocs> stringDocsHashMap = new HashMap<String, requiredDocs>(){
        {
            put("CV", requiredDocs.CV);
            put("Cover Letter", requiredDocs.COVERLETTER);
            put("Reference Letters", requiredDocs.REFERENCELETTERS);
        }
    };

    public HrCommandHandler(UserCredentials hrUser){
        this.username = hrUser.getUserName();
        this.firmID = String.valueOf(hrUser.getFirmId());

    }

    public void handleCommands() {
        GUI.hrForm(this);
    }

    public List<String> getAllInterviewStages(){
        return this.allInterviewStages;
    }

    public String getUsername(){
        return this.username;
    }

    public String getFirmID(){
        return this.firmID;
    }


// todo: check if filtering method works
    private List<JobApplication> getJobApplicationsbyJobID(String jobID){
        return sessionData.jobAppsDb.getFilteredApplications("job id", Long.parseLong(jobID));

    }



    /**
     * This method sets the selected JobApplication to hired.
     * If the JobPosting associated with this JobApplication has filled its required
     * number of positions, it will be closed.
     * @param appID: the ID associated to the JobApplication to be hired
     */
    public void hireApplicationID(String appID){
        JobApplication inApp = sessionData.jobAppsDb.getApplicationByApplicationID(Long.parseLong(appID));
        JobPosting inJob = sessionData.jobPostingsDb.getJobPostingByID(inApp.getJobID());
        sessionData.jobAppsDb.getApplicationByApplicationID(Long.parseLong(appID)).hire(sessionDate);
        long numHired = inJob.getNumberOfPositions();

        HashMap<JobApplicationDatabase.jobAppFilterKeys, Object> query = new HashMap<>();
        query.put(JobApplicationDatabase.jobAppFilterKeys.JOB_ID, inJob.getJobId());

        for(JobApplication app: filter.getJobAppsFilter(query).getFilteredJobApps()){
            if (app.isSuccessful()){
                numHired--;
            }
        }
        if (numHired == 0){
            inJob.isFilled();
        }
    }

    public void rejectApplicationID(String appID){
        sessionData.jobAppsDb.getApplicationByApplicationID(Long.parseLong(appID)).reject(sessionDate);
    }

    // todo: create a job object here instead?
    public void createJob(
            String title,
            String details,
            String firmID,
            Long numLabour,
            List<String> hashTags,
            List<String> interviewStages,
            String location,
            List<String> skills,
            List<String> docs
    ){
        List<requiredDocs> docsList = new ArrayList<>();
        for (String doc: docs) {
            docsList.add(stringDocsHashMap.get(doc));
        }
        DateRange newRange = new DateRange(sessionDate, sessionDate.plusDays(this.JOBLIFESPAN));
        sessionData.jobPostingsDb.addJob(title, details, Long.parseLong(firmID), numLabour, location, newRange,
                hashTags, interviewStages, skills, docsList);
    }

    public void assignInterviewer(JobApplication application, UserCredentials interviewer) {
        application.setUpInterview(interviewer.getUserID());
    }


}
