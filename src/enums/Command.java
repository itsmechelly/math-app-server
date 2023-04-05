package enums;

public enum Command {
    ADDITION("add"),
    SUBTRACTION("subtract"),
    MULTIPLICATION("multiply"),
    DIVISION("divide");

    final private String commandString;

    Command(String name) {
        this.commandString = name;
    }

    public String getName() {
        return this.commandString;
    }
}
