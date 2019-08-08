package View.HRForms;

import View.GUI;
import Control.CommandHandlers.HrCommandHandler;
import Control.Queries.JobApplicationQuery;
import Control.Queries.JobPostQuery;
import Model.JobApplicationPackage.JobApplicationDatabase;
import Model.JobPostingPackage.JobPostingDatabase;
import Model.UserCredentialsPackage.UserCredentials;
import Model.UserCredentialsPackage.UserCredentialsDatabase;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.util.HashMap;
import java.util.List;

public class HRInterviewerForm extends HRForm {
    private JPanel panel;
    private JScrollPane jobPane;
    private JList<String> jobPostingJList;
    private JScrollPane appPane;
    private JList<String> jobApplicationJList;
    private JTextArea detailedApp;
    private JButton buttonChooseInterviewer;
    private JButton buttonExit;
    private JLabel jobItems;
    private JLabel appItems;
    private JLabel appDesc;

    /**
     * This form provides HR Interviewer options, including:
     * - Assigning Interviewers to JobApplications
     * @param inHRCH: HrCommandHandler of the parent form
     */

    HRInterviewerForm(HrCommandHandler inHRCH) {
        super(inHRCH);
        setContentPane(panel);
        setModal(true);
        setupAttributes();
        this.panel.setBorder(BorderFactory.createTitledBorder("Interviewer Assignment Options"));
        HashMap<JobPostingDatabase.jobPostingFilters, Object> filter = new HashMap<>();
        filter.put(JobPostingDatabase.jobPostingFilters.FIRM, HRInterviewerForm.super.hrCH.getFirmID());
        filter.put(JobPostingDatabase.jobPostingFilters.OPEN, Boolean.TRUE);
        List<String> jobPostingList = hrCH.query.getJobPostsFilter(filter).getRepresentationsList();
        jobPostingJList.setListData(jobPostingList.toArray(new String[jobPostingList.size()]));
        buttonChooseInterviewer.setEnabled(false);

        jobApplicationJList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        jobApplicationJList.setVisibleRowCount(-1);
    }

    private void setupAttributes() {
        jobPostingJList.addListSelectionListener(listSelectionEvent -> onJobSelection());

        jobApplicationJList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent listSelectionEvent) {
                if (jobApplicationJList.getSelectedValue() != null) {
                    onAppSelection(jobApplicationJList.getSelectedValue());
                }
            }
        });

        buttonChooseInterviewer.addActionListener(actionEvent -> onChoose(jobApplicationJList.getSelectedValue()));
        buttonExit.addActionListener(actionEvent -> dispose());
    }

    /**
     * Gets and displays the JobApplications associated with the selected JobPosting
     */
    private void onJobSelection() {
        String jobListString = jobPostingJList.getSelectedValue();
        HashMap<JobApplicationDatabase.jobAppFilterKeys, Object> filter = new HashMap<>();
        filter.put(JobApplicationDatabase.jobAppFilterKeys.JOB_ID, Long.parseLong(JobPostQuery.parseListString(jobListString)));
        filter.put(JobApplicationDatabase.jobAppFilterKeys.OPEN, Boolean.TRUE);
        List<String> appList = hrCH.query.getJobAppsFilter(filter).getListStrings();
        if (appList != null) {
            jobApplicationJList.setListData(appList.toArray(new String[appList.size()]));
            appPane.setViewportView(jobApplicationJList);
        } else {
            jobApplicationJList.setListData(new String[0]);
        }
        detailedApp.setText("");
    }

    /**
     * Displays and the description of the selected JobApplication and
     * enables the Choose button
     * @param appListString: The String representation of the selected JobApplication
     */
    private void onAppSelection(String appListString) {
        HashMap<JobApplicationDatabase.jobAppFilterKeys, Object> filter = new HashMap<>();
        filter.put(JobApplicationDatabase.jobAppFilterKeys.LIST_STRING, appListString);
        detailedApp.setText(hrCH.query.getJobAppsFilter(filter).getRepresentation());
        buttonChooseInterviewer.setEnabled(true);
    }

    /**
     * Calls the selectUser dialog form to select an Interviewer, and assigns
     * that Interviewer to the selected JobApplication
     * @param appListString: The String representation of the selected JobApplication
     */
    private void onChoose(String appListString) {
        HashMap<UserCredentialsDatabase.usersFilterKeys, Object> filter = new HashMap<>();
        filter.put(UserCredentialsDatabase.usersFilterKeys.ACCOUNT_TYPE, UserCredentials.userTypes.INTERVIEWER);
        filter.put(UserCredentialsDatabase.usersFilterKeys.FIRM_ID, HRInterviewerForm.super.hrCH.getFirmID());
        Long userID = GUI.selectUser(filter, super.hrCH);
        if (userID != null) {
            super.hrCH.assignInterviewer(Long.parseLong(JobApplicationQuery.parseListString(appListString)), userID);
        }
        onJobSelection();
    }
}
