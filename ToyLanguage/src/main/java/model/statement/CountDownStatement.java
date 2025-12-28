package model.statement;

import model.exception.TypeException;
import model.state.ProgramState;
import model.type.IType;
import model.type.IntType;
import model.value.IValue;
import model.value.IntValue;

import java.util.Map;

public class CountDownStatement implements IStatement {
    private final String variableName;

    public CountDownStatement(String variableName) {
        this.variableName = variableName;
    }

    @Override
    public ProgramState execute(ProgramState state) throws TypeException {
        // check if variable is defined and is IntType
        if (!state.getSymTable().isDefined(variableName)) {
            throw new TypeException("CountDown: Variable " + variableName + " is not defined");
        }

        IValue varValue = state.getSymTable().lookup(variableName);
        if (!(varValue instanceof IntValue)) {
            throw new TypeException("CountDown: Variable " + variableName + " must be IntValue, got " + varValue.getType());
        }

        int location = ((IntValue) varValue).getValue();

        // check if location exists in LatchTable
        if (!state.getLatchTable().isDefined(location)) {
            throw new TypeException("CountDown: Latch location " + location + " is not defined in LatchTable");
        }

        // get the current count
        int count = state.getLatchTable().get(location);

        // if count > 0, decrement and print thread ID
        if (count > 0) {
            state.getLatchTable().put(location, count - 1);
        }

        // print the thread ID to output (always, even when count is 0)
        state.getOutput().add(new IntValue(state.getId()));

        return null;  // No new thread created
    }

    @Override
    public Map<String, IType> typecheck(Map<String, IType> typeEnv) throws TypeException {
        // check variable exists and is IntType
        if (!typeEnv.containsKey(variableName)) {
            throw new TypeException("CountDown: Variable " + variableName + " is not declared");
        }

        IType varType = typeEnv.get(variableName);
        if (!(varType instanceof IntType)) {
            throw new TypeException("CountDown: Variable must be IntType, got " + varType);
        }

        return typeEnv;
    }

    @Override
    public IStatement deepCopy() {
        return new CountDownStatement(variableName);
    }

    @Override
    public String toString() {
        return "countDown(" + variableName + ")";
    }
}
