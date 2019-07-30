package View;

import Control.HrCommandHandler;

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

    public HRCreateJob(HrCommandHandler inHRCH) {
        super(inHRCH);
        setContentPane(contentPane);
        setModal(true);

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
        List<String> interviewList = new ArrayList<>();
        for (Object obj: this.interviewList.getSelectedValuesList()){
            interviewList.add((String) obj);
        }
        List<String> tags = new ArrayList<>();
        for (JCheckBox checkBox:checkBoxtagsLink.keySet()){
            if (checkBox.isSelected()){
                tags.add(checkBoxtagsLink.get(checkBox));
            }
        }
        this.appCH



    }

}
