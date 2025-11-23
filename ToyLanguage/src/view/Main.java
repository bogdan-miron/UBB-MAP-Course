package view;

import controller.Controller;
import model.exception.ExecutionStackException;
import model.exception.TypeException;
import model.expression.*;
import model.state.ProgramState;
import model.statement.*;
import model.type.IntType;
import model.type.BoolType;
import model.type.RefType;
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

        System.out.println("=== Test 1: Simple Arithmetic with Logging ===");
        runProgram(prog1, "test1_log.txt", true);

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

        System.out.println("\n=== Test 2: If Statement with Logging ===");
        runProgram(prog2, "test2_log.txt", true);

        // Test Program 3: Complex program with multiple variables
        IStatement prog3 = new CompoundStatement(
                new DeclarationStatement("a", new IntType()),
                new CompoundStatement(
                        new DeclarationStatement("b", new IntType()),
                        new CompoundStatement(
                                new AssignmentStatement("a", new ValueExpression(new IntValue(10))),
                                new CompoundStatement(
                                        new AssignmentStatement("b", new ValueExpression(new IntValue(20))),
                                        new CompoundStatement(
                                                new PrintStatement(new VariableExpression("a")),
                                                new CompoundStatement(
                                                        new PrintStatement(new VariableExpression("b")),
                                                        new PrintStatement(new ArithmeticExpression(
                                                                new VariableExpression("a"),
                                                                new VariableExpression("b"),
                                                                "+"
                                                        ))
                                                )
                                        )
                                )
                        )
                )
        );

        System.out.println("\n=== Test 3: Complex Program with Logging ===");
        runProgram(prog3, "test3_log.txt", true);

        // Test Program 4: Boolean operations
        IStatement prog4 = new CompoundStatement(
                new DeclarationStatement("flag", new BoolType()),
                new CompoundStatement(
                        new AssignmentStatement("flag", new ValueExpression(new BooleanValue(true))),
                        new IfStatement(
                                new VariableExpression("flag"),
                                new PrintStatement(new ValueExpression(new IntValue(1))),
                                new PrintStatement(new ValueExpression(new IntValue(0)))
                        )
                )
        );

        System.out.println("\n=== Test 4: Boolean Operations without Logging ===");
        runProgram(prog4, "test4_log.txt", false);

        // Ref int v; new(v,20); Ref Ref int a; new(a,v); print(v); print(a)
        IStatement prog5 = new CompoundStatement(
                new DeclarationStatement("v", new RefType(new IntType())),
                new CompoundStatement(
                        new NewStatement("v", new ValueExpression(new IntValue(20))),
                        new CompoundStatement(
                                new DeclarationStatement("a", new RefType(new RefType(new IntType()))),
                                new CompoundStatement(
                                        new NewStatement("a", new VariableExpression("v")),
                                        new CompoundStatement(
                                                new PrintStatement(new VariableExpression("v")),
                                                new PrintStatement(new VariableExpression("a"))
                                        )
                                )
                        )
                )
        );

        System.out.println("\n=== Test 5: Heap Example - Basic References ===");
        System.out.println("Program: Ref int v; new(v,20); Ref Ref int a; new(a,v); print(v); print(a)");
        runProgram(prog5, "test5_heap_log.txt", true);

        // Ref int v; new(v,20); print(rH(v)); wH(v,30); print(rH(v)+5);
        IStatement prog6 = new CompoundStatement(
                new DeclarationStatement("v", new RefType(new IntType())),
                new CompoundStatement(
                        new NewStatement("v", new ValueExpression(new IntValue(20))),
                        new CompoundStatement(
                                new PrintStatement(new HeapReadExpression(new VariableExpression("v"))),
                                new CompoundStatement(
                                        new HeapWriteStatement("v", new ValueExpression(new IntValue(30))),
                                        new PrintStatement(
                                                new ArithmeticExpression(
                                                        new HeapReadExpression(new VariableExpression("v")),
                                                        new ValueExpression(new IntValue(5)),
                                                        "+"
                                                )
                                        )
                                )
                        )
                )
        );

        System.out.println("\n=== Test 6: Heap Example - Read and Write Operations ===");
        System.out.println("Program: Ref int v; new(v,20); print(rH(v)); wH(v,30); print(rH(v)+5);");
        runProgram(prog6, "test6_heap_log.txt", true);

    }

    private static void runProgram(IStatement program, String logFile, boolean enableLogging) {
        InMemoryRepository repo = new InMemoryRepository(logFile);

        try {
            // Clear the log file before starting
            if (enableLogging) {
                repo.clearLogFile();
            }

            Controller controller = new Controller(program, repo, enableLogging);
            controller.allSteps();

            System.out.println("Final Output:");
            for (IValue value : controller.getProgramState().getOutput().getOutput()) {
                System.out.println("  " + value);
            }

            if (enableLogging) {
                System.out.println("(Execution logged to " + logFile + ")");
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}