package tests;

import controller.Controller;
import model.expression.*;
import model.gc.GarbageCollector;
import model.statement.*;
import model.type.IntType;
import model.type.RefType;
import model.value.IntValue;
import model.value.IValue;
import repository.InMemoryRepository;

public class GarbageCollectorTest {
    public static void main(String[] args) {

        // Ref int v; new(v,20); Ref Ref int a; new(a,v); new(v,30); print(rH(rH(a)))
        IStatement prog = new CompoundStatement(
                new DeclarationStatement("v", new RefType(new IntType())),
                new CompoundStatement(
                        new NewStatement("v", new ValueExpression(new IntValue(20))),
                        new CompoundStatement(
                                new DeclarationStatement("a", new RefType(new RefType(new IntType()))),
                                new CompoundStatement(
                                        new NewStatement("a", new VariableExpression("v")),
                                        new CompoundStatement(
                                                new NewStatement("v", new ValueExpression(new IntValue(30))),
                                                new PrintStatement(
                                                        new HeapReadExpression(
                                                                new HeapReadExpression(
                                                                        new VariableExpression("a")
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                )
        );

        runProgram(prog, "gc_test_log.txt", true);

        // Ref int a; new(a,10); Ref Ref int b; new(b,a); Ref Ref Ref int c; new(c,b); new(a,20); print(rH(rH(rH(c))))
        IStatement prog2 = new CompoundStatement(
                new DeclarationStatement("a", new RefType(new IntType())),
                new CompoundStatement(
                        new NewStatement("a", new ValueExpression(new IntValue(10))),
                        new CompoundStatement(
                                new DeclarationStatement("b", new RefType(new RefType(new IntType()))),
                                new CompoundStatement(
                                        new NewStatement("b", new VariableExpression("a")),
                                        new CompoundStatement(
                                                new DeclarationStatement("c", new RefType(new RefType(new RefType(new IntType())))),
                                                new CompoundStatement(
                                                        new NewStatement("c", new VariableExpression("b")),
                                                        new CompoundStatement(
                                                                new NewStatement("a", new ValueExpression(new IntValue(20))),
                                                                new PrintStatement(
                                                                        new HeapReadExpression(
                                                                                new HeapReadExpression(
                                                                                        new HeapReadExpression(
                                                                                                new VariableExpression("c")
                                                                                        )
                                                                                )
                                                                        )
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                )
        );

        runProgram(prog2, "gc_test_complex_log.txt", true);
    }

    private static void runProgram(IStatement program, String logFile, boolean enableLogging) {
        InMemoryRepository repo = new InMemoryRepository(logFile);

        try {
            if (enableLogging) {
                repo.clearLogFile();
            }

            Controller controller = new Controller(program, repo, enableLogging);
            controller.allSteps();

            System.out.println("Final Output:");
            // Get program states from repository (all threads share the same output)
            if (!repo.getPrgList().isEmpty()) {
                for (IValue value : repo.getPrgList().get(0).getOutput().getOutput()) {
                    System.out.println("  " + value);
                }
            }

            if (enableLogging) {
                System.out.println("(Execution logged to " + logFile + ")");
            }

        } catch (InterruptedException e) {
            System.err.println("Error: Execution interrupted - " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}