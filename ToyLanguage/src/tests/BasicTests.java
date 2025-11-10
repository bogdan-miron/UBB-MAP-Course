package tests;

import controller.Controller;
import model.expression.*;
import model.statement.*;
import model.type.BoolType;
import model.type.IntType;
import model.type.StringType;
import model.value.BooleanValue;
import model.value.IValue;
import model.value.IntValue;
import model.value.StringValue;
import repository.InMemoryRepository;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class BasicTests {

    public static void main(String[] args) {

        // Create test files
        createTestFiles();

        // Run all tests
        testSimpleArithmetic();
        testIfStatement();
        testComplexArithmetic();
        testBooleanOperations();
        testNestedIfStatements();
        testMultipleVariables();
        testStringDeclarations();
        testFileOperationsBasic();
        testFileOperationsMultipleReads();
        testFileOperationsReadPastEOF();
        testFileOperationsMultipleFiles();
        testComplexProgramWithFiles();

        // Error tests
        testErrorUndeclaredVariable();
        testErrorTypeMismatch();
        testErrorRedeclaration();
        testErrorFileNotOpened();
        testErrorFileAlreadyOpened();
        testErrorReadFromNonIntVariable();
        testErrorInvalidFileContent();

        System.out.println("\n===============================================");
        System.out.println("           ALL TESTS COMPLETED");
        System.out.println("===============================================");
    }

    // ==================== BASIC TESTS ====================

    private static void testSimpleArithmetic() {
        System.out.println("\n>>> Test 1: Simple Arithmetic");
        System.out.println("Program: int x; x = 5; int y; y = x + 3; print(y)");

        IStatement prog = new CompoundStatement(
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

        runProgram(prog, false);
        System.out.println("Expected output: 8\n");
    }

    private static void testIfStatement() {
        System.out.println("\n>>> Test 2: If Statement");
        System.out.println("Program: int x; x = 10; if (x > 5) then print(100) else print(0)");

        IStatement prog = new CompoundStatement(
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

        runProgram(prog, false);
        System.out.println("Expected output: 100\n");
    }

    private static void testComplexArithmetic() {
        System.out.println("\n>>> Test 3: More Arithmetic");
        System.out.println("Program: int a; a = 10; int b; b = 5; int c; c = (a - b) * 2; print(c)");

        IStatement prog = new CompoundStatement(
                new DeclarationStatement("a", new IntType()),
                new CompoundStatement(
                        new AssignmentStatement("a", new ValueExpression(new IntValue(10))),
                        new CompoundStatement(
                                new DeclarationStatement("b", new IntType()),
                                new CompoundStatement(
                                        new AssignmentStatement("b", new ValueExpression(new IntValue(5))),
                                        new CompoundStatement(
                                                new DeclarationStatement("c", new IntType()),
                                                new CompoundStatement(
                                                        new AssignmentStatement("c", new ArithmeticExpression(
                                                                new ArithmeticExpression(
                                                                        new VariableExpression("a"),
                                                                        new VariableExpression("b"),
                                                                        "-"
                                                                ),
                                                                new ValueExpression(new IntValue(2)),
                                                                "*"
                                                        )),
                                                        new PrintStatement(new VariableExpression("c"))
                                                )
                                        )
                                )
                        )
                )
        );

        runProgram(prog, false);
        System.out.println("Expected output: 10\n");
    }

    private static void testBooleanOperations() {
        System.out.println("\n>>> Test 4: Boolean Operations");
        System.out.println("Program: bool flag; flag = true; if (flag) then print(1) else print(0)");

        IStatement prog = new CompoundStatement(
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

        runProgram(prog, false);
        System.out.println("Expected output: 1\n");
    }

    private static void testNestedIfStatements() {
        System.out.println("\n>>> Test 5: Nested If Statements");
        System.out.println("Program: int x; x = 15; if (x > 10) then (if (x > 20) then print(2) else print(1)) else print(0)");

        IStatement prog = new CompoundStatement(
                new DeclarationStatement("x", new IntType()),
                new CompoundStatement(
                        new AssignmentStatement("x", new ValueExpression(new IntValue(15))),
                        new IfStatement(
                                new RelationalExpression(
                                        new VariableExpression("x"),
                                        new ValueExpression(new IntValue(10)),
                                        ">"
                                ),
                                new IfStatement(
                                        new RelationalExpression(
                                                new VariableExpression("x"),
                                                new ValueExpression(new IntValue(20)),
                                                ">"
                                        ),
                                        new PrintStatement(new ValueExpression(new IntValue(2))),
                                        new PrintStatement(new ValueExpression(new IntValue(1)))
                                ),
                                new PrintStatement(new ValueExpression(new IntValue(0)))
                        )
                )
        );

        runProgram(prog, false);
        System.out.println("Expected output: 1\n");
    }

    private static void testMultipleVariables() {
        System.out.println("\n>>> Test 6: Multiple Variables");
        System.out.println("Program: Multiple declarations and operations");

        IStatement prog = new CompoundStatement(
                new DeclarationStatement("a", new IntType()),
                new CompoundStatement(
                        new DeclarationStatement("b", new IntType()),
                        new CompoundStatement(
                                new DeclarationStatement("c", new IntType()),
                                new CompoundStatement(
                                        new AssignmentStatement("a", new ValueExpression(new IntValue(10))),
                                        new CompoundStatement(
                                                new AssignmentStatement("b", new ValueExpression(new IntValue(20))),
                                                new CompoundStatement(
                                                        new AssignmentStatement("c", new ValueExpression(new IntValue(30))),
                                                        new CompoundStatement(
                                                                new PrintStatement(new VariableExpression("a")),
                                                                new CompoundStatement(
                                                                        new PrintStatement(new VariableExpression("b")),
                                                                        new PrintStatement(new VariableExpression("c"))
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                )
        );

        runProgram(prog, false);
        System.out.println("Expected output: 10, 20, 30\n");
    }

    private static void testStringDeclarations() {
        System.out.println("\n>>> Test 7: String Declarations");
        System.out.println("Program: string name; name = \"Alice\"; print(name)");

        IStatement prog = new CompoundStatement(
                new DeclarationStatement("name", new StringType()),
                new CompoundStatement(
                        new AssignmentStatement("name", new ValueExpression(new StringValue("Alice"))),
                        new PrintStatement(new VariableExpression("name"))
                )
        );

        runProgram(prog, false);
        System.out.println("Expected output: Alice\n");
    }

    // ==================== FILE OPERATION TESTS ====================

    private static void testFileOperationsBasic() {
        System.out.println("\n>>> Test 8: Basic File Operations");
        System.out.println("Program: Open file, read three integers, close file");

        IStatement prog = new CompoundStatement(
                new DeclarationStatement("filename", new StringType()),
                new CompoundStatement(
                        new AssignmentStatement("filename", new ValueExpression(new StringValue("test1.txt"))),
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

        runProgram(prog, false);
        System.out.println("Expected output: 1, 2, 3\n");
    }

    private static void testFileOperationsMultipleReads() {
        System.out.println("\n>>> Test 9: Multiple Reads with Accumulation");
        System.out.println("Program: Read and accumulate values from file");

        IStatement prog = new CompoundStatement(
                new DeclarationStatement("file", new StringType()),
                new CompoundStatement(
                        new AssignmentStatement("file", new ValueExpression(new StringValue("test1.txt"))),
                        new CompoundStatement(
                                new OpenRFileStatement(new VariableExpression("file")),
                                new CompoundStatement(
                                        new DeclarationStatement("sum", new IntType()),
                                        new CompoundStatement(
                                                new DeclarationStatement("val", new IntType()),
                                                new CompoundStatement(
                                                        new ReadFileStatement(new VariableExpression("file"), "val"),
                                                        new CompoundStatement(
                                                                new AssignmentStatement("sum", new ArithmeticExpression(
                                                                        new VariableExpression("sum"),
                                                                        new VariableExpression("val"),
                                                                        "+"
                                                                )),
                                                                new CompoundStatement(
                                                                        new ReadFileStatement(new VariableExpression("file"), "val"),
                                                                        new CompoundStatement(
                                                                                new AssignmentStatement("sum", new ArithmeticExpression(
                                                                                        new VariableExpression("sum"),
                                                                                        new VariableExpression("val"),
                                                                                        "+"
                                                                                )),
                                                                                new CompoundStatement(
                                                                                        new ReadFileStatement(new VariableExpression("file"), "val"),
                                                                                        new CompoundStatement(
                                                                                                new AssignmentStatement("sum", new ArithmeticExpression(
                                                                                                        new VariableExpression("sum"),
                                                                                                        new VariableExpression("val"),
                                                                                                        "+"
                                                                                                )),
                                                                                                new CompoundStatement(
                                                                                                        new PrintStatement(new VariableExpression("sum")),
                                                                                                        new CloseRFileStatement(new VariableExpression("file"))
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
                )
        );

        runProgram(prog, false);
        System.out.println("Expected output: 6 (1+2+3)\n");
    }

    private static void testFileOperationsReadPastEOF() {
        System.out.println("\n>>> Test 10: Reading Past EOF");
        System.out.println("Program: Read 4 values from file with 3 integers (4th should be 0)");

        IStatement prog = new CompoundStatement(
                new DeclarationStatement("file", new StringType()),
                new CompoundStatement(
                        new AssignmentStatement("file", new ValueExpression(new StringValue("test1.txt"))),
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

        runProgram(prog, false);
        System.out.println("Expected output: 0 (EOF reached)\n");
    }

    private static void testFileOperationsMultipleFiles() {
        System.out.println("\n>>> Test 11: Multiple Files");
        System.out.println("Program: Open and read from two different files");

        IStatement prog = new CompoundStatement(
                new DeclarationStatement("file1", new StringType()),
                new CompoundStatement(
                        new DeclarationStatement("file2", new StringType()),
                        new CompoundStatement(
                                new AssignmentStatement("file1", new ValueExpression(new StringValue("test1.txt"))),
                                new CompoundStatement(
                                        new AssignmentStatement("file2", new ValueExpression(new StringValue("test2.txt"))),
                                        new CompoundStatement(
                                                new OpenRFileStatement(new VariableExpression("file1")),
                                                new CompoundStatement(
                                                        new OpenRFileStatement(new VariableExpression("file2")),
                                                        new CompoundStatement(
                                                                new DeclarationStatement("x", new IntType()),
                                                                new CompoundStatement(
                                                                        new DeclarationStatement("y", new IntType()),
                                                                        new CompoundStatement(
                                                                                new ReadFileStatement(new VariableExpression("file1"), "x"),
                                                                                new CompoundStatement(
                                                                                        new ReadFileStatement(new VariableExpression("file2"), "y"),
                                                                                        new CompoundStatement(
                                                                                                new PrintStatement(new VariableExpression("x")),
                                                                                                new CompoundStatement(
                                                                                                        new PrintStatement(new VariableExpression("y")),
                                                                                                        new CompoundStatement(
                                                                                                                new CloseRFileStatement(new VariableExpression("file1")),
                                                                                                                new CloseRFileStatement(new VariableExpression("file2"))
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
                        )
                )
        );

        runProgram(prog, false);
        System.out.println("Expected output: 1 (from test1.txt), 10 (from test2.txt)\n");
    }

    private static void testComplexProgramWithFiles() {
        System.out.println("\n>>> Test 12: Complex Program with Files and Conditionals");
        System.out.println("Program: Read value, check if > 5, print result");

        IStatement prog = new CompoundStatement(
                new DeclarationStatement("filename", new StringType()),
                new CompoundStatement(
                        new AssignmentStatement("filename", new ValueExpression(new StringValue("test1.txt"))),
                        new CompoundStatement(
                                new OpenRFileStatement(new VariableExpression("filename")),
                                new CompoundStatement(
                                        new DeclarationStatement("value", new IntType()),
                                        new CompoundStatement(
                                                new ReadFileStatement(new VariableExpression("filename"), "value"),
                                                new CompoundStatement(
                                                        new ReadFileStatement(new VariableExpression("filename"), "value"),
                                                        new CompoundStatement(
                                                                new IfStatement(
                                                                        new RelationalExpression(
                                                                                new VariableExpression("value"),
                                                                                new ValueExpression(new IntValue(5)),
                                                                                ">"
                                                                        ),
                                                                        new PrintStatement(new ValueExpression(new IntValue(999))),
                                                                        new PrintStatement(new ValueExpression(new IntValue(0)))
                                                                ),
                                                                new CloseRFileStatement(new VariableExpression("filename"))
                                                        )
                                                )
                                        )
                                )
                        )
                )
        );

        runProgram(prog, false);
        System.out.println("Expected output: 0 (because 2 is not > 5)\n");
    }

    // ==================== ERROR TESTS ====================

    private static void testErrorUndeclaredVariable() {
        System.out.println("\n>>> Test E1: Error - Undeclared Variable");
        System.out.println("Program: x = 5 (without declaration)");

        IStatement prog = new AssignmentStatement("x", new ValueExpression(new IntValue(5)));

        runProgram(prog, false);
        System.out.println("Expected: Error - Variable x is not declared\n");
    }

    private static void testErrorTypeMismatch() {
        System.out.println("\n>>> Test E2: Error - Type Mismatch");
        System.out.println("Program: int x; x = true");

        IStatement prog = new CompoundStatement(
                new DeclarationStatement("x", new IntType()),
                new AssignmentStatement("x", new ValueExpression(new BooleanValue(true)))
        );

        runProgram(prog, false);
        System.out.println("Expected: Error - Type mismatch\n");
    }

    private static void testErrorRedeclaration() {
        System.out.println("\n>>> Test E3: Error - Variable Redeclaration");
        System.out.println("Program: int x; int x;");

        IStatement prog = new CompoundStatement(
                new DeclarationStatement("x", new IntType()),
                new DeclarationStatement("x", new IntType())
        );

        runProgram(prog, false);
        System.out.println("Expected: Error - Variable x is already declared\n");
    }

    private static void testErrorFileNotOpened() {
        System.out.println("\n>>> Test E4: Error - File Not Opened");
        System.out.println("Program: readFile without openRFile");

        IStatement prog = new CompoundStatement(
                new DeclarationStatement("x", new IntType()),
                new ReadFileStatement(new ValueExpression(new StringValue("test1.txt")), "x")
        );

        runProgram(prog, false);
        System.out.println("Expected: Error - File test1.txt is not opened\n");
    }

    private static void testErrorFileAlreadyOpened() {
        System.out.println("\n>>> Test E5: Error - File Already Opened");
        System.out.println("Program: openRFile twice on same file");

        IStatement prog = new CompoundStatement(
                new DeclarationStatement("file", new StringType()),
                new CompoundStatement(
                        new AssignmentStatement("file", new ValueExpression(new StringValue("test1.txt"))),
                        new CompoundStatement(
                                new OpenRFileStatement(new VariableExpression("file")),
                                new OpenRFileStatement(new VariableExpression("file"))
                        )
                )
        );

        runProgram(prog, false);
        System.out.println("Expected: Error - File test1.txt is already opened\n");
    }

    private static void testErrorReadFromNonIntVariable() {
        System.out.println("\n>>> Test E6: Error - Read Into Non-Int Variable");
        System.out.println("Program: readFile into string variable");

        IStatement prog = new CompoundStatement(
                new DeclarationStatement("file", new StringType()),
                new CompoundStatement(
                        new AssignmentStatement("file", new ValueExpression(new StringValue("test1.txt"))),
                        new CompoundStatement(
                                new OpenRFileStatement(new VariableExpression("file")),
                                new CompoundStatement(
                                        new DeclarationStatement("x", new StringType()),
                                        new ReadFileStatement(new VariableExpression("file"), "x")
                                )
                        )
                )
        );

        runProgram(prog, false);
        System.out.println("Expected: Error - Variable x is not of type int\n");
    }

    private static void testErrorInvalidFileContent() {
        System.out.println("\n>>> Test E7: Error - Invalid File Content");
        System.out.println("Program: Read from file with non-integer content");

        IStatement prog = new CompoundStatement(
                new DeclarationStatement("file", new StringType()),
                new CompoundStatement(
                        new AssignmentStatement("file", new ValueExpression(new StringValue("test_invalid.txt"))),
                        new CompoundStatement(
                                new OpenRFileStatement(new VariableExpression("file")),
                                new CompoundStatement(
                                        new DeclarationStatement("x", new IntType()),
                                        new ReadFileStatement(new VariableExpression("file"), "x")
                                )
                        )
                )
        );

        runProgram(prog, false);
        System.out.println("Expected: Error - Invalid integer format\n");
    }

    // ==================== HELPER METHODS ====================

    private static void runProgram(IStatement program, boolean enableLogging) {
        InMemoryRepository repo = new InMemoryRepository("test_log.txt");

        try {
            if (enableLogging) {
                repo.clearLogFile();
            }

            Controller controller = new Controller(program, repo, enableLogging);
            controller.allSteps();

            System.out.println("Output:");
            for (IValue value : controller.getProgramState().getOutput().getOutput()) {
                System.out.println("  " + value);
            }
        } catch (Exception e) {
            System.err.println("  ERROR: " + e.getMessage());
        }
    }

    private static void createTestFiles() {
        System.out.println("Creating test files...");

        createFile("test1.txt", new int[]{1, 2, 3});

        createFile("test2.txt", new int[]{10, 20, 30});

        // contains non-integer
        try (PrintWriter writer = new PrintWriter(new FileWriter("test_invalid.txt"))) {
            writer.println("not_a_number");
        } catch (IOException e) {
            System.err.println("Error creating test_invalid.txt: " + e.getMessage());
        }

        System.out.println("Test files created.\n");
    }

    private static void createFile(String filename, int[] values) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            for (int value : values) {
                writer.println(value);
            }
        } catch (IOException e) {
            System.err.println("Error creating " + filename + ": " + e.getMessage());
        }
    }
}