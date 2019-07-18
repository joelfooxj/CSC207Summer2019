package GuiForms;

import javax.swing.*;
import java.awt.event.*;

public class yesNo extends JDialog {
    private JPanel contentPane;
    private JButton buttonYes;
    private JButton buttonNo;
    private JLabel mainText;
    boolean retBool = false;

    yesNo(String inputText) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonYes);

        buttonYes.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onYes();
            }
        });

        buttonNo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onNo();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(1);
            }
        });

        this.mainText.setText(inputText);
    }

    private void onYes() {
        // add your code here
        this.retBool = true;
        dispose();
    }

    private void onNo() {
        // add your code here if necessary
        this.retBool = false;
        dispose();
    }
}
