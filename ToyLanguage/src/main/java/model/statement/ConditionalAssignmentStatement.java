package model.statement;

import model.exception.TypeException;
import model.expression.IExpression;
import model.state.ProgramState;
import model.type.BoolType;
import model.type.IType;

import java.util.Map;


// Execution transforms this into: if (exp1) then v=exp2 else v=exp3

public class ConditionalAssignmentStatement implements IStatement {
    private final String variableName;
    private final IExpression condition;
    private final IExpression thenExpression;
    private final IExpression elseExpression;

    public ConditionalAssignmentStatement(String variableName, IExpression condition,
                                         IExpression thenExpression, IExpression elseExpression) {
        this.variableName = variableName;
        this.condition = condition;
        this.thenExpression = thenExpression;
        this.elseExpression = elseExpression;
    }

    @Override
    public ProgramState execute(ProgramState state) throws TypeException {
        // v = exp1 ? exp2 : exp3
        // if (exp1) then v=exp2 else v=exp3

        // assignment statements for then and else branches
        IStatement thenAssignment = new AssignmentStatement(variableName, thenExpression);
        IStatement elseAssignment = new AssignmentStatement(variableName, elseExpression);

        // create the if statement with the condition and assignments
        IStatement transformedStatement = new IfStatement(condition, thenAssignment, elseAssignment);

        // push the transformed statement onto the execution stack
        state.getExeStack().push(transformedStatement);

        return null;
    }

    @Override
    public Map<String, IType> typecheck(Map<String, IType> typeEnv) throws TypeException {
        // Check if variable is declared
        if (!typeEnv.containsKey(variableName)) {
            throw new TypeException("Variable " + variableName + " is not declared in type environment");
        }

        // Type check the condition - must be boolean
        IType conditionType = condition.typecheck(typeEnv);
        if (!conditionType.equals(new BoolType())) {
            throw new TypeException("Conditional assignment: condition must have boolean type");
        }

        // Type check both expressions
        IType thenType = thenExpression.typecheck(typeEnv);
        IType elseType = elseExpression.typecheck(typeEnv);

        // Get variable type
        IType variableType = typeEnv.get(variableName);

        // Check that all types match
        if (!variableType.equals(thenType)) {
            throw new TypeException("Conditional assignment: then expression type (" + thenType +
                    ") does not match variable type (" + variableType + ")");
        }

        if (!variableType.equals(elseType)) {
            throw new TypeException("Conditional assignment: else expression type (" + elseType +
                    ") does not match variable type (" + variableType + ")");
        }

        return typeEnv;
    }

    @Override
    public IStatement deepCopy() {
        return new ConditionalAssignmentStatement(
                variableName,
                condition.deepCopy(),
                thenExpression.deepCopy(),
                elseExpression.deepCopy()
        );
    }

    @Override
    public String toString() {
        return variableName + " = (" + condition.toString() + " ? " +
                thenExpression.toString() + " : " + elseExpression.toString() + ")";
    }
}
