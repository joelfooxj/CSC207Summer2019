import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class JobsDatabase extends AbstractDatabase implements java.io.Serializable{

    protected JobsDatabase(){
        data = new HashMap<Long,JobPosting>();
    }

    public void printJobPostings(){
        super.printAll();
    }

    public void createNewJobPostings(String title, String description, String requirements, LocalDate postedDate, LocalDate expiryDate){
        addItem(new JobPosting(title, description, requirements, postedDate, expiryDate));
    }

    public JobPosting getJobPostingByID(Long id){
        return (JobPosting) super.getItemByID(id);
    }

    public void removeJobPosting(Long id){
        super.removeItemByID(id);
    }

    public ArrayList<JobPosting> getJobPostingsByFirm(long firmId){
        ArrayList<JobPosting> jobPostings = new ArrayList<>();
        for(int i = 0; i < data.size(); i++){
            JobPosting jobPosting = (JobPosting) getItemByID(firmId);
            if(jobPosting != null){
                jobPostings.add(jobPosting);
            }
        }
        return jobPostings;
    }

}
