package View;

import Control.HrCommandHandler;

import javax.swing.*;
import java.awt.event.*;

public class HRInterviewerForm extends JDialog {
    private JPanel contentPane;
    private JButton buttonAssign;
    private JButton buttonExit;
    private JList interviewerList;
    private JList applicationList;

    private HrCommandHandler hrCH;
    // private HashMap<String, String> interviewerApplicationLink = new HashMap<>();


    public HRInterviewerForm(HrCommandHandler inHRCH, String inTitle) {
        setContentPane(contentPane);
        setModal(true);
        this.hrCH = inHRCH;
        this.contentPane.setBorder(BorderFactory.createTitledBorder(inTitle + " Interviewer Options"));

//        this.interviewerList.setListData(this.hrCH.getInterviewerNames().toArray());
//        this.applicationList.setListData(this.hrCH.getApplicationNames().toArray());






        buttonAssign.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onAssign();
            }
        });

        buttonExit.addActionListener(new ActionListener() {
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

    private void onAssign() {

    }
}
