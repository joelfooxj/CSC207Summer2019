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
     * @param applicantID:  the unique applicant id of this application
     * @param jobPosting:   the jobPosting being applied to
     */
    public JobApplication addApplication(long applicantID,
                                         JobPosting jobPosting) {
        JobApplication newJobApplication = new JobApplication(super.getCurrID(), applicantID, jobPosting);
        super.addItem(newJobApplication);
        return newJobApplication;
    }

    //Since each application ID is unique, this returns 1 application
    // object with this application id
    public JobApplication getApplicationByApplicationID(long applicationID) {
        return super.getItemByID(applicationID);
    }

    public enum jobAppFilterKeys {
        APPLICATION_ID,
        APPLICANT_ID,
        FIRM_ID,
        JOB_ID,
        OPEN,
        INTERVIEWER_ID,
    }

    public List<JobApplication> filterJobApps(HashMap<jobAppFilterKeys, Object> filtration) {
        List<JobApplication> applicationList = this.getListOfItems();

        if (filtration.containsKey(jobAppFilterKeys.APPLICATION_ID)) {
            applicationList = applicationList.stream().filter(app -> app.getApplicationID().
                    equals(filtration.get(jobAppFilterKeys.APPLICATION_ID))).collect(Collectors.toList());
        }
        if (filtration.containsKey(jobAppFilterKeys.APPLICANT_ID)) {
            applicationList = applicationList.stream().filter(app -> app.getApplicantID().
                    equals(filtration.get(jobAppFilterKeys.APPLICANT_ID))).collect(Collectors.toList());
        }

        if (filtration.containsKey(jobAppFilterKeys.FIRM_ID)) {
            applicationList = applicationList.stream().filter(app -> app.getFirmID().
                    equals(filtration.get(jobAppFilterKeys.FIRM_ID))).collect(Collectors.toList());
        }
        if (filtration.containsKey(jobAppFilterKeys.JOB_ID)) {
            applicationList = applicationList.stream().filter(app -> app.getJobID().
                    equals(filtration.get(jobAppFilterKeys.JOB_ID))).collect(Collectors.toList());
        }
        if (filtration.containsKey(jobAppFilterKeys.OPEN)) {
            applicationList = applicationList.stream().filter(app -> filtration.get(jobAppFilterKeys.OPEN).equals(app.isOpen())).collect(Collectors.toList());
        }
        if (filtration.containsKey(jobAppFilterKeys.INTERVIEWER_ID)) {
            applicationList = applicationList.stream().filter(app -> filtration.get(jobAppFilterKeys.INTERVIEWER_ID).equals(
                    app.getInterviewerID())).collect(Collectors.toList());
        }
        return applicationList;
    }
}