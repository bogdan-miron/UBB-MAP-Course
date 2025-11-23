package model.expression;

import model.exception.TypeException;
import model.state.IHeap;
import model.state.ISymbolTable;
import model.value.IValue;

public interface IExpression {
    IValue evaluate(ISymbolTable symbolTable, IHeap heap) throws TypeException;

    String toString();
}
