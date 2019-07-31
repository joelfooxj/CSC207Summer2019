package View;

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
    private JButton addButton;
    private JButton removeButton;

    private HashMap<JCheckBox, String> checkBoxtagsLink = new HashMap<>(){
        {
            put(fullTimeCheckBox, "fulltime");
            put(partTimeCheckBox, "parttime");
            put(flexWorkCheckBox, "flexwork");
            put(techCheckBox, "tech");
            put(financeCheckBox, "finance");
        }
    };

    private List<String> selectedInterviews = new ArrayList<>();

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

        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedInterview = (String) interviewCombo.getSelectedItem();
                selectedInterviews.add(selectedInterview);
                interviewList.setListData(selectedInterviews.toArray());
            }
        });

        removeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedInterview = (String) interviewList.getSelectedValue();
                selectedInterviews.remove(selectedInterview);
                interviewList.setListData(selectedInterviews.toArray());
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
        super.hrCH.createJob(jobTitle, jobDesc, super.hrCH.getFirmID(), HyreLauncher.getDate(),
                tags, this.selectedInterviews, jobLocation);
    }
}
