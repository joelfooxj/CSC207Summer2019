package View;

import Control.HyreSession;
import Model.UserCredentialsPackage.UserCredentials;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDate;
import java.util.HashMap;

public class Login extends JDialog {
    private JPanel contentPane;
    private JButton buttonLogin;
    private JButton buttonRegister;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel errorLabel;
    private JComboBox<String> userTypeBox;
    private JTextField firmText;
    private LocalDate sessionDate;
    private HyreSession session;

    public UserCredentials retUser;

    private HashMap<String, UserCredentials.userTypes> stringEnumLink = new HashMap<String, UserCredentials.userTypes>() {
        {
            put("Applicant", UserCredentials.userTypes.APPLICANT);
            put("Interviewer", UserCredentials.userTypes.INTERVIEWER);
            put("Human Resources", UserCredentials.userTypes.HR);
            put("Referer", UserCredentials.userTypes.REFERER);
        }
    };

    /**
     * Form for different types of users to login
     *
     * @param session: the HyreSession calling this form
     */
    public Login(HyreSession session) {
        this.sessionDate = sessionDate;
        this.session = session;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonLogin);

        for (String userType : stringEnumLink.keySet()) {
            this.userTypeBox.addItem(userType);
        }

        buttonLogin.addActionListener(e -> onLogin());

        buttonRegister.addActionListener(e -> onRegister());

        userTypeBox.addActionListener(actionEvent -> {
            if (userTypeBox.getSelectedItem().equals("Applicant") || userTypeBox.getSelectedItem().equals("Referer")) {
                firmText.setText("");
                firmText.setEditable(false);
            } else {
                firmText.setEditable(true);
            }
        });

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(1);
            }
        });
    }

    /**
     * Gets the username and password and returns the appropriate UserCredentials.
     * Resets the username and password text fields if not found, or incorrect.
     */
    private void onLogin() {
        String userName = this.usernameField.getText().trim();
        String password = String.valueOf(this.passwordField.getPassword()).trim();

        UserCredentials targetUser = session.getSessionData().usersDb.getUserByCredentials(userName, password);
        if (targetUser == null) {
            this.errorLabel.setText("Incorrect username or password.");
            this.resetFields();
        } else {
            this.retUser = targetUser;
            dispose();
        }
    }

    /**
     * Registers the username, password, and Applicant Type as UserCredential
     * and returns that UserCredential.
     */
    private void onRegister() {
        String userName = this.usernameField.getText().trim();
        String password = String.valueOf(this.passwordField.getPassword()).trim();
        if (userName.equals("") || password.equals("")) {
            this.errorLabel.setText("Please enter a username and a password");
        } else if (session.getSessionData().usersDb.userExists(userName)) {
            this.errorLabel.setText("User already exists");
        } else {
            String accountType = (String) this.userTypeBox.getSelectedItem();
            if (accountType.equals("Applicant") || accountType.equals("Referer")) {
                this.retUser = session.addUser(userName, password,
                        stringEnumLink.get(accountType), "");
                dispose();
            } else {
                String firmName = this.firmText.getText().trim();
                if (!firmName.equals("")) {
                    this.retUser = session.addUser(userName, password, stringEnumLink.get(accountType), firmName);
                    dispose();
                } else {
                    this.errorLabel.setText("Please enter a firm.");
                }
            }

        }
    }

    /**
     * This method resets the username and password text fields.
     */
    private void resetFields() {
        this.usernameField.setText("");
        this.passwordField.setText("");
    }

}
