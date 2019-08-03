package View;

import Control.HrCommandHandler;
import Model.*;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;

public class HRInterviewerForm {
    private JFrame frame = new JFrame();
    private JPanel panel = new JPanel();
    private JScrollPane jobPane = new JScrollPane();
    private JList<JobPosting> jobPostingJList = new JList<>();
    private JScrollPane appPane = new JScrollPane();
    private JList<JobApplication> jobApplicationJList = new JList<>();
    private JTextArea detailedApp = new JTextArea();
    private JButton buttonChooseInterviewer = new JButton("Choose Interviewer");
    private JButton buttonExit;

    private HrCommandHandler hrCH;


    public HRInterviewerForm(HrCommandHandler inHRCH) {
        this.hrCH = inHRCH;
        setupGUI();
        setupAttributes();
        this.panel.setBorder(BorderFactory.createTitledBorder("Interviewer Assignment Options"));

    }

    private void setupGUI() {
        frame.add(panel);
        panel.add(jobPane);
        panel.add(appPane);
        panel.add(detailedApp);
        panel.add(buttonChooseInterviewer);
        panel.add(buttonExit);

        ArrayList<JobPosting> jobs = this.hrCH.filterJobPosting(new HashMap<String, String>() {{
            put("firm", hrCH.getFirmID());
        }});
        JobPosting[] jobArr = jobs.toArray(new JobPosting[jobs.size()]);
        jobPostingJList = new JList<>(jobArr);
        jobPane.setViewportView(jobPostingJList);

        frame.setPreferredSize(new Dimension(550, 600));
        frame.setVisible(true);
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
                System.exit(0);
            }
        });

    }

    private void onJobSelection() {
        Long firm = Long.parseLong(this.hrCH.getFirmID());
        ArrayList<JobApplication> jobList = this.hrCH.filterJobPosting(new HashMap<String, Long>() {{
            put("firm", firm);
        }});
        JobApplication[] jobArr = jobList.toArray(new JobApplication[jobList.size()]);
        jobApplicationJList = new JList<>(jobArr);
        jobApplicationJList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        jobApplicationJList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        jobApplicationJList.setVisibleRowCount(-1);
        appPane.setViewportView(jobApplicationJList);
        frame.setVisible(true);
    }

    private void onAppSelection(JobApplication app) {
        detailedApp.setText(app.toString());
    }

    private void onChoose(JobApplication app) {
        JFrame frameRef = new JFrame("Enter Reference Letter");
        JPanel panelRef = new JPanel();
        String firmID = this.hrCH.getFirmID();
        ArrayList<UserCredentials> interviewerArrayList = this.hrCH.filterUserCredentials(new HashMap<String, String>() { {
            put("firm", firmID);
            put("accounttype", UserCredentials.userTypes.INTERVIEWER.name());
        }});
        UserCredentials[] userArr = interviewerArrayList.toArray(new UserCredentials[interviewerArrayList.size()]);
        JList<UserCredentials> interviewerList = new JList<>(userArr);
        JButton buttonAssign = new JButton("Assign");
        JButton buttonCancel = new JButton("Cancel");
        panelRef.add(buttonAssign);
        panelRef.add(buttonCancel);

        panelRef.setLayout(new BoxLayout(panelRef, BoxLayout.PAGE_AXIS));
        frameRef.setPreferredSize(new Dimension(500, 500));
        frameRef.setVisible(true);

        buttonAssign.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (interviewerList.getSelectedValue() != null) {
                    hrCH.assignInterviewer(app, interviewerList.getSelectedValue());
                    System.exit(0);
                } else {
                    new MessageBox("Select Interviewer", "Please select an interviewer");
                }
            }
        });
    }
}
