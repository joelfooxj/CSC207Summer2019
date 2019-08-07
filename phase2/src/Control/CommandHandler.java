package Control;

import Control.Queries.Filter;

import java.time.LocalDate;


public abstract class CommandHandler {

    SessionData sessionData;
    LocalDate sessionDate;
    public Filter filter;

    // todo: use constructor
    void setSessionData(SessionData sessionData) {

        this.sessionData = sessionData;
        this.filter = new Filter(sessionData);
    }

    void setSessionDate(LocalDate sessionDate) {
        this.sessionDate = sessionDate;
    }


    public abstract void handleCommands();
}
