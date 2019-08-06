package View;

import Control.HrCommandHandler;
import Model.*;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

public class HRInterviewerForm extends HRForm {
    private JPanel panel;
    private JScrollPane jobPane;
    private JList<Long> jobPostingJList = new JList<>();
    private JScrollPane appPane;
    private JList<java.lang.String> jobApplicationJList = new JList<>();
    private JTextArea detailedApp;
    private JButton buttonChooseInterviewer;
    private JButton buttonExit;


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
        Long[] jobPostingsArr = jobPostingList.toArray(new Long[jobPostingList.size()]);
        jobPostingJList = new JList<>(jobPostingsArr);
        buttonChooseInterviewer.setEnabled(false);
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
                onAppSelection(jobApplicationJList.getSelectedValue());
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
        Long firm = Long.parseLong(super.hrCH.getFirmID());
        HashMap<JobPostingDatabase.jobPostingFilters, Object> filter = new HashMap<>();
        filter.put(JobPostingDatabase.jobPostingFilters.FIRM, firm);
        List<java.lang.String> jobList = hrCH.filter.getJobPostsFilter(filter).getJobIDs();
        String[] jobArr = jobList.toArray(new String[jobList.size()]);
        jobApplicationJList = new JList<>(jobArr);
        jobApplicationJList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        jobApplicationJList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        jobApplicationJList.setVisibleRowCount(-1);
        appPane.setViewportView(jobApplicationJList);
    }

    private void onAppSelection(String appID) {
        HashMap<JobApplicationDatabase.jobAppFilterKeys, Object> filter = new HashMap<>();
        filter.put(JobApplicationDatabase.jobAppFilterKeys.APPLICATION_ID, Long.parseLong(appID));
        detailedApp.setText(hrCH.filter.getJobAppsFilter(filter).getRepresentation());
        buttonChooseInterviewer.setEnabled(true);
    }

    private void onChoose(String appID) {
        HashMap<Model.UserCredentialsDatabase.usersFilterKeys, String> filter =
                new HashMap<Model.UserCredentialsDatabase.usersFilterKeys, String>() {{
            put(UserCredentialsDatabase.usersFilterKeys.ACCOUNT_TYPE, UserCredentials.userTypes.INTERVIEWER.name());
            put(UserCredentialsDatabase.usersFilterKeys.FIRM_ID, HRInterviewerForm.super.hrCH.getFirmID());
        }};
        SelectUser selectUser = new SelectUser(filter, super.hrCH);
        Long userID = selectUser.getUser();
        if (userID != null) {
            super.hrCH.assignInterviewer(Long.parseLong(appID), userID);
        }
    }
}
