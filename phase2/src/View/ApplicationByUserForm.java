package View;

import Control.CommandHandler;
import Model.JobApplication;
import Model.JobApplicationDatabase;
import Model.UserCredentials;
import Model.UserCredentialsDatabase;

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
    private JList<Long> userCredentialsJList = new JList<>();
    private JScrollPane appOptions;
    private JList<Long> jobApplicationJList = new JList<>();
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
        this.selectApplication.setEnabled(false);
    }

    public void setupAttributes() {
        buttonExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                dispose();
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

    public JList<Long> getJobApplicationJList() {
        return this.jobApplicationJList;
    }

    public void onFindUser(String search) {

        HashMap<UserCredentialsDatabase.filterKeys, String> query = new HashMap<>();
        query.put(UserCredentialsDatabase.filterKeys.USERNAME, search);
        List<Long> users = commandHandler.filter.getUsersFilter(query).getIDs();

        Long[] userArr =  users.toArray(new Long[users.size()]);
        userCredentialsJList = new JList<Long>(userArr);
        userCredentialsJList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        userCredentialsJList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        userCredentialsJList.setVisibleRowCount(-1);
        userOptions.setViewportView(userCredentialsJList);
    }

    public void onSelectUser(Long user) {
        HashMap<JobApplicationDatabase.jobAppFilterKeys, Long> filter = new HashMap<>();
        filter.put(JobApplicationDatabase.jobAppFilterKeys.APPLICANT_ID, user);
        List<Long> appsList = commandHandler.filter.getJobApplicationsFilter(filter).getApplicationIDs();
        jobApplicationJList = new JList<>(appsList.toArray(new Long[appsList.size()]));
        userCredentialsJList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        userCredentialsJList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        userCredentialsJList.setVisibleRowCount(-1);
        appOptions.setViewportView(jobApplicationJList);
    }

    public void onSelectApplication(Long app) {
        HashMap<JobApplicationDatabase.jobAppFilterKeys, Long> filter = new HashMap<>();
        filter.put(JobApplicationDatabase.jobAppFilterKeys.APPLICATION_ID, app);
        showDetails.setText(commandHandler.filter.getJobApplicationsFilter(filter).getStrings().get(0));
        selectApplication.setEnabled(true);
    }

    public CommandHandler getCommandHandler() {
        return this.commandHandler;
    }

}
