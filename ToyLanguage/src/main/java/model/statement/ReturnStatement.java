package model.statement;

import model.exception.TypeException;
import model.state.ProgramState;
import model.type.IType;

import java.util.Map;

public class ReturnStatement implements IStatement {

    public ReturnStatement() {
    }

    @Override
    public ProgramState execute(ProgramState state) throws TypeException {
        // pop the top SymTable (restore caller's scope)
        state.popSymTable();
        return null;
    }

    @Override
    public Map<String, IType> typecheck(Map<String, IType> typeEnv) throws TypeException {
        return typeEnv;
    }

    @Override
    public IStatement deepCopy() {
        return new ReturnStatement();
    }

    @Override
    public String toString() {
        return "return";
    }
}
