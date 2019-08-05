package Model;

import Control.DateRange;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class JobPostingDatabase extends TemplateDatabase<JobPosting> implements java.io.Serializable{

    /**
     * the date that the user is logged into the program on. i.e. today's date.
     */
    private LocalDate sessionDate;

    /**
     * adds a job Posting by construction a job posting
     * @see JobPosting#JobPosting(String, String, long, long, String, DateRange, List, Collection, List, List)
     */
    //TODO change to addJobPosting; use addItem directly
    public void addJob(String title, String details, long firmId, long numsLabourRequired, String location,
                       DateRange jobDateRange, List<String> interviewStages, Collection<String> hashTags,
                       List<String> skillList, List<requiredDocs> docsList){
        addItem(new JobPosting(title, details, firmId, numsLabourRequired, location, jobDateRange, interviewStages,
                hashTags, skillList, docsList, super.getCurrID()));
    }

    /**
     * returns the job post corresponding to job post id
     * @param - id of job posting
     * @return - Job Post
     */
    public JobPosting getJobPostingByID(Long id){
        return super.getItemByID(id);
    }

    /**
     * returns all ids of job postings that are open
     * @return ArrayList of Job Posting ids
     */

    public ArrayList<Long> getOpenPostingIds(){

        ArrayList<Long> result = new ArrayList<>();
        List<JobPosting> openJobPostings = filterOpenJobPostings(this.getListOfItems());

        for (JobPosting jobPosting: openJobPostings){
            result.add(jobPosting.getJobId());
        }
        return result;
    }

    /**
     * Allows the simultaneous application of multiple filters on job postings
     * Filters based on SearchBy keys and Object arguments in HashMap parameter.
     * e.g. if searchMap contains(SearchBy.LOCATION, "Toronto") and (SearchBy.FIRM, 7), then
     * filterBy will filter for all job postings in Toronto by firm 7.
     * @param searchMap
     * values to put in searchMap are as follows:
     * key -> SearchBy.FIRM to filter for jobs by firm id, value -> long of firm id
     * key -> SearchBy.LOCATION filter for jobs by location, value -> String of location
     * key -> SearchBy.HASHTAG to filter by hashtags, value -> HashSet with any hashtags your filtering for
     * key -> SearchBy.OPEN to filter for open jobs, value -> null, no value required
     * @return
     */
    public List<String> filterBy(HashMap<SearchBy,Object> searchMap){

        List<JobPosting> jobPostings = this.getListOfItems();

        if(searchMap.containsKey(SearchBy.FIRM)){
            jobPostings = filterByFirm((Long)searchMap.get(SearchBy.FIRM), jobPostings);
        }

        if(searchMap.containsKey(SearchBy.LOCATION)){
            jobPostings = filterByLocation((String)searchMap.get(SearchBy.LOCATION), jobPostings);
        }

        if(searchMap.containsKey(SearchBy.HASHTAG)){
            jobPostings = getJobsWithHashTags((HashSet<String>) searchMap.get(SearchBy.HASHTAG), jobPostings);
        }

        if(searchMap.containsKey(SearchBy.OPEN)){
            jobPostings = filterOpenJobPostings(jobPostings);
        }

        return print(jobPostings);
    }

    /**
     * Filters ArrayLists of job postings for open postings only
     * @param jobPostings
     * @return
     */
    private List<JobPosting> filterOpenJobPostings(List<JobPosting> jobPostings){

        jobPostings.removeIf(jobPosting -> !jobPosting.isOpen(sessionDate));

        return jobPostings;
    }


    /**
     * Filters ArrayLists of job postings to those in a certain location
     * @param location
     * @param jobPostings
     * @return
     */
    private List<JobPosting> filterByLocation(String location, List<JobPosting> jobPostings){

        jobPostings.removeIf(jobPosting -> !jobPosting.getLocation().equals(location));

        return jobPostings;
    }

    /**
     * Filters ArrayLists of job postings to those associated with a specific firm
     * @param firmId
     * @param jobPostings
     * @return
     */
    private List<JobPosting> filterByFirm(Long firmId, List<JobPosting> jobPostings){

        jobPostings.removeIf(jobPosting -> !jobPosting.getFirmId().equals(firmId));

        return jobPostings;
    }


    /**
     * returns all job postings that have all of the hashtags being searched for
     * @param hashTags - the collection of hashtags that are being searched for
     * @return
     */
    private List<JobPosting> getJobsWithHashTags(HashSet<String> hashTags, List<JobPosting> jobPostings){

        jobPostings.removeIf(jobPosting -> !jobPosting.containsAllHashTags(hashTags));

        return jobPostings;
    }

    /**
     * Method to print job postings
     * @param jobPostings
     * @return
     */
    private List<String> print(List<JobPosting> jobPostings){

        ArrayList<String> result = new ArrayList<>();

        for (JobPosting jobPosting: jobPostings
             ) {
            result.add(jobPosting.toString());
        }

        return result;
    }

    public enum filterKeys{
        // todo
    }

    public List<JobPosting> filter(HashMap<filterKeys, Long> filter){
        return null; //todo
    }


    //TODO consider renaming to updateDbTime
    public void updateDb(LocalDate sessionDate) {
        this.sessionDate = sessionDate;
    }


    //TODO: remove this. Already done by SearchBy Enum
    public enum jobPostingFilters {
        OPEN,
        FIRM,
        LOCATION,
        JOB_ID
        // 1. since hashmaps cannot have duplicate keys, I make 5 hashtag enums. e.g. if you want to search high-salary
        // and part time, you put (HASHTAG1, "high-salary"), (HASHTAG2, "part-time")
        // 2. The order does not matter because the fiter method only looks at values
        // 3. You can keep using hashset, e.g. (HASHTAG, ["high-salary", "part-time"]), but this requires some changes
        // in my code since java cannot convert hashset to Object.

    }

    //TODO: remove this method
    public List<JobPosting> filterJobPostings(HashMap<jobPostingFilters, Object> filtration){
        List<JobPosting> jobPostList = this.getListOfItems();
        if (filtration.containsKey(jobPostingFilters.FIRM)){
            jobPostList = jobPostList.stream().filter(jobPosting -> jobPosting.getFirmId()
                    == filtration.get(jobPostingFilters.FIRM)).collect(Collectors.toList());
        }
        if (filtration.containsKey(jobPostingFilters.OPEN)){
            jobPostList = jobPostList.stream().filter(jobPosting -> jobPosting.isOpen(sessionDate)
            ).collect(Collectors.toList());
        }
        // You need to implement all the filtration requirements. As long as you write a true/ false statement after
        // "->", that will work
        return jobPostList;
    }


// 4. If you want to keep hashset, 1 solution would be creating another filtration method that only takes care of tags
public List<JobPosting> filterJobPostingsByTags(HashSet<String> tags){
    List<JobPosting> jobPostList = this.getListOfItems();
    jobPostList = jobPostList.stream().filter(jobPosting ->
            jobPosting.containsAllHashTags(tags)).collect(Collectors.toList());
    return jobPostList;
}
}