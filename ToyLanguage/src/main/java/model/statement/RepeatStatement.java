package model.statement;

import model.exception.TypeException;
import model.expression.IExpression;
import model.expression.LogicExpression;
import model.state.ProgramState;
import model.type.BoolType;
import model.type.IType;

import java.util.Map;

/**
 Execute stmt1 at least once
 Continue executing stmt1 while exp2 is not true
 Equivalent to: stmt1; while(!exp2) stmt1
**/
public class RepeatStatement implements IStatement {
    private final IStatement statement;
    private final IExpression expression;

    public RepeatStatement(IStatement statement, IExpression expression) {
        this.statement = statement;
        this.expression = expression;
    }

    @Override
    public ProgramState execute(ProgramState state) throws TypeException {
        // Transform: repeat stmt until exp
        // Into: stmt; while(!exp) stmt

        // create the negated expression: !exp
        IExpression negatedExpression = new LogicExpression(expression, "not");

        // create: while(!exp) stmt
        IStatement whileStatement = new WhileStatement(negatedExpression, statement.deepCopy());

        // create: stmt; while(!exp) stmt
        IStatement transformedStatement = new CompoundStatement(statement.deepCopy(), whileStatement);

        // push the transformed statement onto the execution stack
        state.getExeStack().push(transformedStatement);

        return null;
    }

    @Override
    public IStatement deepCopy() {
        return new RepeatStatement(statement.deepCopy(), expression.deepCopy());
    }

    @Override
    public Map<String, IType> typecheck(Map<String, IType> typeEnv) throws TypeException {
        // type check the expression - must be boolean
        IType expType = expression.typecheck(typeEnv);
        if (!expType.equals(new BoolType())) {
            throw new TypeException("The condition of REPEAT statement must have boolean type");
        }

        // type check the statement
        statement.typecheck(typeEnv);

        return typeEnv;
    }

    @Override
    public String toString() {
        return "repeat (" + statement.toString() + ") until " + expression.toString();
    }
}
