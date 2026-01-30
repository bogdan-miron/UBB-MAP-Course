package model.statement;

import model.exception.TypeException;
import model.state.ExecutionStack;
import model.state.IExecutionStack;
import model.state.ISymbolTable;
import model.state.ProgramState;
import model.type.IType;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

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

        // Clone the entire stack of SymbolTables
        Stack<ISymbolTable> clonedStack = new Stack<>();
        for (ISymbolTable symTable : state.getSymTableStack()) {
            clonedStack.push(symTable.clone());
        }

        // Create a new ProgramState (child thread) with
        // New execution stack containing the forked statement
        // Clone of the parent symbol table stack (deep copy of entire stack)
        // References to the same heap, fileTable, latchTable, barrierTable, lockTable, semaphoreTable, procTable, and output
        ProgramState newState = new ProgramState(
                newStack,
                clonedStack,
                state.getFileTable(),
                state.getHeap(),
                state.getLatchTable(),
                state.getBarrierTable(),
                state.getLockTable(),
                state.getSemaphoreTable(),
                state.getProcTable(),
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
    public IStatement deepCopy() {
        return new ForkStatement(statement.deepCopy());
    }

    @Override
    public String toString() {
        return "fork(" + statement + ")";
    }
}
