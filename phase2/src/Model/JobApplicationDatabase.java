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
    public JobApplication addApplication(long applicantID,
                                         long jobID,
                                         long firmID,
                                         LocalDate creationDate,
                                         List<requiredDocs> docs){
        JobApplication newJobApplication = new JobApplication(super.getCurrID(), applicantID, jobID, firmID,
                creationDate,
                docs);
        super.addItem(newJobApplication);
        return newJobApplication;
    }

    //Since each application ID is unique, this returns 1 application
    // object with this application id
    public JobApplication getApplicationByApplicationID(long applicationID){
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

    public List<JobApplication> filter(HashMap<jobAppFilterKeys, Object> filtration){
        List<JobApplication> applicationList = this.getListOfItems();

        if (filtration.containsKey(jobAppFilterKeys.APPLICATION_ID)){
            applicationList = applicationList.stream().filter(app -> app.getApplicationID().
                    equals(filtration.get(jobAppFilterKeys.APPLICATION_ID))).collect(Collectors.toList());
        }
        if (filtration.containsKey(jobAppFilterKeys.APPLICANT_ID)){
            applicationList = applicationList.stream().filter(app -> app.getApplicantID().
                    equals(filtration.get(jobAppFilterKeys.APPLICANT_ID))).collect(Collectors.toList());
        }

        if (filtration.containsKey(jobAppFilterKeys.FIRM_ID)){
            applicationList = applicationList.stream().filter(app -> app.getFirmID().
                    equals(filtration.get(jobAppFilterKeys.FIRM_ID))).collect(Collectors.toList());
        }
        if (filtration.containsKey(jobAppFilterKeys.JOB_ID)){
            applicationList = applicationList.stream().filter(app -> app.getJobID().
                    equals(filtration.get(jobAppFilterKeys.JOB_ID))).collect(Collectors.toList());
        }
        if (filtration.containsKey(jobAppFilterKeys.OPEN)){
            applicationList = applicationList.stream().filter(app -> app.isOpen()).collect(Collectors.toList());
        }
        if (filtration.containsKey(jobAppFilterKeys.INTERVIEWER_ID)){
            applicationList = applicationList.stream().filter(app -> app.getInterviewerID().
                    equals(filtration.get(jobAppFilterKeys.INTERVIEWER_ID))).collect(Collectors.toList());
        }
        return applicationList;
    }
}