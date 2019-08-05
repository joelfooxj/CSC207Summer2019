package Model;
import Control.CommandHandler;
import Control.HyreLauncher;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 *
 */
public class JobApplication extends Observable implements Serializable {
    private Long applicationID;
    private Long applicantID;
    private Long jobID;
    private Long firmID;

    private Long interviewerID;
    private int passedInterviewNum;

    private List<Model.requiredDocs> setDocs;
    private String CV;
    private String coverLetter;
    // reference letters
    private List<String> referenceLetters = new ArrayList<>();

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
    public JobApplication(long applicationID, long applicantID, long jobID, long firmID, LocalDate creationDate,
                          List<Model.requiredDocs> docs){
        this.applicationID = applicationID;
        this.applicantID = applicantID;
        this.jobID = jobID;
        this.firmID = firmID;
        this.creationDate = creationDate;
        this.setDocs = docs;
    }
    // The followings are necessarily used getter and setters
    public Long getApplicationID() {
        return applicationID;
    }

    public Long getJobID() {
        return jobID;
    }

    public Long getApplicantID() {
        return applicantID;
    }

    public Long getFirmID() {
        return firmID;
    }

    public Long getInterviewerID() {
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
        String interviwerVal = "No Interview invitation";
        if (this.interviewerID != null){
            interviwerVal = String.valueOf(this.interviewerID);
        }
        return
                "[applicationID]: "+ applicationID +
                "\n[Job ID]: " + this.jobID +
                "\n[Applicant ID]: " + this.applicantID+
                "\n[Firm]: " + this.firmID +
                        "\n[interviewer ID]: "+ interviwerVal;
    }

    public boolean isOpen() {
        return isOpen;
    }


    /**
     *
     * @param open: a boolean indicating if the job corresponding to this application is still open
     */
    public void setOpen(boolean open, LocalDate sessionDate) {
        isOpen = open;
        if (!open){
            setClosedDate(sessionDate);
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
        List argument = new ArrayList();
        argument.add("hire");
        argument.add(this.applicantID);
        argument.add(this.firmID);
        notifyObservers(argument);
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
        List argument = new ArrayList();
        argument.add("reject");
        argument.add(this.applicantID);
        argument.add(this.firmID);
        notifyObservers(argument);
    }

    public String getCV(){
        if (this.setDocs.contains(Model.requiredDocs.CV)){
            return this.CV;
        } else {return "CV not required."; }
    }

    /**
     *
     * @param inCV: the resume/ CV of this application
     */
    public void setCV(String inCV){
        this.CV = inCV;
    }

    public String getCoverLetter(){
        if (this.setDocs.contains(Model.requiredDocs.COVERLETTER)){
            return this.coverLetter;
        } else { return "Cover letter not required"; }
    }

    /**
     *
     * @param inCoverLetter: the cover letter of this application
     */
    public void setCoverLetter(String inCoverLetter){
        this.coverLetter = inCoverLetter;
    }

    /**
     *
     * @param referenceLetter: the reference letter to be added
     */
    public void addReferenceLetter(String referenceLetter) {
        this.referenceLetters.add(referenceLetter);
    }

    /**
     *
     * @return reference letters for this application
     */
    public List<String> getReferenceLetters() {
        if (this.setDocs.contains(Model.requiredDocs.REFERENCELETTERS)){
            return this.referenceLetters;
        } else { return new ArrayList<>(); }
    }

    public List<requiredDocs> getRequiredDocs(){
        return this.setDocs;
    }
}