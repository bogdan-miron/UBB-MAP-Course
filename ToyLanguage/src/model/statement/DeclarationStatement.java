package model.statement;

import model.exception.TypeException;
import model.state.ProgramState;
import model.type.IType;
import model.value.BooleanValue;
import model.value.IValue;
import model.value.IntValue;

public class DeclarationStatement implements IStatement {
    private final String variableName;
    private final IType type;

    public DeclarationStatement(String variableName, IType type) {
        this.variableName = variableName;
        this.type = type;
    }

    @Override
    public ProgramState execute(ProgramState programState) throws TypeException {
        if (programState.getSymTable().isDefined(variableName)) {
            throw new TypeException("Variable " + variableName + " is already defined");
        }

        IValue defaultValue = type.defaultValue();
        programState.getSymTable().update(variableName, defaultValue);

        return null;
    }

    @Override
    public String toString() {
        return type + " " + variableName;
    }
}
