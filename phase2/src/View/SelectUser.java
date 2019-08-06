package View;

import Control.CommandHandler;
import Model.JobApplication;
import Model.UserCredentials;
import Model.UserCredentialsDatabase;

import javax.swing.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.List;

public class SelectUser extends JDialog {
    private JPanel contentPane;
    private JButton buttonSelect;
    private JButton buttonCancel;
    private JScrollPane userPane;
    private JList<String> userCredentialsJList;
    private JLabel title;
    private Long user;
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

        List<String> userCredentialsList = this.commandHandler.filter.getUsersFilter(filter).getRepresentations();
        userCredentialsJList.setListData(userCredentialsList.toArray(new String[userCredentialsList.size()]));

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
        if (userCredentialsJList.getSelectedValue() != null) {
            HashMap<UserCredentialsDatabase.usersFilterKeys, Object> filter = new HashMap<>();
            filter.put(UserCredentialsDatabase.usersFilterKeys.REPR, userCredentialsJList.getSelectedValue());
            this.user = commandHandler.filter.getUsersFilter(filter).getIDs().get(0);
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

    public Long getUser() {
        return user;
    }
}
