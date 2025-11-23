package model.expression;

import model.state.IHeap;
import model.state.ISymbolTable;
import model.value.IValue;

public class ValueExpression implements IExpression {
    private final IValue value;

    public ValueExpression(IValue value) {
        this.value = value;
    }

    @Override
    public IValue evaluate(ISymbolTable symbolTable, IHeap heap) {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
