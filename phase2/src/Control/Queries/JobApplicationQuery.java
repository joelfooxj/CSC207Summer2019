package Control.Queries;

import Model.JobApplicationPackage.JobApplication;
import Model.requiredDocs;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class JobApplicationQuery {

    /**
     * Class provides information from filtered lists of Job Applications to forms in the View Package
     */

    private List<JobApplication> filteredJobApps;

    JobApplicationQuery(List<JobApplication> filteredJobApps) {
        this.filteredJobApps = filteredJobApps;
    }

    /**
     * @return a list containing job post identifiers
     */
    public List<Long> getJobIDs() {
        List<Long> jobIDs = new ArrayList<>();
        for (JobApplication jobApplication : this.filteredJobApps) {
            jobIDs.add(jobApplication.getJobID());
        }
        return jobIDs;
    }

    /**
     * @return the identifier of the job selected
     */
    public Long getJobID() {
        if (filteredJobApps.size() != 1) {
            return null;
        }
        return this.filteredJobApps.get(0).getJobID();
    }


    /**
     * @return A String that contains all the reference letters given for this application
     */
    public String getRefLetters() {
        if (filteredJobApps.size() != 1) {
            return null;
        }
        StringBuilder referenceLetters = new StringBuilder();
        for (String referenceLetter : filteredJobApps.get(0).getReferenceLetters()) {
            referenceLetters.append(referenceLetter);
            referenceLetters.append("\n");
        }

        return referenceLetters.toString();
    }

    /**
     * @return A string representation of the resume submitted in the application
     */
    public String getResume() {
        if (filteredJobApps.size() != 1) {
            return null;
        }
        return filteredJobApps.get(0).getCV();
    }

    /**
     * @return A string representation of the coverletter submitted in the application
     */
    public String getCoverLetter() {
        if (filteredJobApps.size() != 1) {
            return null;
        }
        return filteredJobApps.get(0).getCoverLetter();
    }

    /**
     * @return A representation of the selected job application
     */
    public String getRepresentation() {
        if (filteredJobApps.size() != 1) {
            return null;
        }
        return filteredJobApps.get(0).toString();
    }

    /**
     * @return a list of required documents in a job application
     */
    public List<requiredDocs> getRequiredDocuments() {
        if (filteredJobApps.size() != 1) {
            return null;
        }
        return filteredJobApps.get(0).getRequiredDocs();
    }

    public List<JobApplication> getFilteredJobApps() {
        return filteredJobApps;
    }

    /**
     * @return a string representation of all the applications selected
     */
    public String getRepresentations() {
        StringBuilder printout = new StringBuilder();
        for (JobApplication application : this.filteredJobApps) {
            printout.append(application.toString());
            printout.append("\n---------\n");
        }
        return printout.toString();
    }

    /**
     * @return a list containing the representation of all the job applications selected
     */
    public List<String> getListStrings() {
        List<String> listStrings = new ArrayList<>();
        for (JobApplication application : this.filteredJobApps) {
            listStrings.add(application.listString());
        }
        return listStrings;
    }

    public static String parseListString(String listString) {
        return listString.substring(1, listString.indexOf("]"));
    }

    /**
     * @return a list containing the representation of all the applicants in the job applications selected
     */
    public List<String> getApplicantStrings() {
        List<String> applicantList = new ArrayList<>();
        for (JobApplication application : this.filteredJobApps) {
            applicantList.add(application.getUser().toString());
        }
        return applicantList;
    }
}
