package View;

import Control.CommandHandlers.InterviewerCommandHandler;
import Control.Queries.JobApplicationQuery;
import Model.JobApplicationPackage.JobApplicationDatabase.jobAppFilterKeys;
import Model.requiredDocs;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.*;
import java.util.HashMap;
import java.util.List;

class InterviewerForm extends JDialog {
    private JPanel contentPane;
    private JButton recommendButton;
    private JButton exitButton;
    private JScrollPane appScroll;
    private JTextArea applicationText;
    private JList jobApplicationList;
    private JButton rejectButton;
    private JButton seeCVButton;
    private JButton seeCoverLetterButton;
    private JButton seeReferenceLettersButton;
    private InterviewerCommandHandler iCH;

    /**
     * Form for Interviewer Users
     * @param commandHandler
     */

    InterviewerForm(InterviewerCommandHandler commandHandler) {
        this.iCH = commandHandler;
        setContentPane(contentPane);
        setModal(true);
        this.applicationText.setEditable(false);

        this.contentPane.setBorder(BorderFactory.createTitledBorder(iCH.getUsername()));
        this.updateForm();

        exitButton.addActionListener(actionEvent -> dispose());
        recommendButton.addActionListener(e -> setRecommendedApps());

        rejectButton.addActionListener(e -> setRejectedApps());

        seeCVButton.addActionListener(e -> onCVButton());

        seeCoverLetterButton.addActionListener(e -> onCoverLetterButton());

        seeReferenceLettersButton.addActionListener(e -> onRefLettersButton());

        jobApplicationList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                String selectedAppString = (String) jobApplicationList.getSelectedValue();
                if (selectedAppString != null) {
                    checkCVCLRefButtonEnable(selectedAppString);
                   rejectButton.setEnabled(true);
                   recommendButton.setEnabled(true);
                }

            }
        });

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
    }

    private void setRecommendedApps() {
        for (Object value : this.jobApplicationList.getSelectedValuesList()) {
            String selectedString = (String) value;
            iCH.recommendApplication(Long.parseLong(JobApplicationQuery.parseListString(selectedString)));
        }
        updateForm();
    }

    private void setRejectedApps() {
        for (Object value : this.jobApplicationList.getSelectedValuesList()) {
            String selectedString = (String) value;
            iCH.rejectApplication(Long.parseLong(JobApplicationQuery.parseListString(selectedString)));
        }
        updateForm();
    }

    private HashMap<jobAppFilterKeys, Object> filterHM(String applicationString) {
        HashMap<jobAppFilterKeys, Object> newfilter = new HashMap<jobAppFilterKeys, Object>() {
            {
                put(jobAppFilterKeys.LIST_STRING, applicationString);
                put(jobAppFilterKeys.OPEN, Boolean.TRUE);
            }
        };
        return newfilter;
    }


    private void checkCVCLRefButtonEnable(String inJobAppString) {
        List<requiredDocs> requiredDocsList = this.iCH.filter.getJobAppsFilter(filterHM(inJobAppString)).getRequiredDocuments();
        this.seeCVButton.setEnabled(requiredDocsList.contains(requiredDocs.CV));
        this.seeCoverLetterButton.setEnabled(requiredDocsList.contains(requiredDocs.COVERLETTER));
        this.seeReferenceLettersButton.setEnabled(requiredDocsList.contains(requiredDocs.REFERENCELETTERS));
    }

    private void onCVButton(){
        String selectedAppString = (String) this.jobApplicationList.getSelectedValue();
        String inCV = this.iCH.filter.getJobAppsFilter(filterHM(selectedAppString)).getResume();
        GUI.messageBox("CV", inCV);
    }

    private void onCoverLetterButton(){
        String selectedAppString = (String) this.jobApplicationList.getSelectedValue();
        String inCoverLetter = this.iCH.filter.getJobAppsFilter(filterHM(selectedAppString)).getCoverLetter();
        GUI.messageBox("Cover Letter", inCoverLetter);
    }

    private void onRefLettersButton(){
        String selectedAppString = (String) this.jobApplicationList.getSelectedValue();
        String inRefLetters = this.iCH.filter.getJobAppsFilter(filterHM(selectedAppString)).getRefLetters();
        GUI.messageBox("Reference Letters", inRefLetters);
    }

    private void updateForm() {
        setButtonState(false);
        HashMap<jobAppFilterKeys, Object> filter = new HashMap<>();
        filter.put(jobAppFilterKeys.INTERVIEWER_ID, iCH.getInterviewerID());
        filter.put(jobAppFilterKeys.OPEN, Boolean.TRUE);
        JobApplicationQuery jobApplicationQuery = iCH.filter.getJobAppsFilter(filter);
        List<String> inJobAppList = jobApplicationQuery.getListStrings();
        if (inJobAppList.isEmpty()) {
            this.applicationText.setText("You have no application assigned to you.");
        } else {
            this.applicationText.setText(jobApplicationQuery.getPrintout());
        }
        this.jobApplicationList.setListData(inJobAppList.toArray());
    }

    private void setButtonState(boolean enabled) {
        recommendButton.setEnabled(enabled);
        rejectButton.setEnabled(enabled);
        seeCoverLetterButton.setEnabled(enabled);
        seeCVButton.setEnabled(enabled);
        seeReferenceLettersButton.setEnabled(enabled);
    }
}
