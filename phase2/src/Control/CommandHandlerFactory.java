package Control;

import Model.UserCredentials;

public class CommandHandlerFactory {


    /**
     * @param userCredentials The user that is currently logged in
     * @return a commandHandler object
     */
    public CommandHandler getCommandHandler(UserCredentials userCredentials){
        CommandHandler commandHandler;
        // set the value of Control.CommandHandler based on the user type
        if (userCredentials.getUserType() == UserCredentials.userTypes.APPLICANT){
            commandHandler = new ApplicantCommandHandler(userCredentials);
        } else if (userCredentials.getUserType() == UserCredentials.userTypes.INTERVIEWER){
            commandHandler = new InterviewerCommandHandler(userCredentials);
        }
        else if (userCredentials.getUserType() == UserCredentials.userTypes.HR){
            commandHandler = new HrCommandHandler(userCredentials);
        }
        else if (userCredentials.getUserType() == UserCredentials.userTypes.REFERER){
            commandHandler = new RefererCommandHandler(userCredentials);
        }else {
            return null;
        }
        return commandHandler;
    }
}
