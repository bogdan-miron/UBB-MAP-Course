package model.statement;

import model.exception.TypeException;
import model.expression.IExpression;
import model.state.ProgramState;
import model.type.IType;
import model.type.IntType;
import model.value.IValue;
import model.value.IntValue;

import java.util.Map;

public class NewLatchStatement implements IStatement {
    private final String variableName;
    private final IExpression expression;

    public NewLatchStatement(String variableName, IExpression expression) {
        this.variableName = variableName;
        this.expression = expression;
    }

    @Override
    public ProgramState execute(ProgramState state) throws TypeException {
        // check if variable is defined and is IntType
        if (!state.getSymTable().isDefined(variableName)) {
            throw new TypeException("NewLatch: Variable " + variableName + " is not defined");
        }

        IValue varValue = state.getSymTable().lookup(variableName);
        if (!(varValue.getType() instanceof IntType)) {
            throw new TypeException("NewLatch: Variable " + variableName + " must be IntType, got " + varValue.getType());
        }

        // evaluate expression
        IValue exprValue = expression.evaluate(state.getSymTable(), state.getHeap());

        // check expression is IntType
        if (!(exprValue instanceof IntValue)) {
            throw new TypeException("NewLatch: Expression must evaluate to IntValue, got " + exprValue.getType());
        }

        int count = ((IntValue) exprValue).getValue();

        // allocate new latch in LatchTable
        int location = state.getLatchTable().allocate(count);

        // store location in the variable
        state.getSymTable().update(variableName, new IntValue(location));

        return null;  // no new thread created
    }

    @Override
    public Map<String, IType> typecheck(Map<String, IType> typeEnv) throws TypeException {
        // Check variable exists and is IntType
        if (!typeEnv.containsKey(variableName)) {
            throw new TypeException("NewLatch: Variable " + variableName + " is not declared");
        }

        IType varType = typeEnv.get(variableName);
        if (!(varType instanceof IntType)) {
            throw new TypeException("NewLatch: Variable must be IntType, got " + varType);
        }

        // Check expression is IntType
        IType exprType = expression.typecheck(typeEnv);
        if (!(exprType instanceof IntType)) {
            throw new TypeException("NewLatch: Expression must be IntType, got " + exprType);
        }

        return typeEnv;
    }

    @Override
    public IStatement deepCopy() {
        return new NewLatchStatement(variableName, expression.deepCopy());
    }

    @Override
    public String toString() {
        return "newLatch(" + variableName + "," + expression.toString() + ")";
    }
}
