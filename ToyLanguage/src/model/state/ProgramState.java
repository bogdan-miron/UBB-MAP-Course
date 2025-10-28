package model.state;

public class ProgramState {
    private final IExecutionStack exeStack;
    private final ISymbolTable symTable;
    private final IOutput output = new Output();

    public ProgramState(IExecutionStack exeStack, ISymbolTable symTable){
        this.exeStack = exeStack;
        this.symTable = symTable;
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
}
