package model.statement;

import model.exception.TypeException;
import model.state.ProgramState;
import model.type.IType;
import model.type.IntType;
import model.value.IValue;
import model.value.IntValue;

import java.util.Map;

public class ReleaseStatement implements IStatement {
    private final String variableName;

    public ReleaseStatement(String variableName) {
        this.variableName = variableName;
    }

    @Override
    public ProgramState execute(ProgramState state) throws TypeException {
        // check if variable is defined and is IntType
        if (!state.getSymTable().isDefined(variableName)) {
            throw new TypeException("Release: Variable " + variableName + " is not defined");
        }

        IValue varValue = state.getSymTable().lookup(variableName);
        if (!(varValue instanceof IntValue)) {
            throw new TypeException("Release: Variable " + variableName + " must be IntValue, got " + varValue.getType());
        }

        int location = ((IntValue) varValue).getValue();

        // check if location exists in SemaphoreTable
        if (!state.getSemaphoreTable().isDefined(location)) {
            throw new TypeException("Release: Semaphore location " + location + " is not defined in SemaphoreTable");
        }

        // get the current permit count
        int permits = state.getSemaphoreTable().get(location);

        // increment permit count (release one permit)
        state.getSemaphoreTable().put(location, permits + 1);

        return null;  // no new thread created
    }

    @Override
    public Map<String, IType> typecheck(Map<String, IType> typeEnv) throws TypeException {
        // check variable exists and is IntType
        if (!typeEnv.containsKey(variableName)) {
            throw new TypeException("Release: Variable " + variableName + " is not declared");
        }

        IType varType = typeEnv.get(variableName);
        if (!(varType instanceof IntType)) {
            throw new TypeException("Release: Variable must be IntType, got " + varType);
        }

        return typeEnv;
    }

    @Override
    public IStatement deepCopy() {
        return new ReleaseStatement(variableName);
    }

    @Override
    public String toString() {
        return "release(" + variableName + ")";
    }
}
