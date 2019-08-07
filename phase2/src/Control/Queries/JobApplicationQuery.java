package Control.Queries;

import Model.JobApplication;
import Model.requiredDocs;

import java.util.ArrayList;
import java.util.List;

public class JobApplicationQuery {
    private List<JobApplication> filteredJobApps;

    JobApplicationQuery(List<JobApplication> filteredJobApps) {
        this.filteredJobApps = filteredJobApps;
    }

    public List<String> getApplicantIDs() {
        List<String> applicantsIDs = new ArrayList<String>();
        for (JobApplication jobApp : this.filteredJobApps) {
            applicantsIDs.add(String.valueOf(jobApp.getApplicantID()));
        }
        return applicantsIDs;
    }


    public List<String> getJobAppsID() {
        List<String> applicantionsIDs = new ArrayList<String>();
        for (JobApplication jobApp : this.filteredJobApps) {
            applicantionsIDs.add(String.valueOf(jobApp.getApplicationID()));
        }
        return applicantionsIDs;
    }

    public List<Long> getJobIDs() {
        List<Long> jobIDs = new ArrayList<>();
        for (JobApplication jobApplication : this.filteredJobApps) {
            jobIDs.add(jobApplication.getJobID());
        }
        return jobIDs;
    }

    public Long getJobID() {
        if (filteredJobApps.size() != 1) {
            return null;
        }
        return this.filteredJobApps.get(0).getJobID();
    }


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

    public String getResume() {
        if (filteredJobApps.size() != 1) {
            return null;
        }
        return filteredJobApps.get(0).getCV();
    }

    public String getCoverLetter() {
        if (filteredJobApps.size() != 1) {
            return null;
        }
        return filteredJobApps.get(0).getCoverLetter();
    }

    public String getRepresentation() {
        if (filteredJobApps.size() != 1) {
            return null;
        }
        return filteredJobApps.get(0).toString();
    }

    public String printOut() {
        String printout = "";
        for (JobApplication application : this.filteredJobApps) {
            printout = printout + application.toString() + "\n----------\n";
        }
        return printout;
    }

    public List<requiredDocs> getRequiredDocuments() {
        if (filteredJobApps.size() != 1) {
            return null;
        }
        return filteredJobApps.get(0).getRequiredDocs();
    }

    public List<JobApplication> getFilteredJobApps() {
        return filteredJobApps;
    }

    public String getPrintout() {
        String ret = "";
        for (JobApplication application : this.filteredJobApps) {
            ret = ret + application.toString() + "\n---------\n";
        }
        return ret;
    }

    public List<String> getListStrings() {
        List<String> listStrings = new ArrayList<>();
        for (JobApplication application : this.filteredJobApps) {
            listStrings.add(application.listString());
        }
        return listStrings;
    }
}
