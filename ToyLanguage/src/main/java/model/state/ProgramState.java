package model.state;

import model.exception.ExecutionStackException;
import model.exception.TypeException;
import model.statement.IStatement;

import java.util.Stack;

public class ProgramState {
    private final IExecutionStack exeStack;
    private final Stack<ISymbolTable> symTableStack;
    private final IOutput output;
    private final IFileTable fileTable;
    private final IHeap heap;
    private final ILatchTable latchTable;
    private final IBarrierTable barrierTable;
    private final ILockTable lockTable;
    private final ISemaphoreTable semaphoreTable;
    private final IProcTable procTable;
    private final int id;

    private static int nextId = 1;

    public ProgramState(IExecutionStack exeStack, Stack<ISymbolTable> symTableStack, IFileTable fileTable, IHeap heap, ILatchTable latchTable, IBarrierTable barrierTable, ILockTable lockTable, ISemaphoreTable semaphoreTable, IProcTable procTable, IOutput output) {
        this.exeStack = exeStack;
        this.symTableStack = symTableStack;
        this.fileTable = fileTable;
        this.heap = heap;
        this.latchTable = latchTable;
        this.barrierTable = barrierTable;
        this.lockTable = lockTable;
        this.semaphoreTable = semaphoreTable;
        this.procTable = procTable;
        this.output = output;
        this.id = getNextId();
    }

    public ProgramState(IExecutionStack exeStack, ISymbolTable symTable, IFileTable fileTable, IHeap heap, ILatchTable latchTable, IBarrierTable barrierTable, ILockTable lockTable, ISemaphoreTable semaphoreTable, IOutput output) {
        this.exeStack = exeStack;
        this.symTableStack = new Stack<>();
        this.symTableStack.push(symTable);
        this.fileTable = fileTable;
        this.heap = heap;
        this.latchTable = latchTable;
        this.barrierTable = barrierTable;
        this.lockTable = lockTable;
        this.semaphoreTable = semaphoreTable;
        this.procTable = new ProcTable();
        this.output = output;
        this.id = getNextId();
    }

    public ProgramState(IExecutionStack exeStack, ISymbolTable symTable, IFileTable fileTable, IHeap heap) {
        this(exeStack, symTable, fileTable, heap, new LatchTable(), new BarrierTable(), new LockTable(), new SemaphoreTable(), new Output());
    }

    public ProgramState(IExecutionStack exeStack, ISymbolTable symTable) {
        this(exeStack, symTable, new FileTable(), new Heap(), new LatchTable(), new BarrierTable(), new LockTable(), new SemaphoreTable(), new Output());
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
        return symTableStack.peek();
    }

    public Stack<ISymbolTable> getSymTableStack() {
        return symTableStack;
    }

    public void pushSymTable(ISymbolTable symTable) {
        symTableStack.push(symTable);
    }

    public ISymbolTable popSymTable() {
        return symTableStack.pop();
    }

    public IProcTable getProcTable() {
        return procTable;
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

    public ILockTable getLockTable() {
        return lockTable;
    }

    public ISemaphoreTable getSemaphoreTable() {
        return semaphoreTable;
    }

    @Override
    public String toString() {
        return "ProgramState{" +
                "id=" + id +
                ", exeStack=" + exeStack +
                ", symTableStack=" + symTableStack +
                ", output=" + output +
                ", fileTable=" + fileTable +
                ", heap=" + heap +
                ", latchTable=" + latchTable +
                ", barrierTable=" + barrierTable +
                ", lockTable=" + lockTable +
                ", semaphoreTable=" + semaphoreTable +
                ", procTable=" + procTable +
                '}';
    }
}
