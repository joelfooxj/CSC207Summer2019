package View;

import Control.ApplicantCommandHandler;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.*;

public class ApplicantApplicationForm extends ApplicantForm {
    private JPanel contentPane;
    private JButton CVButton;
    private JButton coverletterButton;
    private JList appList;
    private JTextArea appTextArea;
    private JButton withdrawButton;
    private JButton exitButton;

    private ApplicantCommandHandler appCH;

    public ApplicantApplicationForm(ApplicantCommandHandler appCH) {
        super(appCH);
        this.appCH = super.appCH;
        setContentPane(contentPane);
        setModal(true);
        this.appList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        this.appList.setLayoutOrientation(JList.VERTICAL);

        this.appList.setListData(this.appCH.getAllOpenApplicationIDs().toArray());
        this.appList.setSelectedIndex(0);
        String selectedAppID = (String) this.appList.getSelectedValue();
        this.appTextArea.setText(this.appCH.getApplicationDesc(selectedAppID));

        CVButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openCVForm();
            }
        });

        coverletterButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openCoverLetterForm();
            }
        });

        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        withdrawButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                withdrawConfirmation();
            }
        });

        // on changing selection, get app desc
        appList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                String selectedAppID = (String) appList.getSelectedValue();
                appTextArea.setText(appCH.getApplicationDesc(selectedAppID));
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

    private void withdrawConfirmation(){
        boolean confirm = GUI.yesNoForm("Are you sure you want to withdraw? ");
        if(confirm){
            int selectedListIndex = this.appList.getSelectedIndex();
            String selectedAppID = (String) this.appList.getSelectedValue();
            this.appCH.withdrawApplication(selectedAppID);
            this.appTextArea.setText("");
            this.appList.clearSelection();
            this.appList.remove(selectedListIndex);
        }
    }

    private void openCVForm(){
        String selectedAppID = (String) this.appList.getSelectedValue();
        String inCV = this.appCH.getApplicationCV(selectedAppID);
        if (inCV == null){inCV = "";}
        String outCV = GUI.editTextForm(inCV, "CV editor");
        if (outCV != null){
            this.appCH.setApplicationCV(selectedAppID, outCV);
        }
    }

    private void openCoverLetterForm(){
        String selectedAppID = (String) this.appList.getSelectedValue();
        String inCoverLetter = this.appCH.getApplicationCoverLetter(selectedAppID);
        if (inCoverLetter == null) {inCoverLetter = "";}
        String outCoverLetter = GUI.editTextForm(inCoverLetter, "Cover Letter editor");
        if (outCoverLetter != null){
            this.appCH.setApplicationCoverLetter(selectedAppID, outCoverLetter);
        }
    }
}
