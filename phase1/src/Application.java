import java.time.temporal.Temporal;
import java.util.Date;

public class Application {
    private long applicationID;
    //Do we need applicant ID or applicant object?
    private long applicantID;
    private long jobID;
    private long firmID;

    private int numInterview = 0;
    private long interviewerID;

    //Not sure how to use this
    private boolean recommend;

    private String cvPath;
    private String clPath;

    private Temporal creationDate;
    private Temporal closedDate;

    private boolean isOpen = true;

    public Application(long id){
        this.applicationID = id;
    }

    public Application(long applicationID, long applicantID, long jobID, long firmID){
        this.applicationID = applicationID;
        this.applicantID = applicantID;
        this.jobID = jobID;
        this.firmID = firmID;
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

    public int getNumInterview() {
        return numInterview;
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

    public void setFirmID(long firmID) {
        this.firmID = firmID;
    }

    public void setNumInterview(int numInterview) {
        this.numInterview = numInterview;
    }

    public void setInterviewerID(long interviewerID) {
        this.interviewerID = interviewerID;
    }

    public void setCvPath(String cvPath) {
        this.cvPath = cvPath;
    }

    public void setClPath(String clPath) {
        this.clPath = clPath;
    }

    @Override
    public String toString() {
        return
                " applicationID: " + applicationID +
                ", jobID: " + jobID +
                ", applicantID: " + applicantID +
                ", firmID: " + firmID + "\n"+
                ", number of interviews taken " + numInterview +
                ", the next interviewer has ID " + interviewerID + "\n"+
                ", the path to the CV is " + cvPath +  " and the cover letter path is "+ clPath
                ;
    }

    public Temporal getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Temporal creationDate) {
        this.creationDate = creationDate;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public Temporal getClosedDate() {
        return closedDate;
    }

    public void setClosedDate(Temporal closeDate) {
        this.closedDate = closeDate;
    }

    public Object getResume() {
        // to be discussed
        return null;
    }

    public Object getCoverLetter() {
        // to be discussed
        return null;
    }

    public void setUpInterview(String targetInterviewerId) {
        // to be discussed
    }
}