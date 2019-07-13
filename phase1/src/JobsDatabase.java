import java.time.LocalDate;
import java.util.ArrayList;

public class JobsDatabase extends AbstractDatabase<JobPosting> implements java.io.Serializable{

    public void printOpenJobPostings(){
        for (JobPosting job:this.getListOfItems()){
            if (job.isOpen()){
                System.out.println(job);
            }
        }
    }

    public void addJob(String title, String details, long firmId, LocalDate postedDate, LocalDate expiryDate){
        addItem(new JobPosting(title, details, firmId, postedDate, expiryDate));
    }

    public JobPosting getJobPostingByID(Long id){
        return super.getItemByID(id);
    }



    public void printJobsByFirmId(long firmId){
        System.out.println(getJobPostingByID(firmId));
    }

    public ArrayList<Long> getOpenJobIDs(){
        ArrayList<Long> jobPostingIds = new ArrayList<>();
        for(int i = 0; i < super.getCurrID(); i++){
            JobPosting jobPosting = getJobPostingByID((long) i);
            if(jobPosting.isOpen()){
                jobPostingIds.add(jobPosting.getJobId());
            }
        }
        return jobPostingIds;
    }

    public void updateDb(LocalDate sessionDate) {
    }

}
