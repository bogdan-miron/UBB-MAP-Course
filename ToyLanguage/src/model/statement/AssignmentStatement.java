package model.statement;

import model.exception.TypeException;
import model.expression.IExpression;
import model.state.ProgramState;
import model.value.IValue;

public class AssignmentStatement implements IStatement{
    private final String variableName;
    private final IExpression expression;

    public AssignmentStatement(String variableName, IExpression expression){
        this.variableName = variableName;
        this.expression = expression;
    }

    @Override
    public ProgramState execute(ProgramState state) throws TypeException {
        IValue value = expression.evaluate(state.getSymTable());
        state.getSymTable().update(variableName, value);
        return state;
    }

    @Override
    public String toString(){
        return variableName + " = " + expression;
    }
}
