package model.statement;

import model.exception.TypeException;
import model.expression.IExpression;
import model.state.ProgramState;
import model.type.IType;
import model.value.IValue;

import java.util.Map;

public class AssignmentStatement implements IStatement {
    private final String variableName;
    private final IExpression expression;

    public AssignmentStatement(String variableName, IExpression expression) {
        this.variableName = variableName;
        this.expression = expression;
    }

    @Override
    public ProgramState execute(ProgramState state) throws TypeException {

        // Check if variable is already declared
        if (!state.getSymTable().isDefined(variableName)) {
            throw new TypeException("Variable " + variableName + " is not declared");
        }

        IValue value = expression.evaluate(state.getSymTable(), state.getHeap());

        // Type checking
        if (!state.getSymTable().getType(variableName).equals(value.getType())) {
            throw new TypeException("Type mismatch: cannot assign " + value.getType() +
                    " to variable " + variableName + " of type " +
                    state.getSymTable().getType(variableName));
        }

        state.getSymTable().update(variableName, value);
        return null;
    }

    @Override
    public Map<String, IType> typecheck(Map<String, IType> typeEnv) throws TypeException {
        if (!typeEnv.containsKey(variableName)) {
            throw new TypeException("Variable " + variableName + " is not declared in type environment");
        }
        IType typeVar = typeEnv.get(variableName);
        IType typeExp = expression.typecheck(typeEnv);
        if (typeVar.equals(typeExp)) {
            return typeEnv;
        } else {
            throw new TypeException("Assignment: right hand side and left hand side have different types");
        }
    }

    @Override
    public String toString() {
        return variableName + " = " + expression;
    }
}
