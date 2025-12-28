package model.statement;

import model.exception.TypeException;
import model.expression.IExpression;
import model.expression.RelationalExpression;
import model.state.ProgramState;
import model.type.IType;

import java.util.Map;

public class SwitchStatement implements IStatement {
    private final IExpression exp;       // expression to switch on
    private final IExpression exp1;      // case 1 value
    private final IExpression exp2;      // case 2 value
    private final IStatement stmt;       // case 1 statement
    private final IStatement stmt1;      // case 2 statement
    private final IStatement stmt2;      // default statement

    public SwitchStatement(IExpression exp, IExpression exp1, IExpression exp2,
                          IStatement stmt, IStatement stmt1, IStatement stmt2) {
        this.exp = exp;
        this.exp1 = exp1;
        this.exp2 = exp2;
        this.stmt = stmt;
        this.stmt1 = stmt1;
        this.stmt2 = stmt2;
    }

    @Override
    public ProgramState execute(ProgramState state) throws TypeException {
        //switch(exp) (case(exp1): stmt) (case(exp2): stmt1) (default: stmt2)
        // (exp == exp1) then stmt else if (exp == exp2) then stmt1 else stmt2

        IStatement transformed = new IfStatement(
                new RelationalExpression(exp, exp1, "=="),
                stmt,
                new IfStatement(
                        new RelationalExpression(exp, exp2, "=="),
                        stmt1,
                        stmt2
                )
        );

        state.getExeStack().push(transformed);
        return null;
    }

    @Override
    public Map<String, IType> typecheck(Map<String, IType> typeEnv) throws TypeException {
        // Type check all expressions
        IType typeExp = exp.typecheck(typeEnv);
        IType typeExp1 = exp1.typecheck(typeEnv);
        IType typeExp2 = exp2.typecheck(typeEnv);

        // All expressions must be the same type
        if (!typeExp.equals(typeExp1)) {
            throw new TypeException("Switch: case expression 1 type " + typeExp1 +
                    " does not match switch expression type " + typeExp);
        }
        if (!typeExp.equals(typeExp2)) {
            throw new TypeException("Switch: case expression 2 type " + typeExp2 +
                    " does not match switch expression type " + typeExp);
        }

        // Type check all statements
        stmt.typecheck(cloneTypeEnv(typeEnv));
        stmt1.typecheck(cloneTypeEnv(typeEnv));
        stmt2.typecheck(cloneTypeEnv(typeEnv));

        return typeEnv;
    }

    private Map<String, IType> cloneTypeEnv(Map<String, IType> typeEnv) {
        return new java.util.HashMap<>(typeEnv);
    }

    @Override
    public IStatement deepCopy() {
        return new SwitchStatement(
                exp.deepCopy(),
                exp1.deepCopy(),
                exp2.deepCopy(),
                stmt.deepCopy(),
                stmt1.deepCopy(),
                stmt2.deepCopy()
        );
    }

    @Override
    public String toString() {
        return "(switch(" + exp.toString() + ")" +
                " (case(" + exp1.toString() + "): " + stmt.toString() + ")" +
                " (case(" + exp2.toString() + "): " + stmt1.toString() + ")" +
                " (default: " + stmt2.toString() + "))";
    }
}
