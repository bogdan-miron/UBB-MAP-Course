package view;

import controller.Controller;
import model.exception.ExecutionStackException;
import model.exception.TypeException;
import model.expression.ArithmeticExpression;
import model.expression.RelationalExpression;
import model.expression.ValueExpression;
import model.expression.VariableExpression;
import model.state.ProgramState;
import model.statement.*;
import model.type.BoolType;
import model.type.IntType;
import model.value.BooleanValue;
import model.value.IValue;
import model.value.IntValue;
import repository.IRepository;
import repository.InMemoryRepository;

public class Main {
    public static void main(String[] args) {
        // Test Program 1: int x; x = 5; int y; y = x + 3; print(y)
        IStatement prog1 = new CompoundStatement(
                new DeclarationStatement("x", new IntType()),
                new CompoundStatement(
                        new AssignmentStatement("x", new ValueExpression(new IntValue(5))),
                        new CompoundStatement(
                                new DeclarationStatement("y", new IntType()),
                                new CompoundStatement(
                                        new AssignmentStatement("y", new ArithmeticExpression(
                                                new VariableExpression("x"),
                                                new ValueExpression(new IntValue(3)),
                                                "+"
                                        )),
                                        new PrintStatement(new VariableExpression("y"))
                                )
                        )
                )
        );

        System.out.println("=== Test 1 ===");
        runProgram(prog1);

        // Test Program 2: int x; x = 10; if (x > 5) then print(100) else print(0)
        IStatement prog2 = new CompoundStatement(
                new DeclarationStatement("x", new IntType()),
                new CompoundStatement(
                        new AssignmentStatement("x", new ValueExpression(new IntValue(10))),
                        new IfStatement(
                                new RelationalExpression(
                                        new VariableExpression("x"),
                                        new ValueExpression(new IntValue(5)),
                                        ">"
                                ),
                                new PrintStatement(new ValueExpression(new IntValue(100))),
                                new PrintStatement(new ValueExpression(new IntValue(0)))
                        )
                )
        );

        System.out.println("\n=== Test 2 ===");
        runProgram(prog2);

        // Test Program 3: Error case - assignment without declaration
        IStatement prog3 = new AssignmentStatement("z", new ValueExpression(new IntValue(42)));

        System.out.println("\n=== Test 3 ===");
        runProgram(prog3);

        // Test Program 4: Error case - type mismatch
        IStatement prog4 = new CompoundStatement(
                new DeclarationStatement("a", new IntType()),
                new AssignmentStatement("a", new ValueExpression(new BooleanValue(true)))
        );

        System.out.println("\n=== Test 4 ===");
        runProgram(prog4);

        // Test Program 5: Boolean type declaration
        IStatement prog5 = new CompoundStatement(
                new DeclarationStatement("flag", new BoolType()),
                new CompoundStatement(
                        new AssignmentStatement("flag", new ValueExpression(new BooleanValue(true))),
                        new PrintStatement(new VariableExpression("flag"))
                )
        );

        System.out.println("\n=== Test 5 ===");
        runProgram(prog5);
    }

    private static void runProgram(IStatement program) {
        IRepository repo = new InMemoryRepository();
        Controller controller = new Controller(program, repo);

        try {
            controller.allSteps();

            System.out.println("\nFinal Output:\n");
            for (IValue value: controller.getProgramState().getOutput().getOutput()){
                System.out.println(value);
            }

            /*
            for (ProgramState state : repo.getAllStates()){
                ; // check all program states
            }
             */

        }
        catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }
}

