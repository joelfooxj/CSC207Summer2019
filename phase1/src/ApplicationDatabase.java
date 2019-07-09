import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ApplicationDatabase extends AbstractDatabase<Application>{
    public ApplicationDatabase() {
    }

    // Form in bracket is (application ID, application object)
    // Dependency Inversion
    public void addApplication(Application application){
        super.addItem(application);
    }

    //Add an application which contains applicant and job info
    // Not a dependency inversion
    public void addApplication(long applicantID, long jobID, long firmID, LocalDate creationDate){
        Application newApplication = new Application(super.getCurrID(), applicantID, jobID, firmID, creationDate);
        newApplication.setApplicantID(applicantID);
        newApplication.setJobID(jobID);
        super.addItem(newApplication);
        //super.addItem(new Application(super.getCurrID(), applicantID, jobID, firmID, creationDate));
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
    public List<Application> getApplicationsByApplicantID(long applicantID){
        List<Application> applicationList = new ArrayList<Application>();
        for(Long i = 0L; i<super.getCurrID();i++){
            //added to deal with case where applicant has no applications
//            if(applicationList.isEmpty()){
//                break;
//            }
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

    public void printApplicationsByApplicantID(long applicationId, long firmId){
        for (Long i = 0L; i<super.getCurrID();i++){
            Application item = super.getItemByID(i);
            if (item.getApplicantID() == applicationId && item.getFirmID()==firmId){
                System.out.println(item.toString());
            }
        }
    }

    //The 5 print methods
    //print a String which adds all the toString of applications with that applicant ID
    public void printApplicationsByApplicantID(long applicantID){
        StringBuilder allApplication = new StringBuilder();
        for (Long i = 0L;i<super.getCurrID();i++){
            Application item = super.getItemByID(i);
            if (item.getApplicantID() == applicantID)
                allApplication.append(item.toString()+"\n\n");
        }
        System.out.println(allApplication);
    }

    //print a String …… job ID
    public void printApplicationsByJobID(long jobID){
        StringBuilder allApplication = new StringBuilder();
        for (long i = 0L;i <super.getCurrID();i++){
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

    // I check isOpen() and isHired().  Note if the hire() method is called, isOpen() will be false
    public List getOpenApplicationIDList(){
        List<Long> idList = new ArrayList<>();
        for (Application app:this.getAllApplications()){
            if (app.isOpen()) {
                idList.add(app.getApplicationID());
            }
        }
        return idList;
    }

    public List<Application> getAllApplications(){
        List<Application> applicationList = new ArrayList<>();
        for (Long i = 0L; i<super.getCurrID();i++){
            applicationList.add(data.get(i));
        }
        return applicationList;
    }

    public List<Long> getAllApplicationIDs(){
        List<Long> retLongList = new ArrayList<>();
        for (Application app:this.getAllApplications()){
            retLongList.add(app.getApplicationID());
        }
        return retLongList;
    }

}

