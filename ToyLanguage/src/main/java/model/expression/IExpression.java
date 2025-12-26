package model.expression;

import model.exception.TypeException;
import model.state.IHeap;
import model.state.ISymbolTable;
import model.type.IType;
import model.value.IValue;

import java.util.Map;

public interface IExpression {
    IValue evaluate(ISymbolTable symbolTable, IHeap heap) throws TypeException;

    IType typecheck(Map<String, IType> typeEnv) throws TypeException;

    IExpression deepCopy();

    String toString();
}
