package repository;

import model.exception.RepositoryException;
import model.state.ProgramState;

import java.util.List;

public interface IRepository {
    void addState(ProgramState state);

    ProgramState getCurrentState();

    List<ProgramState> getAllStates();

    void logPrgStateExec() throws RepositoryException;
}
