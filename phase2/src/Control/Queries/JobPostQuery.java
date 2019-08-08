package Control.Queries;

import Model.JobPostingPackage.JobPosting;
import Model.JobPostingPackage.jobTags;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class JobPostQuery {

    /**
     * Class provides information from filtered lists of Job Postings to forms in the View Package
     */

    private List<JobPosting> filteredJobPosts;

    JobPostQuery(List<JobPosting> filteredJobApps) {
        this.filteredJobPosts = filteredJobApps;
    }

    /**
     * @return A string representation of the selected job
     */
    public String getRepresentation() {
        if (filteredJobPosts.size() != 1) {
            return null;
        }
        return filteredJobPosts.get(0).toString();
    }

    /**
     * @return a string representation of all the selected jobs
     */
    public String getRepresentations() {
        StringBuilder repr = new StringBuilder();
        for (JobPosting job : this.filteredJobPosts) {
            repr.append(job.toString());
            repr.append("\n----------\n");
        }
        return repr.toString();
    }

    /**
     * @return A list of string containing the unique set of locations in the filtered jobs
     */
    public List<String> getLocationList() {
        HashSet<String> locationSet = new HashSet<>();
        for (JobPosting job : filteredJobPosts) {
            locationSet.add(job.getLocation());
        }
        return new ArrayList<>(locationSet);
    }

    /** filters out the jobs that don't contain any of the tags provided
     * @param tags
     */
    public void applyHashtagFilter(HashSet<jobTags> tags) {
        List<JobPosting> jobPostList = this.filteredJobPosts;
        jobPostList = jobPostList.stream().filter(jobPosting ->
                jobPosting.containsAllHashTags(tags)).collect(Collectors.toList());
        this.filteredJobPosts = jobPostList;
    }

    /** filters out the jobs that don't who's ID is not provided in the list JobIDs
     * @param jobIDs list of jobs to keep
     */
    public void filterJobsByJobIDs(List<Long> jobIDs) {
        List<JobPosting> jobPostList = this.filteredJobPosts;
        jobPostList = jobPostList.stream().filter(jobPosting ->
                !jobIDs.contains(jobPosting.getJobId())).collect(Collectors.toList());
        this.filteredJobPosts = jobPostList;
    }

    /**
     * @return a list of strings of the job posts representation
     */
    public List<String> getRepresentationsList() {
        List<String> listString = new ArrayList<>();
        for (JobPosting jobPosting : this.filteredJobPosts) {
            listString.add(jobPosting.listString());
        }
        return listString;
    }

    public static String parseListString(String listString) {
        return listString.substring(1, listString.indexOf("]"));
    }
}
