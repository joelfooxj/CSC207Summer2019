package View;

import Control.ApplicantCommandHandler;

import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import Control.Queries.JobApplicationQuery;
import Control.Queries.JobPostQuery;
import Model.JobApplicationDatabase;
import Model.JobPostingDatabase.jobPostingFilters;

public class ApplicantJobsForm extends ApplicantForm {
    private JPanel contentPane;
    private JButton applyButton;
    private JButton exitButton;
    private JScrollPane jobsTextScroll;
    private JTextArea jobsTextArea;
    private JList jobsList;
    private JCheckBox fullTimeCheckBox;
    private JCheckBox partTimeCheckBox;
    private JCheckBox flexWorkCheckBox;
    private JCheckBox techCheckBox;
    private JComboBox locationFilterCombo;
    private JCheckBox financeCheckBox;

    private HashMap<JCheckBox, String> checkBoxtagsLink = new HashMap<JCheckBox, String>(){
        {
            put(fullTimeCheckBox, "Full-Time");
            put(partTimeCheckBox, "Part-time");
            put(flexWorkCheckBox, "Flex-work");
            put(techCheckBox, "Tech");
            put(financeCheckBox, "Finance");
        }
    };

    ApplicantJobsForm(ApplicantCommandHandler appCH) {
        super(appCH);
        setContentPane(contentPane);
        setModal(true);

        this.updatejobsFields();
        this.jobsTextArea.setEditable(false);

        List<String> locationList = this.getLocations();
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


        HashMap<jobPostingFilters, Object> query = new HashMap<>();
        if (!(setLocation == null)) {
            query.put(jobPostingFilters.LOCATION, setLocation);
        }
        JobPostQuery jobPostQuery = this.appCH.filter.getJobPostsFilter(query);
        jobPostQuery.applyHashtagFilter(tagsList);
        HashMap<JobApplicationDatabase.jobAppFilterKeys, Object> appQuery = new HashMap<>();
        appQuery.put(JobApplicationDatabase.jobAppFilterKeys.APPLICANT_ID, appCH.getApplicantID());
        JobApplicationQuery jobApplicationQuery = this.appCH.filter.getJobAppsFilter(appQuery);
        jobPostQuery.filterByHasApplied(jobApplicationQuery.getJobIDs());

        String jobsRepr;
        if (jobPostQuery.getRepresentations() == null){
            jobsRepr ="There are no open job postings.";
        } else {
            jobsRepr = jobPostQuery.getRepresentations();
        }

        this.jobsTextArea.setText((jobsRepr != null ? jobsRepr:" "));


        List<String> inJobsList = jobPostQuery.getJobIDs();
        if (inJobsList != null){
            this.jobsList.setListData(inJobsList.toArray());
        }
    }

    private List<String> getLocations(){
        HashMap<jobPostingFilters, Object> filterHM = new HashMap<jobPostingFilters, Object>(){
            { put(jobPostingFilters.OPEN, 1L);}};
        return this.appCH.filter.getJobPostsFilter(filterHM).getLocationList();
    }
}
