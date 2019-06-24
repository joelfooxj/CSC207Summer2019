public class HrCommandHandler extends CommandHandler {

    public HrCommandHandler(ApplicationsDatabase appsDb, JobsDatabase jobsDb, UserCredentialsDatabase usersDb){
        super(appsDb, jobsDb, usersDb);
    }
    void printCommandList(){
        System.out.println("[1] view applicants CVs");
        System.out.println("[2] view applicants cover letters");
    }

    void handleCommand(String commandId){
        if (commandId.equals("1")){
            return;
        } else if (commandId.equals("2")){
            return;
        }
    }
}
