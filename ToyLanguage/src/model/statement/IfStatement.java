package model.statement;

import model.exception.TypeException;
import model.expression.IExpression;
import model.state.ProgramState;
import model.value.BooleanValue;
import model.value.IValue;

public class IfStatement implements IStatement {
    private final IExpression condition;
    private final IStatement thenStatement;
    private final IStatement elseStatement;

    public IfStatement(IExpression condition, IStatement thenStatement, IStatement elseStatement) {
        this.condition = condition;
        this.thenStatement = thenStatement;
        this.elseStatement = elseStatement;
    }

    public ProgramState execute(ProgramState state) throws TypeException {
        IValue condValue = condition.evaluate(state.getSymTable(), state.getHeap());

        if (!(condValue instanceof BooleanValue)) {
            throw new TypeException("If statement condition is not a boolean");
        }

        boolean condBool = ((BooleanValue) condValue).getValue();

        if (condBool) {
            state.getExeStack().push(thenStatement);
        } else {
            state.getExeStack().push(elseStatement);
        }

        return null;
    }

    @Override
    public String toString() {
        return "if (" + condition.toString() + " then " + thenStatement.toString() + " else " + elseStatement.toString() + ")";
    }
}
