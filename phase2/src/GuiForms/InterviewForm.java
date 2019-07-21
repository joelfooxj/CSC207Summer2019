package GuiForms;
import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

class InterviewForm extends JDialog {
    private JPanel contentPane;
    private JButton recommendButton;
    private JButton exitButton;
    private JLabel errorLabel;
    private JTextArea applicationText;
    private JList applicationList;
    List<String> recommendedApps = new ArrayList<>();

    InterviewForm(String allApplicationStrings, List<String> applications, String username) {
        setContentPane(contentPane);
        setModal(true);

        this.applicationText.setText(allApplicationStrings);
        this.applicationList.setListData(applications.toArray());
        this.contentPane.setBorder(BorderFactory.createTitledBorder(username));

        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        recommendButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setRecommendedApps();
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

    private void setRecommendedApps(){
        StringBuilder testString = new StringBuilder();
        for (Object value:this.applicationList.getSelectedValuesList()){
            testString.append((String) value);
            testString.append("\n");
        }
        GUI.messageBox(new String(testString));
//        this.recommendedApps = this.applicationList.getSelectedValuesList();
        List<String> retList = new ArrayList<>();
        for (Object selected: this.applicationList.getSelectedValuesList()) {
            retList.add((String) selected);
        }
        this.recommendedApps = retList;
    }
}
