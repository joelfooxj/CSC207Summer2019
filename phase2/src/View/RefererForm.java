package View;

import Control.HyreLauncher;
import Control.RefererCommandHandler;
import Model.JobApplication;
import Model.UserCredentials;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class RefererForm {
    private JFrame frame = new JFrame("Referer");
    private JPanel panel = new JPanel();

    private JScrollPane chooseOptions = new JScrollPane();
    private JTextArea showDetails = new JTextArea(5, 20);
    private JTextField findUser = new JTextField(20);
    private JButton buttonFindUser = new JButton("Find User");
    private JButton buttonExit = new JButton("Exit");

    private JList<UserCredentials> userCredentialsJList = new JList<>();
    private JList<JobApplication> jobApplicationJList = new JList<>();

    private JButton selectApplication = new JButton("Add reference letter for this application");

    private RefererCommandHandler rch;

    public RefererForm(RefererCommandHandler rch) {
        this.rch = rch;
        setupGUI();
        setupAttributes();

    }

    public void setupGUI() {
        frame.add(panel);
        panel.add(chooseOptions);
        panel.add(showDetails);
        panel.add(findUser);
        panel.add(buttonFindUser);
        panel.add(buttonExit);
        panel.setLayout(new GridBagLayout());

        frame.setPreferredSize(new Dimension(550, 600));
        frame.setVisible(true);
    }

    public void setupAttributes() {
        buttonExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.exit(0);
            }
        });
        buttonFindUser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                onFindUser(findUser.getText());
            }
        });
        userCredentialsJList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent listSelectionEvent) {
                onChooseUser(userCredentialsJList.getSelectedValue());
            }
        });
//        jobApplicationJList.addListSelectionListener(new ListSelectionListener() {
//            @Override
//            public void valueChanged(ListSelectionEvent listSelectionEvent) {
//                showDetails.setText(jobApplicationJList.getSelectedValue().getDetailed());
//            }
//        });
        selectApplication.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    onChooseApplication(jobApplicationJList.getSelectedValue());
                } catch (NullPointerException ex) {
                    return;
                }
            }
        });
    }

    public void onFindUser(String search) {
        ArrayList<UserCredentials> users = HyreLauncher.getUsersDb().getUsersByUsername(search);
        String arr[] = {"Hello", "What", "Who"};
        UserCredentials[] userArr = users.toArray(new UserCredentials[users.size()]);
        userCredentialsJList = new JList<>(userArr);
        JList<String> temp = new JList<>(arr);
        temp.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        temp.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        temp.setVisibleRowCount(-1);
        chooseOptions.setViewportView(temp);
        frame.setVisible(true);
    }

    public void onChooseUser(UserCredentials user) {
        ArrayList<JobApplication> appsList = (ArrayList<JobApplication>) rch.findApplication(HyreLauncher.getAppsDb(), user);
        jobApplicationJList = new JList<JobApplication>(appsList.toArray(new JobApplication[appsList.size()]));
        chooseOptions.setViewportView(jobApplicationJList);
    }

    public void onChooseApplication(JobApplication app) {
        JFrame frameRef = new JFrame("Enter Reference Letter");
        JPanel panelRef = new JPanel();
        JTextArea referenceLetter = new JTextArea(5, 20);
        JButton buttonSubmit = new JButton("Submit");
        panelRef.add(referenceLetter);
        panelRef.add(buttonSubmit);

        panelRef.setLayout(new BoxLayout(panelRef, BoxLayout.PAGE_AXIS));
        frameRef.setPreferredSize(new Dimension(500, 500));
        frameRef.setVisible(true);

    }

//    public static void main(String[] args) {
//        RefererForm rf = new RefererForm();
//    }
}
