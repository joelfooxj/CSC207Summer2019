package Model;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class JobPostingDatabase extends TemplateDatabase<JobPosting> implements java.io.Serializable {

    /**
     * the date that the user is logged into the program on. i.e. today's date.
     */
    private LocalDate sessionDate;

    /**
     * returns the job post corresponding to job post id
     *
     * @param id  - id of job posting
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

    public List<JobPosting> filterJobPostings(HashMap<jobPostingFilters, Object> filtration) {
        List<JobPosting> jobPostList = this.getListOfItems();
        if (filtration.containsKey(jobPostingFilters.FIRM)) {
            jobPostList = jobPostList.stream().filter(jobPosting -> jobPosting.getFirmId().equals(
                    filtration.get(jobPostingFilters.FIRM))).collect(Collectors.toList());
        }
        if (filtration.containsKey(jobPostingFilters.OPEN)) {
            jobPostList = jobPostList.stream().filter(jobPosting -> filtration.get(jobPostingFilters.OPEN).equals(jobPosting.isOpen(sessionDate))
            ).collect(Collectors.toList());
        }
        if (filtration.containsKey(jobPostingFilters.LOCATION)) {
            jobPostList = jobPostList.stream().filter(jobPosting -> this.checkLocations(filtration.get(jobPostingFilters.LOCATION), jobPosting)).collect(Collectors.toList());
        }
        if (filtration.containsKey(jobPostingFilters.JOB_ID)) {
            jobPostList = jobPostList.stream().filter(jobPosting -> jobPosting.getJobId().equals(filtration.get(jobPostingFilters.JOB_ID))).collect(Collectors.toList());
        }
        if (filtration.containsKey(jobPostingFilters.LIST_STRING)) {
            jobPostList = jobPostList.stream().filter(jobPosting -> filtration.get(jobPostingFilters.LIST_STRING).equals(
                    jobPosting.listString()
            )).collect(Collectors.toList());
        }
        return jobPostList;
    }

    private boolean checkLocations(Object location, JobPosting jobPosting) {
        return (location.equals("All locations") || location.equals(jobPosting.getLocation()));
    }

}