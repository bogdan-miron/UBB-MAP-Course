package repository;

import model.exception.RepositoryException;
import model.state.ProgramState;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class InMemoryRepository implements IRepository{

    private final List<ProgramState> programStates;
    private final String filename;

    public InMemoryRepository(String filename){
        this.filename = filename;
        this.programStates = new ArrayList<ProgramState>();
    }

    public InMemoryRepository(){
        this("log.txt");
    }

    @Override
    public void addState(ProgramState state) {
        if (state == null) {
            throw new IllegalArgumentException("Cannot add null state to repository");
        }
        programStates.add(state);
    }

    @Override
    public ProgramState getCurrentState(){
        if (programStates.isEmpty()){
            return null;
        }
        return programStates.get(programStates.size() - 1);
    }

    @Override
    public List<ProgramState> getAllStates() {
        return programStates;
    }

    @Override
    public void logPrgStateExec() throws RepositoryException {
        ProgramState currentState = getCurrentState();
        if (currentState == null) {
            throw new RepositoryException("No program state to log");
        }

        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(filename, true)))) {
            writer.println("=".repeat(80));
            writer.println("PROGRAM STATE EXECUTION LOG");
            writer.println("=".repeat(80));
            writer.println();

            // Log Execution Stack
            writer.println("-".repeat(80));
            writer.println("EXECUTION STACK:");
            writer.println("-".repeat(80));
            writer.println(currentState.getExeStack().toString());
            writer.println();

            // Log Symbol Table
            writer.println("-".repeat(80));
            writer.println("SYMBOL TABLE:");
            writer.println("-".repeat(80));
            writer.println(currentState.getSymTable().toString());
            writer.println();

            // Log Output
            writer.println("-".repeat(80));
            writer.println("OUTPUT:");
            writer.println("-".repeat(80));
            if (currentState.getOutput().getOutput().isEmpty()) {
                writer.println("  [Empty]");
            } else {
                for (int i = 0; i < currentState.getOutput().getOutput().size(); i++) {
                    writer.println("  [" + i + "] " + currentState.getOutput().getOutput().get(i));
                }
            }
            writer.println();
            writer.println("=".repeat(80));
            writer.println();

        } catch (IOException e) {
            throw new RepositoryException("Error writing to log file: " + e.getMessage());
        }

    }

    public void clearLogFile() throws RepositoryException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename, false))) {
            // opening in write mode (false) clears the file
            writer.println("Program Execution Log");
            writer.println("Initialized: " + java.time.LocalDateTime.now());
            writer.println();
        } catch (IOException e) {
            throw new RepositoryException("Error clearing log file: " + e.getMessage());
        }
    }

}
