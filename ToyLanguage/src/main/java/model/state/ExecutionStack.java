package model.state;

import model.exception.ExecutionStackException;
import model.statement.IStatement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class ExecutionStack implements IExecutionStack {
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

    @Override
    public String toString() {
        if (stack.isEmpty()) {
            return "ExecutionStack: [Empty]";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("ExecutionStack (top to bottom):\n");

        // Get a copy of the stack to display without modifying
        List<IStatement> stackCopy = new ArrayList<>(stack);
        Collections.reverse(stackCopy); // show top first

        for (int i = 0; i < stackCopy.size(); i++) {
            sb.append("  [").append(i).append("] ").append(stackCopy.get(i).toString()).append("\n");
        }

        return sb.toString();
    }
}
