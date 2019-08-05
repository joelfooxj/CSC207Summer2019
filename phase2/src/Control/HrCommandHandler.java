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

    private List<JobPosting> getOpenJobs(){
        List<JobPosting> openJobs = new ArrayList<>();
        List<Long> allJobIDs = sessionData.jobPostingsDb.getOpenPostingIds();
        if (!allJobIDs.isEmpty()){
            for(Long jobID:allJobIDs) {
                openJobs.add(sessionData.jobPostingsDb.getJobPostingByID(jobID));
            }
        }
        return openJobs;
    }

    public List<String> getOpenJobsList(){
        List<String> jobsList = new ArrayList<>();
        for(JobPosting job: this.getOpenJobs()){
            jobsList.add(String.valueOf(job.getJobId()));
        }
        return jobsList;
    }

// todo: check if filtering method works
    private List<JobApplication> getJobApplicationsbyJobID(String jobID){
        return sessionData.jobAppsDb.getFilteredApplications("job id", Long.parseLong(jobID));

    }

     //todo: use filter
    public List<String> getApplicationsIDbyJobID(String jobID){
        List<String> appIDsList = new ArrayList<>();
        for (JobApplication app: this.getJobApplicationsbyJobID(jobID)){
            if(app.isOpen()){
                appIDsList.add(String.valueOf(app.getApplicationID()));
            }
        }
        return appIDsList;
    }

    private JobApplication getJobAppByApplicationID(String inAppID){
        return sessionData.jobAppsDb.getFilteredApplications("application id", Long.parseLong(inAppID)).get(0);
    }
     //todo: user filter
    public String getApplicationCVbyApplicationID(String inAppID){
        JobApplication inApp = this.getJobAppByApplicationID(inAppID);
        return inApp.getCV();
    }

     //todo: use filter
    public String getApplicationCoverLetterbyApplicationID(String inAppID){
        JobApplication inApp = this.getJobAppByApplicationID(inAppID);
        return inApp.getCoverLetter();
    }


    // todo: use filter
    public String getApplicationRLsByApplicationID(String inAppID){
        JobApplication inApp = this.getJobAppByApplicationID(inAppID);
        StringBuilder retString = new StringBuilder();
        for (String RL: inApp.getReferenceLetters()){
            retString.append(RL);
            retString.append("\n");
        }
        return retString.toString();
    }

    // todo: use filter
    public List<String> getApplicantIDsByFirmID(){
        List<String> retList = new ArrayList<>();
        Long firmID = Long.parseLong(this.firmID);
        List<UserCredentials> inUsers = sessionData.usersDb.getListOfItems();
        for (UserCredentials user:inUsers){
            if(user.getUserType() == UserCredentials.userTypes.APPLICANT && user.getFirmId() == firmID){
                retList.add(String.valueOf(user.getUserID()));
            }
        }
        return retList;
    }

    // todo: user filter
    public List<String> getApplicationIDsByApplicantID(String inApplicantID){
        List<String> retList = new ArrayList<>();
        List<JobApplication> inJobApps = sessionData.jobAppsDb.getFilteredApplications(
                "applicant id", Long.parseLong(inApplicantID));
        for (JobApplication job: inJobApps){
            retList.add(String.valueOf(job.getApplicationID()));
        }
        return retList;
    }

    public String getApplicationDescByApplicationID(String inJobAppID){
        JobApplication inJobApp = sessionData.jobAppsDb.getApplicationByApplicationID(Long.parseLong(inJobAppID));
        return inJobApp.toString();

    }



    public String getApplicantDescByApplicantID(String inApplicantID){
        UserCredentials inUser = sessionData.usersDb.getUserByID(Long.parseLong(inApplicantID));
        return inUser.toString();
    }


    // todo: use filter
    public String getJobPostingDesc(String jobID){
        JobPosting inJob = sessionData.jobPostingsDb.getJobPostingByID(Long.parseLong(jobID));
        return inJob.getJobDetails();
    }

    //todo: use filter
    public String getJobApplicationPrintout(String appID){
        JobApplication inApp = sessionData.jobAppsDb.getApplicationByApplicationID(Long.parseLong(appID));
        return inApp.toString();
    }


    // todo: use filter
    public List<String> checkJobAppRequiredDocs(String inJobAppID){
        JobApplication inJobApp = this.getJobAppByApplicationID(inJobAppID);
        List<requiredDocs> inDocs = inJobApp.getRequiredDocs();
        List<String> retList = new ArrayList<>();
        if (inDocs.contains(requiredDocs.COVERLETTER)){
            retList.add("Cover Letter");
        }
        if(inDocs.contains(requiredDocs.CV)){
            retList.add("CV");
        }
        if(inDocs.contains(requiredDocs.REFERENCELETTERS)){
            retList.add("Reference Letters");
        }
        return retList;
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
        for(JobApplication app:this.getJobApplicationsbyJobID(String.valueOf(inJob.getJobId()))){
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
