package model.state;

import model.exception.ExecutionStackException;
import model.exception.TypeException;
import model.statement.IStatement;

public class ProgramState {
    private final IExecutionStack exeStack;
    private final ISymbolTable symTable;
    private final IOutput output;
    private final IFileTable fileTable;
    private final IHeap heap;
    private final ILatchTable latchTable;
    private final IBarrierTable barrierTable;
    private final int id;

    private static int nextId = 1;

    public ProgramState(IExecutionStack exeStack, ISymbolTable symTable, IFileTable fileTable, IHeap heap, ILatchTable latchTable, IBarrierTable barrierTable, IOutput output) {
        this.exeStack = exeStack;
        this.symTable = symTable;
        this.fileTable = fileTable;
        this.heap = heap;
        this.latchTable = latchTable;
        this.barrierTable = barrierTable;
        this.output = output;
        this.id = getNextId();
    }

    public ProgramState(IExecutionStack exeStack, ISymbolTable symTable, IFileTable fileTable, IHeap heap) {
        this(exeStack, symTable, fileTable, heap, new LatchTable(), new BarrierTable(), new Output());
    }

    public ProgramState(IExecutionStack exeStack, ISymbolTable symTable) {
        this(exeStack, symTable, new FileTable(), new Heap(), new LatchTable(), new BarrierTable(), new Output());
    }

    private static synchronized int getNextId() {
        return nextId++;
    }

    public int getId() {
        return id;
    }

    public boolean isNotCompleted() {
        return !exeStack.isEmpty();
    }

    public ProgramState oneStep() throws ExecutionStackException, TypeException {
        if (exeStack.isEmpty()) {
            throw new ExecutionStackException("Program state stack is empty");
        }

        IStatement currentStatement = exeStack.pop();
        return currentStatement.execute(this);
    }

    public ISymbolTable getSymTable() {
        return symTable;
    }

    public IExecutionStack getExeStack() {
        return exeStack;
    }

    public IOutput getOutput() {
        return output;
    }

    public IFileTable getFileTable() {
        return fileTable;
    }

    public IHeap getHeap() {
        return heap;
    }

    public ILatchTable getLatchTable() {
        return latchTable;
    }

    public IBarrierTable getBarrierTable() {
        return barrierTable;
    }

    @Override
    public String toString() {
        return "ProgramState{" +
                "id=" + id +
                ", exeStack=" + exeStack +
                ", symTable=" + symTable +
                ", output=" + output +
                ", fileTable=" + fileTable +
                ", heap=" + heap +
                ", latchTable=" + latchTable +
                ", barrierTable=" + barrierTable +
                '}';
    }
}
