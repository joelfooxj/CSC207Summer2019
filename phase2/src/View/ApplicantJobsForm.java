package View;

import Control.ApplicantCommandHandler;

import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class ApplicantJobsForm extends ApplicantForm {
    private JPanel contentPane;
    private JButton applyButton;
    private JButton exitButton;
    private JTextArea jobsTextArea;
    private JList jobsList;

    protected List<String> selectedJobIDs = new ArrayList<>();

    public ApplicantJobsForm(ApplicantCommandHandler appCH) {
        super(appCH);
        setContentPane(contentPane);
        setModal(true);

        this.jobsTextArea.setText(super.appCH.getOpenJobsPrintout());
        this.jobsList.setListData(super.appCH.getOpenJobsList().toArray());


        applyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onApply();
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

    private void onApply(){
        List<String> jobIDs = new ArrayList<>();
        for(Object obj:this.jobsList.getSelectedValuesList()){
            jobIDs.add((String) obj);
        }
        this.selectedJobIDs = jobIDs;
    }
}
