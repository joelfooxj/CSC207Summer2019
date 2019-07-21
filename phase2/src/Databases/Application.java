package Databases;
import CommandHandlers.UserInterface;
import java.io.Serializable;
import java.time.LocalDate;

public class Application implements Serializable {
    private long applicationID;
    private long applicantID;
    private long jobID;
    private long firmID;

    private long interviewerID;

    private int passedInterviewNum;

    private String cvPath;
    private String clPath;

    private LocalDate creationDate;
    private LocalDate closedDate;

    // The application is closed in the following conditions
    // 1. it's hired
    // 2. it is manually set to closed
    /**
     *
     */
    private boolean isOpen = true;
    private boolean isHired = false;
    private boolean isRejected = false;

    // for each application creation, you need all these 5 parameters

    /**
     *
     * @param applicationID: this is the...
     * @param applicantID
     * @param jobID
     * @param firmID
     * @param creationDate
     */
    public Application(long applicationID, long applicantID, long jobID, long firmID, LocalDate creationDate){
        this.applicationID = applicationID;
        this.applicantID = applicantID;
        this.jobID = jobID;
        this.firmID = firmID;
        this.creationDate = creationDate;
    }

    // The followings are necessarily used getter and setters
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

    public String getCvPath() {
        return cvPath;
    }

    public String getClPath() {
        return clPath;
    }

    public void setJobID(long jobID) {
        this.jobID = jobID;
    }

    public void setApplicantID(long applicantID) {
        this.applicantID = applicantID;
    }

    public void setCvPath(String cvPath) {
        this.cvPath = cvPath;
    }

    public void setClPath(String clPath) {
        this.clPath = clPath;
    }

    // Everytime an interviewer recommend this application,
    // the applicant pass 1 more round(1 phone interview + 3 in-person interviews),
    // and the number of passed interview increases by 1
    public void recommend(){
        this.passedInterviewNum += 1;
    }

    // Note the unique application number is inside a square bracket
    @Override
    public String toString() {
        return
                "[applicationID]: "+ applicationID +
                "\n[jobID]: " + jobID +
                "\n[applicantID]: " + applicantID +
                "\n[firmID]: " + firmID +
                        "\n[interviewer ID]: "+ interviewerID;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
        if (!open){
            setClosedDate(UserInterface.getDate());
        }
    }

    public LocalDate getClosedDate() {
        return closedDate;
    }

    public void setClosedDate(LocalDate closeDate) {
        this.closedDate = closeDate;
    }

    // Based on the amount of passed interview, the applicant's application can have 5
    // status, which are no interview assigned, pass phone interview, pass 1/2 interview
    // If he passes all 4, the status is "in consideration"
    public String status(){
        if (this.isHired) {
            return "This application has been hired.";
        } else if (this.isRejected) {
            return "This application has been rejected.";
        }
        switch (passedInterviewNum){
            case 1: return "Passed Phone Interview";
            case 2: return "Passed 1st in-person interview";
            case 3: return "Passed 2nd in-person interview";
            case 4: return "In consideration";
            default: return "No interviews scheduled yet";
        }
    }

    // For phase 2, we will have specific resumes and cover letters. For now, we only
    // have the path to resume and cover letter.
    public Object getResume() {
        return null;
    }

    public Object getCoverLetter() {
        return null;
    }

    public void setUpInterview(long targetInterviewerId) {
        this.interviewerID = targetInterviewerId;
    }

    // When an HR decides to hire this person, 3 variables(status) will be changed.
    // the application gets closed and the close date is right now.
    public void hire(LocalDate date) {
        this.isHired = true;
        this.isOpen = false;
        this.closedDate = date;
    }

    public boolean isHired(){
        return this.isHired;
    }

    // Similar with the hire() method, 3 variables will be changed.
    public void reject(LocalDate date) {
        this.isRejected = true;
        this.isOpen = false;
        this.closedDate = date;
    }
}