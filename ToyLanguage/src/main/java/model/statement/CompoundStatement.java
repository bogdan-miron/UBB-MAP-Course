package model.statement;

import model.exception.TypeException;
import model.state.ISymbolTable;
import model.state.ProgramState;
import model.type.IType;

import java.util.Map;

public class CompoundStatement implements IStatement {
    private final IStatement first;
    private final IStatement second;

    public CompoundStatement(IStatement first, IStatement second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public ProgramState execute(ProgramState state) {
        // push statements in reverse order
        state.getExeStack().push(second);
        state.getExeStack().push(first);
        return null;
    }

    @Override
    public IStatement deepCopy() {
        return new CompoundStatement(first.deepCopy(), second.deepCopy());
    }

    @Override
    public Map<String, IType> typecheck(Map<String, IType> typeEnv) throws TypeException {
        return second.typecheck(first.typecheck(typeEnv));
    }

    @Override
    public String toString() {
        return "(" + first.toString() + " " + second.toString() + ")";
    }
}
