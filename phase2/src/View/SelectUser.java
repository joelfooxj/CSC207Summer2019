package View;

import Control.CommandHandler;
import Model.JobApplication;
import Model.UserCredentials;

import javax.swing.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.List;

public class SelectUser extends JDialog {
    private JPanel contentPane;
    private JButton buttonSelect;
    private JButton buttonCancel;
    private JScrollPane userPane;
    private JList<UserCredentials> userCredentialsJList;
    private UserCredentials user;
    private CommandHandler commandHandler;


    public SelectUser(HashMap filter, CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonSelect);

        buttonSelect.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onSelect();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        List<UserCredentials> userCredentialsList = this.commandHandler.filterUserCredentials(filter);
        UserCredentials[] userCredentials = userCredentialsList.toArray(new UserCredentials[userCredentialsList.size()]);
        userCredentialsJList = new JList<>(userCredentials);

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onSelect() {
        // add your code here
        if (userCredentialsJList.getSelectedValue() != null) {
            this.user = userCredentialsJList.getSelectedValue();
            MessageBox messageBox = new MessageBox("Select User", "User " + this.user.toString() + " Selected!");
            dispose();
        } else {
            MessageBox messageBox = new MessageBox("Select User", "Select a User!");
        }
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public UserCredentials getUser() {
        return user;
    }
}
