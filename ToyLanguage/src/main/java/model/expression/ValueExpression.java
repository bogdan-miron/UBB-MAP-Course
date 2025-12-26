package model.expression;

import model.exception.TypeException;
import model.state.IHeap;
import model.state.ISymbolTable;
import model.type.IType;
import model.value.IValue;

import java.util.Map;

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
    public IType typecheck(Map<String, IType> typeEnv) throws TypeException {
        return value.getType();
    }

    @Override
    public IExpression deepCopy() {
        return new ValueExpression(value);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
