package View;

import java.sql.Ref;
import java.time.LocalDate;

import Control.*;
import Model.UserCredentials;
import Model.UserCredentialsDatabase;

public class GUI {
    /**
     * - This class provides the interface to the different form types
     * - Handles the setup and teardown of the forms
     */

    /**
     * This method creates a yes/no dialog form and returns
     * true if User clicks Yes, and false if User clicks No
     *
     * @param inputText: text to be shown in dialog box
     * @return stored response in dialog
     */
    public static boolean yesNoForm(String inputText) {
        YesNo dialog = new YesNo(inputText);
        dialog.pack();
        dialog.setVisible(true);
        return dialog.retBool;
    }

    /**
     * This method creates a dialog for date input and
     * returns a LocalDate object. Default is today's date.
     *
     * @return LocalDate
     */
    public static LocalDate setDateForm() {
        SetDate dialog = new SetDate();
        dialog.pack();
        dialog.setVisible(true);
        return dialog.retDate;
    }

    public static UserCredentials loginForm(HyreSession session) {
        Login dialog = new Login(session);
        dialog.pack();
        dialog.setVisible(true);
        return dialog.retUser;
    }

    // todo: remove this method - too specific to the InterviewerCommandHandler
    public static void interviewerForm(InterviewerCommandHandler commandHandler) {
        InterviewerForm dialog = new InterviewerForm(commandHandler);
        dialog.pack();
        dialog.setVisible(true);
    }

    public static void messageBox(String inTitle, String inputString) {
        MessageBox dialog = new MessageBox(inTitle, inputString);
        dialog.pack();
        dialog.setVisible(true);
    }

    public static String editTextForm(String inText, String title) {
        TextEditorForm dialog = new TextEditorForm(inText, title);
        dialog.pack();
        dialog.setVisible(true);
        return dialog.text;
    }

    // todo: remove this method - too specific to the ApplicantCommandHandler
    public static void applicantForm(ApplicantCommandHandler commandHandler) {
        ApplicantForm dialog = new ApplicantForm(commandHandler);
        dialog.pack();
        dialog.setVisible(true);
    }

    public static void hrForm(HrCommandHandler commandHandler) {
        HRForm dialog = new HRForm(commandHandler);
        dialog.pack();
        dialog.setVisible(true);
    }

    public static void refererForm(RefererCommandHandler commandHandler) {
        RefererForm dialog = new RefererForm(commandHandler);
        dialog.pack();
        dialog.setVisible(true);
    }
}
