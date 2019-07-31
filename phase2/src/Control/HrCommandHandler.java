package Control;

import Model.JobApplication;
import Model.JobPosting;
import Model.UserCredentials;

import java.time.LocalDate;
import java.util.*;

public class HrCommandHandler implements CommandHandler {

    private final int JOBLIFESPAN = 30;
    private String username;
    private String firmID;
    private List<String> allInterviewStages = Arrays.asList(
            "One-on-one",
            "Group",
            "Phone",
            "Technical");

    public HrCommandHandler(UserCredentials hrUser){
        this.username = hrUser.getUserName();
        this.firmID = String.valueOf(hrUser.getFirmId());
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
        List<Long> allJobIDs = HyreLauncher.getJobsDb().getOpenJobIDs();
        if (!allJobIDs.isEmpty()){
            for(Long jobID:allJobIDs) {
                openJobs.add(HyreLauncher.getJobsDb().getJobPostingByID(jobID));
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

    public List<String> getApplicationsbyJobID(String jobID){
        List<String> appsList = new ArrayList<>();
        for (JobApplication app: HyreLauncher.getAppsDb().getAllApplications()){
            if(app.isOpen() && app.getJobID() == Long.parseLong(jobID)){
                appsList.add(String.valueOf(app.getApplicationID()));
            }
        }
        return appsList;
    }

    public String getJobPostingDesc(String jobID){
        JobPosting inJob = HyreLauncher.getJobsDb().getJobPostingByID(Long.parseLong(jobID));
        return inJob.getJobDetails();
    }

    public String getJobApplicationPrintout(String appID){
        JobApplication inApp = HyreLauncher.getAppsDb().getApplicationByApplicationID(Long.parseLong(appID));
        return inApp.toString();
    }

    // todo: check if total number of jobs in jobsPosting is fulfilled
    // if so - close job
    public void hireApplicationID(String appID){
        JobApplication inApp = HyreLauncher.getAppsDb().getApplicationByApplicationID(Long.parseLong(appID));
        JobPosting inJob = HyreLauncher.getJobsDb().getJobPostingByID(inApp.getJobID());
        HyreLauncher.getAppsDb().getApplicationByApplicationID(Long.parseLong(appID)).hire(HyreLauncher.getDate());

    }

    public void rejectApplicationID(String appID){
        HyreLauncher.getAppsDb().getApplicationByApplicationID(Long.parseLong(appID)).reject(HyreLauncher.getDate());
    }

    public void createJob(
            String title,
            String details,
            String firmID,
            LocalDate todaysDate,
            List<String> hashTags,
            List<String> interviewStages,
            String location
    ){
        DateRange newRange = new DateRange(todaysDate, todaysDate.plusDays(this.JOBLIFESPAN));
        HyreLauncher.getJobsDb().addJob(title, details, Long.parseLong(firmID), newRange,
                hashTags, interviewStages, location);
    }

    // testing the git commit
}
