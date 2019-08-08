package Control.CommandHandlers;

import Control.Queries.Filter;
import Control.SessionData;

import java.time.LocalDate;


public abstract class CommandHandler {

    /**
     * Abstract class to provide concrete command handlers with
     * session data, session date and handleCommands method.
     */

    SessionData sessionData;
    LocalDate sessionDate;
    public Filter filter;

    // todo: use constructor
    public void setSessionData(SessionData sessionData) {

        this.sessionData = sessionData;
        this.filter = new Filter(sessionData);
    }

    public void setSessionDate(LocalDate sessionDate) {
        this.sessionDate = sessionDate;
    }

    /**
     * Abstract method used to execute different commands based on the user type,
     * and pass the CommandHandler object to the GUI so the GUI can interact
     * with the CommandHandler.
     */
    public abstract void handleCommands();
}
