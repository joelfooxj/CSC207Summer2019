package View;

import Control.CommandHandler;
import Control.HrCommandHandler;
import Control.HyreLauncher;

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

    private HashMap<JCheckBox, String> checkBoxtagsLink = new HashMap<JCheckBox, String>(){
        {
            put(fullTimeCheckBox, "Full-Time");
            put(partTimeCheckBox, "Part-time");
            put(flexWorkCheckBox, "Flex-work");
            put(techCheckBox, "Tech");
            put(financeCheckBox, "Finance");
        }
    };

    private HashMap<JCheckBox, String> checkBoxDocsLink = new HashMap<JCheckBox, String>(){
        {
            put(CVCheckBox, "CV");
            put(coverLetterCheckBox, "Cover Letter");
            put(referenceLetterCheckBox, "Reference Letters");
        }
    };

    private List<String> selectedInterviews = new ArrayList<>();
    private List<String> selectedSkills = new ArrayList<>();

    public HRCreateJob(HrCommandHandler inHRCH) {
        super(inHRCH);
        setContentPane(contentPane);
        setModal(true);

        this.contentPane.setBorder(BorderFactory.createTitledBorder(super.subMenuTitle + " - Create Job"));
        for(String interviewType:super.hrCH.getAllInterviewStages()){
            this.interviewCombo.addItem(interviewType);
        }

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

            }
        });

        removeSkillButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedSkill = (String) skillList.getSelectedValue();
                selectedSkills.add(selectedSkill);
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

    private void createJob(){
        String jobTitle = this.jobTitleText.getText();
        String jobDesc = this.jobDescriptionText.getText();
        String jobLocation = this.jobLocationText.getText();
        List<String> tags = new ArrayList<>();
        for (JCheckBox checkBox:checkBoxtagsLink.keySet()){
            if (checkBox.isSelected()){
                tags.add(checkBoxtagsLink.get(checkBox));
            }
        }
        List<String> docs = new ArrayList<>();
        for (JCheckBox checkBox:checkBoxDocsLink.keySet()){
            if(checkBox.isSelected()){
                docs.add(checkBoxDocsLink.get(checkBox));
            }
        }
        Long numLabour = Long.parseLong((String) this.numLabourSpinner.getValue());
        super.hrCH.createJob(jobTitle, jobDesc, super.hrCH.getFirmID(), numLabour, tags,
                this.selectedInterviews, jobLocation, this.selectedSkills, docs);
    }

    private void createUIComponents() {
        this.numLabourSpinner = new JSpinner(new SpinnerNumberModel(1,1,10,1));
    }
}
