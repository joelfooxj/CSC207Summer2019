package View;

import Control.ApplicantCommandHandler;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.*;
import java.util.HashMap;
import java.util.List;
import Model.JobApplicationDatabase.jobAppFilterKeys;

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

        this.updateForm();

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
                checkCVCLButtonEnable(selectedAppID);
                withdrawButton.setEnabled(true);
                appTextArea.setText(appCH.filter.getJobAppsFilter(filterHM(selectedAppID)).getRepresentation());
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

    private HashMap<jobAppFilterKeys, Object> filterHM(String applicationID){
        HashMap<jobAppFilterKeys, Object> newfilter = new HashMap<jobAppFilterKeys, Object>(){
            {
                put(jobAppFilterKeys.APPLICATION_ID, Long.parseLong(applicationID));
                put(jobAppFilterKeys.OPEN, 1L);
            }
        };
        return newfilter;
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
            this.updateForm();
        }
    }

    private void openCVForm(){
        String selectedAppID = (String) this.appList.getSelectedValue();
        String inCV = this.appCH.filter.getJobAppsFilter(filterHM(selectedAppID)).getResume();
        if (inCV == null){inCV = "";}
        String outCV = GUI.editTextForm(inCV, "CV editor");
        if (outCV != null){
            this.appCH.setApplicationCV(selectedAppID, outCV);
        }
    }

    private void openCoverLetterForm(){
        String selectedAppID = (String) this.appList.getSelectedValue();
        String inCoverLetter = this.appCH.filter.getJobAppsFilter(filterHM(selectedAppID)).getCoverLetter();
        if (inCoverLetter == null) {inCoverLetter = "";}
        String outCoverLetter = GUI.editTextForm(inCoverLetter, "Cover Letter editor");
        if (outCoverLetter != null){
            this.appCH.setApplicationCoverLetter(selectedAppID, outCoverLetter);
        }
    }

    private void checkCVCLButtonEnable(String inJobAppID){
        List<String> requiredDocsList = this.appCH.filter.getJobAppsFilter(filterHM(inJobAppID)).getRequiredDocuments();
        this.CVButton.setEnabled(requiredDocsList.contains("CV"));
        this.coverletterButton.setEnabled(requiredDocsList.contains("Cover Letter"));
    }

    private void updateForm(){
        setButtonStatus(false);
        HashMap<jobAppFilterKeys, Object> filterHM = new HashMap<jobAppFilterKeys, Object>(){
            {
                put(jobAppFilterKeys.OPEN, 1L);
                put(jobAppFilterKeys.APPLICANT_ID, appCH.getApplicantID());
            }
        };
        List<String> inJobAppIDs = this.appCH.filter.getJobAppsFilter(filterHM).getJobAppsID();
        if (inJobAppIDs.isEmpty()){
            this.appTextArea.setText("You have no open applications.");
        } else {
            this.appList.setListData(inJobAppIDs.toArray());
        }
    }

    private void setButtonStatus(boolean enabled){
        this.withdrawButton.setEnabled(enabled);
        this.coverletterButton.setEnabled(enabled);
        this.CVButton.setEnabled(enabled);
        this.coverletterButton.setEnabled(enabled);
    }
}
