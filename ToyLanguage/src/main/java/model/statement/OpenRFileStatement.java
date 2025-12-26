package model.statement;

import model.exception.TypeException;
import model.expression.IExpression;
import model.state.ProgramState;
import model.type.IType;
import model.type.StringType;
import model.value.IValue;
import model.value.StringValue;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

public class OpenRFileStatement implements IStatement {
    private final IExpression expression;

    public OpenRFileStatement(IExpression expression) {
        this.expression = expression;
    }


    @Override
    public ProgramState execute(ProgramState programState) throws TypeException {
        IValue value = expression.evaluate(programState.getSymTable(), programState.getHeap());

        if (!value.getType().equals(new StringType())) {
            throw new TypeException("OpenRFile: expression must evaluate to a string");
        }

        StringValue filename = (StringValue) value;

        if (programState.getFileTable().isDefined(filename)) {
            throw new TypeException("OpenRFile: filename" + filename + "already opened");
        }

        try {
            BufferedReader fileDescriptor = new BufferedReader(new FileReader(filename.getValue()));
            programState.getFileTable().add(filename, fileDescriptor);
        } catch (IOException e) {
            throw new TypeException("OpenRFile: Could not open filename " + filename);
        }

        return null;
    }

    @Override
    public IStatement deepCopy() {
        return new OpenRFileStatement(expression.deepCopy());
    }

    @Override
    public Map<String, IType> typecheck(Map<String, IType> typeEnv) throws TypeException {
        IType typeExp = expression.typecheck(typeEnv);
        if (typeExp.equals(new StringType())) {
            return typeEnv;
        } else {
            throw new TypeException("OpenRFile: expression must be of type String");
        }
    }

    @Override
    public String toString() {
        return "openRFile(" + expression + ")";
    }
}
