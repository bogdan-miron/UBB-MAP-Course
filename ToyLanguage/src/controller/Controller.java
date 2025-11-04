package controller;

import model.exception.ExecutionStackException;
import model.exception.TypeException;
import model.state.ExecutionStack;
import model.state.IExecutionStack;
import model.state.ProgramState;
import model.state.SymbolTable;
import model.statement.IStatement;
import repository.IRepository;

public class Controller {
    private ProgramState state;
    private IRepository repo;

    public Controller(IStatement program, IRepository repo) { // the program will be a compound statement
        IExecutionStack exeStack = new ExecutionStack();
        exeStack.push(program);
        this.state = new ProgramState(exeStack, new SymbolTable());
        this.repo = repo;
    }

    public void oneStep() throws ExecutionStackException, TypeException {
        IExecutionStack stack = state.getExeStack();

        if (((ExecutionStack)stack).isEmpty()) {
            throw new RuntimeException("Program has finished execution");
        }

        IStatement statement = stack.pop();
        state = statement.execute(state);
        repo.addState(state);
    }

    public void allSteps() throws ExecutionStackException, TypeException {
        IExecutionStack stack = state.getExeStack();
        while(!stack.isEmpty()){
            oneStep();
            // System.out.println(state.getSymTable().toString());
        }
    }

    public ProgramState getProgramState() {
        return state;
    }

    public IRepository getRepo() {
        return repo;
    }
}
