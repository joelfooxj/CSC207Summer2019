package View.HRForms;

import Control.CommandHandlers.HrCommandHandler;
import Control.Queries.JobApplicationQuery;
import Model.JobApplicationPackage.JobApplicationDatabase.jobAppFilterKeys;
import Model.requiredDocs;
import View.GUI;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class HRApplicantOptions extends HRForm {
    private JPanel contentPane;
    private JList applicantList;
    private JList associatedApplicationsList;
    private JButton cvButton;
    private JButton coverLetterButton;
    private JButton refLetterButton;
    private JButton exitButton;
    private JLabel applicantLabel;
    private JTextArea applicationLabel;

    /**
     * This form provides HR Applicant options, including:
     * - See CV of a JobApplication of an Applicant
     * - See cover letter of a JobApplication of an Applicant
     * - See reference letters of a JobApplication of an Applicant
     * @param inHRCH: the HrCommandHandler of the parent form
     */

    HRApplicantOptions(HrCommandHandler inHRCH) {
        super(inHRCH);
        setContentPane(contentPane);
        setModal(true);

        this.contentPane.setBorder(BorderFactory.createTitledBorder(super.subMenuTitle + " - Applicant Options"));
        this.applicantList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        this.applicantList.setLayoutOrientation(JList.VERTICAL);
        this.associatedApplicationsList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        this.associatedApplicationsList.setLayoutOrientation(JList.VERTICAL);
        updateApplicantList();

        cvButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!associatedApplicationsList.isSelectionEmpty()) {
                    String inAppID = JobApplicationQuery.parseListString((String) associatedApplicationsList.getSelectedValue());

                    HashMap<jobAppFilterKeys, Object> query = new HashMap<>();
                    query.put(jobAppFilterKeys.APPLICATION_ID, Long.parseLong(inAppID));
                    String inCV = HRApplicantOptions.super.hrCH.query.getJobAppsFilter(query).getResume();
                    GUI.messageBox("CV", inCV);
                }
            }
        });

        coverLetterButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!associatedApplicationsList.isSelectionEmpty()) {
                    String inAppID = JobApplicationQuery.parseListString((String) associatedApplicationsList.getSelectedValue());

                    HashMap<jobAppFilterKeys, Object> query = new HashMap<>();
                    query.put(jobAppFilterKeys.APPLICATION_ID, Long.parseLong(inAppID));
                    String inCL = HRApplicantOptions.super.hrCH.query.getJobAppsFilter(query).getCoverLetter();
                    GUI.messageBox("Cover Letter", inCL);
                }
            }
        });

        refLetterButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!associatedApplicationsList.isSelectionEmpty()) {
                    String inAppID = JobApplicationQuery.parseListString((String) associatedApplicationsList.getSelectedValue());

                    HashMap<jobAppFilterKeys, Object> query = new HashMap<>();
                    query.put(jobAppFilterKeys.APPLICATION_ID, Long.parseLong(inAppID));
                    String inRL = HRApplicantOptions.super.hrCH.query.getJobAppsFilter(query).getRefLetters();

                    GUI.messageBox("Reference Letters", inRL);
                }
            }
        });

        associatedApplicationsList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                setApplicationDesc();
                checkRequiredButtonEnable();
            }
        });

        applicantList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                setApplicantDesc();
                updateApplicationList();
            }
        });

        exitButton.addActionListener(actionEvent -> dispose());

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
    }

    /**
     * Gets and displays the selected Applicant name
     */
    private void setApplicantDesc() {
        String applicantDesc = (String) this.applicantList.getSelectedValue();
        this.applicantLabel.setText(applicantDesc);
    }

    /**
     * Gets and displays the JobApplication description for the selected JobApplication
     */
    private void setApplicationDesc() {
        String applicationID = JobApplicationQuery.parseListString((String) this.associatedApplicationsList.getSelectedValue());
        HashMap<jobAppFilterKeys, Object> query = new HashMap<>();
        query.put(jobAppFilterKeys.APPLICATION_ID, Long.parseLong(applicationID));
        String applicationDesc = super.hrCH.query.getJobAppsFilter(query).getRepresentation();
        this.applicationLabel.setText(applicationDesc);
    }

    /**
     * Disables required documents buttons, and sets and displays the JobApplication list associated
     * with the selected JobApplicant
     */
    private void updateApplicationList() {
        disableButtons();
        String applicantString = (String) this.applicantList.getSelectedValue();
        HashMap<jobAppFilterKeys, Object> query = new HashMap<>();
        query.put(jobAppFilterKeys.APPLICANT_REPR, applicantString);
        List<String> applicationList = HRApplicantOptions.super.hrCH.query.getJobAppsFilter(query).getListStrings();
        if (applicationList.isEmpty()) {
            this.applicationLabel.setText("This applicant has not applied for any jobs.");
        } else {
            this.associatedApplicationsList.setListData(applicationList.toArray());
        }
    }

    /**
     * Disables required documents buttons, and refreshes the Applicant list and the JobApplication list
     */
    private void updateApplicantList() {
        disableButtons();
        HashMap<jobAppFilterKeys, Object> query = new HashMap<>();
        query.put(jobAppFilterKeys.FIRM_ID, HRApplicantOptions.super.hrCH.getFirmID());
        List<String> applicantList = HRApplicantOptions.super.hrCH.query.getJobAppsFilter(query).getApplicantStrings().stream().distinct().collect(Collectors.toList());
        if (applicantList.isEmpty()) {
            this.applicantLabel.setText("There are no applicants.");
        } else {
            this.applicantList.setListData(applicantList.toArray());
        }
    }

    /**
     * Disables all required documents buttons
     */
    private void disableButtons() {
        coverLetterButton.setEnabled(false);
        cvButton.setEnabled(false);
        refLetterButton.setEnabled(false);
    }

    /**
     * Gets the list of required documents for the selected JobApplication
     * and enables the required documents buttons accordingly
     */
    private void checkRequiredButtonEnable() {
        String selectedAppString = (String) this.associatedApplicationsList.getSelectedValue();
        HashMap<jobAppFilterKeys, Object> query = new HashMap<>();
        query.put(jobAppFilterKeys.LIST_STRING, selectedAppString);
        List<requiredDocs> inDocs = HRApplicantOptions.super.hrCH.query.getJobAppsFilter(query).getRequiredDocuments();
        coverLetterButton.setEnabled(inDocs.contains(requiredDocs.COVERLETTER));
        cvButton.setEnabled(inDocs.contains(requiredDocs.CV));
        refLetterButton.setEnabled(inDocs.contains(requiredDocs.REFERENCELETTERS));
    }
}
