package model.expression;

import model.exception.TypeException;
import model.state.IHeap;
import model.state.ISymbolTable;
import model.type.IntType;
import model.type.IType;
import model.value.IValue;
import model.value.IntValue;

import java.util.Map;

/**
 * MUL(exp1, exp2) is evaluated to ((exp1*exp2)-(exp1+exp2))
 */
public class MulExpression implements IExpression {
    private final IExpression exp1;
    private final IExpression exp2;

    public MulExpression(IExpression exp1, IExpression exp2) {
        this.exp1 = exp1;
        this.exp2 = exp2;
    }

    @Override
    public IValue evaluate(ISymbolTable symbolTable, IHeap heap) throws TypeException {
        IValue value1 = exp1.evaluate(symbolTable, heap);
        IValue value2 = exp2.evaluate(symbolTable, heap);

        if (!(value1 instanceof IntValue)) {
            throw new TypeException("MUL expression: first operand is not an integer", new IntType(), value1.getType());
        }

        if (!(value2 instanceof IntValue)) {
            throw new TypeException("MUL expression: second operand is not an integer", new IntType(), value2.getType());
        }

        int int1 = ((IntValue) value1).getValue();
        int int2 = ((IntValue) value2).getValue();

        // MUL(exp1, exp2) = (exp1 * exp2) - (exp1 + exp2)
        int result = (int1 * int2) - (int1 + int2);

        return new IntValue(result);
    }

    @Override
    public IType typecheck(Map<String, IType> typeEnv) throws TypeException {
        IType type1 = exp1.typecheck(typeEnv);
        IType type2 = exp2.typecheck(typeEnv);

        if (type1.equals(new IntType())) {
            if (type2.equals(new IntType())) {
                return new IntType();
            } else {
                throw new TypeException("MUL expression: second operand is not an integer");
            }
        } else {
            throw new TypeException("MUL expression: first operand is not an integer");
        }
    }

    @Override
    public IExpression deepCopy() {
        return new MulExpression(exp1.deepCopy(), exp2.deepCopy());
    }

    @Override
    public String toString() {
        return "MUL(" + exp1.toString() + ", " + exp2.toString() + ")";
    }
}
