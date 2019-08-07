package View;

import javax.swing.*;
import java.awt.event.*;

public class TextEditorForm extends JDialog {
    private JPanel contentPane;
    private JButton buttonSave;
    private JButton buttonCancel;
    private JTextArea editTextArea;

    public String text;

    public TextEditorForm(String inText, String title) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonCancel);
        this.text = inText;
        this.editTextArea.setText(inText);

        this.contentPane.setBorder(BorderFactory.createTitledBorder(title));

        buttonSave.addActionListener(e -> onSave());

        buttonCancel.addActionListener(e -> onCancel());

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

    }

    private void onSave() {
        this.text = this.editTextArea.getText();
        dispose();
    }

    private void onCancel() {
        this.text = null;
        dispose();
    }
}
