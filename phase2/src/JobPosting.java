import java.io.Serializable;
import java.time.LocalDate;

public class JobPosting extends JobsDatabase implements Serializable {

    //question: is numberOfJobs redundant? Should I just copy it from the JobsDatabase currID?
    private static long numberOfJobs = 0;

    private Long jobId;
    private String jobTitle;
    private String jobDetails;
    private long firmid;

    private LocalDate publishDate;
    private LocalDate expiryDate;
    //TODO: allow numLabourRequired to be changeable
    private int numLabourRequired = 1;
    private boolean isFilled = false;

    public JobPosting(String jobTitle, String jobDetails, long firmId, LocalDate publishDate, LocalDate expiryDate){
        jobId = numberOfJobs;
        numberOfJobs ++;
        this.jobTitle = jobTitle;
        this.jobDetails = jobDetails;
        this.firmid = firmId;
        this.publishDate = publishDate;
        this.expiryDate = expiryDate;
    }

    //how do we check if a job has been filled?
    public boolean isOpen(){
        return !isFilled() && !isExpired();
    }

    private boolean isExpired(){
        //condition should be based on today's date
        if(publishDate.compareTo(expiryDate) > 0){
            return true;
        }
        else{
            return false;
        }
    }
    //what's the difference between isOpen() and isFilled()?
    private boolean isFilled(){
        return isFilled;
    }

    @Override
    public String toString(){
        return "Job ID: " + jobId + "\n" + "Job Title: " + jobTitle + "\n" + "Job Description: " +
                jobDetails + "\n" + "Positions Available: " + numLabourRequired + "\n" +
                "Date Published: " + publishDate + "\n" + "Expiry Date: " + expiryDate + "\n";
    }

    public Long getJobId() {
        return jobId;
    }

    public long getFirmid() {
        return firmid;
    }

}
