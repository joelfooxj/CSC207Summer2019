package Control;

import Model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Filter {

    private SessionData sessionData;

    public Filter(SessionData sessionData){
        this.sessionData = sessionData;
    }

    JobApplicationFilter jobApplications(HashMap<JobApplicationDatabase.filterKeys, Long> filter){
        return new JobApplicationFilter(sessionData.jobAppsDb.filter(filter));
    }

    JobPostFilter jobPosts(HashMap<JobPostingDatabase.filterKeys, Long> filter){
        return new JobPostFilter(sessionData.jobPostingsDb.filter(filter));
    }

    UserFilter users(HashMap<UserCredentialsDatabase.filterKeys, String> filter){
        return new UserFilter(sessionData.usersDb.filter(filter));
    }


}

class JobApplicationFilter{
    private List<JobApplication> filteredJobApps;
    JobApplicationFilter(List<JobApplication> filteredJobApps){
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

}

class JobPostFilter{

    private List<JobPosting> filteredJobPosts;
    JobPostFilter(List<JobPosting> filteredJobApps){
        this.filteredJobPosts = filteredJobApps;
    }
    public List<String> getJobIDs(){
        List<String> jobIDs= new ArrayList<String>();
        for (JobPosting job: filteredJobPosts){
            jobIDs.add(String.valueOf(job.getJobId()));
        }
        return jobIDs;
    }

    public String getDescription(){
        if (filteredJobPosts.size()!= 1){
            return null;
        }
        return filteredJobPosts.get(0).getJobDetails();
    }

    public String getRepresentation(){
        if (filteredJobPosts.size()!= 1){
            return null;
        }
        return filteredJobPosts.get(0).toString();
    }
}

class UserFilter{
    private List<UserCredentials> filteredUsers;
    UserFilter(List<UserCredentials> filteredUsers){
        this.filteredUsers = filteredUsers;
    }

    public String getRepresentation(){
        if (filteredUsers.size() != 1){
            return null;
        }
        return filteredUsers.get(0).toString();
    }

}