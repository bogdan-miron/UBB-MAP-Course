package view;

import controller.Controller;
import model.exception.RepositoryException;
import model.state.ProgramState;

import java.util.List;

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

            // Get the program states from the repository
            List<ProgramState> prgList = controller.getRepo().getPrgList();

            // All threads share the same output, so we can get it from any program state, or the first one if the list is not empty
            if (!prgList.isEmpty()) {
                prgList.get(0).getOutput().getOutput().forEach(System.out::println);
            } else {
                // If the list is empty, we need to check if there was an initial state
                System.out.println("No output to display.");
            }

            System.out.println();
        } catch (InterruptedException e) {
            System.err.println("Execution was interrupted: " + e.getMessage());
        } catch (RepositoryException e) {
            System.err.println("Repository error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
