package model.statement;

import model.exception.TypeException;
import model.expression.IExpression;
import model.state.ProgramState;
import model.type.IType;
import model.type.RefType;
import model.value.IValue;
import model.value.RefValue;

import java.util.Map;

public class HeapWriteStatement implements IStatement {
    private final String variableName;
    private final IExpression expression;

    public HeapWriteStatement(String variableName, IExpression expression) {
        this.variableName = variableName;
        this.expression = expression;
    }

    @Override
    public ProgramState execute(ProgramState state) throws TypeException {
        // check if variable is defined and is RefType
        if (!state.getSymTable().isDefined(variableName)) {
            throw new TypeException("Heap write: Variable " + variableName + " is not defined");
        }

        IValue varValue = state.getSymTable().lookup(variableName);
        if (!(varValue instanceof RefValue)) {
            throw new TypeException("Heap write: Variable " + variableName + " must be RefValue, got " + varValue.getType());
        }

        RefValue refValue = (RefValue) varValue;
        int address = refValue.getAddr();

        // check if address exists in heap
        if (!state.getHeap().isDefined(address)) {
            throw new TypeException("Heap write: Address " + address + " is not defined in heap");
        }

        // evaluate expression
        IValue exprValue = expression.evaluate(state.getSymTable(), state.getHeap());

        // check type compatibility
        if (!exprValue.getType().equals(refValue.getLocationType())) {
            throw new TypeException("Heap write: Type mismatch. Expected " + refValue.getLocationType() +
                    " but got " + exprValue.getType());
        }

        // update heap
        state.getHeap().put(address, exprValue);

        return null;
    }

    @Override
    public Map<String, IType> typecheck(Map<String, IType> typeEnv) throws TypeException {
        if (!typeEnv.containsKey(variableName)) {
            throw new TypeException("Variable " + variableName + " is not declared in type environment");
        }
        IType typeVar = typeEnv.get(variableName);
        IType typeExp = expression.typecheck(typeEnv);

        if (typeVar instanceof RefType) {
            RefType refType = (RefType) typeVar;
            if (refType.getInner().equals(typeExp)) {
                return typeEnv;
            } else {
                throw new TypeException("Heap write: right hand side and left hand side have different types");
            }
        } else {
            throw new TypeException("Heap write: variable must be RefType");
        }
    }

    @Override
    public String toString() {
        return "wH(" + variableName + "," + expression.toString() + ")";
    }
}