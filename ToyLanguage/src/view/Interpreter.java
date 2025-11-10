package view;

import controller.Controller;
import model.expression.ArithmeticExpression;
import model.expression.RelationalExpression;
import model.expression.ValueExpression;
import model.expression.VariableExpression;
import model.statement.*;
import model.type.IntType;
import model.type.StringType;
import model.value.IntValue;
import model.value.StringValue;
import repository.IRepository;
import repository.InMemoryRepository;

public class Interpreter {
    public static void main(String[] args) {
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

        // Create text menu and add commands
        TextMenu menu = new TextMenu();
        menu.addCommand(new ExitCommand("0", "exit"));
        menu.addCommand(new RunExample("1", ex1.toString(), ctr1));
        menu.addCommand(new RunExample("2", ex2.toString(), ctr2));
        menu.addCommand(new RunExample("3", ex3.toString(), ctr3));

        // Show the menu
        menu.show();
    }
}