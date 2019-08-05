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

    public String getReferenceLetters(){
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

    public String getDescription(){
        if (filteredJobApps.size()!= 1){
            return null;
        }
        return filteredJobApps.get(0).toString();
    }

    public String getPrintout(){
        if (filteredJobApps.size() < 1){
            return null;
        } else {
            StringBuilder retString = new StringBuilder();
            for (JobApplication jobApp:filteredJobApps){
                retString.append(jobApp);
                retString.append("\n");
            }
            return retString.toString();
        }
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

    public List<Long> getApplicationIDs() {
        ArrayList<Long> appIDs = new ArrayList<>();
        for (JobApplication application : this.filteredJobApps) {
            appIDs.add(application.getApplicationID());
        }
        return appIDs;
    }

    public List<String> getStrings() {
        ArrayList<String> strings = new ArrayList<>();
        for (JobApplication application : this.filteredJobApps) {
            strings.add(application.toString());
        }
        return strings;
    }

}
