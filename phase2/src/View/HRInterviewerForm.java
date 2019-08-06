package View;

import Control.HrCommandHandler;
import Model.*;
import com.sun.org.apache.xpath.internal.operations.Bool;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.*;
import java.util.List;
import java.util.HashMap;

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


    public HRInterviewerForm(HrCommandHandler inHRCH) {
        super(inHRCH);
        setContentPane(panel);
        setModal(true);
        setupAttributes();
        this.panel.setBorder(BorderFactory.createTitledBorder("Interviewer Assignment Options"));
        // Setup JobPosting JList ot only show open applications for this user's firm
        HashMap<JobPostingDatabase.jobPostingFilters, Object> filter = new HashMap<>();
        filter.put(JobPostingDatabase.jobPostingFilters.FIRM, Long.parseLong(HRInterviewerForm.super.hrCH.getFirmID()));
        filter.put(JobPostingDatabase.jobPostingFilters.OPEN, Boolean.TRUE);
        List<String> jobPostingList = hrCH.filter.getJobPostsFilter(filter).getJobIDs();
        jobPostingJList.setListData(jobPostingList.toArray(new String[jobPostingList.size()]));
        buttonChooseInterviewer.setEnabled(false);

        jobApplicationJList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        jobApplicationJList.setVisibleRowCount(-1);

    }

    private void setupAttributes() {
        jobPostingJList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent listSelectionEvent) {
                onJobSelection();
            }
        });

        jobApplicationJList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent listSelectionEvent) {
                if (jobApplicationJList.getSelectedValue() != null) {
                    onAppSelection(jobApplicationJList.getSelectedValue());
                }
            }
        });

        buttonChooseInterviewer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                onChoose(jobApplicationJList.getSelectedValue());
            }
        });

        buttonExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                dispose();
            }
        });

    }

    private void onJobSelection() {
//        Long firm = Long.parseLong(super.hrCH.getFirmID());
//        HashMap<JobPostingDatabase.jobPostingFilters, Object> filter = new HashMap<>();
//        filter.put(JobPostingDatabase.jobPostingFilters.FIRM, firm);
//        List<String> jobList = hrCH.filter.getJobPostsFilter(filter).getJobIDs();

        Long jobID = Long.parseLong(jobPostingJList.getSelectedValue());
        HashMap<JobApplicationDatabase.jobAppFilterKeys, Object> filter = new HashMap<>();
        filter.put(JobApplicationDatabase.jobAppFilterKeys.JOB_ID, jobID);
        filter.put(JobApplicationDatabase.jobAppFilterKeys.OPEN, Boolean.TRUE);
        List<String> appList = hrCH.filter.getJobAppsFilter(filter).getJobAppsID();
        if (appList != null) {
            jobApplicationJList.setListData(appList.toArray(new String[appList.size()]));
            appPane.setViewportView(jobApplicationJList);
        } else {
            jobApplicationJList.setListData(new String[0]);
        }
        detailedApp.setText("");
    }

    private void onAppSelection(String appID) {
        HashMap<JobApplicationDatabase.jobAppFilterKeys, Object> filter = new HashMap<>();
        filter.put(JobApplicationDatabase.jobAppFilterKeys.APPLICATION_ID, Long.parseLong(appID));
        detailedApp.setText(hrCH.filter.getJobAppsFilter(filter).getRepresentation());
        buttonChooseInterviewer.setEnabled(true);
    }

    private void onChoose(String appID) {
        HashMap<Model.UserCredentialsDatabase.usersFilterKeys, String> filter = new HashMap<>();
        filter.put(UserCredentialsDatabase.usersFilterKeys.ACCOUNT_TYPE, UserCredentials.userTypes.INTERVIEWER.name());
        filter.put(UserCredentialsDatabase.usersFilterKeys.FIRM_ID, HRInterviewerForm.super.hrCH.getFirmID());
        SelectUser selectUser = new SelectUser(filter, super.hrCH);
        Long userID = selectUser.getUser();
        if (userID != null) {
            super.hrCH.assignInterviewer(Long.parseLong(appID), userID);
        }
    }
}
