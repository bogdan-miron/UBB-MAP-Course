package model.expression;

import model.state.ISymbolTable;
import model.value.IValue;

public class VariableExpression implements IExpression{
    private final String variableName;

    public VariableExpression(String variableName){
        this.variableName = variableName;
    }

    @Override
    public IValue evaluate(ISymbolTable symbolTable){
        if (!symbolTable.isDefined(variableName)){
            throw new RuntimeException("Variable " + variableName + " is not defined");
        }

        return ((ISymbolTable)symbolTable).lookup(variableName);
    }

    @Override
    public String toString(){
        return variableName;
    }
}
