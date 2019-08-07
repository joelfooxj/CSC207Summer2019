package Control.Queries;

import Model.JobApplicationPackage.JobApplication;
import Model.requiredDocs;

import java.util.ArrayList;
import java.util.List;

public class JobApplicationQuery {
    private List<JobApplication> filteredJobApps;

    JobApplicationQuery(List<JobApplication> filteredJobApps) {
        this.filteredJobApps = filteredJobApps;
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
        StringBuilder printout = new StringBuilder();
        for (JobApplication application : this.filteredJobApps) {
            printout.append(application.toString());
            printout.append("\n---------\n");
        }
        return printout.toString();
    }

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

    public List<String> getApplicantStrings() {
        List<String> applicantList = new ArrayList<>();
        for (JobApplication application : this.filteredJobApps) {
            applicantList.add(application.getUser().toString());
        }
        return applicantList;
    }
}
