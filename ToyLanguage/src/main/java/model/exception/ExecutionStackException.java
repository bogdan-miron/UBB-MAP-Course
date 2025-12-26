package model.exception;

public class ExecutionStackException extends Exception {
    public ExecutionStackException(String msg) {
        super(msg);
    }

    public ExecutionStackException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
