package model.statement;

import model.exception.TypeException;
import model.expression.IExpression;
import model.state.ProgramState;
import model.type.BoolType;
import model.type.IType;
import model.value.BooleanValue;
import model.value.IValue;

import java.util.HashMap;
import java.util.Map;

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
    public Map<String, IType> typecheck(Map<String, IType> typeEnv) throws TypeException {
        IType typeExp = condition.typecheck(typeEnv);
        if (typeExp.equals(new BoolType())) {
            statement.typecheck(cloneTypeEnv(typeEnv));
            return typeEnv;
        } else {
            throw new TypeException("The condition of WHILE has not the type bool");
        }
    }

    private Map<String, IType> cloneTypeEnv(Map<String, IType> typeEnv) {
        return new HashMap<>(typeEnv);
    }

    @Override
    public String toString() {
        return "while (" + condition.toString() + ") " + statement.toString();
    }
}
