package model.statement;

import model.exception.TypeException;
import model.state.ProgramState;
import model.type.IType;
import model.type.IntType;
import model.value.IValue;
import model.value.IntValue;

import java.util.Map;

public class AcquireStatement implements IStatement {
    private final String variableName;

    public AcquireStatement(String variableName) {
        this.variableName = variableName;
    }

    @Override
    public ProgramState execute(ProgramState state) throws TypeException {
        // check if variable is defined and is IntType
        if (!state.getSymTable().isDefined(variableName)) {
            throw new TypeException("Acquire: Variable " + variableName + " is not defined");
        }

        IValue varValue = state.getSymTable().lookup(variableName);
        if (!(varValue instanceof IntValue)) {
            throw new TypeException("Acquire: Variable " + variableName + " must be IntValue, got " + varValue.getType());
        }

        int location = ((IntValue) varValue).getValue();

        // check if location exists in SemaphoreTable
        if (!state.getSemaphoreTable().isDefined(location)) {
            throw new TypeException("Acquire: Semaphore location " + location + " is not defined in SemaphoreTable");
        }

        // get the permit count at this location
        int permits = state.getSemaphoreTable().get(location);

        // if permits > 0, acquire one permit (decrement) and continue
        if (permits > 0) {
            state.getSemaphoreTable().put(location, permits - 1);
            // permit acquired, don't re-push, continue execution
        } else {
            // permits == 0, busy-wait. re-push this statement onto execution stack
            state.getExeStack().push(this);
        }

        return null;  // no new thread created
    }

    @Override
    public Map<String, IType> typecheck(Map<String, IType> typeEnv) throws TypeException {
        // check variable exists and is IntType
        if (!typeEnv.containsKey(variableName)) {
            throw new TypeException("Acquire: Variable " + variableName + " is not declared");
        }

        IType varType = typeEnv.get(variableName);
        if (!(varType instanceof IntType)) {
            throw new TypeException("Acquire: Variable must be IntType, got " + varType);
        }

        return typeEnv;
    }

    @Override
    public IStatement deepCopy() {
        return new AcquireStatement(variableName);
    }

    @Override
    public String toString() {
        return "acquire(" + variableName + ")";
    }
}
