package GuiForms;
import javax.swing.*;
import java.awt.event.*;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.Date;

public class setDate extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JLabel errorLabel;
    private JTextField monthField;
    private JTextField yearField;
    private JTextField dayField;

    LocalDate retDate = LocalDate.of(2019, 1, 1);

    public setDate() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

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

        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) { onOK();}
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        this.dayField.setText(String.valueOf(LocalDate.now().getDayOfMonth()));
        this.monthField.setText(String.valueOf(LocalDate.now().getMonthValue()));
        this.yearField.setText(String.valueOf(LocalDate.now().getYear()));
    }

    private void onOK() {
        try{
            int day = Integer.parseInt(dayField.getText());
            int month = Integer.parseInt(monthField.getText());
            int year = Integer.parseInt(yearField.getText());
            this.retDate = LocalDate.of(year, month, day);
            dispose();
        } catch (NumberFormatException e){
            this.errorLabel.setText("Enter integers only.");
            resetFields();
        } catch (DateTimeException e){
            this.errorLabel.setText("Enter a valid date.");
            resetFields();
        }
    }

    private void onCancel() {
        this.retDate = LocalDate.now();
        dispose();
    }

    private void resetFields(){
        this.dayField.setText("");
        this.monthField.setText("");
        this.yearField.setText("");
    }
}
