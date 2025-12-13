package model.expression;

import model.exception.TypeException;
import model.state.IHeap;
import model.state.ISymbolTable;
import model.type.IType;
import model.value.IValue;

import java.util.Map;

public class VariableExpression implements IExpression {
    private final String variableName;

    public VariableExpression(String variableName) {
        this.variableName = variableName;
    }

    @Override
    public IValue evaluate(ISymbolTable symbolTable, IHeap heap) {
        if (!symbolTable.isDefined(variableName)) {
            throw new RuntimeException("Variable " + variableName + " is not defined");
        }

        return ((ISymbolTable) symbolTable).lookup(variableName);
    }

    @Override
    public IType typecheck(Map<String, IType> typeEnv) throws TypeException {
        if (!typeEnv.containsKey(variableName)) {
            throw new TypeException("Variable " + variableName + " is not defined in type environment");
        }
        return typeEnv.get(variableName);
    }

    @Override
    public String toString() {
        return variableName;
    }
}
