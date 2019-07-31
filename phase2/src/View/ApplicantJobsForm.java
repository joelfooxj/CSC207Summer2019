package View;

import Control.ApplicantCommandHandler;

import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class ApplicantJobsForm extends ApplicantForm {
    private JPanel contentPane;
    private JButton applyButton;
    private JButton exitButton;
    private JTextArea jobsTextArea;
    private JList jobsList;
    private JCheckBox fullTimeCheckBox;
    private JCheckBox partTimeCheckBox;
    private JCheckBox flexWorkCheckBox;
    private JCheckBox techCheckBox;
    private JComboBox locationFilterCombo;
    private JCheckBox financeCheckBox;

    private HashMap<JCheckBox, String> checkBoxtagsLink = new HashMap<>(){
        {
            put(fullTimeCheckBox, "fulltime");
            put(partTimeCheckBox, "parttime");
            put(flexWorkCheckBox, "flexwork");
            put(techCheckBox, "tech");
            put(financeCheckBox, "finance");
        }
    };

    ApplicantJobsForm(ApplicantCommandHandler appCH) {
        super(appCH);
        setContentPane(contentPane);
        setModal(true);

        this.updatejobsFields();

        List<String> locationList = this.appCH.getLocationList();
        this.locationFilterCombo.addItem("All locations");
        for (String location:locationList){
            this.locationFilterCombo.addItem(location);
        }
        this.locationFilterCombo.setSelectedIndex(0);

        for (JCheckBox checkBox:checkBoxtagsLink.keySet()){
            checkBox.setSelected(true);
            checkBox.addItemListener(new ItemListener() {
                     @Override
                     public void itemStateChanged(ItemEvent e) {
                        updatejobsFields();
                     }
                 }
            );
        }

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

        //todo: check this ActionListener...
        locationFilterCombo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updatejobsFields();
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
        this.appCH.applyForJobs(jobIDs);
        updatejobsFields();
    }

    private void updatejobsFields(){
        HashSet<String> tagsList = new HashSet<>();
        for (JCheckBox checkBox: checkBoxtagsLink.keySet()){
            if (checkBox.isSelected()){
                tagsList.add(checkBoxtagsLink.get(checkBox));
            }
        }
        String setLocation = (String) this.locationFilterCombo.getSelectedItem();
        this.jobsTextArea.setText(this.appCH.getFilteredJobsPrintout(tagsList, setLocation));
        this.jobsList.setListData(this.appCH.getFilteredJobsList(tagsList, setLocation).toArray());
    }
}
