package View;

import Control.HrCommandHandler;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.*;

public class HRJobOptionsForm extends HRForm {
    private JPanel contentPane;
    private JList jobsList;
    private JList associatedApplicationsList;
    private JButton createJobButton;
    private JButton hireButton;
    private JButton rejectButton;
    private JButton exitButton;
    private JLabel jobDescLabel;
    private JLabel applicationDescLabel;

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
        this.updateJobsList();
        this.updateAppList();


        createJobButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                createJob();
            }
        });

        hireButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedAppID = (String) associatedApplicationsList.getSelectedValue();
                HRJobOptionsForm.super.hrCH.hireApplicationID(selectedAppID);
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
                updateAppList();
                setJobsDesc();
            }
        });

        associatedApplicationsList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
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
        String jobID = (String) this.jobsList.getSelectedValue();
        String jobDesc = super.hrCH.getJobPostingDesc(jobID);
        this.jobDescLabel.setText(jobDesc);
    }

    private void setAppDesc(){
        String appID = (String) this.associatedApplicationsList.getSelectedValue();
        String appDesc = super.hrCH.getJobApplicationPrintout(appID);
        this.applicationDescLabel.setText(appDesc);
    }

    private void updateJobsList(){
        this.jobsList.setListData(super.hrCH.getOpenJobsList().toArray());
        this.jobsList.setSelectedIndex(0);
        this.setJobsDesc();
        this.updateAppList();
    }

    private void updateAppList(){
        String selectedJobID = (String) this.jobsList.getSelectedValue();
        this.associatedApplicationsList.setListData(super.hrCH.getApplicationsIDbyJobID(selectedJobID).toArray());
        this.applicationDescLabel.setText("");
    }

    private void createJob(){
        HRCreateJob createJob = new HRCreateJob(super.hrCH);
        this.setAlwaysOnTop(false);
        createJob.setAlwaysOnTop(true);
        createJob.pack();
        createJob.setVisible(true);
        this.setAlwaysOnTop(true);
        this.updateJobsList();
    }

}
