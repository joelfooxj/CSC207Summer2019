package Control.Queries;

import Model.JobApplication;
import Model.requiredDocs;

import java.util.ArrayList;
import java.util.List;

public class JobApplicationQuery {
    private List<JobApplication> filteredJobApps;
    JobApplicationQuery(List<JobApplication> filteredJobApps){
        this.filteredJobApps = filteredJobApps;
    }

    public List<String> getApplicantIDs(){
        if (filteredJobApps.size() < 1){
            return null;
        }
        List<String> applicantsIDs = new ArrayList<String>();
        for (JobApplication jobApp: this.filteredJobApps){
            applicantsIDs.add(String.valueOf(jobApp.getApplicantID()));
        }
        return applicantsIDs;
    }


    public List<String> getJobAppsID(){
        if (filteredJobApps.size() < 1){
            return null;
        }
        List<String> applicantionsIDs = new ArrayList<String>();
        for (JobApplication jobApp: this.filteredJobApps){
            applicantionsIDs.add(String.valueOf(jobApp.getApplicationID()));
        }
        return applicantionsIDs;



    }


    public String getRefLetters(){
        if (filteredJobApps.size()!= 1){
            return null;
        }
        StringBuilder referenceLetters = new StringBuilder();
        for (String referenceLetter: filteredJobApps.get(0).getReferenceLetters()){
            referenceLetters.append(referenceLetter);
            referenceLetters.append("\n");
        }

        return referenceLetters.toString();
    }

    public String getResume(){
        if (filteredJobApps.size()!= 1){
            return null;
        }
        return filteredJobApps.get(0).getCV();
    }

    public String getCoverLetter(){
        if (filteredJobApps.size()!= 1){
            return null;
        }
        return filteredJobApps.get(0).getCoverLetter();
    }

    public String getRepresentation(){
        if (filteredJobApps.size()!= 1){
            return null;
        }
        return filteredJobApps.get(0).toString();
    }

    public List<String> getRequiredDocuments(){
        if (filteredJobApps.size()!= 1){
            return null;
        }
        List<requiredDocs> inDocs = filteredJobApps.get(0).getRequiredDocs();
        List<String> requiredDocuments = new ArrayList<>();
        if (inDocs.contains(requiredDocs.COVERLETTER)){
            requiredDocuments.add("Cover Letter");
        }
        if(inDocs.contains(requiredDocs.CV)){
            requiredDocuments.add("CV");
        }
        if(inDocs.contains(requiredDocs.REFERENCELETTERS)){
            requiredDocuments.add("Reference Letters");
        }
        return requiredDocuments;
    }

    public List<JobApplication> getFilteredJobApps() {
        return filteredJobApps;
    }

    public String getPrintout() {
        String ret = "";
        for (JobApplication application : this.filteredJobApps) {
            ret = ret + application.toString();
        }
        return ret;
    }
}
