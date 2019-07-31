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

        this.applicantList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        this.applicantList.setLayoutOrientation(JList.VERTICAL);
        this.associatedApplicationsList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        this.associatedApplicationsList.setLayoutOrientation(JList.VERTICAL);

        this.contentPane.setBorder(BorderFactory.createTitledBorder(super.subMenuTitle + " - Applicant Options"));
        updateApplicantList();
        updateApplicationList();
        updateButtonEnabled();


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
                updateButtonEnabled();
            }
        });

        applicantList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                setApplicantDesc();
                updateApplicationList();
                updateButtonEnabled();
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
        String applicantID = (String) this.applicantList.getSelectedValue();
        List<String> applicationList = super.hrCH.getApplicationIDsByApplicantID(applicantID);
        this.associatedApplicationsList.setListData(applicationList.toArray());
        updateButtonEnabled();
    }

    private void updateApplicantList(){
        this.applicantList.setListData(super.hrCH.getApplicantIDsByFirmID().toArray());
        this.applicantList.setSelectedIndex(0);
    }

    // call whenever associatedApplicationsList is updated or selected.
    private void updateButtonEnabled(){
        boolean empty = associatedApplicationsList.isSelectionEmpty();
        coverLetterButton.setEnabled(!empty);
        cvButton.setEnabled(!empty);
        refLetterButton.setEnabled(!empty);
    }
}
