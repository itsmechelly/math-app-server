package operations;

import enums.Command;

/**
 * MathOperation class is an abstract class.
 * The main method in this class is named perform(), this method parses commands received from the math-client-app.
 * This class will be extended by all the command types that exist in this system.
 * Each one of those subclasses will be performing and using the methods in this abstract class,
 * and must implement the actual calculation parsenCalc(String n1, String n2) method.
 */
public abstract class MathOperation {

    final public double perform(String unparsedCommand) {
        StringBuilder firstNumStr = new StringBuilder();
        StringBuilder secondNumStr = new StringBuilder();

        boolean isAtFirstNum = true;
        for (int i = getFirstNumberIndex(); i < unparsedCommand.length(); i++) {
            char c = unparsedCommand.charAt(i);
            if (Character.isDigit(c) || c == '.') {
                if (isAtFirstNum) {
                    firstNumStr.append(c);
                } else {
                    secondNumStr.append(c);
                }
            } else {
                isAtFirstNum = false;
            }
        }
        return parseAndCalc(firstNumStr.toString(), secondNumStr.toString());
    }

    private int getFirstNumberIndex() {
        return getCommand().getName().length() + 1;
    }

    protected abstract Command getCommand();

    protected abstract double parseAndCalc(String n1, String n2);
}
