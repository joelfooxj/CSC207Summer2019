import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ApplicationDatabase extends AbstractDatabase<Application>{
    public ApplicationDatabase() {
    }

    //Add an application which contains applicant， job， firm and creation date info
    public void addApplication(long applicantID, long jobID, long firmID, LocalDate creationDate){
        Application newApplication = new Application(super.getCurrID(), applicantID, jobID, firmID, creationDate);
        newApplication.setApplicantID(applicantID);
        newApplication.setJobID(jobID);
        super.addItem(newApplication);
    }

    public List<Application> getFilteredApplications(String filter, long id){
        List<Application> applicationList = new ArrayList<Application>();
        for (Long i = 0L; i<super.getCurrID();i++) {
            Application item = super.getItemByID(i);
            if (!applicationList.contains(item)) {
                applicationList.add(item);
            } else if (!item.passFilter(filter, id)) {
                applicationList.remove(item);
            }
        }
        return applicationList;
    }

    public List<Application> getFilteredApplications(HashMap<String, Long> filters){
        List<Application> applicationList = new ArrayList<Application>();
        for (String filter: filters.keySet()){
            for (Long i = 0L; i<super.getCurrID();i++) {
                Application item = super.getItemByID(i);
                if (!applicationList.contains(item)) {
                    applicationList.add(item);
                } else if (!item.passFilter(filter, filters.get(filter))) {
                    applicationList.remove(item);
                }
            }
        }
        return applicationList;
    }
    //Since each application ID is unique, this returns 1 application
    // object with this application id
    public Application getApplicationByApplicationID(long applicationID){
        return super.getItemByID(applicationID);
    }

    //Get a list of applications by its applicant ID
    public List<Application> getApplicationsByApplicantID(long applicantID){
        List<Application> applicationList = new ArrayList<Application>();
        for(Long i = 0L; i<super.getCurrID();i++){
            Application item = super.getItemByID(i);
            if (item.getApplicantID() == applicantID){
                applicationList.add(item);
            }
        }
        if (applicationList.isEmpty()) {
            System.out.println("This applicant does not have any applications submitted");
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

    // Print a list of applications with the same application id and firm id
    public void printApplicationsByApplicantID(long applicationId, long firmId){
        for (Long i = 0L; i<super.getCurrID();i++){
            Application item = super.getItemByID(i);
            if (item.getApplicantID() == applicationId && item.getFirmID()==firmId){
                System.out.println(item.toString());
            }
        }
    }

    // prints applications that are not closed (filled/expired)
    // this will be called when the HR wants to set up an interview
    // An application is open when it is open and the person hasn't been hired yet
    public void printOpenApplicationsByJobID(long jobID) {
        StringBuilder allApplication = new StringBuilder();
        for (Long i = 0L;i <super.getCurrID();i++){
            Application item = super.getItemByID(i);
            if (item.getJobID() == jobID && item.isOpen() && !item.isHired())
                allApplication.append(item.toString() +"; ");
        }
        System.out.println(allApplication);
    }

    // I check isOpen() and isHired().  Note if the hire() method is called, isOpen() will be set to false
    public List<Long> getOpenAppIdsByJob(Long jobId) {
        List<Long> idList = new ArrayList<>();
        for (Application app : this.getAllApplications()) {
            if (app.isOpen() && app.getJobID() == jobId) {
                idList.add(app.getApplicationID());
            }
        }
        return idList;
    }

    // print all the applications in the application database
    public List<Application> getAllApplications(){
        List<Application> applicationList = new ArrayList<>();
        for (Long i = 0L; i<super.getCurrID();i++){
            applicationList.add(data.get(i));
        }
        return applicationList;
    }
}