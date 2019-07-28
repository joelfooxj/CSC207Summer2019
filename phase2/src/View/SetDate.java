package View;
import javax.swing.*;
import java.awt.event.*;
import java.time.DateTimeException;
import java.time.LocalDate;

public class SetDate extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JLabel errorLabel;
    private JTextField monthField;
    private JTextField yearField;
    private JTextField dayField;

    public LocalDate retDate = LocalDate.of(2019, 1, 1);

    public SetDate() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(1);
            }
        });

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

    private void resetFields(){
        this.dayField.setText("");
        this.monthField.setText("");
        this.yearField.setText("");
    }
}
