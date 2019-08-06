package View;

import Control.HrCommandHandler;


import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.*;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import Model.JobApplicationDatabase.jobAppFilterKeys;
import Model.UserCredentialsDatabase.usersFilterKeys;
//import com.sun.org.apache.xpath.internal.operations.Bool;

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

    public HRApplicantOptions(HrCommandHandler inHRCH) {
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
                if (!associatedApplicationsList.isSelectionEmpty()){
                    String inAppID = (String) associatedApplicationsList.getSelectedValue();

                    HashMap<jobAppFilterKeys, Object> query = new HashMap<>();
                    query.put(jobAppFilterKeys.APPLICATION_ID, Long.parseLong(inAppID));
                    String inCV = HRApplicantOptions.super.hrCH.filter.getJobAppsFilter(query).getResume();
                    GUI.messageBox("CV", "<html>" + inCV + "</html>");
                }
            }
        });

        coverLetterButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!associatedApplicationsList.isSelectionEmpty()){
                    String inAppID = (String) associatedApplicationsList.getSelectedValue();

                    HashMap<jobAppFilterKeys, Object> query = new HashMap<>();
                    query.put(jobAppFilterKeys.APPLICATION_ID, Long.parseLong(inAppID));
                    String inCL = HRApplicantOptions.super.hrCH.filter.getJobAppsFilter(query).getCoverLetter();
                    GUI.messageBox("Cover Letter", "<html>" + inCL + "</html>");
                }
            }
        });

        refLetterButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!associatedApplicationsList.isSelectionEmpty()){
                    String inAppID = (String) associatedApplicationsList.getSelectedValue();

                    HashMap<jobAppFilterKeys, Object> query = new HashMap<>();
                    query.put(jobAppFilterKeys.APPLICATION_ID, Long.parseLong(inAppID));
                    String inRL = HRApplicantOptions.super.hrCH.filter.getJobAppsFilter(query).getRefLetters();

                    GUI.messageBox("Reference Letters", "<html>" + inRL + "</html>");
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

    private void setApplicantDesc(){
        String applicantID = (String) this.applicantList.getSelectedValue();

        HashMap<usersFilterKeys, Object> query = new HashMap<>();
        query.put(usersFilterKeys.USER_ID, Long.parseLong(applicantID));

        String applicantDesc = super.hrCH.filter.getUsersFilter(query).getRepresentation();

        this.applicantLabel.setText(applicantDesc);
    }

    private void setApplicationDesc(){
        String applicationID = (String) this.associatedApplicationsList.getSelectedValue();


        HashMap<jobAppFilterKeys, Object> query = new HashMap<>();
        query.put(jobAppFilterKeys.APPLICATION_ID, Long.parseLong(applicationID));
        String applicationDesc = super.hrCH.filter.getJobAppsFilter(query).getRepresentation();

        this.applicationLabel.setText(applicationDesc);
    }

    private void updateApplicationList(){
        disableButtons();
        String applicantID = (String) this.applicantList.getSelectedValue();

        HashMap<jobAppFilterKeys, Object> query = new HashMap<>();
        query.put(jobAppFilterKeys.APPLICANT_ID, Long.parseLong(applicantID));
        List<String> applicationList = HRApplicantOptions.super.hrCH.filter.getJobAppsFilter(query).getJobAppsID();

        if (applicationList.isEmpty()){
                this.applicationLabel.setText("This applicant has not applied for any jobs.");
        } else{
            this.associatedApplicationsList.setListData(applicationList.toArray());
        }

    }

    private void updateApplicantList(){
        disableButtons();


        HashMap<jobAppFilterKeys, Object> query = new HashMap<>();
        query.put(jobAppFilterKeys.FIRM_ID, Long.parseLong(HRApplicantOptions.super.hrCH.getFirmID()));
        List<String> applicantList = HRApplicantOptions.super.hrCH.filter.getJobAppsFilter(query).getApplicantIDs().stream().distinct().collect(Collectors.toList());
        if (applicantList == null){
            this.applicantLabel.setText("There are no applicants.");
        } else{
            this.applicantList.setListData(applicantList.toArray());
        }
    }

    private void disableButtons(){
        coverLetterButton.setEnabled(false);
        cvButton.setEnabled(false);
        refLetterButton.setEnabled(false);
    }

    private void checkRequiredButtonEnable(){
        String selectedAppID = (String) this.associatedApplicationsList.getSelectedValue();


        HashMap<jobAppFilterKeys, Object> query = new HashMap<>();
        query.put(jobAppFilterKeys.APPLICATION_ID, Long.parseLong(selectedAppID));
        List<String> inDocs = HRApplicantOptions.super.hrCH.filter.getJobAppsFilter(query).getRequiredDocuments();

        coverLetterButton.setEnabled(inDocs.contains("Cover Letter"));
        cvButton.setEnabled(inDocs.contains("CV"));
        refLetterButton.setEnabled(inDocs.contains("Reference Letters"));
    }
}
