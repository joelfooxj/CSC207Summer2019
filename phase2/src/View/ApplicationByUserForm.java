package View;

import Control.CommandHandler;
import Model.JobApplication;
import Model.UserCredentials;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.HashMap;

public abstract class ApplicationByUserForm extends JDialog {

    private JPanel panel;
    private JTextField findUser;
    private JButton buttonFindUser;
    private JScrollPane userOptions;
    private JList<UserCredentials> userCredentialsJList = new JList<>();
    private JScrollPane appOptions;
    private JList<JobApplication> jobApplicationJList = new JList<>();
    private JTextArea showDetails;
    private JButton selectApplication;
    private JButton buttonExit;


    private CommandHandler commandHandler;

    public ApplicationByUserForm(CommandHandler commandHandler, String selectApplicationName) {
        setContentPane(panel);
        setModal(true);
        this.commandHandler = commandHandler;
        this.selectApplication = new JButton((selectApplicationName));
        setupAttributes();
        showDetails.setEditable(false);
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
        List<UserCredentials> users = commandHandler.filterUserCredentials(new HashMap<String, String>() {{
            put("user", search);
        }});
        UserCredentials[] userArr =  users.toArray(new UserCredentials[users.size()]);
        userCredentialsJList = new JList<>(userArr);
        userCredentialsJList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        userCredentialsJList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        userCredentialsJList.setVisibleRowCount(-1);
        userOptions.setViewportView(userCredentialsJList);
    }

    public void onSelectUser(UserCredentials user) {
        List<JobApplication> appsList = commandHandler.filterJobApplication(new HashMap<String, Long>() {{
            put("user", user.getUserID());
        }});
        jobApplicationJList = new JList<>(appsList.toArray(new JobApplication[appsList.size()]));
        userCredentialsJList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        userCredentialsJList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        userCredentialsJList.setVisibleRowCount(-1);
        appOptions.setViewportView(jobApplicationJList);
    }

    public void onSelectApplication(JobApplication app) {
        showDetails.setText(app.toString());
    }

    public CommandHandler getCommandHandler() {
        return this.commandHandler;
    }

}
