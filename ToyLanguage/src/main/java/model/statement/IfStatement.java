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
    public Map<String, IType> typecheck(Map<String, IType> typeEnv) throws TypeException {
        IType typeExp = condition.typecheck(typeEnv);
        if (typeExp.equals(new BoolType())) {
            thenStatement.typecheck(cloneTypeEnv(typeEnv));
            elseStatement.typecheck(cloneTypeEnv(typeEnv));
            return typeEnv;
        } else {
            throw new TypeException("The condition of IF has not the type bool");
        }
    }

    private Map<String, IType> cloneTypeEnv(Map<String, IType> typeEnv) {
        return new HashMap<>(typeEnv);
    }

    @Override
    public IStatement deepCopy() {
        return new IfStatement(condition.deepCopy(), thenStatement.deepCopy(), elseStatement.deepCopy());
    }

    @Override
    public String toString() {
        return "if (" + condition.toString() + " then " + thenStatement.toString() + " else " + elseStatement.toString() + ")";
    }
}
