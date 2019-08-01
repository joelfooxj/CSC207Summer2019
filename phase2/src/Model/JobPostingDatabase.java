package Model;

import Control.DateRange;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class JobPostingDatabase extends TemplateDatabase<JobPosting> implements java.io.Serializable{

    /**
     * the date that the user is logged into the program on. i.e. today's date.
     */
    private LocalDate sessionDate;

    /**
     * provides all open job postings
     */
    //TODO make this send a string and connect to an interface
    public void printOpenJobPostings(){
        for (JobPosting job:this.getListOfItems()){
            if (job.isOpen(sessionDate)){
                System.out.println(job);
            }
        }
    }

    /**
     * adds a job Posting by construction a job posting
     * @see JobPosting#JobPosting(String, String, long, long, String, DateRange, List, Collection)
     */
    //TODO change to addJobPosting
    public void addJob(String title, String details, long firmId, long numsLabourRequired, String location,
                       DateRange jobDateRange, List<String> interviewStages, Collection<String> hashTags,
                       List<String> skillList, List<requiredDocs> docsList){
        addItem(new JobPosting(title, details, firmId, numsLabourRequired, location, jobDateRange, interviewStages,
                hashTags, skillList, docsList));
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
        ArrayList<JobPosting> openJobPostings = filterOpenJobPostings(this.getListOfItems());

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
    public ArrayList<String> filterBy(HashMap<SearchBy,Object> searchMap){

        ArrayList<JobPosting> jobPostings = this.getListOfItems();

        if(searchMap.containsKey(SearchBy.FIRM)){
            jobPostings = filterByFirm((Long)searchMap.get(SearchBy.FIRM),jobPostings);
        }

        if(searchMap.containsKey(SearchBy.LOCATION)){
            jobPostings = filterByLocation((String)searchMap.get(SearchBy.LOCATION),jobPostings);
        }

        if(searchMap.containsKey(SearchBy.HASHTAG)){
            jobPostings = getJobsWithHashTags((HashSet<String>) searchMap.get(SearchBy.HASHTAG),jobPostings);
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
    private ArrayList<JobPosting> filterOpenJobPostings(ArrayList<JobPosting> jobPostings){

        jobPostings.removeIf(jobPosting -> !jobPosting.isOpen(sessionDate));

        return jobPostings;
    }


    /**
     * Filters ArrayLists of job postings to those in a certain location
     * @param location
     * @param jobPostings
     * @return
     */
    private ArrayList<JobPosting> filterByLocation(String location, ArrayList<JobPosting> jobPostings){

        jobPostings.removeIf(jobPosting -> !jobPosting.getLocation().equals(location));

        return jobPostings;
    }

    /**
     * Filters ArrayLists of job postings to those associated with a specific firm
     * @param firmId
     * @param jobPostings
     * @return
     */
    private ArrayList<JobPosting> filterByFirm(Long firmId, ArrayList<JobPosting> jobPostings){

        jobPostings.removeIf(jobPosting -> !jobPosting.getFirmId().equals(firmId));

        return jobPostings;
    }


    /**
     * returns all job postings that have all of the hashtags being searched for
     * @param hashTags - the collection of hashtags that are being searched for
     * @return
     */
    private ArrayList<JobPosting> getJobsWithHashTags(HashSet<String> hashTags, ArrayList<JobPosting> jobPostings){

        jobPostings.removeIf(jobPosting -> !jobPosting.containsAllHashTags(hashTags));

        return jobPostings;
    }

    /**
     * Method to print job postings
     * @param jobPostings
     * @return
     */
    private ArrayList<String> print(ArrayList<JobPosting> jobPostings){

        ArrayList<String> result = new ArrayList<>();

        for (JobPosting jobPosting: jobPostings
             ) {
            result.add(jobPosting.toString());
        }

        return result;
    }

    /**
     * updates the session date of database
     * @param sessionDate - the date that the user is logged into the program on. i.e. today's date.
     */
    //TODO consider renaming to updateDbTime
    public void updateDb(LocalDate sessionDate) {
        this.sessionDate = sessionDate;
    }

}
