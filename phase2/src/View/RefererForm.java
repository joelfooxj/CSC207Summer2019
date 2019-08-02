package View;

import Control.HyreLauncher;
import Control.RefererCommandHandler;
import Model.JobApplication;
import Model.UserCredentials;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class RefererForm extends ApplicationByUserForm {

    public RefererForm(RefererCommandHandler rch) {
        super(rch, "Add Reference Letter");
        setupAttributes();
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
                RefererForm.super.getRch().addReferenceLetter(app, referenceLetter.getText());
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

//    public static void main(String[] args) {
//        RefererForm rf = new RefererForm();
//    }
}
