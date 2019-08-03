package View;

import Control.HrCommandHandler;


import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.*;
import java.util.List;

public class HRApplicantOptions extends HRForm {
    private JPanel contentPane;
    private JList applicantList;
    private JList associatedApplicationsList;
    private JButton cvButton;
    private JButton coverLetterButton;
    private JButton refLetterButton;
    private JButton exitButton;
    private JLabel applicantLabel;
    private JLabel applicationLabel;

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
                    String inCV = HRApplicantOptions.super.hrCH.getApplicationCVbyApplicationID(inAppID);
                    GUI.messageBox("CV", "<html>" + inCV + "</html>");
                }
            }
        });

        coverLetterButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!associatedApplicationsList.isSelectionEmpty()){
                    String inAppID = (String) associatedApplicationsList.getSelectedValue();
                    String inCL = HRApplicantOptions.super.hrCH.getApplicationCoverLetterbyApplicationID(inAppID);
                    GUI.messageBox("Cover Letter", "<html>" + inCL + "</html>");
                }
            }
        });

        refLetterButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!associatedApplicationsList.isSelectionEmpty()){
                    String inAppID = (String) associatedApplicationsList.getSelectedValue();
                    String inRL = HRApplicantOptions.super.hrCH.getApplicationRLsByApplicationID(inAppID);
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
        String applicantDesc = super.hrCH.getApplicantDescByApplicantID(applicantID);
        this.applicantLabel.setText(applicantDesc);
    }

    private void setApplicationDesc(){
        String applicationID = (String) this.associatedApplicationsList.getSelectedValue();
        String applicationDesc = super.hrCH.getApplicationDescByApplicationID(applicationID);
        this.applicationLabel.setText(applicationDesc);
    }

    private void updateApplicationList(){
        disableButtons();
        String applicantID = (String) this.applicantList.getSelectedValue();
        List<String> applicationList = super.hrCH.getApplicationIDsByApplicantID(applicantID);
        if (applicationList.isEmpty()){
                this.applicationLabel.setText("This applicant has not applied for any jobs.");
        } else{
            this.associatedApplicationsList.setListData(applicationList.toArray());
        }

    }

    private void updateApplicantList(){
        disableButtons();
        List<String> applicantList = super.hrCH.getApplicantIDsByFirmID();
        if (applicantList.isEmpty()){
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
        List<String> inDocs = this.hrCH.checkJobAppRequiredDocs(selectedAppID);
        coverLetterButton.setEnabled(inDocs.contains("Cover Letter"));
        cvButton.setEnabled(inDocs.contains("CV"));
        refLetterButton.setEnabled(inDocs.contains("Reference Letters"));
    }
}
