package model.state;

public class ProgramState {
    private final IExecutionStack exeStack;
    private final ISymbolTable symTable;
    private final IOutput output;
    private final IFileTable fileTable;
    private final IHeap heap;

    public ProgramState(IExecutionStack exeStack, ISymbolTable symTable, IFileTable fileTable, IHeap heap, IOutput output) {
        this.exeStack = exeStack;
        this.symTable = symTable;
        this.fileTable = fileTable;
        this.heap = heap;
        this.output = output;
    }

    public ProgramState(IExecutionStack exeStack, ISymbolTable symTable, IFileTable fileTable, IHeap heap) {
        this(exeStack, symTable, fileTable, heap, new Output());
    }

    public ProgramState(IExecutionStack exeStack, ISymbolTable symTable) {
        this(exeStack, symTable, new FileTable(), new Heap(), new Output());
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
}
