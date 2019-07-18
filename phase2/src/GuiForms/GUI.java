package GuiForms;

import java.time.LocalDate;

public class GUI {
    /**
     * This class provides the interface to the different form types
     * Handles the setup and teardown of the forms
     */

    //try with a single question first
    public static boolean yesNoForm(String inputText){
        yesNo dialog = new yesNo(inputText);
        dialog.pack();
        dialog.setVisible(true);
        return dialog.retBool;
    }

    public static LocalDate setDateForm(){
        setDate dialog = new setDate();
        dialog.pack();
        dialog.setVisible(true);
        return dialog.retDate;
    }

}
