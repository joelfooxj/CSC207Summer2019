package Control;

import Model.JobApplication;

public class RefererCommandHandler implements CommandHandler {

    public void addReferenceLetter(JobApplication app, String referenceLetter) {
        app.addReferenceLetter(referenceLetter);
    }
}
