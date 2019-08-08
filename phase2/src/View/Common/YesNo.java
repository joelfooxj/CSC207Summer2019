package View.Common;

import javax.swing.*;
import java.awt.event.*;

public class YesNo extends JDialog {

    /**
     * Form for selecting yes or no
     */

    private JPanel contentPane;
    private JButton buttonYes;
    private JButton buttonNo;
    private JLabel mainText;
    public boolean retBool = false;

    public YesNo(String inputText) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonYes);

        buttonYes.addActionListener(e -> onYes());
        buttonNo.addActionListener(e -> onNo());

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(1);
            }
        });

        this.mainText.setText(inputText);
    }

    private void onYes() {
        this.retBool = true;
        dispose();
    }

    private void onNo() {
        this.retBool = false;
        dispose();
    }
}
