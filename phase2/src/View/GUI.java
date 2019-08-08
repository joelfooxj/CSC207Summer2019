package View;

import Control.CommandHandlers.*;
import Control.HyreSession;
import Model.UserCredentialsPackage.UserCredentials;
import View.ApplicantForms.ApplicantForm;
import View.Common.*;
import View.HRForms.HRForm;

import java.time.LocalDate;
import java.util.HashMap;

public class GUI {
    /**
     * This class provides the interface to the different form types
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
        return dialog.getRetDate();
    }


    /**
     * This method creates a dialog for user login, returning
     * the UserCredentials of the user that logged in/registered
     * @param session: The HyreSession that has called this form
     * @return UserCredentials of a user
     */
    public static UserCredentials loginForm(HyreSession session) {
        Login dialog = new Login(session);
        dialog.pack();
        dialog.setVisible(true);
        return dialog.retUser;
    }

    /**
     * This method creates the main dialog for the interviewer
     * @param commandHandler: The InterviewCommandHandler calling this form
     */
    public static void interviewerForm(InterviewerCommandHandler commandHandler) {
        InterviewerForm dialog = new InterviewerForm(commandHandler);
        dialog.pack();
        dialog.setSize(500, 500);
        dialog.setVisible(true);
    }

    /**
     * This method creates a form to display text
     * @param inTitle The title of the form
     * @param inputString The text to be displayed
     */
    public static void messageBox(String inTitle, String inputString) {
        MessageBox dialog = new MessageBox(inTitle, inputString);
        dialog.pack();
        dialog.setVisible(true);
    }

    /**
     * This method creates a form to display and edit text
     * @param inTitle The title of the form
     * @param inputString The text to be displayed and edited
     */
    public static String editTextForm(String inText, String title) {
        TextEditorForm dialog = new TextEditorForm(inText, title);
        dialog.pack();
        dialog.setVisible(true);
        return dialog.text;
    }

    /**
     * This method creates the main dialog for the Applicant
     * @param commandHandler: The ApplicantCommandHandler calling this form
     */
    public static void applicantForm(ApplicantCommandHandler commandHandler) {
        ApplicantForm dialog = new ApplicantForm(commandHandler);
        dialog.pack();
        dialog.setSize(500, 500);
        dialog.setVisible(true);
    }

    /**
     * This method creates the main dialog for the HR user
     * @param commandHandler: The HrCommandHandler calling this form
     */
    public static void hrForm(HrCommandHandler commandHandler) {
        HRForm dialog = new HRForm(commandHandler);
        dialog.pack();
        dialog.setVisible(true);
    }

    /**
     * This method creates the main dialog for the referer
     * @param commandHandler: The RefererCommandHandler calling this form
     */
    public static void refererForm(RefererCommandHandler commandHandler) {
        RefererForm dialog = new RefererForm(commandHandler);
        dialog.pack();
        dialog.setSize(500, 500);
        dialog.setVisible(true);
    }

    /**
     * This method creates a dialog form that selects a User and
     * returns the ID of that User
     * @param filter: the HashMap filter for selecting Users
     * @param inCH: the CommandHandler that this form references
     * @return User ID
     */
    public static Long selectUser(HashMap filter, CommandHandler inCH) {
        SelectUser selectUser = new SelectUser(filter, inCH);
        selectUser.setAlwaysOnTop(true);
        selectUser.pack();
        selectUser.setSize(500, 500);
        selectUser.setVisible(true);
        return selectUser.getUser();
    }
}
