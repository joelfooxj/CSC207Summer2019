package Databases;

import CommandHandlers.DateRange;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

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
     * @see JobPosting#JobPosting(String, String, long, LocalDate, LocalDate, Collection)
     */
    //TODO change to addJobPosting
    public void addJob(String title, String details, long firmId, DateRange jobDateRange, Collection<String> hashTags){
        addItem(new JobPosting(title, details, firmId, jobDateRange, hashTags));
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
     * proves all job postings that are related to a firm
     * @param firmId - id of firm that are offering job postings
     */
    //TODO: connect to interface instead of print
    public void printJobsByFirmId(long firmId){
        System.out.println(getJobPostingByID(firmId));
    }

    /**
     * returns all ids of job postings that are open
     * @return ArrayList of Job Posting ids
     */
    public ArrayList<Long> getOpenJobIDs(){

        ArrayList<Long> jobPostingIds = new ArrayList<>();

        for(int i = 0; i < super.getCurrID(); i++){
            JobPosting jobPosting = getJobPostingByID((long) i);
            if(jobPosting.isOpen(sessionDate)){
                jobPostingIds.add(jobPosting.getJobId());
            }
        }
        return jobPostingIds;
    }

    /**
     * returns all job postings that have one or more hashtags matching the ones being searched
     * @param hashTags - the collection of hashtags that are being searched for
     * @return
     */
    public ArrayList<JobPosting> getJobsWithHashTags(HashSet<String> hashTags){

        ArrayList<JobPosting> jobPostings = new ArrayList<>();

        for(int i = 0; i < super.getCurrID(); i++){
            HashSet<String> hashTagsClone = (HashSet<String>) hashTags.clone();
            JobPosting jobPosting = getJobPostingByID((long) i);
            if(jobPosting.containsHashTags(hashTagsClone)){
                jobPostings.add(jobPosting);
            }
        }
        return jobPostings;
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
