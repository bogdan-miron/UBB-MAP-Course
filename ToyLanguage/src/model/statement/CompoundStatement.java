package model.statement;

import model.state.ISymbolTable;
import model.state.ProgramState;

public class CompoundStatement implements IStatement{
    private final IStatement first;
    private final IStatement second;

    public CompoundStatement(IStatement first, IStatement second){
        this.first = first;
        this.second = second;
    }

    @Override
    public ProgramState execute(ProgramState state){
        // push statements in reverse order
        state.getExeStack().push(second);
        state.getExeStack().push(first);
        return state;
    }

    @Override
    public String toString(){
        return "(" + first.toString() + " " + second.toString() + ")";
    }
}
