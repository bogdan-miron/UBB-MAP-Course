package model.statement;

import model.exception.TypeException;
import model.expression.IExpression;
import model.state.ProgramState;
import model.type.IntType;
import model.type.StringType;
import model.value.IValue;
import model.value.IntValue;
import model.value.StringValue;

import java.io.BufferedReader;
import java.io.IOException;

public class ReadFileStatement implements IStatement {

    private final IExpression expression;
    private final String variableName;

    public ReadFileStatement(IExpression expression, String variableName) {
        this.expression = expression;
        this.variableName = variableName;
    }

    @Override
    public ProgramState execute(ProgramState programState) throws TypeException {
        // Check if variable is declared and is of int type
        if (!programState.getSymTable().isDefined(variableName)) {
            throw new TypeException("ReadFile: variable" + variableName + " not defined");
        }

        if (!programState.getSymTable().getType(variableName).equals(new IntType())) {
            throw new TypeException("ReadFile: variable" + variableName + " is not of type int");
        }

        IValue value = expression.evaluate(programState.getSymTable());

        if (!value.getType().equals(new StringType())) {
            throw new TypeException("ReadFile: variable" + variableName + " is not of type String");
        }

        StringValue filename = (StringValue) value;

        // Check if file is opened
        if (!programState.getFileTable().isDefined(filename)) {
            throw new TypeException("ReadFile: filename" + variableName + " not opened");
        }

        BufferedReader fileDescriptor = programState.getFileTable().lookup(filename);

        try {
            String line = fileDescriptor.readLine();
            IntValue intValue;

            if (line == null) {
                // EOF reached, return default value
                intValue = new IntValue(0);
            } else {
                try {
                    intValue = new IntValue(Integer.parseInt(line.trim()));
                } catch (NumberFormatException e) {
                    throw new TypeException("ReadFile: invalid integer format in file" + filename);
                }
            }

            programState.getSymTable().update(variableName, intValue);

        } catch (IOException e) {
            throw new TypeException("ReadFile: Error reading from file: " + filename);
        }

        return programState;
    }

    @Override
    public String toString() {
        return "readFile(" + expression + ", " + variableName + ")";
    }
}
