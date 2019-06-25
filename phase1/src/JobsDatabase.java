import java.util.Scanner;

public class JobsDatabase extends AbstractDatabase{

    public void populateArrayFromTextFile(){
        //TODO: pull text from file and place in ArrayList
    }

    public void eraseTextFile(){
        //TODO: erase text file
    }

    public void writeToTextFile(){
        //TODO: Write to text file
    }

    public void printJobPostings(){
        super.printAll();
    }

    @Override
    public void addItem(){
        //TODO: this function will be replaced by addItem(JobPosting jobPosting)
        }

    public void addItem(long id, JobPosting jobPosting){
        //TODO: this method should be written in the super class
        data.put(id,jobPosting);
    }

    public void createNewJobPostings(String title, String description){
        addItem(JobPosting.getNumberOfJobs(), new JobPosting(title, description));
    }

    public void removeJobPosting(Long id){
        super.removeItemByID(id);
    }

}
