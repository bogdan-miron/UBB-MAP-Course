package repository;

import model.state.ProgramState;

import java.util.ArrayList;
import java.util.List;

public class InMemoryRepository implements IRepository{

    private final List<ProgramState> programStates;

    public InMemoryRepository(){
        this.programStates = new ArrayList<ProgramState>();
    }

    @Override
    public void addState(ProgramState state) {
        if (state == null) {
            throw new IllegalArgumentException("Cannot add null state to repository");
        }
        programStates.add(state);
    }

    @Override
    public List<ProgramState> getAllStates() {
        return programStates;
    }
}
