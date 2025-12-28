package model.statement;

import model.exception.TypeException;
import model.state.ProgramState;
import model.type.IType;
import model.type.IntType;
import model.value.IValue;
import model.value.IntValue;

import java.util.Map;

public class UnlockStatement implements IStatement {
    private final String variableName;

    public UnlockStatement(String variableName) {
        this.variableName = variableName;
    }

    @Override
    public ProgramState execute(ProgramState state) throws TypeException {
        // check if variable is defined and is IntType
        if (!state.getSymTable().isDefined(variableName)) {
            throw new TypeException("Unlock: Variable " + variableName + " is not defined");
        }

        IValue varValue = state.getSymTable().lookup(variableName);
        if (!(varValue instanceof IntValue)) {
            throw new TypeException("Unlock: Variable " + variableName + " must be IntValue, got " + varValue.getType());
        }

        int location = ((IntValue) varValue).getValue();

        // check if location exists in LockTable
        if (!state.getLockTable().isDefined(location)) {
            throw new TypeException("Unlock: Lock location " + location + " is not defined in LockTable");
        }

        // only unlock if current thread owns the lock
        int currentValue = state.getLockTable().get(location);
        int currentThreadId = state.getId();

        if (currentValue == currentThreadId) {
            // current thread owns the lock - release it
            state.getLockTable().put(location, -1);
        }
        // else: thread doesn't own the lock - do nothing

        return null;  // no new thread created
    }

    @Override
    public Map<String, IType> typecheck(Map<String, IType> typeEnv) throws TypeException {
        // check variable exists and is IntType
        if (!typeEnv.containsKey(variableName)) {
            throw new TypeException("Unlock: Variable " + variableName + " is not declared");
        }

        IType varType = typeEnv.get(variableName);
        if (!(varType instanceof IntType)) {
            throw new TypeException("Unlock: Variable must be IntType, got " + varType);
        }

        return typeEnv;
    }

    @Override
    public IStatement deepCopy() {
        return new UnlockStatement(variableName);
    }

    @Override
    public String toString() {
        return "unlock(" + variableName + ")";
    }
}
