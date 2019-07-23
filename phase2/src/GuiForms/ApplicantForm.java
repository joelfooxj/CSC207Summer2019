package GuiForms;

import CommandHandlers.ApplicantCommandHandler;

import javax.swing.*;
import java.awt.event.*;

public class ApplicantForm extends JDialog {
    private JPanel contentPane;
    private JButton jobsButton;
    private JButton Applications;
    private JLabel creationDateLabel;
    private JLabel applicantIDLabel;
    private JTextArea openAppsTextArea;
    private JTextArea closedAppsTextArea;
    private JLabel errorLabel;
    private JButton exitButton;
    private JLabel minDaysLabel;
    protected ApplicantCommandHandler appCH;

    public ApplicantForm(ApplicantCommandHandler appCH) {
        this.appCH = appCH;
        setContentPane(contentPane);
        setModal(true);

        this.contentPane.setBorder(BorderFactory.createTitledBorder(this.appCH.getUsername()));
        this.creationDateLabel.setText(this.appCH.getCreationDate());
        this.applicantIDLabel.setText(this.appCH.getApplicantID());
        this.updateForm();

        jobsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onJobs();
            }
        });

        Applications.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onApps();
            }
        });

        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
    }

    private void onJobs(){
        ApplicantJobsForm appJobsForm = new ApplicantJobsForm(this.appCH);
        appJobsForm.pack();
        appJobsForm.setVisible(true);
        this.appCH.applyForJobs(appJobsForm.selectedJobIDs);
        this.updateForm();
    }

    // todo: complete
    private void onApps(){
        ApplicantApplicationForm appApplicationForm = new ApplicantApplicationForm();
        appApplicationForm.pack();
        appApplicationForm.setVisible(true);
        this.updateForm();

    }

    private void updateForm(){
        this.openAppsTextArea.setText(this.appCH.getApplicationsPrintout(true));
        this.closedAppsTextArea.setText(this.appCH.getApplicationsPrintout(false));
        this.minDaysLabel.setText("It's been " + this.appCH.getMinDays() + " days since your last closed application.");
    }

}
