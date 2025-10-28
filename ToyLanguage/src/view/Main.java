package view;

import controller.Controller;
import model.exception.ExecutionStackException;
import model.exception.TypeException;
import model.expression.ArithmeticExpression;
import model.expression.RelationalExpression;
import model.expression.ValueExpression;
import model.expression.VariableExpression;
import model.statement.*;
import model.value.BooleanValue;
import model.value.IValue;
import model.value.IntValue;

public class Main {
    public static void main(String[] args) {
        // Test Program 1: x = 5; y = x + 3; print(y)

        IStatement prog1 = new CompoundStatement(
                new AssignmentStatement("x", new ValueExpression(new IntValue(5))),
                new CompoundStatement(
                        new AssignmentStatement("y", new ArithmeticExpression(
                                new VariableExpression("x"),
                                new ValueExpression(new IntValue(3)),
                                "+"
                        )),
                        new PrintStatement(new VariableExpression("y"))
                )
        );

        System.out.println("=== Test 1: Simple Arithmetic ===");
        runProgram(prog1);

        // Test Program 2: x = 10; if (x > 5) then print(100) else print(0)
        IStatement prog2 = new CompoundStatement(
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
        );

        System.out.println("\n=== Test 2: If Statement ===");
        runProgram(prog2);
    }

    private static void runProgram(IStatement program) {
        Controller controller = new Controller(program);

        try {
            controller.allSteps();

            System.out.println("\nFinal Output:\n");
            for (IValue value: controller.getProgramState().getOutput().getOutput()){
                System.out.println(value);
            }
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }
}

