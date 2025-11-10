package model.expression;

import model.exception.TypeException;
import model.state.ISymbolTable;
import model.type.IntType;
import model.value.IValue;
import model.value.IntValue;

public class ArithmeticExpression implements IExpression {
    private final IExpression left;
    private final IExpression right;
    private final String operator; // +, -, *, /

    public ArithmeticExpression(IExpression left, IExpression right, String operator) {
        this.left = left;
        this.right = right;
        this.operator = operator;
    }

    @Override
    public IValue evaluate(ISymbolTable symbolTable) throws TypeException {
        IValue leftValue = left.evaluate(symbolTable);
        IValue rightValue = right.evaluate(symbolTable);

        if (!(leftValue instanceof IntValue)) {
            throw new TypeException("Type mismatch", new IntType(), leftValue.getType());
        }

        if (!(rightValue instanceof IntValue)) {
            throw new TypeException("Type mismatch", new IntType(), rightValue.getType());
        }

        int leftInt = ((IntValue) leftValue).getValue();
        int rightInt = ((IntValue) rightValue).getValue();

        int result = 0;
        switch (operator) {
            case "+":
                result = leftInt + rightInt;
                break;
            case "-":
                result = leftInt - rightInt;
                break;
            case "*":
                result = leftInt * rightInt;
                break;
            case "/":
                if (rightInt == 0) {
                    throw new RuntimeException("Division by zero");
                }
                result = leftInt / rightInt;
                break;
            default:
                throw new RuntimeException("Unknown operator: " + operator);
        }

        return new IntValue(result);
    }

    @Override
    public String toString() {
        return "(" + left.toString() + " " + operator + " " + right.toString() + ")";
    }
}
