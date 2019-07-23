package GuiForms;
import java.time.LocalDate;
import CommandHandlers.*;
import Databases.UserCredentials;

public class GUI{
    /**
     * - This class provides the interface to the different form types
     * - Handles the setup and teardown of the forms
     */

    /**
     * This method creates a yes/no dialog form and returns
     * true if User clicks Yes, and false if User clicks No
     * @param inputText: text to be shown in dialog box
     * @return stored response in dialog
     */
    public static boolean yesNoForm(String inputText){
        YesNo dialog = new YesNo(inputText);
        dialog.pack();
        dialog.setVisible(true);
        return dialog.retBool;
    }

    /**
     * This method creates a dialog for date input and
     * returns a LocalDate object. Default is today's date.
     * @return LocalDate
     */
    public static LocalDate setDateForm(){
        SetDate dialog = new SetDate();
        dialog.pack();
        dialog.setVisible(true);
        return dialog.retDate;
    }

    public static UserCredentials loginForm(){
        Login dialog = new Login();
        dialog.pack();
        dialog.setVisible(true);
        return dialog.retUser;
    }

    public static void interviewerForm(InterviewerCommandHandler commandHandler){
        InterviewerForm dialog = new InterviewerForm(commandHandler);
        dialog.pack();
        dialog.setVisible(true);
    }

    public static void messageBox(String inputString){
        MessageBox dialog = new MessageBox(inputString);
        dialog.pack();
        dialog.setVisible(true);
    }
}
