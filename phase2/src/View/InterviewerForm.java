package View;

import Control.CommandHandlers.InterviewerCommandHandler;
import Control.Queries.JobApplicationQuery;
import Model.JobApplicationPackage.JobApplicationDatabase.jobAppFilterKeys;
import Model.requiredDocs;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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
     * This form provides the Interviewer user with the following options:
     * - View required documents for a JobApplication
     * - Reject/Recommend a JobApplication
     *
     * @param commandHandler: InterviewerCommandHandler that calls this form
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

    /**
     * This method recommends the selected JobApplications and updates the list
     * of JobApplications accordingly
     */
    private void setRecommendedApps() {
        for (Object value : this.jobApplicationList.getSelectedValuesList()) {
            String selectedString = (String) value;
            iCH.recommendApplication(Long.parseLong(JobApplicationQuery.parseListString(selectedString)));
        }
        updateForm();
    }

    /**
     * This method rejects the selected JobApplications and updates the list
     * of JobApplications accordingly
     */
    private void setRejectedApps() {
        for (Object value : this.jobApplicationList.getSelectedValuesList()) {
            String selectedString = (String) value;
            iCH.rejectApplication(Long.parseLong(JobApplicationQuery.parseListString(selectedString)));
        }
        updateForm();
    }

    /**
     * This creates the HashMap filter for JobApplications that are open and match
     * the given String representation.
     * @param applicationString: The String representation of the selected JobApplication
     * @return HashMap filter
     */
    private HashMap<jobAppFilterKeys, Object> filterHM(String applicationString) {
        HashMap<jobAppFilterKeys, Object> newfilter = new HashMap<jobAppFilterKeys, Object>() {
            {
                put(jobAppFilterKeys.LIST_STRING, applicationString);
                put(jobAppFilterKeys.OPEN, Boolean.TRUE);
            }
        };
        return newfilter;
    }


    /**
     * This method enables/disables the required documents buttons based on
     * the selected JobApplications.
     * @param inJobAppString: The String representation of the selected JobApplication
     */
    private void checkCVCLRefButtonEnable(String inJobAppString) {
        List<requiredDocs> requiredDocsList = this.iCH.query.getJobAppsFilter(filterHM(inJobAppString)).getRequiredDocuments();
        this.seeCVButton.setEnabled(requiredDocsList.contains(requiredDocs.CV));
        this.seeCoverLetterButton.setEnabled(requiredDocsList.contains(requiredDocs.COVERLETTER));
        this.seeReferenceLettersButton.setEnabled(requiredDocsList.contains(requiredDocs.REFERENCELETTERS));
    }

    /**
     * This method displays the selected JobApplications's CV.
     */
    private void onCVButton() {
        String selectedAppString = (String) this.jobApplicationList.getSelectedValue();
        String inCV = this.iCH.query.getJobAppsFilter(filterHM(selectedAppString)).getResume();
        GUI.messageBox("CV", inCV);
    }

    /**
     * This method displays the selected JobApplications's cover letter.
     */
    private void onCoverLetterButton() {
        String selectedAppString = (String) this.jobApplicationList.getSelectedValue();
        String inCoverLetter = this.iCH.query.getJobAppsFilter(filterHM(selectedAppString)).getCoverLetter();
        GUI.messageBox("Cover Letter", inCoverLetter);
    }

    /**
     * This method displays the selected JobApplication's Reference Letters
     */
    private void onRefLettersButton() {
        String selectedAppString = (String) this.jobApplicationList.getSelectedValue();
        String inRefLetters = this.iCH.query.getJobAppsFilter(filterHM(selectedAppString)).getRefLetters();
        GUI.messageBox("Reference Letters", inRefLetters);
    }

    /**
     * This method gets the list of JobApplications that have been assigned to this
     * interviewer and displays them for selection.
     */
    private void updateForm() {
        setButtonState(false);
        HashMap<jobAppFilterKeys, Object> filter = new HashMap<>();
        filter.put(jobAppFilterKeys.INTERVIEWER_ID, iCH.getInterviewerID());
        filter.put(jobAppFilterKeys.OPEN, Boolean.TRUE);
        JobApplicationQuery jobApplicationQuery = iCH.query.getJobAppsFilter(filter);
        List<String> inJobAppList = jobApplicationQuery.getListStrings();
        if (inJobAppList.isEmpty()) {
            this.applicationText.setText("You have no application assigned to you.");
        } else {
            this.applicationText.setText(jobApplicationQuery.getRepresentations());
        }
        this.jobApplicationList.setListData(inJobAppList.toArray());
    }

    /**
     * This sets the state of the following buttons to enabled/disabled:
     * - Recommend
     * - Reject
     * - Required Document buttons
     * @param enabled: the boolean state to set the buttons to
     */
    private void setButtonState(boolean enabled) {
        recommendButton.setEnabled(enabled);
        rejectButton.setEnabled(enabled);
        seeCoverLetterButton.setEnabled(enabled);
        seeCVButton.setEnabled(enabled);
        seeReferenceLettersButton.setEnabled(enabled);
    }
}
