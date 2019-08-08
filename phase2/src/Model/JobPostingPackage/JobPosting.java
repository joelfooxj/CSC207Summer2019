package Model.JobPostingPackage;

import Control.DateRange;
import Model.FirmPackage.Firm;
import Model.requiredDocs;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

public class JobPosting extends Observable implements Serializable {
    private Long jobId;
    private String jobTitle;
    private String jobDetails;
    private Firm firm;
    private LocalDate publishDate;
    private LocalDate expiryDate;
    private String location;
    private Collection<jobTags> hashTags;
    private long numberOfPositions;
    private boolean isFilled = false;
    private List<String> interviewStages;
    private List<String> skills;
    private List<requiredDocs> setDocs;
    private HashMap<Model.requiredDocs, String> printDocsHashMap = new HashMap<Model.requiredDocs, String>() {
        {
            put(requiredDocs.COVERLETTER, "Cover Letter");
            put(requiredDocs.CV, "CV");
            put(requiredDocs.REFERENCELETTERS, "Reference Letters");
        }
    };


    private HashMap<jobTags, String> printJobTagsHM = new HashMap<jobTags, String>(){
        {
            put(jobTags.FULL_TIME, "Full-time");
            put(jobTags.PART_TIME, "Part-time");
            put(jobTags.FLEX_WORK, "Flex-work");
            put(jobTags.TECH, "Tech");
            put(jobTags.FINANCE, "Finance");
        }
    };

    /**
     * constructor for job postings
     *
     * @param title             title of job posting
     * @param details           details of job
     * @param firm              Firm where job exists
     * @param numberOfPositions number of positions job is hiring for
     * @param location          location of job
     * @param jobDateRange      - contains {@link #publishDate} and {@link #expiryDate}
     * @param hashTags          - contains hashtags associated with a job
     */
    //TODO code smell: too many paramaters.
    public JobPosting(String title, String details, Firm firm, long numberOfPositions, String location,
                      DateRange jobDateRange, List<String> interviewStages, Collection<jobTags> hashTags,
                      List<String> skills, List<requiredDocs> docs, Long jobId) {
        this.jobId = jobId;
        this.jobTitle = title;
        this.jobDetails = details;
        this.firm = firm;
        this.numberOfPositions = numberOfPositions;
        this.location = location;
        this.publishDate = jobDateRange.getStartDate();
        this.expiryDate = jobDateRange.getEndDate();
        this.hashTags = hashTags;
        this.location = location;
        this.interviewStages = interviewStages;
        this.skills = skills;
        this.setDocs = docs;
    }

    private Collection<jobTags> getHashTags() {
        return this.hashTags;
    }

    /**
     * Returns true if the job is not filled and has not expired
     *
     * @return true or false
     */
    boolean isOpen(LocalDate todaysDate) {
        return !getIsFilled() && !isExpired(todaysDate);
    }

    /**
     * Indicates whether the job has expired {@link #expiryDate}
     *
     * @return whether this job has expired
     */

    private boolean isExpired(LocalDate todaysDate) {
        return todaysDate.compareTo(expiryDate) > 0;
    }

    /**
     * Used to set a job as having been filled
     */
    public void isFilled(LocalDate closedDate) {
        isFilled = true;
        setChanged();
        List<Object> arg = new ArrayList<>();
        arg.add("Closed");
        arg.add(closedDate);
        notifyObservers(arg);
    }

    /**
     * Indicates whether all the job positions have been filled or not
     *
     * @return {@link #isFilled}
     */
    private boolean getIsFilled() {
        return isFilled;
    }

    @Override
    public String toString() {
        List<String> printDocs = new ArrayList<>();
        for (Model.requiredDocs doc : this.setDocs) {
            printDocs.add(printDocsHashMap.get(doc));
        }
        List<String> printTags = new ArrayList<>();
        for (jobTags tag: this.hashTags){
            printTags.add(printJobTagsHM.get(tag));
        }
        return "Job ID: " + jobId + "\n" + "Job Title: " + jobTitle + "\n" + "Job Description: " +
                jobDetails + "\n" + "Positions Available: " + numberOfPositions + "\n" +
                "Date Published: " + publishDate + "\n" + "Deadline to apply: " + expiryDate + "\n"
                + "HashTags: " + printTags + "\n" + "Skills Required: " + skills + "\n" + "Required requiredDocs: "
                + printDocs;
    }

    public String listString() {
        return "[" + this.jobId + "] " + this.jobTitle + " @ " + this.firm.toString();
    }

    /**
     * returns jobId
     *
     * @return {@link #jobId}
     */
    public Long getJobId() {
        return jobId;
    }

    public Long getFirmId() {
        return this.firm.getFirmId();
    }

    public String getFirmName(){
        return this.firm.getFirmName();
    }

    public long getNumberOfPositions() {
        return numberOfPositions;
    }

    public String getLocation() {
        return location;
    }

    public List<requiredDocs> getRequiredDocs() {
        return this.setDocs;
    }

    public List<String> getInterviewStages() {
        return this.interviewStages;
    }

    /**
     * Checks whether all of the hashtags being searched for are contained in the hashtags
     * variable of the job posting
     *
     * @param searchHashTags - a collection of hashtags being searched for
     * @return true or false
     */
    public boolean containsAllHashTags(HashSet<jobTags> searchHashTags) {

        if(searchHashTags.isEmpty()){
            return true;
        }

        for (jobTags hash : searchHashTags) {
            if (!this.getHashTags().contains(hash)) {
                return false;
            }
        }
        return true;
    }
}
