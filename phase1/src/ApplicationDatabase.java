import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ApplicationDatabase extends AbstractDatabase<Application>{
    //private HashMap<Long, Application> applicationDatabase;
    //private int total = super().size();

    public ApplicationDatabase() {
    }

    // Form in bracket is (application ID, application object)
    public void addApplication(Application application){
        super.addItem(application);
    }

    //Add an application which contains applicant and job info
    public void addApplication(long applicantID, long jobID){
        Application newApplication = new Application();
        newApplication.setApplicantID(applicantID);
        newApplication.setJobID(jobID);
        super.addItem(newApplication);
    }

    //This ID must be application ID
    public void removeItemByID(long removeID){
        super.removeItemByID(removeID);
    }

    //Since each application ID is unique, this returns 1 application object
    public Application getApplicationByApplicationID(long applicationID){
        return super.getItemByID(applicationID);
    }

    //Get a list of applications by its applicant ID
    public List<Application> getApplicationByApplicantID(long applicantID){
        List<Application> applicationList = new ArrayList<Application>();
        for(Long i = 0L; i<super.getCurrID();i++){
            Application item = super.getItemByID(i);
            if (item.getApplicantID() == applicantID){
                applicationList.add(item);
            }
        }
        return applicationList;
    }

    //Get a list of applications by its Job ID
    public List<Application> getApplicationByJobID(long jobID){
        List<Application> applicationList = new ArrayList<Application>();
        for(Long i = 0L; i<super.getCurrID();i++){
            Application item = super.getItemByID(i);
            if (item.getJobID() == jobID){
                applicationList.add(item);
            }
        }
        return applicationList;
    }

    //Get a list of applications by its Firm ID
    public List<Application> getApplicationByFirmID(long firmID){
        List<Application> applicationList = new ArrayList<Application>();
        for(Long i = 0L; i<super.getCurrID();i++){
            Application item = super.getItemByID(i);
            if (item.getFirmID() == firmID){
                applicationList.add(item);
            }
        }
        return applicationList;
    }

    //Get a list of applications by its interviewerID
    public List<Application> getApplicationByInterviewerID(long interviewerID){
        List<Application> applicationList = new ArrayList<Application>();
        for(Long i = 0L; i<super.getCurrID();i++){
            Application item = super.getItemByID(i);
            if (item.getInterviewerID() == interviewerID){
                applicationList.add(item);
            }
        }
        return applicationList;
    }

    public void printApplicationsByApplicantID(String username, long firmId){
        // applicant ids are the username
    }

    //The 5 print methods
    //print a String which adds all the toString of applications with that applicant ID
    public void printApplicationsByApplicantID(long applicantID){
        StringBuilder allApplication = new StringBuilder();
        for (Long i = 0L;i<super.getCurrID();i++){
            Application item = super.getItemByID(i);
            if (item.getApplicantID() == applicantID)
            allApplication.append(item.toString()+";  ");
        }
        System.out.println(allApplication);
    }

    //print a String …… job ID
    public void printApplicationsByJobID(long jobID){
        StringBuilder allApplication = new StringBuilder();
        for (long i = 0L;i < super.getCurrID();i++){
            Application item = super.getItemByID(i);
            if (item.getJobID() == jobID)
                allApplication.append(item.toString()+"; ");
        }
        System.out.println(allApplication);
    }

    //print a String …… firmID
    public void printApplicationsByFirmID(long firmID){
        StringBuilder allApplication = new StringBuilder();
        for (Long i = 0L;i<super.getCurrID();i++){
            Application item = super.getItemByID(i);
            if (item.getFirmID() == firmID)
                allApplication.append(item.toString()+"; ");
        }
        System.out.println(allApplication);
    }

    //print a String …… interviewer ID
    public void printApplicationsByInterviewerID(long interviewerID){
        StringBuilder allApplication = new StringBuilder();
        for (Long i = 0L;i<super.getCurrID();i++){
            Application item = super.getItemByID(i);
            if (item.getInterviewerID() == interviewerID)
                allApplication.append(item.toString()+"; ");
        }
        System.out.println(allApplication);
    }

    public void printPassedApplications(long jobID){
        StringBuilder allApplication = new StringBuilder();
        for (Long i = 0L;i<super.getCurrID();i++){
            Application item = super.getItemByID(i);
            if (item.getJobID() == jobID && item.getPassedInterviewNum()==4 )
                allApplication.append(item.toString()+"; ");
        }
        System.out.println(allApplication);
    }

    // prints applications that are not closed (filled/expired)
    // this will be called when the HR wants to set up an interview
    public void printOpenApplicationsByJobID(long jobID) {
        StringBuilder allApplication = new StringBuilder();
        for (Long i = 0L;i < super.getCurrID();i++){
            Application item = super.getItemByID(i);
            if (item.getJobID() == jobID && item.isOpen())
                allApplication.append(item.toString() +"; ");
        }
        System.out.println(allApplication);
    }
}
