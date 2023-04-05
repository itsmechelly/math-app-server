package exceptions;

public class ApplicationException extends RuntimeException {
    private String message;

    public ApplicationException(String message) {
        this.message = "ERROR: " + message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
