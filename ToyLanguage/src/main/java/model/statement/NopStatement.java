package model.statement;

import model.exception.TypeException;
import model.state.ProgramState;
import model.type.IType;

import java.util.Map;

public class NopStatement implements IStatement {

    @Override
    public ProgramState execute(ProgramState state) {
        return null;
    }

    @Override
    public IStatement deepCopy() {
        return new NopStatement();
    }

    @Override
    public Map<String, IType> typecheck(Map<String, IType> typeEnv) throws TypeException {
        return typeEnv;
    }

    @Override
    public String toString() {
        return "no operation";
    }
}
