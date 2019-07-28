package CommandHandlers;

import Databases.UserCredentials;

public class CommandHandlerFactory {


    /**
     * @param userCredentials The user that is currently logged in
     * @return a commandHandler object
     */
    public CommandHandler getCommandHandler(UserCredentials userCredentials){
        CommandHandler commandHandler;
        // set the value of CommandHandlers.CommandHandler based on the user type
        if (userCredentials.getUserType().equals(HyreLauncher.getApplicantUserType())){
            commandHandler = new ApplicantCommandHandler(userCredentials);
        } else if (userCredentials.getUserType().equals(HyreLauncher.getInterviewerUserType())){
            commandHandler = new InterviewerCommandHandler(userCredentials);
        }
        else if (userCredentials.getUserType().equals(HyreLauncher.getHrUserType())){
            commandHandler = new HrCommandHandler();
        }
        else {
            return null;
        }
        return commandHandler;
    }
}
