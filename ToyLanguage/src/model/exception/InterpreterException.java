package model.exception;

public class InterpreterException extends Exception {
    public InterpreterException(String msg) {
        super(msg);
    }

    public InterpreterException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
