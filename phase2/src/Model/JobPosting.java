package Model;

import Control.DateRange;

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
    private Collection<String> hashTags;
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

    /**
     * constructor for job postings
     *
     * @param title             {@link #jobTitle}
     * @param details           - {@link #jobDetails}
     * @param firm              Firm where job exists
     * @param numberOfPositions {@link #numberOfPositions}
     * @param location          {@link #location}
     * @param jobDateRange      - contains {@link #publishDate} and {@link #expiryDate}
     * @param hashTags          - contains hashtags associated with a job
     */
    //TODO code smell: too many paramaters.
    public JobPosting(String title, String details, Firm firm, long numberOfPositions, String location,
                      DateRange jobDateRange, List<String> interviewStages, Collection<String> hashTags,
                      List<String> skills, List<Model.requiredDocs> docs, Long jobId) {
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

    public Collection<String> getHashTags() {
        return this.hashTags;
    }

    /**
     * Returns true if the job is not filled and has not expired
     *
     * @return true or false
     */
    public boolean isOpen(LocalDate todaysDate) {
        return !getIsFilled() && !isExpired(todaysDate);
    }

    /**
     * Indicates whether the job has expired {@link #expiryDate}
     *
     * @return
     */

    public boolean isExpired(LocalDate todaysDate) {
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
        return "Job ID: " + jobId + "\n" + "Job Title: " + jobTitle + "\n" + "Job Description: " +
                jobDetails + "\n" + "Positions Available: " + numberOfPositions + "\n" +
                "Date Published: " + publishDate + "\n" + "Deadline to apply: " + expiryDate + "\n"
                + "HashTags: " + hashTags + "\n" + "Skills Required: " + skills + "\n" + "Required requiredDocs: "
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
    public boolean containsAllHashTags(HashSet<String> searchHashTags) {
        for (String hash : searchHashTags) {
            if (this.getHashTags().contains(hash)) {
                return true;
            }
        }
        return false;
    }
}
