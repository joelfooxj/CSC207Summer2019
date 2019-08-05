package Control.Queries;

import Model.JobPosting;

import java.util.ArrayList;
import java.util.List;

public class JobPostQuery {

    private List<JobPosting> filteredJobPosts;
    JobPostQuery(List<JobPosting> filteredJobApps){
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
