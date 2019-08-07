package View.ApplicantForms;

import Control.CommandHandlers.ApplicantCommandHandler;

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
import Model.jobTags;

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
    private JComboBox<String> locationFilterCombo;
    private JCheckBox financeCheckBox;

    private HashMap<JCheckBox, jobTags> checkBoxtagsLink = new HashMap<JCheckBox, jobTags>() {
        {
            put(fullTimeCheckBox, jobTags.FULL_TIME);
            put(partTimeCheckBox, jobTags.PART_TIME);
            put(flexWorkCheckBox, jobTags.FLEX_WORK);
            put(techCheckBox, jobTags.TECH);
            put(financeCheckBox, jobTags.FINANCE);
        }
    };

    ApplicantJobsForm(ApplicantCommandHandler appCH) {
        super(appCH);
        setContentPane(contentPane);
        setModal(true);

        this.updateJobsFields();
        this.jobsTextArea.setEditable(false);

        this.locationFilterCombo.addItem("All locations");

        for (String location : this.getLocations()) {
            this.locationFilterCombo.addItem(location);
        }
        this.locationFilterCombo.setSelectedIndex(0);

        for (JCheckBox checkBox : checkBoxtagsLink.keySet()) {
            checkBox.setSelected(false);
            checkBox.addItemListener(itemEvent -> updateJobsFields());
        }

        applyButton.addActionListener(actionEvent -> onApply());

        exitButton.addActionListener(actionEvent -> dispose());

        locationFilterCombo.addActionListener(actionEvent -> updateJobsFields());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
    }

    private void onApply() {
        List<String> jobIDs = new ArrayList<>();
        for (Object obj : this.jobsList.getSelectedValuesList()) {
            String jobListString = (String) obj;
            jobIDs.add(JobApplicationQuery.parseListString(jobListString));
        }
        this.appCH.applyForJobs(jobIDs);
        updateJobsFields();
    }

    /**
     * This method updates and populates the jLists of this form.
     */
    private void updateJobsFields() {
        HashSet<jobTags> tagsList = new HashSet<>();
        for (JCheckBox checkBox : checkBoxtagsLink.keySet()) {
            if (checkBox.isSelected()) {
                tagsList.add(checkBoxtagsLink.get(checkBox));
            }
        }
        String setLocation = (String) this.locationFilterCombo.getSelectedItem();


        HashMap<jobPostingFilters, Object> query = new HashMap<>();
        if (!(setLocation == null)) {
            query.put(jobPostingFilters.LOCATION, setLocation);
        }
        query.put(jobPostingFilters.OPEN, Boolean.TRUE);
        JobPostQuery jobPostQuery = this.appCH.filter.getJobPostsFilter(query);
        jobPostQuery.applyHashtagFilter(tagsList);
        HashMap<JobApplicationDatabase.jobAppFilterKeys, Object> appQuery = new HashMap<>();
        appQuery.put(JobApplicationDatabase.jobAppFilterKeys.APPLICANT_ID, appCH.getApplicantID());
        appQuery.put(JobApplicationDatabase.jobAppFilterKeys.OPEN, Boolean.TRUE);
        JobApplicationQuery jobApplicationQuery = this.appCH.filter.getJobAppsFilter(appQuery);
        if (jobApplicationQuery.getJobIDs() != null) {
            jobPostQuery.filterByHasApplied(jobApplicationQuery.getJobIDs());
        }

        String jobsRepr;
        if (jobPostQuery.getRepresentations() == null) {
            jobsRepr = "There are no open job postings.";
        } else {
            jobsRepr = jobPostQuery.getRepresentations();
        }

        this.jobsTextArea.setText((jobsRepr != null ? jobsRepr : " "));

        List<String> inJobsList = jobPostQuery.getListStrings();
        this.jobsList.setListData(inJobsList.toArray());
    }

    private List<String> getLocations() {
        HashMap<jobPostingFilters, Object> filterHM = new HashMap<jobPostingFilters, Object>() {
            {
                put(jobPostingFilters.OPEN, Boolean.TRUE);
            }
        };
        return this.appCH.filter.getJobPostsFilter(filterHM).getLocationList();
    }
}
