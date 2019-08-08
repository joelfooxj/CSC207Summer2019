package View.ApplicantForms;

import Control.CommandHandlers.ApplicantCommandHandler;
import Control.Queries.JobApplicationQuery;
import Model.JobApplicationPackage.JobApplicationDatabase.jobAppFilterKeys;
import Model.JobPostingPackage.JobPostingDatabase.jobPostingFilters;
import Model.requiredDocs;
import View.GUI;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.List;

public class ApplicantApplicationForm extends ApplicantForm {
    private JPanel contentPane;
    private JButton CVButton;
    private JButton coverletterButton;
    private JList appList;
    private JTextArea appTextArea;
    private JButton withdrawButton;
    private JButton exitButton;
    private JScrollPane jobScrollPane;
    private JTextArea jobTextArea;
    private JButton referenceLettersButton;

    private ApplicantCommandHandler appCH;

    /**
     * This form provides Applicant Application options, including:
     * - Withdrawing from a JobApplication
     * - Editing the CV of a JobApplication
     * - Editing the cover letter of a JobApplication
     * - Seeing the reference letters of a JobApplication
     * @param appCH: the ApplicantCommandHandler of the parent form
     */
    ApplicantApplicationForm(ApplicantCommandHandler appCH) {
        super(appCH);
        this.appCH = super.appCH;
        setContentPane(contentPane);
        setModal(true);
        this.appList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        this.appList.setLayoutOrientation(JList.VERTICAL);

        this.updateForm();

        CVButton.addActionListener(actionEvent -> openCVForm());
        coverletterButton.addActionListener(actionEvent -> openCoverLetterForm());
        referenceLettersButton.addActionListener(actionEvent -> openRefLettersForm());
        exitButton.addActionListener(actionEvent -> dispose());
        withdrawButton.addActionListener(actionEvent -> withdrawConfirmation());

        appList.addListSelectionListener(listSelectionEvent -> appListSelection());

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
    }

    /**
     * This returns a HashMap query that filters for JobApplications that are Open and
     * associated with the JobApplication
     *
     * @param appListString: the selected JobApplication ID
     * @return HashMap query to pass to the Query class
     */
    private HashMap<jobAppFilterKeys, Object> filterHM(String appListString) {
        return new HashMap<jobAppFilterKeys, Object>() {
            {
                put(jobAppFilterKeys.LIST_STRING, appListString);
                put(jobAppFilterKeys.OPEN, Boolean.TRUE);
            }
        };
    }

    /**
     * This executes the following when selecting an item from the JobApplication list:
     * - Enables CV, Cover Letter, Reference Letters, Withdraw button accordingly
     * - Gets and displays the JobApplication description and JobPosting description
     */
    private void appListSelection() {
        String selectedAppID = (String) appList.getSelectedValue();
        if (selectedAppID != null) {
            checkCVCLRefButtonEnable(selectedAppID);
            withdrawButton.setEnabled(true);
            Long inJobPostingID = appCH.query.getJobAppsFilter(filterHM(selectedAppID)).getJobID();
            HashMap<jobPostingFilters, Object> jobPostingHM = new HashMap<>();
            jobPostingHM.put(jobPostingFilters.JOB_ID, inJobPostingID);
            jobPostingHM.put(jobPostingFilters.OPEN, Boolean.TRUE);
            String inJobPostDesc = appCH.query.getJobPostsFilter(jobPostingHM).getRepresentation();
            String inJobAppDesc = appCH.query.getJobAppsFilter(filterHM(selectedAppID)).getRepresentation();
            jobTextArea.setText(inJobAppDesc);
            appTextArea.setText(inJobPostDesc);
        }
    }

    /**
     * Withdraws from a JobApplication and removes it from the JobApplication list
     */
    private void withdrawConfirmation() {
        boolean confirm = GUI.yesNoForm("Are you sure you want to withdraw? ");
        if (confirm) {
            String appListString = (String) this.appList.getSelectedValue();
            this.appCH.withdrawApplication(JobApplicationQuery.parseListString(appListString));
            this.appTextArea.setText("");
            this.jobTextArea.setText("");
            this.appList.clearSelection();
            this.updateForm();
        }
    }

    /**
     * Opens a TextEditorForm for editing the CV of the selected JobApplication
     */
    private void openCVForm() {
        String selectedAppListString = (String) this.appList.getSelectedValue();
        String inCV = this.appCH.query.getJobAppsFilter(filterHM(selectedAppListString)).getResume();
        if (inCV == null) {
            inCV = "";
        }
        String outCV = GUI.editTextForm(inCV, "CV editor");
        if (outCV != null) {
            this.appCH.setApplicationCV(JobApplicationQuery.parseListString(selectedAppListString), outCV);
        }
    }

    /**
     * Opens a TextEditorForm for editing the cover letter of the selected JobApplication
     */
    private void openCoverLetterForm() {
        String selectedAppListString = (String) this.appList.getSelectedValue();
        String inCoverLetter = this.appCH.query.getJobAppsFilter(filterHM(selectedAppListString)).getCoverLetter();
        if (inCoverLetter == null) {
            inCoverLetter = "";
        }
        String outCoverLetter = GUI.editTextForm(inCoverLetter, "Cover Letter editor");
        if (outCoverLetter != null) {
            this.appCH.setApplicationCoverLetter(JobApplicationQuery.parseListString(selectedAppListString), outCoverLetter);
        }
    }


    /**
     * Opens a messageBox displaying the reference letters for the selected JobApplication
     */
    private void openRefLettersForm() {
        String selectedAppListString = (String) this.appList.getSelectedValue();
        String inRefLetters= this.appCH.query.getJobAppsFilter(filterHM(selectedAppListString)).getRefLetters();

        if (inRefLetters == null) {
            inRefLetters = "";
        }
        GUI.messageBox("Reference Letters", inRefLetters);
    }

    /**
     * This method checks for the required Documents of the selected JobApplication and
     * enables the document buttons appropriately.
     *
     * @param appListString: The ID of the selected JobApplication
     */
    private void checkCVCLRefButtonEnable(String appListString) {
        List<requiredDocs> requiredDocsList = this.appCH.query.getJobAppsFilter(filterHM(appListString)).getRequiredDocuments();
        this.CVButton.setEnabled(requiredDocsList.contains(requiredDocs.CV));
        this.coverletterButton.setEnabled(requiredDocsList.contains(requiredDocs.COVERLETTER));
        this.referenceLettersButton.setEnabled(requiredDocsList.contains(requiredDocs.REFERENCELETTERS));
    }

    /**
     * This methods gets a list of open JobApplication names associated with
     * this applicant, and populates the JobApplication list
     */
    private void updateForm() {
        setButtonStatus(false);
        HashMap<jobAppFilterKeys, Object> filterHM = new HashMap<jobAppFilterKeys, Object>() {
            {
                put(jobAppFilterKeys.OPEN, Boolean.TRUE);
                put(jobAppFilterKeys.APPLICANT_ID, appCH.getApplicantID());
            }
        };
        List<String> inJobAppIDs = this.appCH.query.getJobAppsFilter(filterHM).getListStrings();
        if (inJobAppIDs.isEmpty()) {
            this.appTextArea.setText("You have no open applications.");
            this.jobTextArea.setText("You have no open applications.");
        }
        this.appList.setListData(inJobAppIDs.toArray());
    }

    /**
     * This method sets the following buttons to enabled if true,
     * disabled if false:
     * - Withdraw
     * - Cover Letter
     * - CV
     * - Reference Letters
     *
     * @param enabled: boolean representing the button state
     */
    private void setButtonStatus(boolean enabled) {
        this.withdrawButton.setEnabled(enabled);
        this.coverletterButton.setEnabled(enabled);
        this.CVButton.setEnabled(enabled);
        this.referenceLettersButton.setEnabled(enabled);
    }
}
