package View.ApplicantForms;

import Control.CommandHandlers.ApplicantCommandHandler;
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
    private JScrollPane openAppScroll;
    private JTextArea openAppsTextArea;
    private JScrollPane closedAppScroll;
    private JTextArea closedAppsTextArea;
    private JButton exitButton;
    private JLabel minDaysLabel;
    ApplicantCommandHandler appCH;

    public ApplicantForm(ApplicantCommandHandler appCH) {
        this.appCH = appCH;
        setContentPane(contentPane);
        setModal(true);

        this.closedAppsTextArea.setEditable(false);
        this.openAppsTextArea.setEditable(false);

        this.contentPane.setBorder(BorderFactory.createTitledBorder(this.appCH.getUsername()));
        this.creationDateLabel.setText(this.appCH.getCreationDate());
        this.applicantIDLabel.setText(this.appCH.getApplicantID().toString());
        this.updateForm();

        jobsButton.addActionListener(actionEvent -> onJobs());
        Applications.addActionListener(actionEvent -> onApps());
        exitButton.addActionListener(actionEvent -> dispose());

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
    }

    private void onJobs() {
        ApplicantJobsForm appJobsForm = new ApplicantJobsForm(this.appCH);
        appJobsForm.pack();
        appJobsForm.setSize(500, 500);
        appJobsForm.setVisible(true);
        this.updateForm();
    }

    private void onApps() {
        ApplicantApplicationForm appApplicationForm = new ApplicantApplicationForm(this.appCH);
        appApplicationForm.pack();
        appApplicationForm.setSize(500, 500);
        appApplicationForm.setVisible(true);
        this.updateForm();

    }

    private void updateForm() {
        this.updateAppFields(Boolean.TRUE);
        this.updateAppFields(Boolean.FALSE);
        this.minDaysLabel.setText("It's been " + this.appCH.getMinDays() + " days since your last closed application.");
    }

    private void updateAppFields(Boolean open) {
        HashMap<jobAppFilterKeys, Object> filterHM = new HashMap<>();
        filterHM.put(jobAppFilterKeys.APPLICANT_ID, this.appCH.getApplicantID());
        filterHM.put(jobAppFilterKeys.OPEN, open);
        String inJobAppPrintout = this.appCH.filter.getJobAppsFilter(filterHM).getPrintout();
        if (open.equals(true)) {
            this.openAppsTextArea.setText(inJobAppPrintout);
        } else {
            this.closedAppsTextArea.setText(inJobAppPrintout);
        }
    }
}
