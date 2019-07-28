package View;

import javax.swing.*;
import java.awt.event.*;

public class MessageBox extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JLabel mainText;

    public MessageBox(String inputString) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        this.mainText.setText(inputString);
        this.setAlwaysOnTop(true);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
    }
}
