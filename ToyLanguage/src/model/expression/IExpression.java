package model.expression;

import model.exception.TypeException;
import model.state.ISymbolTable;
import model.value.IValue;

public interface IExpression {
    IValue evaluate(ISymbolTable symbolTable) throws TypeException;
    String toString();
}
