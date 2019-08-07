package View;
import Control.HrCommandHandler;
import Model.requiredDocs;
import Model.jobTags;

import javax.swing.*;
import java.awt.event.*;
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
    private JComboBox interviewCombo;
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

    public HRCreateJob(HrCommandHandler inHRCH) {
        super(inHRCH);
        setContentPane(contentPane);
        setModal(true);

        this.contentPane.setBorder(BorderFactory.createTitledBorder(super.subMenuTitle + " - Create Job"));
        for (String interviewType : super.hrCH.getAllInterviewStages()) {
            this.interviewCombo.addItem(interviewType);
        }

        this.numLabourSpinner.setModel(new SpinnerNumberModel(1, 1, 10, 1));

        createButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                createJob();
            }
        });

        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

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

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

    }

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
