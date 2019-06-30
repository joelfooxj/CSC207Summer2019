import java.util.List;
import java.util.Scanner;

public class InputFormatting {
    private static Scanner mainScanner = new Scanner(System.in);
    /**
     * This method is a wrapper around user input which
     * checks for correct input type(String, int, double) and a list of allowed inputs.
     *
     * Check for escape character '~' to bring user back to the main menu.
     *
     * If input type is wrong, or not in the list of allowed inputs, will loop until
     * correction is made.
     *
     * Usage:
     * Long inputLong = (Long) InputFormatting.inputWrapper("long", Arrays.asList(1,2,3));
     *
     * This will check for a Long type, will raise an exception otherwise.
     * This will check for inputs 1, 2, 3 - any other input will loop input until the one of those inputs are selected.
     * This will check for '~' character to raise EscapeLoopException which should be used to escape to main menus.
     *
     */
    public static Object inputWrapper(String returnType, List<? extends Object> compareList)
            throws EscapeLoopException {
        while (true) {
            String inString = mainScanner.nextLine().trim();
            if (inString.equals("~")) {
                throw new EscapeLoopException();
            }
            List<Integer> IntegerList;
            List<Double> DoubleList;
            List<String> StringList;
            List<Long> LongList;
            try {
                switch (returnType) {
                    case "long":
                        Long outLong = Long.parseLong(inString);
                        if (compareList == null) {
                            return outLong;
                        }
                        LongList = (List<Long>) compareList;
                        if (LongList.contains(outLong)) {
                            return outLong;
                        }
                        break;
                    case "int":
                        Integer outInt = Integer.parseInt(inString);
                        if (compareList == null) {
                            return outInt;
                        }
                        IntegerList = (List<Integer>) compareList;
                        if (IntegerList.contains(outInt)) {
                            return outInt;
                        }
                        break;
                    case "double":
                        Double outDouble = Double.parseDouble(inString);
                        if (compareList == null) {
                            return outDouble;
                        }
                        DoubleList = (List<Double>) compareList;
                        if (DoubleList.contains(outDouble)) {
                            return outDouble;
                        }
                        break;
                    case "string":
                        if (compareList == null) {
                            return inString;
                        }
                        StringList = (List<String>) compareList;
                        if (StringList.contains(inString)) {
                            return inString;
                        }
                }
            } catch (NumberFormatException e) {
                System.out.println("That is not a number");
            }
            System.out.println("Wrong input. Try again.");
        }
    }

}

/**
 * EscapeLoopException is for escaping to main menus of
 * the login menu, member menu and staff menu.

 */
class EscapeLoopException extends Exception {
}



