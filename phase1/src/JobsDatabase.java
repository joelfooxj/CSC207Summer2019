import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class JobsDatabase extends AbstractDatabase<JobPosting> implements java.io.Serializable{

    public void printJobPostings(){
        super.printAll();
    }

    public void addJob(String title, String details, long firmId, LocalDate postedDate, LocalDate expiryDate){
        addItem(new JobPosting(title, details, firmId, postedDate, expiryDate));
    }

    public JobPosting getJobPostingByID(Long id){
        return super.getItemByID(id);
    }

    public void removeJobPosting(Long id){
        super.removeItemByID(id);
    }


    private ArrayList<JobPosting> getJobPostingsByFirm(long firmId){
        ArrayList<JobPosting> jobPostings = new ArrayList<>();
        for(int i = 0; i < super.getCurrID(); i++){
            JobPosting jobPosting = getItemByID((long) i);
            if(jobPosting != null && jobPosting.getFirmid() == firmId){
                jobPostings.add(jobPosting);
            }
        }
        return jobPostings;
    }

    public void printJobsByFirmId(long firmId){
        System.out.println(getJobPostingByID(firmId));
    }

    public ArrayList<Long> getOpenJobIDs(){
        ArrayList<Long> jobPostingIds = new ArrayList<>();
        for(int i = 0; i < data.size(); i++){
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
