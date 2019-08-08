package View.HRForms;

import Control.CommandHandlers.HrCommandHandler;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class HRForm extends JDialog {
    private JPanel contentPane;
    private JButton jobsButton;
    private JButton applicantsButton;
    private JButton interviewerButton;
    private JButton exitButton;

    HrCommandHandler hrCH;
    String subMenuTitle;

    public HRForm(HrCommandHandler inHRCH) {
        setContentPane(contentPane);
        setModal(true);

        this.hrCH = inHRCH;
        String userName = this.hrCH.getUsername();
        Long firmName = this.hrCH.getFirmID();
        this.subMenuTitle = "Firm " + firmName + ": " + userName;

        this.contentPane.setBorder(BorderFactory.createTitledBorder(this.subMenuTitle));

        jobsButton.addActionListener(actionEvent -> onJobs());
        applicantsButton.addActionListener(actionEvent -> onApplicants());
        interviewerButton.addActionListener(actionEvent -> onInterviewer());
        exitButton.addActionListener(actionEvent -> dispose());

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
    }

    private void onJobs() {
        HRJobOptionsForm hrJobOptionsForm = new HRJobOptionsForm(this.hrCH);
        hrJobOptionsForm.setAlwaysOnTop(true);
        hrJobOptionsForm.pack();
        hrJobOptionsForm.setSize(500, 500);
        hrJobOptionsForm.setVisible(true);
    }

    private void onApplicants() {
        HRApplicantOptions hrApplicantOptions = new HRApplicantOptions(this.hrCH);
        hrApplicantOptions.setAlwaysOnTop(true);
        hrApplicantOptions.pack();
        hrApplicantOptions.setSize(500, 500);
        hrApplicantOptions.setVisible(true);
    }

    private void onInterviewer() {
        HRInterviewerForm hrInterviewerForm = new HRInterviewerForm(this.hrCH);
        hrInterviewerForm.setAlwaysOnTop(true);
        hrInterviewerForm.pack();
        hrInterviewerForm.setSize(500, 500);
        hrInterviewerForm.setVisible(true);
    }
}
