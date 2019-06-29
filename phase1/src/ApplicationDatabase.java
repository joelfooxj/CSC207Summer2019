import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ApplicationDatabase extends AbstractDatabase{
    private HashMap<Long, Application> applicationDatabase;

    public ApplicationDatabase() {
        applicationDatabase = new HashMap<>();
    }

    // Form in bracket is (application ID, application object)
    public void createNewApplication(Application application){
        applicationDatabase.put(application.getApplicationID(), application);
    }

    //This ID must be application ID
    public void removeItemByID(long removeID){
        applicationDatabase.remove(removeID);
    }

    //Get a list of applications by its applicant ID
    public List<Application> getApplicationByApplicantID(long applicantID){
        List<Application> applicationList = new ArrayList<Application>();
        for(int i = 0; i<applicationDatabase.size();i++){
            Application item = applicationDatabase.get(i);
            if (item.getApplicantID() == applicantID){
                applicationList.add(item);
            }
        }
        return applicationList;
    }

    //Get a list of applications by its Job ID
    public List<Application> getApplicationByJobID(long jobID){
        List<Application> applicationList = new ArrayList<Application>();
        for(int i = 0; i<applicationDatabase.size();i++){
            Application item = applicationDatabase.get(i);
            if (item.getJobID() == jobID){
                applicationList.add(item);
            }
        }
        return applicationList;
    }

    //Get a list of applications by its Firm ID
    public List<Application> getApplicationByFirmID(long firmID){
        List<Application> applicationList = new ArrayList<Application>();
        for(int i = 0; i<applicationDatabase.size();i++){
            Application item = applicationDatabase.get(i);
            if (item.getFirmID() == firmID){
                applicationList.add(item);
            }
        }
        return applicationList;
    }

    //The 3 print methods
    public void printApplicationsByApplicantID(long applicantID){
        StringBuilder allApplication = new StringBuilder();
        for (int i = 0;i<applicationDatabase.size();i++){
            Application item = applicationDatabase.get(i);
            if (item.getApplicantID() == applicantID)
            allApplication.append(item.toString());
        }
        System.out.println(allApplication);
    }

    public void printApplicationsByJobID(long jobID){
        StringBuilder allApplication = new StringBuilder();
        for (int i = 0;i < applicationDatabase.size();i++){
            Application item = applicationDatabase.get(i);
            if (item.getJobID() == jobID)
                allApplication.append(item.toString());
        }
        System.out.println(allApplication);
    }

    public void printApplicationsByFirmID(long firmID){
        StringBuilder allApplication = new StringBuilder();
        for (int i = 0;i<applicationDatabase.size();i++){
            Application item = applicationDatabase.get(i);
            if (item.getFirmID() == firmID)
                allApplication.append(item.toString());
        }
        System.out.println(allApplication);
    }
}
