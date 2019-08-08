package View.HRForms;

import Control.CommandHandlers.HrCommandHandler;
import Model.JobPostingPackage.jobTags;
import Model.requiredDocs;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HRCreateJob extends HRJobOptionsForm {
    private JPanel contentPane;
    private JButton createButton;
    private JButton exitButton;
    private JTextField jobTitleText;
    private JTextArea jobDescriptionText;
    private JTextField jobLocationText;
    private JCheckBox fullTimeCheckBox;
    private JCheckBox partTimeCheckBox;
    private JCheckBox flexWorkCheckBox;
    private JCheckBox techCheckBox;
    private JCheckBox financeCheckBox;
    private JList interviewList;
    private JComboBox<String> interviewCombo;
    private JButton addInterviewButton;
    private JButton removeInterviewButton;
    private JSpinner numLabourSpinner;
    private JTextField skillText;
    private JList skillList;
    private JButton removeSkillButton;
    private JButton addSkillButton;
    private JCheckBox CVCheckBox;
    private JCheckBox referenceLetterCheckBox;
    private JCheckBox coverLetterCheckBox;

    private HashMap<JCheckBox, jobTags> checkBoxtagsLink = new HashMap<JCheckBox, jobTags>() {
        {
            put(fullTimeCheckBox, jobTags.FULL_TIME);
            put(partTimeCheckBox, jobTags.PART_TIME);
            put(flexWorkCheckBox, jobTags.FLEX_WORK);
            put(techCheckBox, jobTags.TECH);
            put(financeCheckBox, jobTags.FINANCE);
        }
    };

    private HashMap<JCheckBox, requiredDocs> checkBoxDocsLink = new HashMap<JCheckBox, requiredDocs>() {
        {
            put(CVCheckBox, requiredDocs.CV);
            put(coverLetterCheckBox, requiredDocs.COVERLETTER);
            put(referenceLetterCheckBox, requiredDocs.REFERENCELETTERS);
        }
    };

    private List<String> selectedInterviews = new ArrayList<>();
    private List<String> selectedSkills = new ArrayList<>();

    /**
     * This form creates JobPostings
     * @param inHRCH: the HrCommandHandler of the parent form
     */

    HRCreateJob(HrCommandHandler inHRCH) {
        super(inHRCH);
        setContentPane(contentPane);
        setModal(true);

        this.contentPane.setBorder(BorderFactory.createTitledBorder(super.subMenuTitle + " - Create Job"));
        for (String interviewType : super.hrCH.getAllInterviewStages()) {
            this.interviewCombo.addItem(interviewType);
        }

        this.numLabourSpinner.setModel(new SpinnerNumberModel(1, 1, 10, 1));

        createButton.addActionListener(actionEvent -> createJob());

        exitButton.addActionListener(actionEvent -> dispose());

        addInterviewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedInterview = (String) interviewCombo.getSelectedItem();
                selectedInterviews.add(selectedInterview);
                interviewList.setListData(selectedInterviews.toArray());
            }
        });

        removeInterviewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedInterview = (String) interviewList.getSelectedValue();
                selectedInterviews.remove(selectedInterview);
                interviewList.setListData(selectedInterviews.toArray());
            }
        });

        addSkillButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedSkill = skillText.getText();
                selectedSkills.add(selectedSkill);
                skillList.setListData(selectedSkills.toArray());
                skillText.setText("");

            }
        });

        removeSkillButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedSkill = (String) skillList.getSelectedValue();
                selectedSkills.remove(selectedSkill);
                skillList.setListData(selectedSkills.toArray());

            }
        });

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

    }

    /**
     * Gets the following data from this form and associated CommandHandler and creates a new JobPosting:
     * - Title
     * - Description
     * - Location
     * - Checked Tags
     * - Check required documents
     * - Number of positions
     * - The firmID of this form's associated HrCommandHandler
     * - The list of added interview stages
     * - The list of added skills
     */
    private void createJob() {
        String jobTitle = this.jobTitleText.getText();
        String jobDesc = this.jobDescriptionText.getText();
        String jobLocation = this.jobLocationText.getText();
        List<jobTags> tags = new ArrayList<>();
        for (JCheckBox checkBox : checkBoxtagsLink.keySet()) {
            if (checkBox.isSelected()) {
                tags.add(checkBoxtagsLink.get(checkBox));
            }
        }
        List<requiredDocs> docs = new ArrayList<>();
        for (JCheckBox checkBox : checkBoxDocsLink.keySet()) {
            if (checkBox.isSelected()) {
                docs.add(checkBoxDocsLink.get(checkBox));
            }
        }
        Long numLabour = ((Number) this.numLabourSpinner.getValue()).longValue();
        super.hrCH.createJob(jobTitle, jobDesc, super.hrCH.getFirmID(), numLabour, tags,
                this.selectedInterviews, jobLocation, this.selectedSkills, docs);
        this.dispose();
    }
}
