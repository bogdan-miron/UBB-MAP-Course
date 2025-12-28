package model.statement;

import model.exception.TypeException;
import model.state.ProgramState;
import model.type.IType;
import model.type.IntType;
import model.value.IValue;
import model.value.IntValue;

import java.util.Map;

public class NewLockStatement implements IStatement {
    private final String variableName;

    public NewLockStatement(String variableName) {
        this.variableName = variableName;
    }

    @Override
    public ProgramState execute(ProgramState state) throws TypeException {
        // check if variable is defined and is IntType
        if (!state.getSymTable().isDefined(variableName)) {
            throw new TypeException("NewLock: Variable " + variableName + " is not defined");
        }

        IValue varValue = state.getSymTable().lookup(variableName);
        if (!(varValue.getType() instanceof IntType)) {
            throw new TypeException("NewLock: Variable " + variableName + " must be IntType, got " + varValue.getType());
        }

        // allocate new lock in LockTable (initialized to -1)
        int location = state.getLockTable().allocate();

        // store location in the variable
        state.getSymTable().update(variableName, new IntValue(location));

        return null;  // no new thread created
    }

    @Override
    public Map<String, IType> typecheck(Map<String, IType> typeEnv) throws TypeException {
        // check variable exists and is IntType
        if (!typeEnv.containsKey(variableName)) {
            throw new TypeException("NewLock: Variable " + variableName + " is not declared");
        }

        IType varType = typeEnv.get(variableName);
        if (!(varType instanceof IntType)) {
            throw new TypeException("NewLock: Variable must be IntType, got " + varType);
        }

        return typeEnv;
    }

    @Override
    public IStatement deepCopy() {
        return new NewLockStatement(variableName);
    }

    @Override
    public String toString() {
        return "newLock(" + variableName + ")";
    }
}
