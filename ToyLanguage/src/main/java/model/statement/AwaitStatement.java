package model.statement;

import model.exception.TypeException;
import model.state.ProgramState;
import model.type.IType;
import model.type.IntType;
import model.value.IValue;
import model.value.IntValue;

import java.util.Map;

public class AwaitStatement implements IStatement {
    private final String variableName;

    public AwaitStatement(String variableName) {
        this.variableName = variableName;
    }

    @Override
    public ProgramState execute(ProgramState state) throws TypeException {
        // check if variable is defined and is IntType
        if (!state.getSymTable().isDefined(variableName)) {
            throw new TypeException("Await: Variable " + variableName + " is not defined");
        }

        IValue varValue = state.getSymTable().lookup(variableName);
        if (!(varValue instanceof IntValue)) {
            throw new TypeException("Await: Variable " + variableName + " must be IntValue, got " + varValue.getType());
        }

        int location = ((IntValue) varValue).getValue();

        // check if location exists in LatchTable
        if (!state.getLatchTable().isDefined(location)) {
            throw new TypeException("Await: Latch location " + location + " is not defined in LatchTable");
        }

        // get the count at this location
        int count = state.getLatchTable().get(location);

        // if count > 0, re-push this statement onto execution stack (busy wait)
        if (count > 0) {
            state.getExeStack().push(this);
        }
        // ff count == 0, the await completes, don't re-push

        return null;  // no new thread created
    }

    @Override
    public Map<String, IType> typecheck(Map<String, IType> typeEnv) throws TypeException {
        // check variable exists and is IntType
        if (!typeEnv.containsKey(variableName)) {
            throw new TypeException("Await: Variable " + variableName + " is not declared");
        }

        IType varType = typeEnv.get(variableName);
        if (!(varType instanceof IntType)) {
            throw new TypeException("Await: Variable must be IntType, got " + varType);
        }

        return typeEnv;
    }

    @Override
    public IStatement deepCopy() {
        return new AwaitStatement(variableName);
    }

    @Override
    public String toString() {
        return "await(" + variableName + ")";
    }
}
