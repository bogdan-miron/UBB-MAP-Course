package model.statement;

import model.exception.TypeException;
import model.state.ProgramState;
import model.type.IType;
import model.type.IntType;
import model.util.Pair;
import model.value.IValue;
import model.value.IntValue;

import java.util.List;
import java.util.Map;

public class BarrierAwaitStatement implements IStatement {
    private final String variableName;

    public BarrierAwaitStatement(String variableName) {
        this.variableName = variableName;
    }

    @Override
    public ProgramState execute(ProgramState state) throws TypeException {
        // check if variable is defined and is IntType
        if (!state.getSymTable().isDefined(variableName)) {
            throw new TypeException("BarrierAwait: Variable " + variableName + " is not defined");
        }

        IValue varValue = state.getSymTable().lookup(variableName);
        if (!(varValue instanceof IntValue)) {
            throw new TypeException("BarrierAwait: Variable " + variableName + " must be IntValue, got " + varValue.getType());
        }

        int location = ((IntValue) varValue).getValue();

        // check if location exists in BarrierTable
        if (!state.getBarrierTable().isDefined(location)) {
            throw new TypeException("BarrierAwait: Barrier location " + location + " is not defined in BarrierTable");
        }

        // get the barrier entry (capacity, waitingList)
        Pair<Integer, List<Integer>> barrierEntry = state.getBarrierTable().get(location);
        int capacity = barrierEntry.getFirst();
        List<Integer> waitingList = barrierEntry.getSecond();

        int currentThreadId = state.getId();

        // if capacity > list size, threads still need to arrive
        if (capacity > waitingList.size()) {
            // add current thread to list if not already there
            if (!waitingList.contains(currentThreadId)) {
                waitingList.add(currentThreadId);
                // update barrier table with modified list
                state.getBarrierTable().put(location, new Pair<>(capacity, waitingList));
            }
            // re-push to wait (busy-wait)
            state.getExeStack().push(this);
        }
        // if capacity == list size, barrier opens, all proceed (don't re-push)

        return null;  // no new thread created
    }

    @Override
    public Map<String, IType> typecheck(Map<String, IType> typeEnv) throws TypeException {
        // check variable exists and is IntType
        if (!typeEnv.containsKey(variableName)) {
            throw new TypeException("BarrierAwait: Variable " + variableName + " is not declared");
        }

        IType varType = typeEnv.get(variableName);
        if (!(varType instanceof IntType)) {
            throw new TypeException("BarrierAwait: Variable must be IntType, got " + varType);
        }

        return typeEnv;
    }

    @Override
    public IStatement deepCopy() {
        return new BarrierAwaitStatement(variableName);
    }

    @Override
    public String toString() {
        return "barrierAwait(" + variableName + ")";
    }
}
