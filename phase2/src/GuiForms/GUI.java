package GuiForms;
import java.time.LocalDate;
import java.util.List;

import Databases.UserCredentials;

import javax.swing.*;

public class GUI{
    /**
     * This class provides the interface to the different form types
     * Handles the setup and teardown of the forms
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

    public static List<String> interviewForm(String printApplications, List<String> applicationList, String username){
        InterviewForm dialog = new InterviewForm(printApplications, applicationList, username);
        dialog.pack();
        dialog.setVisible(true);
        return dialog.recommendedApps;
    }

    public static void messageBox(String inputString){
        MessageBox dialog = new MessageBox(inputString);
        dialog.pack();
        dialog.toFront();
        dialog.setVisible(true);
    }
}
