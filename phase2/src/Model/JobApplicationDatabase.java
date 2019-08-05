package Model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class JobApplicationDatabase extends TemplateDatabase<JobApplication> {
    public JobApplicationDatabase() {
    }

    //Add an application which contains applicant， job， firm and creation date info

    /**
     *
     * @param applicantID: the unique applicant id of this application
     * @param jobID: the unique id of the job
     * @param firmID: the unique id of the company
     * @param creationDate: the creation date of the application
     */
    public JobApplication addApplication(long applicantID, long jobID, long firmID, LocalDate creationDate, List<requiredDocs> docs){
        JobApplication newJobApplication = new JobApplication(super.getCurrID(), applicantID, jobID, firmID, creationDate,
                docs);
        newJobApplication.setApplicantID(applicantID);
        newJobApplication.setJobID(jobID);
        super.addItem(newJobApplication);
        return newJobApplication;
    }

    /**
     *
     * @param filter: the filter requirement of the job application
     * @param id: the id of the filter
     * @return a list of applications which satisfy this requirement
     */
    public List<JobApplication> getFilteredApplications(String filter, long id){
        List<JobApplication> jobApplicationList = new ArrayList<JobApplication>();
        for (JobApplication app: this) {
            if (!jobApplicationList.contains(app)) {
                jobApplicationList.add(app);
            } else if (!app.passFilter(filter, id)) {
                jobApplicationList.remove(app);
            }
        }
        return jobApplicationList;
    }

    /**
     *
     * @param filters: a list of filtration requirements
     * @return a list of job applications which satisfy all the requirements
     */
    public List<JobApplication> getFilteredApplications(HashMap<String, Long> filters){
        List<JobApplication> jobApplicationList = new ArrayList<JobApplication>();
        for (String filter: filters.keySet()){
            for (JobApplication app: this) {
                if (!jobApplicationList.contains(app)) {
                    jobApplicationList.add(app);
                } else if (!app.passFilter(filter, filters.get(filter))) {
                    jobApplicationList.remove(app);
                }
            }
        }
        return jobApplicationList;
    }
    //Since each application ID is unique, this returns 1 application
    // object with this application id
    public JobApplication getApplicationByApplicationID(long applicationID){
        return super.getItemByID(applicationID);
    }

    //Get a list of applications by its applicant ID
    public List<JobApplication> getApplicationsByApplicantID(long applicantID){
        List<JobApplication> jobApplicationList = new ArrayList<JobApplication>();
        for(JobApplication app: this){
            if (app.getApplicantID() == applicantID){
                jobApplicationList.add(app);
            }
        }
//        if (jobApplicationList.isEmpty()) {
//            System.out.println("This applicant does not have any applications submitted");
//        }
        return jobApplicationList;
    }

    //Get a list of applications by its interviewerID
    public List<JobApplication> getApplicationByInterviewerID(long interviewerID){
        List<JobApplication> jobApplicationList = new ArrayList<JobApplication>();
        for(JobApplication app: this){
            if (app.getInterviewerID() == interviewerID){
                jobApplicationList.add(app);
            }
        }
        return jobApplicationList;
    }

    // I check isOpen() and isHired().  Note if the hire() method is called, isOpen() will be set to false
    public List<Long> getOpenApplicationIdsByJob(Long jobId) {
        List<Long> idList = new ArrayList<>();
        for (JobApplication app: this) {
            if (app.isOpen() && app.getJobID() == jobId) {
                idList.add(app.getApplicationID());
            }
        }
        return idList;
    }


    public enum jobAppFilterKeys {
        APPLICATION_ID,
        APPLICANT_ID,
        FIRM_ID,
        JOB_ID,
        OPEN,
    }

    public List<JobApplication> filter(HashMap<jobAppFilterKeys, Object> filtration){
        List<JobApplication> applicationList = this.getListOfItems();

        if (filtration.containsKey(jobAppFilterKeys.APPLICATION_ID)){
            applicationList = applicationList.stream().filter(app -> app.getApplicationID().equals(filtration.get(jobAppFilterKeys.APPLICATION_ID))).collect(Collectors.toList());
        }
        if (filtration.containsKey(jobAppFilterKeys.APPLICANT_ID)){
            applicationList = applicationList.stream().filter(app -> app.getApplicantID().equals(filtration.get(jobAppFilterKeys.APPLICANT_ID))).collect(Collectors.toList());
        }

        if (filtration.containsKey(jobAppFilterKeys.FIRM_ID)){
            applicationList = applicationList.stream().filter(app -> app.getFirmID().equals(filtration.get(jobAppFilterKeys.FIRM_ID))).collect(Collectors.toList());
        }
        if (filtration.containsKey(jobAppFilterKeys.JOB_ID)){
            applicationList = applicationList.stream().filter(app -> app.getJobID().equals(filtration.get(jobAppFilterKeys.JOB_ID))).collect(Collectors.toList());
        }
        if (filtration.containsKey(jobAppFilterKeys.OPEN)){
            applicationList = applicationList.stream().filter(app -> app.isOpen()).collect(Collectors.toList());
        }
        return applicationList;
    }
}