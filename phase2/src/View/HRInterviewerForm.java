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

    private HrCommandHandler hrCH;


    public HRInterviewerForm(HrCommandHandler inHRCH) {
        super(inHRCH);
        setContentPane(panel);
        setModal(true);
        this.hrCH = inHRCH;
        setupAttributes();
        this.panel.setBorder(BorderFactory.createTitledBorder("Interviewer Assignment Options"));
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
        List<JobPosting> jobList = this.hrCH.filterJobPosting(new HashMap<String, Long>() {{
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
    }

    private void onChoose(JobApplication app) {
        JFrame frameRef = new JFrame("Enter Reference Letter");
        JPanel panelRef = new JPanel();
        String firmID = this.hrCH.getFirmID();
        List<UserCredentials> interviewerArrayList = this.hrCH.filterUserCredentials(new HashMap<String, String>() { {
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
