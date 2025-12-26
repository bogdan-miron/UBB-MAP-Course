package model.expression;

import model.exception.TypeException;
import model.state.IHeap;
import model.state.ISymbolTable;
import model.type.BoolType;
import model.type.IType;
import model.value.BooleanValue;
import model.value.IValue;

import java.util.Map;

public class LogicExpression implements IExpression {
    private final IExpression left;
    private final IExpression right;
    private final String operator; // and, or, not

    // constructor for binary operations (and, or)
    public LogicExpression(IExpression left, IExpression right, String operator) {
        this.left = left;
        this.right = right;
        this.operator = operator;
    }

    // constructor for unary operations (not)
    public LogicExpression(IExpression expression, String operator) {
        this.left = expression;
        this.right = null;
        this.operator = operator;
    }

    @Override
    public IValue evaluate(ISymbolTable symbolTable, IHeap heap) throws TypeException {
        IValue leftValue = left.evaluate(symbolTable, heap);

        if (!(leftValue instanceof BooleanValue)) {
            throw new TypeException("Logic expressions require boolean operands");
        }

        boolean leftBool = ((BooleanValue) leftValue).getValue();

        if (operator.equals("not")) {
            return new BooleanValue(!leftBool);
        }

        // for binary operations
        if (right == null) {
            throw new TypeException("Binary logic operation requires two operands");
        }

        IValue rightValue = right.evaluate(symbolTable, heap);
        if (!(rightValue instanceof BooleanValue)) {
            throw new TypeException("Logic expressions require boolean operands");
        }

        boolean rightBool = ((BooleanValue) rightValue).getValue();

        boolean result;
        switch (operator) {
            case "and":
                result = leftBool && rightBool;
                break;
            case "or":
                result = leftBool || rightBool;
                break;
            default:
                throw new RuntimeException("Unknown logic operator: " + operator);
        }

        return new BooleanValue(result);
    }

    @Override
    public IType typecheck(Map<String, IType> typeEnv) throws TypeException {
        IType type1 = left.typecheck(typeEnv);

        if (!type1.equals(new BoolType())) {
            throw new TypeException("Logic expression: first operand is not a boolean");
        }

        if (operator.equals("not")) {
            return new BoolType();
        }

        // For binary operations
        if (right == null) {
            throw new TypeException("Binary logic operation requires two operands");
        }

        IType type2 = right.typecheck(typeEnv);
        if (!type2.equals(new BoolType())) {
            throw new TypeException("Logic expression: second operand is not a boolean");
        }

        return new BoolType();
    }

    @Override
    public IExpression deepCopy() {
        if (right == null) {
            return new LogicExpression(left.deepCopy(), operator);
        }
        return new LogicExpression(left.deepCopy(), right.deepCopy(), operator);
    }

    @Override
    public String toString() {
        if (operator.equals("not")) {
            return "!(" + left.toString() + ")";
        }
        return "(" + left.toString() + " " + operator + " " + right.toString() + ")";
    }
}
