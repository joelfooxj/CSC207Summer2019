package Control.Queries;

import Model.JobPosting;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

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

    public String getRepresentation(){
        if (filteredJobPosts.size() != 1){
            return null;
        }
        return filteredJobPosts.get(0).toString();
    }

    public String getRepresentations(){
        StringBuilder repr = new StringBuilder();
        for (JobPosting job: this.filteredJobPosts){
            repr.append(job.toString());
            repr.append("\n");
        }
        return repr.toString();
    }

    public List<String> getRepresentationsList(){
        List<String> reprList = new ArrayList<>();
        for (JobPosting job: this.filteredJobPosts){
            reprList.add(job.toString());
        }
        return reprList;
    }


    public List<String> getLocationList(){
        HashSet<String> locationSet = new HashSet<>();
        for (JobPosting job: filteredJobPosts){
            locationSet.add(job.getLocation());
        }
        return new ArrayList<>(locationSet);
    }

    public void applyHashtagFilter(HashSet<String> tags){
        List<JobPosting> jobPostList = this.filteredJobPosts;
        this.filteredJobPosts = jobPostList.stream().filter(jobPosting ->
                jobPosting.containsAllHashTags(tags)).collect(Collectors.toList());


    }
}
