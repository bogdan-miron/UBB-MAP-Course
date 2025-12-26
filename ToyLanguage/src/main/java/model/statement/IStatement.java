package model.statement;

import model.exception.TypeException;
import model.state.ProgramState;
import model.type.IType;

import java.util.Map;

public interface IStatement {
    ProgramState execute(ProgramState programState) throws TypeException;

    Map<String, IType> typecheck(Map<String, IType> typeEnv) throws TypeException;

    IStatement deepCopy();
}
