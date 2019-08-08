package View.Common;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MessageBox extends JDialog {
    /**
     * Form for displaying a message
     */

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
