package view;

import controller.Controller;
import model.exception.TypeException;
import model.expression.*;
import model.statement.*;
import model.type.IntType;
import model.type.RefType;
import model.type.StringType;
import model.value.IntValue;
import model.value.RefValue;
import model.value.StringValue;
import repository.IRepository;
import repository.InMemoryRepository;

public class Interpreter {
    public static void main(String[] args) throws TypeException {
        // Example 1: int x; x = 5; int y; y = x + 3; print(y)
        IStatement ex1 = new CompoundStatement(
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

        IRepository repo1 = new InMemoryRepository("log1.txt");
        Controller ctr1 = new Controller(ex1, repo1, true);

        // Example 2: int x; x = 10; if (x > 5) then print(100) else print(0)
        IStatement ex2 = new CompoundStatement(
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

        IRepository repo2 = new InMemoryRepository("log2.txt");
        Controller ctr2 = new Controller(ex2, repo2, true);

        // Example 3: File operations
        IStatement ex3 = new CompoundStatement(
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
                                                                        new CloseRFileStatement(new VariableExpression("filename"))
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                )
        );

        IRepository repo3 = new InMemoryRepository("log3.txt");
        Controller ctr3 = new Controller(ex3, repo3, true);

        // Example 4: Fork statement demonstration
        // int v; Ref int a; v=10; new(a,22);
        // fork(wH(a,30); v=32; print(v); print(rH(a)));
        // print(v); print(rH(a))
        IStatement ex4 = new CompoundStatement(
                new DeclarationStatement("v", new IntType()),
                new CompoundStatement(
                        new DeclarationStatement("a", new RefType(new IntType())),
                        new CompoundStatement(
                                new AssignmentStatement("v", new ValueExpression(new IntValue(10))),
                                new CompoundStatement(
                                        new NewStatement("a", new ValueExpression(new IntValue(22))),
                                        new CompoundStatement(
                                                new ForkStatement(
                                                        new CompoundStatement(
                                                                new HeapWriteStatement("a", new ValueExpression(new IntValue(30))),
                                                                new CompoundStatement(
                                                                        new AssignmentStatement("v", new ValueExpression(new IntValue(32))),
                                                                        new CompoundStatement(
                                                                                new PrintStatement(new VariableExpression("v")),
                                                                                new PrintStatement(new HeapReadExpression(new VariableExpression("a")))
                                                                        )
                                                                )
                                                        )
                                                ),
                                                new CompoundStatement(
                                                        new PrintStatement(new VariableExpression("v")),
                                                        new PrintStatement(new HeapReadExpression(new VariableExpression("a")))
                                                )
                                        )
                                )
                        )
                )
        );

        IRepository repo4 = new InMemoryRepository("fork_test_log.txt");
        Controller ctr4 = new Controller(ex4, repo4, true);

        // Create text menu and add commands
        TextMenu menu = new TextMenu();
        menu.addCommand(new ExitCommand("0", "exit"));
        menu.addCommand(new RunExample("1", ex1.toString(), ctr1));
        menu.addCommand(new RunExample("2", ex2.toString(), ctr2));
        menu.addCommand(new RunExample("3", ex3.toString(), ctr3));
        menu.addCommand(new RunExample("4", "Fork Example: " + ex4.toString(), ctr4));

        // Show the menu
        menu.show();
    }
}