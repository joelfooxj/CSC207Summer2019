package View;

import Control.HrCommandHandler;
import Model.*;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.HashMap;

public class HRInterviewerForm extends HRForm {
    private JPanel panel;
    private JScrollPane jobPane;
    private JList<JobPosting> jobPostingJList = new JList<>();
    private JScrollPane appPane;
    private JList<JobApplication> jobApplicationJList = new JList<>();
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
        List<JobPosting> jobPostingList = super.hrCH.filterJobPosting(new HashMap<String, Long>() {{
            put("firm", Long.parseLong(HRInterviewerForm.super.hrCH.getFirmID()));
            put("open", 1L);
        }});
        JobPosting[] jobPostingsArr = jobPostingList.toArray(new JobPosting[jobPostingList.size()]);
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
        List<JobPosting> jobList = super.hrCH.filterJobPosting(new HashMap<String, Long>() {{
            put("firm", firm);
        }});
        JobApplication[] jobArr = jobList.toArray(new JobApplication[jobList.size()]);
        jobApplicationJList = new JList<>(jobArr);
        jobApplicationJList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        jobApplicationJList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        jobApplicationJList.setVisibleRowCount(-1);
        appPane.setViewportView(jobApplicationJList);
    }

    private void onAppSelection(JobApplication app) {
        detailedApp.setText(app.toString());
        buttonChooseInterviewer.setEnabled(true);
    }

    private void onChoose(JobApplication app) {
        HashMap<String, String> filter = new HashMap<String, String>() { {
            put("firm", HRInterviewerForm.super.hrCH.getFirmID());
            put("accounttype", UserCredentials.userTypes.INTERVIEWER.name());
        }};
        SelectUser selectUser = new SelectUser(filter, super.hrCH);
        UserCredentials user = selectUser.getUser();
        if (user != null) {
            super.hrCH.assignInterviewer(app, user);
        }
    }
}
