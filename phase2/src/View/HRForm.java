package View;

import Control.HrCommandHandler;

import javax.swing.*;
import java.awt.event.*;

public class HRForm extends JDialog {
    private JPanel contentPane;
    private JButton jobsButton;
    private JButton applicantsButton;
    private JButton interviewerButton;
    private JButton exitButton;

    protected HrCommandHandler hrCH;
    protected String subMenuTitle;

    public HRForm(HrCommandHandler inHRCH) {
        setContentPane(contentPane);
        setModal(true);

        this.hrCH = inHRCH;
        String userName= this.hrCH.getUsername();
        String firmName = this.hrCH.getFirmID();
        this.subMenuTitle = "Firm " + firmName + ": " + userName;

        this.contentPane.setBorder(BorderFactory.createTitledBorder(this.subMenuTitle));

        jobsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onJobs();
            }
        });

        applicantsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onApplicants();
            }
        });

        interviewerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onInterviewer();
            }
        });

        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
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

    private void onJobs(){
        HRJobOptionsForm hrJobOptionsForm = new HRJobOptionsForm(this.hrCH);
        hrJobOptionsForm.setAlwaysOnTop(true);
        hrJobOptionsForm.pack();
        hrJobOptionsForm.setVisible(true);
    }

    private void onApplicants(){
        HRApplicantOptions hrApplicantOptions = new HRApplicantOptions(this.hrCH);
        hrApplicantOptions.setAlwaysOnTop(true);
        hrApplicantOptions.pack();
        hrApplicantOptions.setVisible(true);
    }

    private void onInterviewer(){
        HRInterviewerForm hrInterviewerForm = new HRInterviewerForm(this.hrCH);
        hrInterviewerForm.setAlwaysOnTop(true);
        hrInterviewerForm.pack();
        hrInterviewerForm.setVisible(true);
    }
}
