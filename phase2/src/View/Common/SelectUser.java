package View.Common;

import Control.CommandHandlers.CommandHandler;
import Model.UserCredentialsPackage.UserCredentialsDatabase;
import View.GUI;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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


    /**
     * This form displays Users for selection and returns the selected User ID
     *
     * @param filter
     * @param commandHandler
     */
    public SelectUser(HashMap filter, CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonSelect);

        buttonSelect.addActionListener(e -> onSelect());
        buttonCancel.addActionListener(e -> onCancel());

        List<String> userCredentialsList = this.commandHandler.query.getUsersFilter(filter).getRepresentations();
        userCredentialsJList.setListData(userCredentialsList.toArray(new String[userCredentialsList.size()]));

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        contentPane.registerKeyboardAction(e -> onCancel(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }


    private void onSelect() {
        if (userCredentialsJList.getSelectedValue() != null) {

            HashMap<UserCredentialsDatabase.usersFilterKeys, Object> filter = new HashMap<>();
            filter.put(UserCredentialsDatabase.usersFilterKeys.REPR, userCredentialsJList.getSelectedValue());
            this.user = commandHandler.query.getUsersFilter(filter).getIDs().get(0);
            GUI.messageBox("Select User", "User " + this.user.toString() + " Selected!");
            dispose();
        } else {
            GUI.messageBox("Select User", "Select a User!");
        }
    }

    private void onCancel() {
        dispose();
    }

    public Long getUser() {
        return user;
    }
}
