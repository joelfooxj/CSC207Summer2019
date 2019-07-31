package Model;

import Control.DateRange;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;

public class JobPosting implements Serializable {

    /**
     * The total number of JobPostings that have been created
     */
    private static long numberOfJobs = 0;

    /**
     * The id of the job, used to store and retrieve in databases
     */
    private Long jobId;

    /**
     * The title of the job
     */
    private String jobTitle;

    /**
     * The details and requirements of the job
     */
    private String jobDetails;

    /**
     * The id of the firm that is offering the job
     */
    private long firmId;

    /**
     * The date the job posting was published
     */
    private LocalDate publishDate;

    /**
     * The date the job expires, where applications can no longer be submitted
     */
    private LocalDate expiryDate;

    /**
     * The location of the job
     */
    private String location;

    /**
     * A list of hashtags related to a job posting
     */
    private Collection<String> hashTags;

    //TODO: allow numLabourRequired to be changeable
    /**
     * The number of positions open for a job posting
     */
    private long numberOfPositions;

    /**
     * True all the job positions for a posting have been filled by candidates
     */
    private boolean isFilled = false;

    /**
     *constructor for job postings
     * @param title {@link #jobTitle}
     * @param details - {@link #jobDetails}
     * @param firmId - {@link #firmId}
     * @param numberOfPositions {@link #numberOfPositions}
     * @param location {@link #location}
     * @param jobDateRange - contains {@link #publishDate} and {@link #expiryDate}
     * @param hashTags - contains hashtags associated with a job
     */
    //TODO code smell: too many paramaters.
    public JobPosting(String title, String details, long firmId, long numberOfPositions, String location,
                      DateRange jobDateRange, Collection<String> hashTags){
        jobId = numberOfJobs;
        numberOfJobs ++;
        this.jobTitle = title;
        this.jobDetails = details;
        this.firmId = firmId;
        this.numberOfPositions = numberOfPositions;
        this.location = location;
        this.publishDate = jobDateRange.getStartDate();
        this.expiryDate = jobDateRange.getEndDate();
        this.hashTags = hashTags;
    }

    /**
     * Returns true if the job is not filled and has not expired
     * @return true or false
     */
    public boolean isOpen(LocalDate todaysDate){
        return !getIsFilled() && !isExpired(todaysDate);
    }

    /**
     * Indicates whether the job has expired {@link #expiryDate}
     * @return
     */

    private boolean isExpired(LocalDate todaysDate){
        //condition should be based on today's date
        if(todaysDate.compareTo(expiryDate) > 0){
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * Used to set a job as having been filled
     */
    public void isFilled(){
        isFilled = true;
    }

    /**
     * Indicates whether all the job positions have been filled or not
     * @return {@link #isFilled}
     */
    private boolean getIsFilled(){
        return isFilled;
    }

    @Override
    public String toString(){
        return "Job ID: " + jobId + "\n" + "Job Title: " + jobTitle + "\n" + "Job Description: " +
                jobDetails + "\n" + "Positions Available: " + numberOfPositions + "\n" +
                "Date Published: " + publishDate + "\n" + "Expiry Date: " + expiryDate + "\n"
                + "HashTags: " + hashTags;
    }

    /**
     * returns jobId
     * @return {@link #jobId}
     */
    public Long getJobId() {
        return jobId;
    }

    /**
     * returns firmId
     * @return {@link #firmId}
     */
    public Long getFirmId() {
        return firmId;
    }

    public long getNumberOfPositions() {
        return numberOfPositions;
    }

    String getLocation(){return location;}

    /**
     * Checks whether all of the hashtags being searched for are contained in the hashtags
     * variable of the job posting
     * @param searchHashTags - a collection of hashtags being searched for
     * @return true or false
     */
    boolean containsAllHashTags(HashSet<String> searchHashTags) {
        searchHashTags = (HashSet<String>) searchHashTags.clone();
        Integer startSize = searchHashTags.size();
        searchHashTags.retainAll(hashTags);
        if(startSize.equals(searchHashTags.size())){
            return true;
        }
        else{
            return false;
        }
    }

}
