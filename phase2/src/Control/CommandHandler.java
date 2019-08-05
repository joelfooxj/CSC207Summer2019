package Control;

import Control.Queries.Filter;

import java.time.LocalDate;


public abstract class CommandHandler {

    public static SessionData sessionData;
    public static LocalDate sessionDate;
    public Filter filter = new Filter(sessionData);


    public static void setSessionData(SessionData sessionData) {
        CommandHandler.sessionData = sessionData;
    }

    public static void setSessionDate(LocalDate sessionDate){
        CommandHandler.sessionDate = sessionDate;
    }


    public abstract void handleCommands();
}
