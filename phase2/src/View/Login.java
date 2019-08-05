package View;
import Control.CommandHandler;
import Control.HyreLauncher;
import Control.HyreSession;
import Model.UserCredentials;
import Model.UserCredentialsDatabase;

import javax.swing.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.util.HashMap;

public class Login extends JDialog {
    private JPanel contentPane;
    private JButton buttonLogin;
    private JButton buttonRegister;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel errorLabel;
    private JComboBox userTypeBox;
    private JTextField firmText;
    private LocalDate sessionDate;
    private HyreSession session;

    public UserCredentials retUser;

    private HashMap<String, UserCredentials.userTypes> stringEnumLink = new HashMap<String, UserCredentials.userTypes>(){
        {
            put("Applicant", UserCredentials.userTypes.APPLICANT);
            put("Interviewer", UserCredentials.userTypes.INTERVIEWER);
            put("Human Resources", UserCredentials.userTypes.HR);
            put("Referer", UserCredentials.userTypes.REFERER);
        }
    };

    public Login(HyreSession session) {
        this.sessionDate = sessionDate;
        this.session = session;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonLogin);

        for(String userType:stringEnumLink.keySet()){
            this.userTypeBox.addItem(userType);
        }

        buttonLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onLogin();
            }
        });

        buttonRegister.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onRegister();
            }
        });

        userTypeBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (userTypeBox.getSelectedItem().equals("Applicant") || userTypeBox.getSelectedItem().equals("Referer")) {
                    firmText.setText("");
                    firmText.setEditable(false);
                } else {
                    firmText.setEditable(true);
                }
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(1);
            }
        });
    }

    private void onLogin() {
        String userName = this.usernameField.getText();
        String password = String.valueOf(this.passwordField.getPassword());
//        GUI.messageBox(userName + "\n" + password);
        // pass userName and password back to the interface, which will handle the query...
        // HyreLauncher.adduser -> userdb to add user...


        UserCredentials targetUser = session.getSessionData().usersDb.getUserByCredentials(userName, password);
        if (targetUser == null){
            this.errorLabel.setText("Incorrect username or password.");
            this.resetFields();
        } else {
            this.retUser = targetUser;
            dispose();
        }
    }

    private void onRegister() {
        String userName = this.usernameField.getText();
        String password = String.valueOf(this.passwordField.getPassword());
        if (userName.equals("") || password.equals("")) {
            this.errorLabel.setText("Please enter a username and a password");
        } else if (session.getSessionData().usersDb.userExists(userName)) {
            this.errorLabel.setText("User already exists");
        } else {
            String accountType = (String) this.userTypeBox.getSelectedItem();
            // todo: maybe combine the addUser methods?
            if (accountType.equals("Applicant") || accountType.equals("Referer")){
                this.retUser = session.addUser(userName, password,
                        stringEnumLink.get(accountType), "");
                dispose();
            } else {
                String firmName = this.firmText.getText();
                if (!firmName.equals("")) {
                    this.retUser = session.addUser(userName, password, stringEnumLink.get(accountType), firmName);
                    dispose();
                } else {
                    this.errorLabel.setText("Please enter a firm.");
                }
            }

        }
    }

    private void resetFields(){
        this.usernameField.setText("");
        this.passwordField.setText("");
    }

}
