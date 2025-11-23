package repository;

import model.exception.RepositoryException;
import model.state.ProgramState;

public interface IRepository {
    void setProgramState(ProgramState state);

    ProgramState getProgramState();

    void logPrgStateExec() throws RepositoryException;
}