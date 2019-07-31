package View;
import Control.HyreLauncher;
import Model.UserCredentials;
import javax.swing.*;
import java.awt.event.*;

public class Login extends JDialog {
    private JPanel contentPane;
    private JButton buttonLogin;
    private JButton buttonRegister;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel errorLabel;
    private JComboBox userTypeBox;
    private JTextField firmText;


    public UserCredentials retUser;

    public Login() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonLogin);

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


        UserCredentials targetUser = HyreLauncher.getUsersDb().getUserByCredentials(userName, password);
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
        if (HyreLauncher.getUsersDb().userExists(userName)){
            this.errorLabel.setText("User already exists");
        } else {
            String accountType = (String) this.userTypeBox.getSelectedItem();
            if (accountType.equals("Applicant")){
                this.retUser = HyreLauncher.getUsersDb().addUser(userName, password, accountType, HyreLauncher.getDate());
            } else {
                Long firmID = Long.parseLong(this.firmText.getText());
//                GUI.messageBox("passing in: " + accountType);
                this.retUser = HyreLauncher.getUsersDb().addUser(userName, password, accountType, firmID);
            }
            dispose();
        }
    }

    private void resetFields(){
        this.usernameField.setText("");
        this.passwordField.setText("");
    }

}
