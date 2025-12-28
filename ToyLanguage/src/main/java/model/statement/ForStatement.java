package model.statement;

import model.exception.TypeException;
import model.expression.IExpression;
import model.expression.RelationalExpression;
import model.expression.VariableExpression;
import model.state.ProgramState;
import model.type.IType;
import model.type.IntType;

import java.util.HashMap;
import java.util.Map;

public class ForStatement implements IStatement {
    private final String variableName;
    private final IExpression exp1;
    private final IExpression exp2;
    private final IExpression exp3;
    private final IStatement statement;

    public ForStatement(String variableName, IExpression exp1, IExpression exp2, IExpression exp3, IStatement statement) {
        this.variableName = variableName;
        this.exp1 = exp1;
        this.exp2 = exp2;
        this.exp3 = exp3;
        this.statement = statement;
    }

    @Override
    public ProgramState execute(ProgramState state) throws TypeException {
        // for(v=exp1;v<exp2;v=exp3) stmt
        // int v; v=exp1; while(v<exp2) {stmt; v=exp3}

        IStatement transformed = new CompoundStatement(
            new DeclarationStatement(variableName, new IntType()),
            new CompoundStatement(
                new AssignmentStatement(variableName, exp1),
                new WhileStatement(
                    new RelationalExpression(
                        new VariableExpression(variableName),
                        exp2,
                        "<"
                    ),
                    new CompoundStatement(
                        statement,
                        new AssignmentStatement(variableName, exp3)
                    )
                )
            )
        );

        state.getExeStack().push(transformed);
        return null;
    }

    @Override
    public Map<String, IType> typecheck(Map<String, IType> typeEnv) throws TypeException {
        // create new environment with v:IntType
        Map<String, IType> newEnv = cloneTypeEnv(typeEnv);
        newEnv.put(variableName, new IntType());

        // check all expressions are IntType
        IType type1 = exp1.typecheck(newEnv);
        if (!(type1 instanceof IntType)) {
            throw new TypeException("For: exp1 must be IntType, got " + type1);
        }

        IType type2 = exp2.typecheck(newEnv);
        if (!(type2 instanceof IntType)) {
            throw new TypeException("For: exp2 must be IntType, got " + type2);
        }

        IType type3 = exp3.typecheck(newEnv);
        if (!(type3 instanceof IntType)) {
            throw new TypeException("For: exp3 must be IntType, got " + type3);
        }

        // check statement in new environment
        statement.typecheck(newEnv);

        // return original environment
        // v is local to the for loop
        return typeEnv;
    }

    @Override
    public IStatement deepCopy() {
        return new ForStatement(variableName, exp1.deepCopy(), exp2.deepCopy(), exp3.deepCopy(), statement.deepCopy());
    }

    @Override
    public String toString() {
        return "for(" + variableName + "=" + exp1 + ";" + variableName + "<" + exp2 + ";" + variableName + "=" + exp3 + ") " + statement;
    }

    private Map<String, IType> cloneTypeEnv(Map<String, IType> typeEnv) {
        return new HashMap<>(typeEnv);
    }
}
