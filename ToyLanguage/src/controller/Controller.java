package controller;

import model.exception.ExecutionStackException;
import model.exception.TypeException;
import model.state.ExecutionStack;
import model.state.IExecutionStack;
import model.state.ProgramState;
import model.state.SymbolTable;
import model.statement.IStatement;

public class Controller {
    private ProgramState state;

    public Controller(IStatement program) { // the program will be a compound statement
        IExecutionStack exeStack = new ExecutionStack();
        exeStack.push(program);
        this.state = new ProgramState(exeStack, new SymbolTable());
    }

    public void oneStep() throws ExecutionStackException, TypeException {
        IExecutionStack stack = state.getExeStack();

        if (((ExecutionStack)stack).isEmpty()) {
            throw new RuntimeException("Program has finished execution");
        }

        IStatement statement = stack.pop();
        state = statement.execute(state);
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
}
