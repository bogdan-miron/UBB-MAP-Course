package model.expression;

import model.exception.TypeException;
import model.state.IHeap;
import model.state.ISymbolTable;
import model.type.BoolType;
import model.type.IntType;
import model.type.IType;
import model.value.BooleanValue;
import model.value.IValue;
import model.value.IntValue;

import java.util.Map;

public class RelationalExpression implements IExpression {
    private IExpression left;
    private IExpression right;
    private final String operator; // <, >, <=, >=, ==, !=

    public RelationalExpression(IExpression left, IExpression right, String operator) {
        this.left = left;
        this.right = right;
        this.operator = operator;
    }

    @Override
    public IValue evaluate(ISymbolTable symbolTable, IHeap heap) throws TypeException {
        IValue leftValue = left.evaluate(symbolTable, heap);
        IValue rightValue = right.evaluate(symbolTable, heap);

        if (!(leftValue instanceof IntValue) || !(rightValue instanceof IntValue)) {
            throw new TypeException("Relational expressions require integer operands");
        }

        int leftInt = ((IntValue) leftValue).getValue();
        int rightInt = ((IntValue) rightValue).getValue();

        boolean result;
        switch (operator) {
            case "<":
                result = leftInt < rightInt;
                break;
            case ">":
                result = leftInt > rightInt;
                break;
            case "<=":
                result = leftInt <= rightInt;
                break;
            case ">=":
                result = leftInt >= rightInt;
                break;
            case "==":
                result = leftInt == rightInt;
                break;
            case "!=":
                result = leftInt != rightInt;
                break;
            default:
                throw new RuntimeException("Unknown operator: " + operator);
        }
        return new BooleanValue(result);
    }

    @Override
    public IType typecheck(Map<String, IType> typeEnv) throws TypeException {
        IType type1 = left.typecheck(typeEnv);
        IType type2 = right.typecheck(typeEnv);

        if (type1.equals(new IntType())) {
            if (type2.equals(new IntType())) {
                return new BoolType();
            } else {
                throw new TypeException("Relational expression: second operand is not an integer");
            }
        } else {
            throw new TypeException("Relational expression: first operand is not an integer");
        }
    }

    @Override
    public String toString() {
        return "(" + left.toString() + " " + operator + " " + right.toString() + ")";
    }
}
