package View.HRForms;

import Control.CommandHandlers.HrCommandHandler;
import Control.Queries.JobApplicationQuery;
import Control.Queries.JobPostQuery;
import Model.JobApplicationDatabase.jobAppFilterKeys;
import Model.JobPostingDatabase.jobPostingFilters;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;

import java.util.List;


import javax.swing.event.ListSelectionListener;
import java.awt.event.*;
import java.util.HashMap;

public class HRJobOptionsForm extends HRForm {
    private JPanel contentPane;
    private JList jobsList;
    private JList associatedApplicationsList;
    private JButton createJobButton;
    private JButton hireButton;
    private JButton rejectButton;
    private JButton exitButton;
    private JTextArea jobDesc;
    private JTextArea appDesc;

    public HRJobOptionsForm(HrCommandHandler inHRCH) {
        super(inHRCH);
        setContentPane(contentPane);
        setModal(true);
        this.setAlwaysOnTop(true);

        this.jobsList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        this.jobsList.setLayoutOrientation(JList.VERTICAL);
        this.associatedApplicationsList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        this.associatedApplicationsList.setLayoutOrientation(JList.VERTICAL);

        this.contentPane.setBorder(BorderFactory.createTitledBorder(super.subMenuTitle + " - Job Options"));
        setButtonEnabled(false);
        this.updateJobsList();

        this.jobDesc.setEditable(false);
        this.appDesc.setEditable(false);

        createJobButton.addActionListener(actionEvent -> onCreateJob());
        hireButton.addActionListener(actionEvent -> onHire());
        rejectButton.addActionListener(actionEvent -> onReject());
        exitButton.addActionListener(actionEvent -> dispose());

        jobsList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                associatedApplicationsList.clearSelection();
                updateAppList();
                setJobsDesc();
            }
        });

        associatedApplicationsList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                setButtonEnabled(true);
                setAppDesc();
            }
        });

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
    }

    private void onCreateJob() {
        HRCreateJob createJob = new HRCreateJob(HRJobOptionsForm.super.hrCH);
        setAlwaysOnTop(false);
        createJob.setAlwaysOnTop(true);
        createJob.pack();
        createJob.setVisible(true);
        setAlwaysOnTop(true);
        updateJobsList();
    }

    private void onHire() {
        String selectedAppID = JobApplicationQuery.parseListString((String) associatedApplicationsList.getSelectedValue());
        HRJobOptionsForm.super.hrCH.hireApplicationID(selectedAppID);
        updateJobsList();
        updateAppList();
    }

    private void onReject() {
        String selectedAppID = JobApplicationQuery.parseListString((String) associatedApplicationsList.getSelectedValue());
        HRJobOptionsForm.super.hrCH.rejectApplicationID(selectedAppID);
        updateJobsList();
        updateAppList();
    }

    private void setJobsDesc() {
        if (this.jobsList.getSelectedValue() != null) {
            String jobList = this.jobsList.getSelectedValue().toString();
            HashMap<jobPostingFilters, Object> query = new HashMap<>();
            query.put(jobPostingFilters.LIST_STRING, jobList);
            String jobDesc = super.hrCH.filter.getJobPostsFilter(query).getRepresentation();
            this.jobDesc.setText(jobDesc);
        } else {
            this.jobDesc.setText("");
        }
    }

    private void setAppDesc() {
        String appString = (String) this.associatedApplicationsList.getSelectedValue();
        if (appString != null) {
            HashMap<jobAppFilterKeys, Object> query = new HashMap<>();
            query.put(jobAppFilterKeys.LIST_STRING, appString);
            String appDesc = super.hrCH.filter.getJobAppsFilter(query).getRepresentation();

            this.appDesc.setText(appDesc);
        } else {
            this.appDesc.setText("");
        }
    }

    private void updateJobsList() {
        setButtonEnabled(false);
        this.jobsList.clearSelection();
        this.associatedApplicationsList.clearSelection();
        this.jobDesc.setText("");
        this.appDesc.setText("");


        HashMap<jobPostingFilters, Object> query = new HashMap<>();
        query.put(jobPostingFilters.OPEN, Boolean.TRUE);

        List<String> inJobList = super.hrCH.filter.getJobPostsFilter(query).getListStrings();


        //List<String> inJobList = super.hrCH.getOpenJobsList();
        if (inJobList.isEmpty()) {
            this.jobDesc.setText("There are no jobs.");
        } else {
            this.jobsList.setListData(inJobList.toArray());
        }
    }

    private void updateAppList() {
        setButtonEnabled(false);
        this.appDesc.setText("");
        String selectedJobID =(String) this.jobsList.getSelectedValue();
        if (selectedJobID != null) {
            selectedJobID = JobPostQuery.parseListString((String) this.jobsList.getSelectedValue());
            HashMap<jobAppFilterKeys, Object> query = new HashMap<>();
            query.put(jobAppFilterKeys.JOB_ID, Long.parseLong(selectedJobID));
            query.put(jobAppFilterKeys.OPEN, Boolean.TRUE);
            List<String> inJobAppList = super.hrCH.filter.getJobAppsFilter(query).getListStrings();

            if (inJobAppList.isEmpty()) {
                this.appDesc.setText("There are no applications for this job.");
            } else {
                this.associatedApplicationsList.setListData(inJobAppList.toArray());
            }
        } else {
            this.associatedApplicationsList.setListData(new Object[0]);
            this.appDesc.setText("");
        }
    }

    private void setButtonEnabled(boolean setEnabled) {
        hireButton.setEnabled(setEnabled);
        rejectButton.setEnabled(setEnabled);
    }
}
