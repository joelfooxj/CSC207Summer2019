package Model.JobApplicationPackage;

import Model.JobPostingPackage.JobPosting;
import Model.TemplateDatabase;
import Model.UserCredentialsPackage.UserCredentials;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class JobApplicationDatabase extends TemplateDatabase<JobApplication> {
    public JobApplicationDatabase() {
    }

    /**
     * @param user:       the user making the application
     * @param jobPosting: the jobPosting being applied to
     */
    public JobApplication addApplication(UserCredentials user,
                                         JobPosting jobPosting) {
        JobApplication newJobApplication = new JobApplication(super.getCurrID(), user, jobPosting);
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
        LIST_STRING,
        APPLICANT_REPR
    }

    /**
     * Returns a list of job applications that meet the criteria of filters and their values contained
     * in the HashMap. For list of filters see enum jobAppFilterKeys.
     *
     * @param filtration - HashMap containing filters that are being applied
     * @return
     */

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
            applicationList = applicationList.stream().filter(app -> filtration.get(jobAppFilterKeys.OPEN).equals
                    (app.isOpen())).collect(Collectors.toList());
        }
        if (filtration.containsKey(jobAppFilterKeys.INTERVIEWER_ID)) {
            applicationList = applicationList.stream().filter(app -> filtration.get(jobAppFilterKeys.INTERVIEWER_ID).
                    equals(app.getInterviewerID())).collect(Collectors.toList());
        }
        if (filtration.containsKey(jobAppFilterKeys.LIST_STRING)) {
            applicationList = applicationList.stream().filter(application -> filtration.get(jobAppFilterKeys
                    .LIST_STRING).equals(application.listString())).collect(Collectors.toList());
        }
        if (filtration.containsKey(jobAppFilterKeys.APPLICANT_REPR)) {
            applicationList = applicationList.stream().filter(application -> filtration.get(jobAppFilterKeys
                    .APPLICANT_REPR).equals(application.getUser().toString())).collect(Collectors.toList());
        }
        return applicationList;
    }
}