package model.statement;

import model.exception.TypeException;
import model.expression.IExpression;
import model.state.ProgramState;
import model.value.BooleanValue;
import model.value.IValue;

public class WhileStatement implements IStatement {
    private final IExpression condition;
    private final IStatement statement;

    public WhileStatement(IExpression condition, IStatement statement) {
        this.condition = condition;
        this.statement = statement;
    }

    @Override
    public ProgramState execute(ProgramState programState) throws TypeException {
        IValue condValue = condition.evaluate(programState.getSymTable(), programState.getHeap());

        if (!(condValue instanceof BooleanValue)) {
            throw new TypeException("While statement condition is not a boolean");
        }

        boolean condBool = ((BooleanValue) condValue).getValue();

        if (condBool) {
            programState.getExeStack().push(this);
            programState.getExeStack().push(statement);
        }

        return null;
    }

    @Override
    public String toString() {
        return "while (" + condition.toString() + ") " + statement.toString();
    }
}
