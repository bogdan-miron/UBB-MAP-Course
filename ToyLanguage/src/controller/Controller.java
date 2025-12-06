package controller;

import model.exception.ExecutionStackException;
import model.exception.RepositoryException;
import model.exception.TypeException;
import model.gc.GarbageCollector;
import model.state.ExecutionStack;
import model.state.IExecutionStack;
import model.state.ISymbolTable;
import model.state.ProgramState;
import model.state.SymbolTable;
import model.statement.IStatement;
import model.value.IValue;
import repository.IRepository;
import repository.InMemoryRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class Controller {
    private final IRepository repo;
    private final boolean logSteps;
    private final GarbageCollector gc;
    private ExecutorService executor;

    public Controller(IStatement program, IRepository repo, boolean logSteps) {
        this.logSteps = logSteps;
        this.gc = new GarbageCollector();
        this.repo = repo;

        // Create the initial program state
        IExecutionStack exeStack = new ExecutionStack();
        exeStack.push(program);
        ProgramState initialState = new ProgramState(exeStack, new SymbolTable());

        // Initialize the repository with a list containing the initial program state
        List<ProgramState> initialPrgList = new ArrayList<>();
        initialPrgList.add(initialState);
        repo.setPrgList(initialPrgList);

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

    public List<ProgramState> removeCompletedPrg(List<ProgramState> inPrgList) {
        return inPrgList.stream()
                .filter(p -> p.isNotCompleted())
                .collect(Collectors.toList());
    }

    public void oneStepForAllPrg(List<ProgramState> prgList) throws InterruptedException, RepositoryException {
        // before the execution, print the PrgState List into the log file
        if (logSteps) {
            for (ProgramState prg : prgList) {
                repo.logPrgStateExec(prg);
            }
        }

        // prepare the list of callables
        List<Callable<ProgramState>> callList = prgList.stream()
                .map((ProgramState p) -> (Callable<ProgramState>) (() -> {
                    return p.oneStep();
                }))
                .collect(Collectors.toList());

        // start the execution of the callables
        // it returns the list of new created thread (programStates)
        List<ProgramState> newPrgList = executor.invokeAll(callList).stream()
                .map(future -> {
                    try {
                        return future.get();
                    } catch (Exception e) {
                        // Handle exceptions thrown during statement execution
                        System.err.println("Error during execution: " + e.getMessage());
                        if (e.getCause() != null) {
                            System.err.println("Caused by: " + e.getCause().getMessage());
                        }
                        return null;
                    }
                })
                .filter(p -> p != null)
                .collect(Collectors.toList());

        // add the new created threads to the list of existing threads
        prgList.addAll(newPrgList);

        // after the execution, print the PrgState List into the log file
        if (logSteps) {
            for (ProgramState prg : prgList) {
                repo.logPrgStateExec(prg);
            }
        }

        // save the current programs in the repository
        repo.setPrgList(prgList);
    }

    public void allSteps() throws InterruptedException, RepositoryException {
        executor = Executors.newFixedThreadPool(2);

        // remove the completed programs
        List<ProgramState> prgList = removeCompletedPrg(repo.getPrgList());

        while (prgList.size() > 0) {
            // perform garbage collection before executing the next step
            performGarbageCollection(prgList);

            // execute one step for all programs
            oneStepForAllPrg(prgList);

            // remove the completed programs
            prgList = removeCompletedPrg(repo.getPrgList());
        }

        executor.shutdownNow();

        // at this point, the repository still contains at least one completed program
        // (from the last oneStepForAllPrg call), so we can access the shared output/heap
    }

    private void performGarbageCollection(List<ProgramState> prgList) {
        // collect all symbol tables from all program states
        List<ISymbolTable> symTables = prgList.stream()
                .map(ProgramState::getSymTable)
                .collect(Collectors.toList());

        // get the heap from the first program state - all states share the same heap
        if (!prgList.isEmpty()) {
            Map<Integer, IValue> cleanedHeap = gc.collectConcurrent(
                    symTables,
                    prgList.get(0).getHeap()
            );

            prgList.get(0).getHeap().setContent(cleanedHeap);
        }
    }

    public IRepository getRepo() {
        return repo;
    }

    public GarbageCollector getGarbageCollector() {
        return gc;
    }
}