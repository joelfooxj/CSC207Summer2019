package View;

import Control.Queries.JobApplicationQuery;
import Control.CommandHandlers.RefererCommandHandler;
import Model.JobApplicationDatabase;
import Model.UserCredentials;
import Model.UserCredentialsDatabase;
import View.Common.MessageBox;

import javax.swing.*;
import java.util.List;
import java.util.HashMap;

public class RefererForm extends JDialog {

    private JPanel panel;
    private JTextField findUser;
    private JButton buttonFindUser;
    private JScrollPane userOptions;
    private JList<String> userCredentialsJList = new JList<>();
    private JScrollPane appOptions;
    private JList<String> jobApplicationJList = new JList<>();
    private JTextArea showDetails;
    private JButton addReferenceLetterButton;
    private JButton buttonExit;


    private RefererCommandHandler commandHandler;

    RefererForm(RefererCommandHandler commandHandler) {
        setContentPane(panel);
        setModal(true);
        this.commandHandler = commandHandler;
        setupAttributes();
        showDetails.setEditable(false);
        this.addReferenceLetterButton.setEnabled(false);
        userCredentialsJList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        userCredentialsJList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        userCredentialsJList.setVisibleRowCount(-1);
        jobApplicationJList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        jobApplicationJList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        jobApplicationJList.setVisibleRowCount(-1);
    }

    private void setupAttributes() {
        buttonExit.addActionListener(actionEvent -> dispose());
        buttonFindUser.addActionListener(actionEvent -> onFindUser(findUser.getText()));
        userCredentialsJList.addListSelectionListener(listSelectionEvent -> onSelectUser(userCredentialsJList.getSelectedValue()));
        addReferenceLetterButton.addActionListener(actionEvent -> onChooseApplication(jobApplicationJList.getSelectedValue()));

        jobApplicationJList.addListSelectionListener(listSelectionEvent -> {
            if (jobApplicationJList.getSelectedValue() != null) {
                onSelectApplication(jobApplicationJList.getSelectedValue());
            }
        });
    }

    private void onFindUser(String search) {

        HashMap<UserCredentialsDatabase.usersFilterKeys, Object> query = new HashMap<>();
        query.put(UserCredentialsDatabase.usersFilterKeys.USERNAME, search);
        query.put(UserCredentialsDatabase.usersFilterKeys.ACCOUNT_TYPE, UserCredentials.userTypes.APPLICANT);
        List<String> users = commandHandler.filter.getUsersFilter(query).getRepresentations();

        userCredentialsJList.setListData(users.toArray(new String[users.size()]));
        userOptions.setViewportView(userCredentialsJList);
    }

    private void onSelectUser(String userREPR) {
        HashMap<JobApplicationDatabase.jobAppFilterKeys, Object> filter = new HashMap<>();
        filter.put(JobApplicationDatabase.jobAppFilterKeys.APPLICANT_REPR, userREPR);
        filter.put(JobApplicationDatabase.jobAppFilterKeys.OPEN, Boolean.TRUE);
        List<String> appsList = commandHandler.filter.getJobAppsFilter(filter).getListStrings();
        jobApplicationJList.setListData(appsList.toArray(new String[appsList.size()]));
        appOptions.setViewportView(jobApplicationJList);
        addReferenceLetterButton.setEnabled(false);
    }

    private void onSelectApplication(String app) {
        HashMap<JobApplicationDatabase.jobAppFilterKeys, Object> filter = new HashMap<>();
        filter.put(JobApplicationDatabase.jobAppFilterKeys.LIST_STRING, app);
        showDetails.setText(commandHandler.filter.getJobAppsFilter(filter).getRepresentation());
        addReferenceLetterButton.setEnabled(true);
    }

    private void onChooseApplication(String app) {
        String refLetter = GUI.editTextForm("", "Cover Letter editor");
        boolean verify = GUI.yesNoForm("Do you want to submit this reference letter?");
        if (verify) {
            this.commandHandler.addReferenceLetter(Long.parseLong(JobApplicationQuery.parseListString(app)), refLetter);
            MessageBox messageBox = new MessageBox("Reference Letter Submission", "Reference letter submitted!");
        } else {
            MessageBox messageBox = new MessageBox("Reference Letter Submission", "Reference letter not submitted, good choice!");
        }
    }
}