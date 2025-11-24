package controller;

import model.exception.ExecutionStackException;
import model.exception.RepositoryException;
import model.exception.TypeException;
import model.gc.GarbageCollector;
import model.state.ExecutionStack;
import model.state.IExecutionStack;
import model.state.ProgramState;
import model.state.SymbolTable;
import model.statement.IStatement;
import model.value.IValue;
import repository.IRepository;
import repository.InMemoryRepository;

import java.util.Map;

public class Controller {
    private ProgramState state;
    private final IRepository repo;
    private final boolean logSteps;
    private final GarbageCollector gc;

    public Controller(IStatement program, IRepository repo, boolean logSteps) {
        this.logSteps = logSteps;
        this.gc = new GarbageCollector();

        // the program will be a compound statement
        IExecutionStack exeStack = new ExecutionStack();
        exeStack.push(program);
        this.state = new ProgramState(exeStack, new SymbolTable());
        this.repo = repo;
        repo.setProgramState(this.state);

        // Clear log file if logging is enabled
        if (this.logSteps) {
            try {
                ((InMemoryRepository) repo).clearLogFile();
            } catch (RepositoryException e) {
                System.err.println("Warning: Could not clear log file: " + e.getMessage());
            }
        }
    }

    public Controller(IStatement program, IRepository repo) {
        this(program, repo, false);
    }

    public void oneStep() throws ExecutionStackException, TypeException, RepositoryException {
        IExecutionStack stack = state.getExeStack();

        if (((ExecutionStack) stack).isEmpty()) {
            throw new RuntimeException("Program has finished execution");
        }

//        if (logSteps) {
//            repo.logPrgStateExec();
//        }

        IStatement statement = stack.pop();
        state = statement.execute(state);
        repo.setProgramState(state);

    }

    public void allSteps() throws ExecutionStackException, TypeException, RepositoryException {
        IExecutionStack stack = state.getExeStack();

        if (logSteps) {
            repo.logPrgStateExec();
        }

        while (!stack.isEmpty()) {
            oneStep();
            performGarbageCollection();
            if (logSteps) {
                repo.logPrgStateExec();
            }
        }
    }

    private void performGarbageCollection() {
        Map<Integer, IValue> cleanedHeap = gc.collect(
                state.getSymTable(),
                state.getHeap()
        );

        state.getHeap().setContent(cleanedHeap);
    }

    public ProgramState getProgramState() {
        return state;
    }

    public IRepository getRepo() {
        return repo;
    }

    public GarbageCollector getGarbageCollector() {
        return gc;
    }
}