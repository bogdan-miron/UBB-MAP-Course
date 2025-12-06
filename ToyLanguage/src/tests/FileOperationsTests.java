package tests;

import controller.Controller;
import model.expression.ValueExpression;
import model.expression.VariableExpression;
import model.statement.*;
import model.type.IntType;
import model.type.StringType;
import model.value.IntValue;
import model.value.IValue;
import model.value.StringValue;
import repository.InMemoryRepository;

public class FileOperationsTests {
    public static void main(String[] args) {
        createTestFile("testFileOperations.txt");

        // Test Program:
        // string filename;
        // filename = "testFileOperations.txt";
        // openRFile(filename);
        // int x;
        // readFile(filename, x); print(x);
        // readFile(filename, x); print(x);
        // readFile(filename, x); print(x);
        // closeRFile(filename);

        IStatement prog = new CompoundStatement(
                new DeclarationStatement("filename", new StringType()),
                new CompoundStatement(
                        new AssignmentStatement("filename", new ValueExpression(new StringValue("testFileOperations.txt"))),
                        new CompoundStatement(
                                new OpenRFileStatement(new VariableExpression("filename")),
                                new CompoundStatement(
                                        new DeclarationStatement("x", new IntType()),
                                        new CompoundStatement(
                                                new ReadFileStatement(new VariableExpression("filename"), "x"),
                                                new CompoundStatement(
                                                        new PrintStatement(new VariableExpression("x")),
                                                        new CompoundStatement(
                                                                new ReadFileStatement(new VariableExpression("filename"), "x"),
                                                                new CompoundStatement(
                                                                        new PrintStatement(new VariableExpression("x")),
                                                                        new CompoundStatement(
                                                                                new ReadFileStatement(new VariableExpression("filename"), "x"),
                                                                                new CompoundStatement(
                                                                                        new PrintStatement(new VariableExpression("x")),
                                                                                        new CloseRFileStatement(new VariableExpression("filename"))
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

        System.out.println("=== Test: File Operations ===");
        runProgram(prog, "file_ops_log.txt", true);

        // Test Program 2: Reading past EOF
        IStatement prog2 = new CompoundStatement(
                new DeclarationStatement("file", new StringType()),
                new CompoundStatement(
                        new AssignmentStatement("file", new ValueExpression(new StringValue("testFileOperations.txt"))),
                        new CompoundStatement(
                                new OpenRFileStatement(new VariableExpression("file")),
                                new CompoundStatement(
                                        new DeclarationStatement("val", new IntType()),
                                        new CompoundStatement(
                                                new ReadFileStatement(new VariableExpression("file"), "val"),
                                                new CompoundStatement(
                                                        new ReadFileStatement(new VariableExpression("file"), "val"),
                                                        new CompoundStatement(
                                                                new ReadFileStatement(new VariableExpression("file"), "val"),
                                                                new CompoundStatement(
                                                                        // Read past EOF - should get 0
                                                                        new ReadFileStatement(new VariableExpression("file"), "val"),
                                                                        new CompoundStatement(
                                                                                new PrintStatement(new VariableExpression("val")),
                                                                                new CloseRFileStatement(new VariableExpression("file"))
                                                                        )
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                )
        );

        System.out.println("\n=== Test: Reading Past EOF ===");
        runProgram(prog2, "eof_test_log.txt", true);

        // Test Program 3: Error - file not opened
        IStatement prog3 = new CompoundStatement(
                new DeclarationStatement("x", new IntType()),
                new ReadFileStatement(new ValueExpression(new StringValue("nonexistent.txt")), "x")
        );

        System.out.println("\n=== Test: Error - File Not Opened ===");
        runProgram(prog3, "error_log.txt", true);
    }

    private static void createTestFile(String filename) {
        try (java.io.PrintWriter writer = new java.io.PrintWriter(new java.io.FileWriter(filename))) {
            writer.println("1");
            writer.println("2");
            writer.println("3");
            System.out.println("Created test file: " + filename);
        } catch (java.io.IOException e) {
            System.err.println("Error creating test file: " + e.getMessage());
        }
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
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}