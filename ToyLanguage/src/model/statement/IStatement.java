package model.statement;

import model.exception.TypeException;
import model.state.ProgramState;

public interface IStatement {
    ProgramState execute(ProgramState programState) throws TypeException;
}
