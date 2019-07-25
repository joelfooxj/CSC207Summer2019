package Databases;
import CommandHandlers.UserInterface;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class JobApplication extends Observable implements Serializable {
    private long applicationID;
    private long applicantID;
    private long jobID;
    private long firmID;

    private String applicantName = String.valueOf(applicantID);
    private String firmName = String.valueOf(firmID);
    private String jobName = String.valueOf(jobID);

    private long interviewerID;
    private int passedInterviewNum;

    private String CV;
    private String coverLetter;

    private LocalDate creationDate;
    private LocalDate closedDate;

    // The application is closed in the following conditions
    // 1. its applicant is hired
    // 2. it is manually set to closed
    private boolean isOpen = true;
    private boolean isSuccessful = false;
    private boolean isRejected = false;

    //dynamic interview process
    private List<String> interviewProcess;
    private List<String> leftInterviewProcess;

    // for each application creation, you need all these 5 parameters
    /**
     *
     * @param applicationID: The Application's unique ID
     * @param applicantID: The applicant's unique ID
     * @param jobID: The unique ID of the job.
     * @param firmID: The unique ID of the company
     * @param creationDate: The creation date of this application
     */
    public JobApplication(long applicationID, long applicantID, long jobID, long firmID, LocalDate creationDate){
        this.applicationID = applicationID;
        this.applicantID = applicantID;
        this.jobID = jobID;
        this.firmID = firmID;
        this.creationDate = creationDate;
    }
    // The followings are necessarily used getter and setters

    /**
    * @param filter: The filtration requirements, e.g. filter by firm id: "firm id"
     * @param id: the id of the filtration parameter
     */
    public boolean passFilter(String filter, long id){
        if (filter.equals("application id")){
            return id == this.getApplicationID();
        }
        else if (filter.equals("applicant id")){
            return id == this.getApplicantID();
        }
        else if (filter.equals("firm id")){
            return id == this.getFirmID();
        }
        else if (filter.equals("job id")){
            return id == this.getJobID();
        }
        else {return false;}
    }

    public long getApplicationID() {
        return applicationID;
    }

    public long getJobID() {
        return jobID;
    }

    public long getApplicantID() {
        return applicantID;
    }

    public long getFirmID() {
        return firmID;
    }

    public long getInterviewerID() {
        return interviewerID;
    }

    public void setJobID(long jobID) {
        this.jobID = jobID;
    }

    public void setApplicantID(long applicantID) {
        this.applicantID = applicantID;
    }

    // Everytime an interviewer recommend this application,
    // the applicant pass 1 more round(1 phone interview + 3 in-person interviews),
    // and the number of passed interview increases by 1
    public void recommend(){
        leftInterviewProcess.remove(interviewProcess.get(passedInterviewNum));
        this.passedInterviewNum ++;
        if (leftInterviewProcess.isEmpty()) isSuccessful=true;
    }

    // The input is a list of interviews. e.g. List["phone", "written test ", "in-person","in-person"]

    /**
     *
     * @param interviewProcess: a list of interview processes
     */
    public void createInterviewProcess(List<String> interviewProcess){
        this.interviewProcess = interviewProcess;
        this.leftInterviewProcess = interviewProcess;
    }

    // Note the unique application number is inside a square bracket
    @Override
    public String toString() {
        return
                "[applicationID]: "+ applicationID +
                "\n[job]: " + jobName +
                "\n[applicant]: " + applicantName +
                "\n[firm]: " + firmName +
                        "\n[interviewer ID]: "+ interviewerID;
    }

    public boolean isOpen() {
        return isOpen;
    }

    /**
     *
     * @param applicantName: The real name of the applicant, default value is its id
     * @param firmName the real name of the company, default value is its id
     * @param jobName the real name of the job, default value is its id
     */
    public void setNames(String applicantName, String firmName, String jobName){
        this.applicantName = applicantName;
        this.firmName = firmName;
        this.jobName = jobName;
    }
    /**
     *
     * @param open: a boolean indicating if the job corresponding to this application is still open
     */
    public void setOpen(boolean open) {
        isOpen = open;
        if (!open){
            setClosedDate(UserInterface.getDate());
        }
    }

    public LocalDate getClosedDate() {
        return closedDate;
    }

    /**
     *
     * @param closeDate: the close date of the job corresponding to this application
     */
    public void setClosedDate(LocalDate closeDate) {
        this.closedDate = closeDate;
    }

    // Based on the amount of passed interview, the applicant's application can have 5
    // status, which are no interview assigned, pass phone interview, pass 1/2 interview
    // If he passes all 4, the status is "in consideration"
    public String status(){
        if (this.isSuccessful) {
            return "This applicant of this application has been hired.";
        } else if (this.isRejected) {
            return "This application has been rejected.";
        }
        else{
            StringBuilder followingInterviews = new StringBuilder();
            for (int i = 0; i<leftInterviewProcess.size();i++){
                followingInterviews.append(leftInterviewProcess.get(i));
            }
            String result = "You have passed "+passedInterviewNum+"interviews"
            + "Your following interviews are "+ followingInterviews;
            return result;
        }
    }

    public String getNextInterview(){
        return leftInterviewProcess.get(0);
    }

    public void removeInterviewer(Long interviewerID){
        this.interviewerID = 0L;
    }

    // For phase 2, we will have specific resumes and cover letters. For now, we only
    // have the path to resume and cover letter.
    public Object getResume() {
        return null;
    }

    /**
     *
     * @param targetInterviewerId: the id of the interviewer who is going to view this application/ applicant
     */
    public void setUpInterview(long targetInterviewerId) {
        this.interviewerID = targetInterviewerId;
    }

    public boolean isSuccessful(){
        return this.isSuccessful;
    }

    // When an HR decides to hire this person, 3 variables(status) will be changed.
    // the application gets closed and the close date is right now.

    /**
     *
     * @param date: the date when HR decides to hire this person
     */
    public void hire(LocalDate date) {
        this.isSuccessful = true;
        this.isOpen = false;
        this.closedDate = date;
        setChanged();
        notifyObservers("Your application is selected by HR. And you are already hired. You are welcome!");
    }

    // Similar with the hire() method, 3 variables will be changed.

    /**
     *
     * @param date: the date when HR/ interviewer rejects the applicant of this application
     */
    public void reject(LocalDate date) {
        this.isRejected = true;
        this.isOpen = false;
        this.closedDate = date;
        setChanged();
        notifyObservers("Your application to our company has been kindly rejected.");
    }

    public String getCV(){
        return this.CV;
    }

    /**
     *
     * @param inCV: the resume/ CV of this application
     */
    public void setCV(String inCV){
        this.CV = inCV;
    }

    public String getCoverLetter(){
        return this.coverLetter;
    }

    /**
     *
     * @param inCoverLetter: the cover letter of this application
     */
    public void setCoverLetter(String inCoverLetter){
        this.coverLetter = inCoverLetter;
    }

}