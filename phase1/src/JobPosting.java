import java.util.Date;

public class JobPosting {

    private static long numberOfJobs = 0;

    private Long jobid;
    private String jobTitle;
    private String jobDescription;
    private Object jobRequirements;

    private Date publishDate;
    private Date expiryDate;
    //TODO: allow numLabourRequired to be changeable
    private int numLabourRequired= 1;
    private boolean isOpen = true;

    public JobPosting(String jobTitle, String jobDescription){
        jobid = numberOfJobs;
        numberOfJobs ++;
        this.jobTitle = jobTitle;
        this.jobDescription = jobDescription;
    }

    public boolean isOpen(){
        return isOpen;
    }

    private boolean isExpired(){
        //condition should be based on today's date
        if(true){
            return true;
        }
        else{
            return false;
        }
    }

    private boolean isFilled(){
        //TODO: write
        return true;
    }

    public static long getNumberOfJobs() {
        return numberOfJobs;
    }
}
