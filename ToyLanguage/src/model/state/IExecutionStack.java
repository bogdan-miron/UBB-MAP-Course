package model.state;

import model.exception.ExecutionStackException;
import model.statement.IStatement;

public interface IExecutionStack {
    void push(IStatement statement);
    IStatement pop() throws ExecutionStackException;
    boolean isEmpty();
    String toString();
}
