package Control.Queries;

import Model.JobPosting;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class JobPostQuery {

    private List<JobPosting> filteredJobPosts;
    JobPostQuery(List<JobPosting> filteredJobApps){
        this.filteredJobPosts = filteredJobApps;
    }

    public List<String> getJobIDs(){
        List<String> jobIDs= new ArrayList<String>();
        for (JobPosting job : filteredJobPosts){
            jobIDs.add(job.getJobId().toString());
            System.out.println(job.getJobId());
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

    public List<String> getLocationList(){
        HashSet<String> locationSet = new HashSet<>();
        for (JobPosting job: filteredJobPosts){
            locationSet.add(job.getLocation());
        }
        return new ArrayList<>(locationSet);
    }
}
