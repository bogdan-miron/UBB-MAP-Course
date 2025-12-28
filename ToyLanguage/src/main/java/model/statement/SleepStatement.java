package model.statement;

import model.exception.TypeException;
import model.state.ProgramState;
import model.type.IType;

import java.util.Map;

public class SleepStatement implements IStatement {
    private final int number;

    public SleepStatement(int number) {
        this.number = number;
    }

    @Override
    public ProgramState execute(ProgramState state) throws TypeException {
        // sleep by re-pushing itself with decremented counter
        // when counter reaches 0, execution continues
        if (number > 0) {
            state.getExeStack().push(new SleepStatement(number - 1));
        }
        // ff number == 0, sleep is complete
        return null;
    }

    @Override
    public Map<String, IType> typecheck(Map<String, IType> typeEnv) throws TypeException {
        // sleep takes a constant integer, no type-checking needed
        return typeEnv;
    }

    @Override
    public IStatement deepCopy() {
        return new SleepStatement(number);
    }

    @Override
    public String toString() {
        return "sleep(" + number + ")";
    }
}
