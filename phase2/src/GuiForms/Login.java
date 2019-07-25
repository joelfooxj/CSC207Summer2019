package GuiForms;
import CommandHandlers.UserInterface;
import Databases.UserCredentials;
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
    private JComboBox firmBox;

    public UserCredentials retUser;

    public Login() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonLogin);

//        String[] userTypes = {"Applicant", "Human Resources", "Interviewer", "Referee"};
//        this.userTypeBox = new JComboBox(userTypes);
//        this.userTypeBox.setSelectedIndex(0);
//
//        // todo: get list of firm id's from firm database
//        Long[] firmIDs = {0L};
//        this.firmBox = new JComboBox(firmIDs);

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

        // call resetFields() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                resetFields();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onLogin() {
        String userName = this.usernameField.getText();
        String password = String.valueOf(this.passwordField.getPassword());
//        GUI.messageBox(userName + "\n" + password);
        // pass userName and password back to the interface, which will handle the query...
        // UserInterface.adduser -> userdb to add user...
        //

        UserCredentials targetUser = UserInterface.getUsersDb().getUserByCredentials(userName, password);
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
        if (UserInterface.getUsersDb().userExists(userName)){
            this.errorLabel.setText("User already exists");
        } else {
            String accountType = (String) this.userTypeBox.getSelectedItem();

            if (accountType.equals("Applicant")){
                this.retUser = UserInterface.getUsersDb().addUser(userName, password, accountType, UserInterface.getDate());
            } else {
                Long firmID = Long.parseLong((String) this.firmBox.getSelectedItem());
                GUI.messageBox("passing in: " + accountType);
                this.retUser = UserInterface.getUsersDb().addUser(userName, password, accountType, firmID);
            }
            dispose();
        }
    }

    private void resetFields(){
        this.usernameField.setText("");
        this.passwordField.setText("");
    }

}
