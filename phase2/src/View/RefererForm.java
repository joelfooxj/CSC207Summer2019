package View;

import Control.RefererCommandHandler;
import Model.JobApplication;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RefererForm extends ApplicationByUserForm {

    public RefererForm(RefererCommandHandler rch) {
        super(rch, "Add Reference Letter");
    }

    public void setupAttributes() {
        super.setupAttributes();
        super.getSelectApplication().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    onChooseApplication(RefererForm.super.getJobApplicationJList().getSelectedValue());
                } catch (NullPointerException ex) {
                    return;
                }
            }
        });
    }

    public void onChooseApplication(JobApplication app) {
        TextEditorForm dialog = new TextEditorForm("", "Enter your reference letter");
        YesNo verify = new YesNo("Do you want to submit this reference letter?");
        if (verify.retBool) {
            ((RefererCommandHandler) super.getCommandHandler()).addReferenceLetter(app, dialog.text);
            MessageBox messageBox = new MessageBox("Reference Letter Submission","Reference letter submitted!");
        } else {
            MessageBox messageBox = new MessageBox("Reference Letter Submission", "Reference letter not submitted, good choice!");
        }
    }
}
