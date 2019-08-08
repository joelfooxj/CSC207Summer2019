package Model.JobPostingPackage;

import Model.TemplateDatabase;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

public class JobPostingDatabase extends TemplateDatabase<JobPosting> implements java.io.Serializable {

    /**
     * the date that the user is logged into the program on. i.e. today's date.
     */
    private LocalDate sessionDate;

    /**
     * returns the job post corresponding to job post id
     *
     * @param id - id of job posting
     * @return - Job Post
     */
    public JobPosting getJobPostingByID(Long id) {
        return super.getItemByID(id);
    }

    public void updateDb(LocalDate sessionDate) {
        this.sessionDate = sessionDate;
    }

    public enum jobPostingFilters {
        OPEN,
        FIRM,
        LOCATION,
        JOB_ID,
        LIST_STRING
    }

    /**
     * Returns a list of job postings that meet the criteria of filters and their values contained
     * in the HashMap. For list of filters see enum jobPostingFilters.
     *
     * @param filtration - HashMap containing filters that are being applied
     * @return
     */

    public List<JobPosting> filterJobPostings(HashMap<jobPostingFilters, Object> filtration) {
        List<JobPosting> jobPostList = this.getListOfItems();
        if (filtration.containsKey(jobPostingFilters.FIRM)) {
            jobPostList.removeIf(jobPosting -> !jobPosting.getFirmId().equals(filtration.get(jobPostingFilters.FIRM)));
        }
        if (filtration.containsKey(jobPostingFilters.OPEN)) {
            jobPostList.removeIf(jobPosting -> !jobPosting.isOpen(sessionDate));
        }
        if (filtration.containsKey(jobPostingFilters.LOCATION)) {
            jobPostList.removeIf(jobPosting -> !checkLocations(filtration.get(jobPostingFilters.LOCATION), jobPosting));
        }
        if (filtration.containsKey(jobPostingFilters.JOB_ID)) {
            jobPostList.removeIf(jobPosting -> !jobPosting.getJobId().equals(filtration.get(jobPostingFilters.JOB_ID)));
        }
        if (filtration.containsKey(jobPostingFilters.LIST_STRING)) {
            jobPostList.removeIf(jobPosting -> !jobPosting.listString().equals(filtration.get(jobPostingFilters.LIST_STRING)));
        }
        return jobPostList;
    }

    private boolean checkLocations(Object location, JobPosting jobPosting) {
        return (location.equals("All locations") || location.equals(jobPosting.getLocation()));
    }

}