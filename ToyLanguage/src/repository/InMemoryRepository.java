package repository;

import model.exception.RepositoryException;
import model.state.ProgramState;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class InMemoryRepository implements IRepository {

    private List<ProgramState> programStates;
    private final String filename;

    public InMemoryRepository(String filename) {
        this.filename = filename;
        this.programStates = new ArrayList<>();
    }

    public InMemoryRepository() {
        this("log.txt");
    }

    @Override
    public List<ProgramState> getPrgList() {
        return programStates;
    }

    @Override
    public void setPrgList(List<ProgramState> prgList) {
        this.programStates = prgList;
    }

    @Override
    public void logPrgStateExec(ProgramState programState) throws RepositoryException {
        if (programState == null) {
            throw new RepositoryException("No program state to log");
        }

        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(filename, true)))) {
            writer.println("=".repeat(80));
            writer.println("PROGRAM STATE EXECUTION LOG - Thread ID: " + programState.getId());
            writer.println("=".repeat(80));
            writer.println();

            // Log Execution Stack
            writer.println("-".repeat(80));
            writer.println("EXECUTION STACK:");
            writer.println("-".repeat(80));
            writer.println(programState.getExeStack().toString());
            writer.println();

            // Log Symbol Table
            writer.println("-".repeat(80));
            writer.println("SYMBOL TABLE:");
            writer.println("-".repeat(80));
            writer.println(programState.getSymTable().toString());
            writer.println();

            // Log Output
            writer.println("-".repeat(80));
            writer.println("OUTPUT:");
            writer.println("-".repeat(80));
            if (programState.getOutput().getOutput().isEmpty()) {
                writer.println("  [Empty]");
            } else {
                for (int i = 0; i < programState.getOutput().getOutput().size(); i++) {
                    writer.println("  [" + i + "] " + programState.getOutput().getOutput().get(i));
                }
            }
            writer.println();
            writer.println("=".repeat(80));
            writer.println();

            // Log File Table
            writer.println("-".repeat(80));
            writer.println("FILE TABLE:");
            writer.println("-".repeat(80));
            writer.println(programState.getFileTable().toString());
            writer.println();

            // Log Heap Table
            writer.println("-".repeat(80));
            writer.println("HEAP TABLE:");
            writer.println("-".repeat(80));
            writer.println(programState.getHeap().toString());
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