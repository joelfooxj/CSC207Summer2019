package View;

import Control.InterviewerCommandHandler;
import Control.Queries.JobApplicationQuery;
import Model.JobApplicationDatabase;
import com.sun.org.apache.xpath.internal.operations.Bool;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.*;
import java.util.HashMap;
import java.util.List;

class InterviewerForm extends JDialog {
    private JPanel contentPane;
    private JButton recommendButton;
    private JButton exitButton;
    private JScrollPane appScroll;
    private JTextArea applicationText;
    private JList jobApplicationList;
    private JLabel errorLabel;
    private JButton rejectButton;
    private InterviewerCommandHandler iCH;

    InterviewerForm(InterviewerCommandHandler commandHandler) {
        this.iCH = commandHandler;
        setContentPane(contentPane);
        setModal(true);
        this.applicationText.setEditable(false);

        this.contentPane.setBorder(BorderFactory.createTitledBorder(iCH.getUsername()));
        this.updateForm();

        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        recommendButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setRecommendedApps();
            }
        });

        rejectButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setRejectedApps();
            }
        });

        jobApplicationList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                setButtonState(true);
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

    private void setRecommendedApps() {
        for (Object value : this.jobApplicationList.getSelectedValuesList()) {
            String selectedString = (String) value;
            iCH.recommendApplication(Long.parseLong(selectedString));
        }
        updateForm();
    }

    private void setRejectedApps() {
        for (Object value : this.jobApplicationList.getSelectedValuesList()) {
            String selectedString = (String) value;
            iCH.rejectApplication(Long.parseLong(selectedString));
        }
        updateForm();
    }

    private void updateForm() {
        setButtonState(false);
        HashMap<JobApplicationDatabase.jobAppFilterKeys, Object> filter = new HashMap<>();
        filter.put(JobApplicationDatabase.jobAppFilterKeys.INTERVIEWER_ID, iCH.getInterviewerID());
        filter.put(JobApplicationDatabase.jobAppFilterKeys.OPEN, Boolean.TRUE);
        JobApplicationQuery jobApplicationQuery = iCH.filter.getJobAppsFilter(filter);
        List<String> inJobAppList = jobApplicationQuery.getJobAppsID();
        if (inJobAppList.isEmpty()) {
            this.jobApplicationList.setListData(inJobAppList.toArray());
            this.applicationText.setText("You have no application assigned to you.");
        } else {
            this.jobApplicationList.setListData(inJobAppList.toArray());
            this.applicationText.setText(jobApplicationQuery.getPrintout());
        }
    }

    private void setButtonState(boolean enabled) {
        recommendButton.setEnabled(enabled);
        rejectButton.setEnabled(enabled);
    }
}
