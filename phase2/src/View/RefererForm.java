package View;

import Control.CommandHandlers.RefererCommandHandler;
import Control.Queries.JobApplicationQuery;
import Model.JobApplicationPackage.JobApplicationDatabase;
import Model.UserCredentialsPackage.UserCredentials;
import Model.UserCredentialsPackage.UserCredentialsDatabase;
import View.Common.MessageBox;

import javax.swing.*;
import java.util.HashMap;
import java.util.List;

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

    /**
     * Form for Referers
     *
     * @param commandHandler the command handler for this form to use
     */
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

    /**
     * This method displays the list of Applicant whose usernames match the text in the Find User
     * text field.
     * @param search: The text in the Find User text field
     */
    private void onFindUser(String search) {

        HashMap<UserCredentialsDatabase.usersFilterKeys, Object> query = new HashMap<>();
        query.put(UserCredentialsDatabase.usersFilterKeys.USERNAME, search);
        query.put(UserCredentialsDatabase.usersFilterKeys.ACCOUNT_TYPE, UserCredentials.userTypes.APPLICANT);
        List<String> users = commandHandler.query.getUsersFilter(query).getRepresentations();

        userCredentialsJList.setListData(users.toArray(new String[users.size()]));
        userOptions.setViewportView(userCredentialsJList);
    }

    /**
     * This method gets the list of open JobApplications associated with the selected Applicant and
     * displays them, and disables the Reference Letter button.
     * @param userREPR: The username of the Applicant
     */
    private void onSelectUser(String userREPR) {
        HashMap<JobApplicationDatabase.jobAppFilterKeys, Object> filter = new HashMap<>();
        filter.put(JobApplicationDatabase.jobAppFilterKeys.APPLICANT_REPR, userREPR);
        filter.put(JobApplicationDatabase.jobAppFilterKeys.OPEN, Boolean.TRUE);
        List<String> appsList = commandHandler.query.getJobAppsFilter(filter).getListStrings();
        jobApplicationJList.setListData(appsList.toArray(new String[appsList.size()]));
        appOptions.setViewportView(jobApplicationJList);
        addReferenceLetterButton.setEnabled(false);
    }

    /**
     * This method sets the description text to the description of the selected JobApplication
     * and enables the Reference Letter button.
     * @param app: The String representation of the selected JobApplication.
     */
    private void onSelectApplication(String app) {
        HashMap<JobApplicationDatabase.jobAppFilterKeys, Object> filter = new HashMap<>();
        filter.put(JobApplicationDatabase.jobAppFilterKeys.LIST_STRING, app);
        showDetails.setText(commandHandler.query.getJobAppsFilter(filter).getRepresentation());
        addReferenceLetterButton.setEnabled(true);
    }

    /**
     * This method opens the text editor form and adds a reference letter text for this
     * application.
     * @param app: The String representation of the selected JobApplication
     */
    private void onChooseApplication(String app) {
        String refLetter = GUI.editTextForm("", "Reference Letter editor");
        boolean verify = GUI.yesNoForm("Do you want to submit this reference letter?");
        if (verify && refLetter != null) {
            this.commandHandler.addReferenceLetter(Long.parseLong(JobApplicationQuery.parseListString(app)), refLetter);
            MessageBox messageBox = new MessageBox("Reference Letter Submission", "Reference letter submitted!");
        } else {
            MessageBox messageBox = new MessageBox("Reference Letter Submission", "Reference letter not submitted, good choice!");
        }
    }
}