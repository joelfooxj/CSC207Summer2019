import java.time.LocalDate;

public class Application {
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
    // 1. the passed interview Num reaches 4
    // 2. it's hired
    private boolean isOpen = true;
    private boolean isHired;

    // for each application creation, you need all these 5 parameters
    public Application(long applicationID, long applicantID, long jobID, long firmID, LocalDate creationDate){
        this.applicationID = applicationID;
        this.applicantID = applicantID;
        this.jobID = jobID;
        this.firmID = firmID;
        this.creationDate = creationDate;
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

    public void setApplicationID(long applicationID) {
        this.applicationID = applicationID;
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

    public void recommend(){
        this.passedInterviewNum += 1;
    }

    public int getPassedInterviewNum() {
        return passedInterviewNum;
    }

    @Override
    public String toString() {
        return
                "[" + applicationID +"]: "+
                "jobID: " + jobID +
                ", applicantID: " + applicantID +
                ", firmID: " + firmID +
                        "the interviewer ID is "+interviewerID;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public LocalDate getClosedDate() {
        return closedDate;
    }

    public void setClosedDate(LocalDate closeDate) {
        this.closedDate = closeDate;
    }

    public String status(){
        switch (passedInterviewNum){
            case 1: return "Passed Phone Interview";
            case 2: return "Passed 1st in-person interview";
            case 3: return "Passed 2nd in-person interview";
            case 4: return "In consideration";
            default: return "Status Error";
        }
    }

    public Object getResume() {
        return null;
    }

    public Object getCoverLetter() {
        return null;
    }

    public void setUpInterview(long targetInterviewerId) {
        this.interviewerID = targetInterviewerId;
    }

    public void hire(){
        this.isHired = true;
        this.isOpen = false;
    }

    public boolean isHired(){
        return this.isHired;
    }
}