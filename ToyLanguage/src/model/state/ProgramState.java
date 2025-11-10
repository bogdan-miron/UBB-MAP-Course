package model.state;

public class ProgramState {
    private final IExecutionStack exeStack;
    private final ISymbolTable symTable;
    private final IOutput output;
    private final IFileTable fileTable;

    public ProgramState(IExecutionStack exeStack, ISymbolTable symTable, IFileTable fileTable, IOutput output) {
        this.exeStack = exeStack;
        this.symTable = symTable;
        this.fileTable = fileTable;
        this.output = output;
    }

    public ProgramState(IExecutionStack exeStack, ISymbolTable symTable, IFileTable fileTable) {
        this(exeStack, symTable, fileTable, new Output());
    }

    public ProgramState(IExecutionStack exeStack, ISymbolTable symTable) {
        this(exeStack, symTable, new FileTable(), new Output());
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
}
