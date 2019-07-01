import java.time.LocalDate;

public class JobPosting {

    //question: is numberOfJobs redundant? Should I just copy it from the JobsDatabase currID?
    private static long numberOfJobs = 0;

    private Long jobid;
    private String jobTitle;
    private String jobDescription;
    private Object jobRequirements;

    private LocalDate publishDate;
    private LocalDate expiryDate;
    //TODO: allow numLabourRequired to be changeable
    private int numLabourRequired = 1;
    private boolean isOpen = true;

    public JobPosting(String jobTitle, String jobDescription, LocalDate publishDate, LocalDate expiryDate){
        jobid = numberOfJobs;
        numberOfJobs ++;
        this.jobTitle = jobTitle;
        this.jobDescription = jobDescription;
        //should the publish date be based on the actual date today? or should it be modifiable for testing purposes?
        this.publishDate = publishDate;
        this.expiryDate = expiryDate;
    }

    //how do we check if a job has been filled?
    public boolean isOpen(){
        return isOpen && !isExpired();
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
    private void isFilled(){
        //what does this do
    }

}
