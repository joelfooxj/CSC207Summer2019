import java.io.Serializable;
import java.time.LocalDate;

public class JobPosting implements Serializable {

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
    private boolean isFilled = false;

    public JobPosting(String jobTitle, String jobDescription, String jobRequirements, LocalDate publishDate, LocalDate expiryDate){
        jobid = numberOfJobs;
        numberOfJobs ++;
        this.jobTitle = jobTitle;
        this.jobDescription = jobDescription;
        this.jobRequirements = jobRequirements;
        this.publishDate = publishDate;
        this.expiryDate = expiryDate;
    }

    //how do we check if a job has been filled?
    public boolean isOpen(){
        return isFilled() && !isExpired();
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

    public void setAsFilled(){
        isFilled = true;
    }

    @Override
    public String toString(){
        return jobid + ", " + jobTitle + ": " + jobDescription;
    }

    public String getDetail(){
        return "Job ID: " + jobid + "\n" + "Job Title: " + jobTitle + "\n" + "Job Description: " +
                jobDescription + "\n" + "Job Requirements: " + jobRequirements + "\n" +
                "Positions Available: " + numLabourRequired + "\n" + "Date Published: " + publishDate + "\n" +
                "Expiry Date: " + expiryDate + "\n";
    }

    public static void main(String[] args) {
        JobsDatabase jd = new JobsDatabase();
        jd.createNewJobPostings("Janitor","Clean stuff up","Experience cleaning", LocalDate.now(),
                LocalDate.of(2019,10,20));
        JobPosting job1 = jd.getJobPostingByID((long) 0);
        System.out.println(job1);
        System.out.println(job1.getDetail());
    }

}
