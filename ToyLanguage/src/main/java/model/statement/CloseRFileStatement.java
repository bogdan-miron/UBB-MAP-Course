package model.statement;

import model.exception.TypeException;
import model.expression.IExpression;
import model.state.ProgramState;
import model.type.IType;
import model.type.StringType;
import model.value.IValue;
import model.value.StringValue;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

public class CloseRFileStatement implements IStatement {
    private final IExpression expression;

    public CloseRFileStatement(IExpression expression) {
        this.expression = expression;
    }

    @Override
    public ProgramState execute(ProgramState programState) throws TypeException {
        IValue value = expression.evaluate(programState.getSymTable(), programState.getHeap());

        if (!value.getType().equals(new StringType())) {
            throw new TypeException("CloseRFile: expression must evaluate to a string");
        }

        StringValue fileName = (StringValue) value;

        if (!programState.getFileTable().isDefined(fileName)) {
            throw new TypeException("CloseRFile: file " + fileName + " is not opened");
        }

        BufferedReader fileDescriptor = programState.getFileTable().lookup(fileName);

        try {
            fileDescriptor.close();
            programState.getFileTable().remove(fileName);
        } catch (IOException e) {
            throw new TypeException("CloseRFile: error closing file " + fileName + " - " + e.getMessage());
        }

        return null;
    }

    @Override
    public IStatement deepCopy() {
        return new CloseRFileStatement(expression.deepCopy());
    }

    @Override
    public Map<String, IType> typecheck(Map<String, IType> typeEnv) throws TypeException {
        IType typeExp = expression.typecheck(typeEnv);
        if (typeExp.equals(new StringType())) {
            return typeEnv;
        } else {
            throw new TypeException("CloseRFile: expression must be of type String");
        }
    }

    @Override
    public String toString() {
        return "closeRFile(" + expression + ")";
    }
}