package model.statement;

import model.exception.TypeException;
import model.expression.IExpression;
import model.state.ProgramState;
import model.type.IType;
import model.type.RefType;
import model.value.IValue;
import model.value.RefValue;

import java.util.Map;

public class NewStatement implements IStatement {
    private final String variableName;
    private final IExpression expression;

    public NewStatement(String variableName, IExpression expression) {
        this.variableName = variableName;
        this.expression = expression;
    }

    @Override
    public ProgramState execute(ProgramState state) throws TypeException {
        // check if variable is defined and is RefType
        if (!state.getSymTable().isDefined(variableName)) {
            throw new TypeException("Heap allocation: Variable " + variableName + " is not defined");
        }

        IValue varValue = state.getSymTable().lookup(variableName);
        if (!(varValue.getType() instanceof RefType)) {
            throw new TypeException("Heap allocation: Variable " + variableName + " must be RefType, got " + varValue.getType());
        }

        // evaluate expression
        IValue exprValue = expression.evaluate(state.getSymTable(), state.getHeap());

        // check type compatibility
        RefType refType = (RefType) varValue.getType();
        if (!exprValue.getType().equals(refType.getInner())) {
            throw new TypeException("Heap allocation: Type mismatch. Expected " + refType.getInner() +
                    " but got " + exprValue.getType());
        }

        // allocate on heap
        int newAddress = state.getHeap().allocate(exprValue);

        // update symbol table with new RefValue
        state.getSymTable().update(variableName, new RefValue(newAddress, refType.getInner()));

        return null;
    }

    @Override
    public Map<String, IType> typecheck(Map<String, IType> typeEnv) throws TypeException {
        if (!typeEnv.containsKey(variableName)) {
            throw new TypeException("Variable " + variableName + " is not declared in type environment");
        }
        IType typeVar = typeEnv.get(variableName);
        IType typeExp = expression.typecheck(typeEnv);
        if (typeVar.equals(new RefType(typeExp))) {
            return typeEnv;
        } else {
            throw new TypeException("NEW stmt: right hand side and left hand side have different types");
        }
    }

    @Override
    public String toString() {
        return "new(" + variableName + "," + expression.toString() + ")";
    }
}