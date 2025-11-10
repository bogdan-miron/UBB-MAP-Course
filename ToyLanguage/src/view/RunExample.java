package view;

import controller.Controller;
import model.exception.ExecutionStackException;
import model.exception.RepositoryException;
import model.exception.TypeException;

public class RunExample extends Command {
    private Controller controller;

    public RunExample(String key, String desc, Controller controller) {
        super(key, desc);
        this.controller = controller;
    }

    @Override
    public void execute() {
        try {

            controller.allSteps();
            System.out.println("\nProgram executed successfully!");
            System.out.println("Output:");
            controller.getProgramState().getOutput().getOutput().forEach(System.out::println);
            System.out.println();
        } catch (ExecutionStackException | TypeException | RepositoryException e) {
            System.err.println("Execution error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
