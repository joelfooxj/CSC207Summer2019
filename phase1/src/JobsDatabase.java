import java.time.LocalDate;
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

    public void createNewJobPostings(String title, String description, LocalDate expiryDate){
        addItem(new JobPosting(title, description, expiryDate));
    }

    public void removeJobPosting(Long id){
        super.removeItemByID(id);
    }

}
