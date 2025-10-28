package model.statement;

import model.exception.TypeException;
import model.expression.IExpression;
import model.state.ProgramState;
import model.value.IValue;

public class PrintStatement implements IStatement{
    private final IExpression expression;

    public PrintStatement(IExpression expression){
        this.expression = expression;
    }

    @Override
    public ProgramState execute(ProgramState state) throws TypeException {
        IValue result = expression.evaluate(state.getSymTable());
        state.getOutput().add(result);
        return state;
    }

    @Override
    public String toString(){
        return "print(" + expression + ")";
    }
}
