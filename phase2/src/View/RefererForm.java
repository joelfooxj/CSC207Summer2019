package View;

import Control.RefererCommandHandler;
import Model.JobApplication;

import javax.swing.*;
import java.awt.*;
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
        JFrame frameRef = new JFrame("Enter Reference Letter");
        JPanel panelRef = new JPanel();
        JTextArea referenceLetter = new JTextArea(5, 20);
        JButton buttonSubmit = new JButton("Submit");
        JButton buttonCancel = new JButton("Cancel");
        panelRef.add(referenceLetter);
        panelRef.add(buttonSubmit);
        panelRef.add(buttonCancel);

        panelRef.setLayout(new BoxLayout(panelRef, BoxLayout.PAGE_AXIS));
        frameRef.setPreferredSize(new Dimension(500, 500));
        frameRef.setVisible(true);

        buttonSubmit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                ((RefererCommandHandler) RefererForm.super.getCommandHandler()).addReferenceLetter(app, referenceLetter.getText());
                System.exit(0);
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.exit(0);
            }
        });

    }
}
