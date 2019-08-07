package View;

import javax.swing.*;
import java.awt.event.*;

public class MessageBox extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JTextArea mainText;

    public MessageBox(String inTitle, String inputString) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        this.mainText.setText(inputString);
        this.setAlwaysOnTop(true);
        this.contentPane.setBorder(BorderFactory.createTitledBorder(inTitle));
        this.mainText.setEditable(false);

        buttonOK.addActionListener(e -> dispose());

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
    }
}
