package View;

import Control.ApplicantCommandHandler;
import Control.Queries.JobApplicationQuery;
import Model.requiredDocs;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.*;
import java.util.HashMap;
import java.util.List;

import Model.JobApplicationDatabase.jobAppFilterKeys;
import Model.JobPostingDatabase.jobPostingFilters;

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

    public ApplicantApplicationForm(ApplicantCommandHandler appCH) {
        super(appCH);
        this.appCH = super.appCH;
        setContentPane(contentPane);
        setModal(true);
        this.appList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        this.appList.setLayoutOrientation(JList.VERTICAL);

        this.updateForm();

        CVButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openCVForm();
            }
        });

        coverletterButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openCoverLetterForm();
            }
        });

        referenceLettersButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openRefLettersForm();
            }
        });

        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        withdrawButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                withdrawConfirmation();
            }
        });

        /**
         * On selecting a jobApplication from appList, do the following:
         * - Enable buttons for CV, CoverLetter and Reference Letters accordingly.
         * - Enable withdraw button .
         * - Set the descriptions for the jobApplication and the jobPosting associated with it.
         */
        appList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                String selectedAppID = (String) appList.getSelectedValue();
                if (selectedAppID != null) {
                    checkCVCLRefButtonEnable(selectedAppID);
                    withdrawButton.setEnabled(true);
                    Long inJobPostingID = appCH.filter.getJobAppsFilter(filterHM(selectedAppID)).getJobID();
                    HashMap<jobPostingFilters, Object> jobPostingHM = new HashMap<>();
                    jobPostingHM.put(jobPostingFilters.JOB_ID, inJobPostingID);
                    jobPostingHM.put(jobPostingFilters.OPEN, Boolean.TRUE);
                    String inJobPostDesc = appCH.filter.getJobPostsFilter(jobPostingHM).getRepresentation();
                    String inJobAppDesc = appCH.filter.getJobAppsFilter(filterHM(selectedAppID)).getRepresentation();
                    jobTextArea.setText(inJobAppDesc);
                    appTextArea.setText(inJobPostDesc);
                }
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

    private HashMap<jobAppFilterKeys, Object> filterHM(String appListString) {
        return new HashMap<jobAppFilterKeys, Object>() {
            {
                put(jobAppFilterKeys.LIST_STRING, appListString);
                put(jobAppFilterKeys.OPEN, Boolean.TRUE);
            }
        };
    }

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

    private void openCVForm() {
        String selectedAppListString = (String) this.appList.getSelectedValue();
        String inCV = this.appCH.filter.getJobAppsFilter(filterHM(selectedAppListString)).getResume();
        if (inCV == null) {
            inCV = "";
        }
        String outCV = GUI.editTextForm(inCV, "CV editor");
        if (outCV != null) {
            this.appCH.setApplicationCV(JobApplicationQuery.parseListString(selectedAppListString), outCV);
        }
    }

    private void openCoverLetterForm() {
        String selectedAppListString = (String) this.appList.getSelectedValue();
        String inCoverLetter = this.appCH.filter.getJobAppsFilter(filterHM(selectedAppListString)).getCoverLetter();
        if (inCoverLetter == null) {
            inCoverLetter = "";
        }
        String outCoverLetter = GUI.editTextForm(inCoverLetter, "Cover Letter editor");
        if (outCoverLetter != null) {
            this.appCH.setApplicationCoverLetter(JobApplicationQuery.parseListString(selectedAppListString), outCoverLetter);
        }
    }

    private void openRefLettersForm(){
        String selectedAppListString = (String) this.appList.getSelectedValue();
        String inRefLetters= this.appCH.filter.getJobAppsFilter(filterHM(selectedAppListString)).getRefLetters();
        if (inRefLetters == null) {
            inRefLetters = "";
        }
        GUI.messageBox("Reference Letters", inRefLetters);
    }

    private void checkCVCLRefButtonEnable(String appListString) {
        List<requiredDocs> requiredDocsList = this.appCH.filter.getJobAppsFilter(filterHM(appListString)).getRequiredDocuments();
        this.CVButton.setEnabled(requiredDocsList.contains(requiredDocs.CV));
        this.coverletterButton.setEnabled(requiredDocsList.contains(requiredDocs.COVERLETTER));
        this.referenceLettersButton.setEnabled(requiredDocsList.contains(requiredDocs.REFERENCELETTERS));
    }

    private void updateForm() {
        setButtonStatus(false);
        HashMap<jobAppFilterKeys, Object> filterHM = new HashMap<jobAppFilterKeys, Object>() {
            {
                put(jobAppFilterKeys.OPEN, Boolean.TRUE);
                put(jobAppFilterKeys.APPLICANT_ID, appCH.getApplicantID());
            }
        };
        List<String> inJobAppIDs = this.appCH.filter.getJobAppsFilter(filterHM).getListStrings();
        if (inJobAppIDs.isEmpty()) {
            this.appTextArea.setText("You have no open applications.");
            this.jobTextArea.setText("You have no open applications.");
        }
        this.appList.setListData(inJobAppIDs.toArray());
    }

    private void setButtonStatus(boolean enabled) {
        this.withdrawButton.setEnabled(enabled);
        this.coverletterButton.setEnabled(enabled);
        this.CVButton.setEnabled(enabled);
        this.coverletterButton.setEnabled(enabled);
        this.referenceLettersButton.setEnabled(enabled);
    }
}
