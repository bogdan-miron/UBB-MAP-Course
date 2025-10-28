package model.state;
import model.exception.ExecutionStackException;
import model.statement.IStatement;

import java.util.Stack;

public class ExecutionStack implements IExecutionStack{
    private Stack<IStatement> stack;

    public ExecutionStack() {
        this.stack = new Stack<>();
    }

    @Override
    public void push(IStatement statement) {
        stack.push(statement);
    }

    @Override
    public IStatement pop() throws ExecutionStackException {
        if (stack.isEmpty()) {
            throw new ExecutionStackException("Stack is empty");
        }
        return stack.pop();
    }

    @Override
    public boolean isEmpty() {
        return stack.isEmpty();
    }
}
