package operations.factory;

import enums.Command;
import exceptions.InvalidCommandException;
import operations.*;

/**
 * A math operation factory class, implementing the factory pattern,
 * responsible for identifying math operations that received from the math-client-app
 * and creating the right object accordingly
 */
public class MathOperationFactory {
    private MathOperationFactory() {}

    private static MathOperationFactory instance = new MathOperationFactory();

    public static MathOperationFactory getOperationsFactory() {
        if (instance == null) {
            synchronized (MathOperationFactory.class) {
                if (instance == null) {
                    instance = new MathOperationFactory();
                }
            }
        }
        return instance;
    }

    public MathOperation getOperation(final String unparsedCommand) throws InvalidCommandException {
        if (unparsedCommand.contains(Command.ADDITION.getName())) {
            return new Addition();
        }
        if (unparsedCommand.contains(Command.SUBTRACTION.getName())) {
            return new Subtraction();
        }
        if (unparsedCommand.contains(Command.MULTIPLICATION.getName())) {
            return new Multiplication();
        }
        if (unparsedCommand.contains(Command.DIVISION.getName())) {
            return new Division();
        }

        throw new InvalidCommandException();
    }
}
