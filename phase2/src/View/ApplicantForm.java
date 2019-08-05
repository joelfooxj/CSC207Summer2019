package View;

import Control.ApplicantCommandHandler;
import Model.JobApplicationDatabase.jobAppFilterKeys;

import javax.swing.*;
import java.awt.event.*;
import java.util.HashMap;

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
        this.updateForm();
    }

    private void onApps(){
        ApplicantApplicationForm appApplicationForm = new ApplicantApplicationForm(this.appCH);
        appApplicationForm.pack();
        appApplicationForm.setVisible(true);
        this.updateForm();

    }

    private void updateForm(){
        this.updateAppFields(1L);
        this.updateAppFields(0L);
        this.minDaysLabel.setText("It's been " + this.appCH.getMinDays() + " days since your last closed application.");
    }

    private void updateAppFields(Long open){
        HashMap<jobAppFilterKeys, Long> filterHM = new HashMap<>();
        filterHM.put(jobAppFilterKeys.APPLICANT_ID, Long.parseLong(this.appCH.getApplicantID()));
        filterHM.put(jobAppFilterKeys.OPEN, open);
        String inJobAppPrintout = this.appCH.filter.getJobApplicationsFilter(filterHM).getPrintout();
        this.openAppsTextArea.setText(inJobAppPrintout);
    }
}
