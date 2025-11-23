package model.expression;

import model.exception.TypeException;
import model.state.ISymbolTable;
import model.state.IHeap;
import model.value.IValue;
import model.value.RefValue;

public class HeapReadExpression implements IExpression {
    private final IExpression expression;

    public HeapReadExpression(IExpression expression) {
        this.expression = expression;
    }

    @Override
    public IValue evaluate(ISymbolTable symbolTable, IHeap heap) throws TypeException {
        IValue value = expression.evaluate(symbolTable, heap);

        if (!(value instanceof RefValue)) {
            throw new TypeException("Heap reading: Expression must evaluate to RefValue, got " + value.getType());
        }

        RefValue refValue = (RefValue) value;
        int address = refValue.getAddr();

        if (!heap.isDefined(address)) {
            throw new TypeException("Heap reading: Address " + address + " is not defined in heap");
        }

        return heap.get(address);
    }

    @Override
    public String toString() {
        return "rH(" + expression.toString() + ")";
    }
}