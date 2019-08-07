package Model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 *
 */
public class JobApplication extends Observable implements Serializable, Observer {
    private Long applicationID;
    private Long interviewerID;
    private JobPosting jobPosting;
    private UserCredentials user;

    private List<Model.requiredDocs> setDocs;
    private String CV;
    private String coverLetter;
    private List<String> referenceLetters = new ArrayList<>();

    private LocalDate closedDate;

    // The application is closed in the following conditions
    // 1. its applicant is hired
    // 2. it is manually set to closed
    private boolean isOpen = true;
    private boolean isSuccessful = false;
    private boolean isRejected = false;

    //dynamic interview process
    private List<String> leftInterviewProcess;

    /**
     * @param applicationID: The Application's unique ID
     * @param user:          The user
     * @param jobPosting:    The job being applied to
     */
    public JobApplication(long applicationID, UserCredentials user, JobPosting jobPosting) {
        this.applicationID = applicationID;
        this.user = user;
        this.jobPosting = jobPosting;
        this.setDocs = jobPosting.getRequiredDocs();
    }

    public String listString() {
        return "[" + this.applicationID + "] " + user.getUserName() + " application for " + this.jobPosting.listString();
    }

    public UserCredentials getUser() {
        return this.user;
    }

    // The followings are necessarily used getter and setters
    public Long getApplicationID() {
        return applicationID;
    }


    public Long getJobID() {
        return this.jobPosting.getJobId();
    }

    public Long getApplicantID() {
        return this.user.getUserID();
    }

    public Long getFirmID() {
        return this.jobPosting.getFirmId();
    }

    public Long getInterviewerID() {
        return interviewerID;
    }



    /**
     * Everytime an interviewer recommend this application,
     * the applicant pass 1 more round(1 phone interview + 3 in-person interviews),
     * and the number of passed interview increases by 1
     */
    public void recommend() {
        leftInterviewProcess.remove(0);
        if (leftInterviewProcess.isEmpty()) isSuccessful = true;
        this.interviewerID = null;
    }

    /**
     * @param interviewProcess: a list of interview processes
     */
    public void createInterviewProcess(List<String> interviewProcess) {
        this.leftInterviewProcess = interviewProcess;
    }

    @Override
    public String toString() {
        String interviwerVal = "No Interview invitation";
        if (this.interviewerID != null) {
            interviwerVal = String.valueOf(this.interviewerID);
        }
        return
                "[applicationID]: " + applicationID +
                        "\n[Job ID]: " + this.jobPosting.getJobId() +
                        "\n[Applicant ID]: " + this.user.getUserID() +
                        "\n[Firm]: " + this.jobPosting.getFirmId() +
                        "\n[interviewer ID]: " + interviwerVal +
                        "\n[Interviews Left]: " + this.leftInterviewProcess;
    }

    public boolean isOpen() {
        return isOpen;
    }


    /**
     * @param open: a boolean indicating if the job corresponding to this application is still open
     */
    public void setOpen(boolean open, LocalDate sessionDate) {
        isOpen = open;
        if (!open) {
            setClosedDate(sessionDate);
        }
    }

    public LocalDate getClosedDate() {
        return closedDate;
    }

    /**
     * @param closeDate: the close date of the job corresponding to this application
     */
    public void setClosedDate(LocalDate closeDate) {
        this.closedDate = closeDate;
    }

    /**
     * @param targetInterviewerId: the id of the interviewer who is going to view this application/ applicant
     */
    public void setUpInterview(long targetInterviewerId) {
        this.interviewerID = targetInterviewerId;
    }

    public boolean isSuccessful() {
        return this.isSuccessful;
    }



    /**
     * When an HR decides to hire this person, 3 variables(status) will be changed.
     * the application gets closed and the close date is right now.
     * @param date: the date when HR decides to hire this person
     */
    public void hire(LocalDate date) {
        this.isSuccessful = true;
        this.isOpen = false;
        this.closedDate = date;
        setChanged();
        List argument = new ArrayList();
        argument.add("hire");
        argument.add(this.getApplicantID());
        argument.add(this.getFirmID());
        notifyObservers(argument);
        this.interviewerID = null;
    }

    /**
     * @param date: the date when HR/ interviewer rejects the applicant of this application
     */
    public void reject(LocalDate date) {
        this.isRejected = true;
        this.isOpen = false;
        this.closedDate = date;
        setChanged();
        List argument = new ArrayList();
        argument.add("reject");
        argument.add(this.getApplicantID());
        argument.add(this.getFirmID());
        notifyObservers(argument);
        this.interviewerID = null;
    }

    public String getCV() {
        if (this.setDocs.contains(Model.requiredDocs.CV)) {
            return this.CV;
        } else {
            return "CV not required.";
        }
    }

    /**
     * @param inCV: the resume/ CV of this application
     */
    public void setCV(String inCV) {
        this.CV = inCV;
    }

    public String getCoverLetter() {
        if (this.setDocs.contains(Model.requiredDocs.COVERLETTER)) {
            return this.coverLetter;
        } else {
            return "Cover letter not required";
        }
    }

    /**
     * @param inCoverLetter: the cover letter of this application
     */
    public void setCoverLetter(String inCoverLetter) {
        this.coverLetter = inCoverLetter;
    }

    /**
     * @param referenceLetter: the reference letter to be added
     */
    public void addReferenceLetter(String referenceLetter) {
        this.referenceLetters.add(referenceLetter);
    }

    /**
     * @return reference letters for this application
     */
    public List<String> getReferenceLetters() {
        return this.referenceLetters;
    }

    public List<requiredDocs> getRequiredDocs() {
        return this.setDocs;
    }

    public void update(Observable o, Object arg) {
        List arguments = (List) arg;
        if (arguments.get(0).equals("Closed") && this.isOpen()) {
            reject((LocalDate) arguments.get(1));
        }
    }
}