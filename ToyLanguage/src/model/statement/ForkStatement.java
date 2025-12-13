package model.statement;

import model.exception.TypeException;
import model.state.ExecutionStack;
import model.state.IExecutionStack;
import model.state.ProgramState;
import model.type.IType;

import java.util.HashMap;
import java.util.Map;

public class ForkStatement implements IStatement {
    private final IStatement statement;

    public ForkStatement(IStatement statement) {
        this.statement = statement;
    }

    @Override
    public ProgramState execute(ProgramState state) throws TypeException {
        // Create a new execution stack for the child thread
        IExecutionStack newStack = new ExecutionStack();
        newStack.push(statement);

        // Create a new ProgramState (child thread) with:
        // New execution stack containing the forked statement
        // Clone of the parent symbol table (deep copy)
        // References to the same heap, fileTable, and output
        ProgramState newState = new ProgramState(
                newStack,
                state.getSymTable().clone(),
                state.getFileTable(),
                state.getHeap(),
                state.getOutput()
        );

        // Return the new thread (child ProgramState), the parent thread continues with the current state (unchanged)
        return newState;
    }

    @Override
    public Map<String, IType> typecheck(Map<String, IType> typeEnv) throws TypeException {
        statement.typecheck(cloneTypeEnv(typeEnv));
        return typeEnv;
    }

    private Map<String, IType> cloneTypeEnv(Map<String, IType> typeEnv) {
        return new HashMap<>(typeEnv);
    }

    @Override
    public String toString() {
        return "fork(" + statement + ")";
    }
}
