package repository;

import model.state.ProgramState;

import java.util.List;

public interface IRepository {
    void addState(ProgramState state);
    List<ProgramState> getAllStates();
}
