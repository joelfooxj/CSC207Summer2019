package Model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
    public void addApplication(long applicantID, long jobID, long firmID, LocalDate creationDate){
        JobApplication newJobApplication = new JobApplication(super.getCurrID(), applicantID, jobID, firmID, creationDate);
        newJobApplication.setApplicantID(applicantID);
        newJobApplication.setJobID(jobID);
        super.addItem(newJobApplication);
    }

    /**
     *
     * @param filter: the filter requirement of the job application
     * @param id: the id of the filter
     * @return a list of applications which satisfy this requirement
     */
    public List<JobApplication> getFilteredApplications(String filter, long id){
        List<JobApplication> jobApplicationList = new ArrayList<JobApplication>();
        for (Long i = 0L; i<super.getCurrID();i++) {
            JobApplication item = super.getItemByID(i);
            if (!jobApplicationList.contains(item)) {
                jobApplicationList.add(item);
            } else if (!item.passFilter(filter, id)) {
                jobApplicationList.remove(item);
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
            for (Long i = 0L; i<super.getCurrID();i++) {
                JobApplication item = super.getItemByID(i);
                if (!jobApplicationList.contains(item)) {
                    jobApplicationList.add(item);
                } else if (!item.passFilter(filter, filters.get(filter))) {
                    jobApplicationList.remove(item);
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
        for(Long i = 0L; i < super.getCurrID();i++){
            JobApplication item = super.getItemByID(i);
            if (item.getApplicantID() == applicantID){
                jobApplicationList.add(item);
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
        for(Long i = 0L; i<super.getCurrID();i++){
            JobApplication item = super.getItemByID(i);
            if (item.getInterviewerID() == interviewerID){
                jobApplicationList.add(item);
            }
        }
        return jobApplicationList;
    }

    // Print a list of applications with the same application id and firm id
    public void printApplicationsByApplicantID(long applicationId, long firmId){
        for (Long i = 0L; i<super.getCurrID();i++){
            JobApplication item = super.getItemByID(i);
            if (item.getApplicantID() == applicationId && item.getFirmID()==firmId){
                System.out.println(item.toString());
            }
        }
    }

    // prints applications that are not closed (filled/expired)
    // this will be called when the HR wants to set up an interview
    // An application is open when it is open and the person hasn't been hired yet
    public void printOpenApplicationsByJobID(long jobID) {
        StringBuilder allApplication = new StringBuilder();
        for (Long i = 0L;i <super.getCurrID();i++){
            JobApplication item = super.getItemByID(i);
            if (item.getJobID() == jobID && item.isOpen() && !item.isSuccessful())
                allApplication.append(item.toString() +"; ");
        }
        System.out.println(allApplication);
    }

    // I check isOpen() and isHired().  Note if the hire() method is called, isOpen() will be set to false
    public List<Long> getOpenAppIdsByJob(Long jobId) {
        List<Long> idList = new ArrayList<>();
        for (JobApplication app : this.getAllApplications()) {
            if (app.isOpen() && app.getJobID() == jobId) {
                idList.add(app.getApplicationID());
            }
        }
        return idList;
    }

    // print all the applications in the application database
    public List<JobApplication> getAllApplications(){
        List<JobApplication> jobApplicationList = new ArrayList<>();
        for (Long i = 0L; i<super.getCurrID();i++){
            jobApplicationList.add(data.get(i));
        }
        return jobApplicationList;
    }
}