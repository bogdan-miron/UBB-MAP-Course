package repository;

import model.exception.RepositoryException;
import model.state.ProgramState;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class InMemoryRepository implements IRepository {

    private ProgramState currentProgramState;
    private final String filename;

    public InMemoryRepository(String filename) {
        this.filename = filename;
        this.currentProgramState = null;
    }

    public InMemoryRepository() {
        this("log.txt");
    }

    @Override
    public void setProgramState(ProgramState state) {
        if (state == null) {
            throw new IllegalArgumentException("Cannot set null state in repository");
        }
        this.currentProgramState = state;
    }

    @Override
    public ProgramState getProgramState() {
        return currentProgramState;
    }

    @Override
    public void logPrgStateExec() throws RepositoryException {
        if (currentProgramState == null) {
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
            writer.println(currentProgramState.getExeStack().toString());
            writer.println();

            // Log Symbol Table
            writer.println("-".repeat(80));
            writer.println("SYMBOL TABLE:");
            writer.println("-".repeat(80));
            writer.println(currentProgramState.getSymTable().toString());
            writer.println();

            // Log Output
            writer.println("-".repeat(80));
            writer.println("OUTPUT:");
            writer.println("-".repeat(80));
            if (currentProgramState.getOutput().getOutput().isEmpty()) {
                writer.println("  [Empty]");
            } else {
                for (int i = 0; i < currentProgramState.getOutput().getOutput().size(); i++) {
                    writer.println("  [" + i + "] " + currentProgramState.getOutput().getOutput().get(i));
                }
            }
            writer.println();
            writer.println("=".repeat(80));
            writer.println();

            // Log File Table
            writer.println("-".repeat(80));
            writer.println("FILE TABLE:");
            writer.println("-".repeat(80));
            writer.println(currentProgramState.getFileTable().toString());
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