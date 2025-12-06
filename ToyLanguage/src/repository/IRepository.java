package repository;

import model.exception.RepositoryException;
import model.state.ProgramState;

import java.util.List;

public interface IRepository {
    List<ProgramState> getPrgList();

    void setPrgList(List<ProgramState> prgList);

    void logPrgStateExec(ProgramState programState) throws RepositoryException;
}