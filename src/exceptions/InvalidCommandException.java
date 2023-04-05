package exceptions;

public class InvalidCommandException extends ApplicationException {
    public InvalidCommandException() {
        super("Could not parse command");
    }
}
