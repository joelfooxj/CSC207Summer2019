package CommandHandlers;

import Databases.JobApplication;

public class RefererCommandHandler implements CommandHandler {

    public void addReferenceLetter(JobApplication app, String referenceLetter) {
        app.addReferenceLetter(referenceLetter);
    }
}
