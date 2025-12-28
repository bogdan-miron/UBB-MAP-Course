package model.statement;

import model.exception.TypeException;
import model.state.ProgramState;
import model.type.IType;
import model.type.IntType;
import model.value.IValue;
import model.value.IntValue;

import java.util.Map;

public class LockStatement implements IStatement {
    private final String variableName;

    public LockStatement(String variableName) {
        this.variableName = variableName;
    }

    @Override
    public ProgramState execute(ProgramState state) throws TypeException {
        // check if variable is defined and is IntType
        if (!state.getSymTable().isDefined(variableName)) {
            throw new TypeException("Lock: Variable " + variableName + " is not defined");
        }

        IValue varValue = state.getSymTable().lookup(variableName);
        if (!(varValue instanceof IntValue)) {
            throw new TypeException("Lock: Variable " + variableName + " must be IntValue, got " + varValue.getType());
        }

        int location = ((IntValue) varValue).getValue();

        // check if location exists in LockTable
        if (!state.getLockTable().isDefined(location)) {
            throw new TypeException("Lock: Lock location " + location + " is not defined in LockTable");
        }

        // synchronized check-and-set operation
        // get current value from lock table
        int currentValue = state.getLockTable().get(location);

        if (currentValue == -1) {
            // lock is FREE - acquire it by setting to current thread ID
            state.getLockTable().put(location, state.getId());
            // don't re-push - lock acquired, continue execution
        } else {
            // lock is OCCUPIED by another thread - busy wait
            // re-push this statement to try again later
            state.getExeStack().push(this);
        }

        return null;  // no new thread created
    }

    @Override
    public Map<String, IType> typecheck(Map<String, IType> typeEnv) throws TypeException {
        // check variable exists and is IntType
        if (!typeEnv.containsKey(variableName)) {
            throw new TypeException("Lock: Variable " + variableName + " is not declared");
        }

        IType varType = typeEnv.get(variableName);
        if (!(varType instanceof IntType)) {
            throw new TypeException("Lock: Variable must be IntType, got " + varType);
        }

        return typeEnv;
    }

    @Override
    public IStatement deepCopy() {
        return new LockStatement(variableName);
    }

    @Override
    public String toString() {
        return "lock(" + variableName + ")";
    }
}
