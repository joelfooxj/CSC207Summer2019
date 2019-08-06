package View;

import Control.HrCommandHandler;
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

        createJobButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                HRCreateJob createJob = new HRCreateJob(HRJobOptionsForm.super.hrCH);
                setAlwaysOnTop(false);
                createJob.setAlwaysOnTop(true);
                createJob.pack();
                createJob.setVisible(true);
                setAlwaysOnTop(true);
                updateJobsList();
            }
        });

        hireButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedAppID = (String) associatedApplicationsList.getSelectedValue();
                HRJobOptionsForm.super.hrCH.hireApplicationID(selectedAppID);
                updateJobsList();
                updateAppList();
            }
        });

        rejectButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedAppID = (String) associatedApplicationsList.getSelectedValue();
                HRJobOptionsForm.super.hrCH.rejectApplicationID(selectedAppID);
                updateAppList();
            }
        });

        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

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

    private void setJobsDesc(){
        String jobID = this.jobsList.getSelectedValue().toString();

        HashMap<jobPostingFilters, Object> query = new HashMap<>();
        query.put(jobPostingFilters.JOB_ID, Long.parseLong(jobID));
        String jobDesc = super.hrCH.filter.getJobPostsFilter(query).getRepresentation();
        this.jobDesc.setText(jobDesc);
    }

    private void setAppDesc(){
        String appID = (String) this.associatedApplicationsList.getSelectedValue();

        HashMap<jobAppFilterKeys, Object> query = new HashMap<>();
        query.put(jobAppFilterKeys.APPLICATION_ID, Long.parseLong(appID));
        String appDesc = super.hrCH.filter.getJobAppsFilter(query).getRepresentation();

        this.appDesc.setText(appDesc);
    }

    private void updateJobsList(){
        setButtonEnabled(false);
        this.jobsList.clearSelection();
        this.associatedApplicationsList.clearSelection();
        this.jobDesc.setText("");
        this.appDesc.setText("");


        HashMap<jobPostingFilters, Object> query = new HashMap<>();
        query.put(jobPostingFilters.OPEN, Boolean.TRUE);

        List<String> inJobList = super.hrCH.filter.getJobPostsFilter(query).getJobIDs();


        //List<String> inJobList = super.hrCH.getOpenJobsList();
        if (inJobList == null){
            this.jobDesc.setText("There are no jobs.");
        } else {
            this.jobsList.setListData(inJobList.toArray());
        }
    }

    private void updateAppList(){
        setButtonEnabled(false);
        this.appDesc.setText("");
        String selectedJobID = (String) this.jobsList.getSelectedValue();

        HashMap<jobAppFilterKeys, Object> query = new HashMap<>();
        query.put(jobAppFilterKeys.JOB_ID, Long.parseLong(selectedJobID));
        query.put(jobAppFilterKeys.OPEN, Boolean.TRUE);
        List<String> inJobAppList = super.hrCH.filter.getJobAppsFilter(query).getJobAppsID();

        if (inJobAppList == null){
            this.appDesc.setText("There are no applications for this job.");
        } else {
            this.associatedApplicationsList.setListData(inJobAppList.toArray());
        }
    }

    private void setButtonEnabled(boolean setEnabled){
        hireButton.setEnabled(setEnabled);
        rejectButton.setEnabled(setEnabled);
    }
}
