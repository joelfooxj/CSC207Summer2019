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

public abstract class ApplicationByUserForm {
    private JFrame frame = new JFrame("Referer");
    private JPanel panel = new JPanel();

    private JTextField findUser = new JTextField(20);
    private JButton buttonFindUser = new JButton("Find User");

    private JScrollPane userOptions = new JScrollPane();
    private JList<UserCredentials> userCredentialsJList = new JList<>();

    private JScrollPane appOptions = new JScrollPane();
    private JTextField textField1;
    private JList<JobApplication> jobApplicationJList = new JList<>();

    private JTextArea showDetails = new JTextArea(5, 20);

    private JButton selectApplication;

    private JButton buttonExit = new JButton("Logout");

    private RefererCommandHandler rch;

    public ApplicationByUserForm(RefererCommandHandler rch, String selectApplicationName) {
        this.rch = rch;
        this.selectApplication = new JButton((selectApplicationName));
        setupGUI();
        setupAttributes();
    }

    public void setupGUI() {
        frame.add(panel);
        panel.add(findUser);
        panel.add(buttonFindUser);
        panel.add(userOptions);
        panel.add(appOptions);
        panel.add(showDetails);
        panel.add(selectApplication);
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
                onSelectUser(userCredentialsJList.getSelectedValue());
            }
        });

        jobApplicationJList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent listSelectionEvent) {
                onSelectApplication(jobApplicationJList.getSelectedValue());
            }
        });
    }

    public JButton getSelectApplication() {
        return this.selectApplication;
    }

    public JList<JobApplication> getJobApplicationJList() {
        return this.jobApplicationJList;
    }

    public void onFindUser(String search) {
        rch.findUser(HyreLauncher.getUsersDb(), search);
        ArrayList<UserCredentials> users = HyreLauncher.getUsersDb().getUsersByUsername(search);
        String arr[] = {"Hello", "What", "Who"};
        UserCredentials[] userArr = users.toArray(new UserCredentials[users.size()]);
        userCredentialsJList = new JList<>(userArr);
        JList<String> temp = new JList<>(arr);
        temp.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        temp.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        temp.setVisibleRowCount(-1);
        userOptions.setViewportView(temp);
        frame.setVisible(true);
    }

    public void onSelectUser(UserCredentials user) {
        ArrayList<JobApplication> appsList = (ArrayList<JobApplication>) rch.findApplication(HyreLauncher.getAppsDb(), user);
        jobApplicationJList = new JList<JobApplication>(appsList.toArray(new JobApplication[appsList.size()]));
        appOptions.setViewportView(jobApplicationJList);
    }

    public void onSelectApplication(JobApplication app) {
        showDetails.setText(app.toString());
    }

}
