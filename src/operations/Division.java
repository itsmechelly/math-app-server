package operations;

import enums.Command;

/**
 * The Division class extends MathOperation abstract class,
 * the main method in use here is the one that performs the calculation de facto - parseAndCalc(String n1, String n2).
 */
public class Division extends MathOperation {

    @Override
    protected Command getCommand() {
        return Command.DIVISION;
    }

    @Override
    protected double parseAndCalc(String n1, String n2) {
        return Double.parseDouble(n1) / Double.parseDouble(n2);
    }
}
