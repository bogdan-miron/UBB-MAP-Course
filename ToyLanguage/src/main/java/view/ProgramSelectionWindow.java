package view;

import controller.Controller;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.exception.TypeException;
import model.expression.*;
import model.statement.*;
import model.type.IntType;
import model.type.RefType;
import model.type.StringType;
import model.value.IntValue;
import model.value.StringValue;
import repository.IRepository;
import repository.InMemoryRepository;

import java.util.ArrayList;
import java.util.List;

public class ProgramSelectionWindow {
    private Stage stage;
    private ListView<String> programListView;
    private TextArea programDetailsArea;
    private List<ProgramDefinition> programs;

    // helper class to store program information
    private static class ProgramDefinition {
        String name;
        String description;
        IStatement program;
        String logFile;

        ProgramDefinition(String name, String description, IStatement program, String logFile) {
            this.name = name;
            this.description = description;
            this.program = program;
            this.logFile = logFile;
        }
    }

    public ProgramSelectionWindow() {
        this.stage = new Stage();
        this.programs = createExamplePrograms();
        setupUI();
    }

    private List<ProgramDefinition> createExamplePrograms() {
        List<ProgramDefinition> programList = new ArrayList<>();

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
        programList.add(new ProgramDefinition(
                "Example 1: Simple Arithmetic",
                ex1.toString(),
                ex1,
                "gui_log1.txt"
        ));

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
        programList.add(new ProgramDefinition(
                "Example 2: If Statement",
                ex2.toString(),
                ex2,
                "gui_log2.txt"
        ));

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
        programList.add(new ProgramDefinition(
                "Example 3: File Operations",
                ex3.toString(),
                ex3,
                "gui_log3.txt"
        ));

        // Example 4: Fork statement demonstration
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
        programList.add(new ProgramDefinition(
                "Example 4: Fork (Concurrency)",
                ex4.toString(),
                ex4,
                "gui_fork_log.txt"
        ));

        // Example 5: While loop
        // int v; v=0; while (v<3) { print(v); v=v+1; } print(100)
        IStatement ex5 = new CompoundStatement(
                new DeclarationStatement("v", new IntType()),
                new CompoundStatement(
                        new AssignmentStatement("v", new ValueExpression(new IntValue(0))),
                        new CompoundStatement(
                                new WhileStatement(
                                        new RelationalExpression(
                                                new VariableExpression("v"),
                                                new ValueExpression(new IntValue(3)),
                                                "<"
                                        ),
                                        new CompoundStatement(
                                                new PrintStatement(new VariableExpression("v")),
                                                new AssignmentStatement("v", new ArithmeticExpression(
                                                        new VariableExpression("v"),
                                                        new ValueExpression(new IntValue(1)),
                                                        "+"
                                                ))
                                        )
                                ),
                                new PrintStatement(new ValueExpression(new IntValue(100)))
                        )
                )
        );
        programList.add(new ProgramDefinition(
                "Example 5: While Loop",
                ex5.toString(),
                ex5,
                "gui_while_log.txt"
        ));

        // Example 6: Heap Operations
        // Ref int v; new(v,20); Ref Ref int a; new(a,v); print(rH(v)); print(rH(rH(a))+5)
        IStatement ex6 = new CompoundStatement(
                new DeclarationStatement("v", new RefType(new IntType())),
                new CompoundStatement(
                        new NewStatement("v", new ValueExpression(new IntValue(20))),
                        new CompoundStatement(
                                new DeclarationStatement("a", new RefType(new RefType(new IntType()))),
                                new CompoundStatement(
                                        new NewStatement("a", new VariableExpression("v")),
                                        new CompoundStatement(
                                                new PrintStatement(new HeapReadExpression(new VariableExpression("v"))),
                                                new PrintStatement(new ArithmeticExpression(
                                                        new HeapReadExpression(new HeapReadExpression(new VariableExpression("a"))),
                                                        new ValueExpression(new IntValue(5)),
                                                        "+"
                                                ))
                                        )
                                )
                        )
                )
        );
        programList.add(new ProgramDefinition(
                "Example 6: Nested References (Heap)",
                ex6.toString(),
                ex6,
                "gui_nested_heap_log.txt"
        ));

        // Example 7: Boolean Operations
        // int a; int b; a=5; b=7; if (a<b) then print(a) else print(b); if (a!=b) then print(100) else print(0)
        IStatement ex7 = new CompoundStatement(
                new DeclarationStatement("a", new IntType()),
                new CompoundStatement(
                        new DeclarationStatement("b", new IntType()),
                        new CompoundStatement(
                                new AssignmentStatement("a", new ValueExpression(new IntValue(5))),
                                new CompoundStatement(
                                        new AssignmentStatement("b", new ValueExpression(new IntValue(7))),
                                        new CompoundStatement(
                                                new IfStatement(
                                                        new RelationalExpression(
                                                                new VariableExpression("a"),
                                                                new VariableExpression("b"),
                                                                "<"
                                                        ),
                                                        new PrintStatement(new VariableExpression("a")),
                                                        new PrintStatement(new VariableExpression("b"))
                                                ),
                                                new IfStatement(
                                                        new RelationalExpression(
                                                                new VariableExpression("a"),
                                                                new VariableExpression("b"),
                                                                "!="
                                                        ),
                                                        new PrintStatement(new ValueExpression(new IntValue(100))),
                                                        new PrintStatement(new ValueExpression(new IntValue(0)))
                                                )
                                        )
                                )
                        )
                )
        );
        programList.add(new ProgramDefinition(
                "Example 7: Relational Operators",
                ex7.toString(),
                ex7,
                "gui_relational_log.txt"
        ));

        // Example 8: While Loop with Heap
        // Ref int v; new(v,20); while (rH(v)>0) { print(rH(v)); wH(v,rH(v)-1); } print(100)
        IStatement ex8 = new CompoundStatement(
                new DeclarationStatement("v", new RefType(new IntType())),
                new CompoundStatement(
                        new NewStatement("v", new ValueExpression(new IntValue(5))),
                        new CompoundStatement(
                                new WhileStatement(
                                        new RelationalExpression(
                                                new HeapReadExpression(new VariableExpression("v")),
                                                new ValueExpression(new IntValue(0)),
                                                ">"
                                        ),
                                        new CompoundStatement(
                                                new PrintStatement(new HeapReadExpression(new VariableExpression("v"))),
                                                new HeapWriteStatement("v", new ArithmeticExpression(
                                                        new HeapReadExpression(new VariableExpression("v")),
                                                        new ValueExpression(new IntValue(1)),
                                                        "-"
                                                ))
                                        )
                                ),
                                new PrintStatement(new ValueExpression(new IntValue(100)))
                        )
                )
        );
        programList.add(new ProgramDefinition(
                "Example 8: While Loop with Heap (Countdown)",
                ex8.toString(),
                ex8,
                "gui_countdown_log.txt"
        ));

        // Example 9: Multiple Forks
        // int a; a=10; fork(a=a+1; print(a)); fork(a=a*2; print(a)); print(a)
        IStatement ex9 = new CompoundStatement(
                new DeclarationStatement("a", new IntType()),
                new CompoundStatement(
                        new AssignmentStatement("a", new ValueExpression(new IntValue(10))),
                        new CompoundStatement(
                                new ForkStatement(
                                        new CompoundStatement(
                                                new AssignmentStatement("a", new ArithmeticExpression(
                                                        new VariableExpression("a"),
                                                        new ValueExpression(new IntValue(1)),
                                                        "+"
                                                )),
                                                new PrintStatement(new VariableExpression("a"))
                                        )
                                ),
                                new CompoundStatement(
                                        new ForkStatement(
                                                new CompoundStatement(
                                                        new AssignmentStatement("a", new ArithmeticExpression(
                                                                new VariableExpression("a"),
                                                                new ValueExpression(new IntValue(2)),
                                                                "*"
                                                        )),
                                                        new PrintStatement(new VariableExpression("a"))
                                                )
                                        ),
                                        new PrintStatement(new VariableExpression("a"))
                                )
                        )
                )
        );
        programList.add(new ProgramDefinition(
                "Example 9: Multiple Forks",
                ex9.toString(),
                ex9,
                "gui_multiple_forks_log.txt"
        ));

        // Example 10: Repeat...Until Statement
        // int v; int x; int y; v=0;
        // repeat (fork(print(v);v=v-1);v=v+1) until v==3;
        // x=1;nop;y=3;nop;
        // print(v*10)
        IStatement ex10 = new CompoundStatement(
                new DeclarationStatement("v", new IntType()),
                new CompoundStatement(
                        new DeclarationStatement("x", new IntType()),
                        new CompoundStatement(
                                new DeclarationStatement("y", new IntType()),
                                new CompoundStatement(
                                        new AssignmentStatement("v", new ValueExpression(new IntValue(0))),
                                        new CompoundStatement(
                                                new RepeatStatement(
                                                        new CompoundStatement(
                                                                new ForkStatement(
                                                                        new CompoundStatement(
                                                                                new PrintStatement(new VariableExpression("v")),
                                                                                new AssignmentStatement("v", new ArithmeticExpression(
                                                                                        new VariableExpression("v"),
                                                                                        new ValueExpression(new IntValue(1)),
                                                                                        "-"
                                                                                ))
                                                                        )
                                                                ),
                                                                new AssignmentStatement("v", new ArithmeticExpression(
                                                                        new VariableExpression("v"),
                                                                        new ValueExpression(new IntValue(1)),
                                                                        "+"
                                                                ))
                                                        ),
                                                        new RelationalExpression(
                                                                new VariableExpression("v"),
                                                                new ValueExpression(new IntValue(3)),
                                                                "=="
                                                        )
                                                ),
                                                new CompoundStatement(
                                                        new AssignmentStatement("x", new ValueExpression(new IntValue(1))),
                                                        new CompoundStatement(
                                                                new NopStatement(),
                                                                new CompoundStatement(
                                                                        new AssignmentStatement("y", new ValueExpression(new IntValue(3))),
                                                                        new CompoundStatement(
                                                                                new NopStatement(),
                                                                                new PrintStatement(new ArithmeticExpression(
                                                                                        new VariableExpression("v"),
                                                                                        new ValueExpression(new IntValue(10)),
                                                                                        "*"
                                                                                ))
                                                                        )
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                )
        );
        programList.add(new ProgramDefinition(
                "Example 10: Repeat...Until",
                ex10.toString(),
                ex10,
                "gui_repeat_until_log.txt"
        ));

        // Example 11: Conditional Assignment
        // Ref int a; Ref int b; int v;
        // new(a,0); new(b,0);
        // wh(a,1); wh(b,2);
        // v=(rh(a)<rh(b))?100:200;
        // print(v);
        // v=((rh(b)-2)>rh(a))?100:200;
        // print(v);
        IStatement ex11 = new CompoundStatement(
                new DeclarationStatement("a", new RefType(new IntType())),
                new CompoundStatement(
                        new DeclarationStatement("b", new RefType(new IntType())),
                        new CompoundStatement(
                                new DeclarationStatement("v", new IntType()),
                                new CompoundStatement(
                                        new NewStatement("a", new ValueExpression(new IntValue(0))),
                                        new CompoundStatement(
                                                new NewStatement("b", new ValueExpression(new IntValue(0))),
                                                new CompoundStatement(
                                                        new HeapWriteStatement("a", new ValueExpression(new IntValue(1))),
                                                        new CompoundStatement(
                                                                new HeapWriteStatement("b", new ValueExpression(new IntValue(2))),
                                                                new CompoundStatement(
                                                                        // v=(rh(a)<rh(b))?100:200
                                                                        new ConditionalAssignmentStatement(
                                                                                "v",
                                                                                new RelationalExpression(
                                                                                        new HeapReadExpression(new VariableExpression("a")),
                                                                                        new HeapReadExpression(new VariableExpression("b")),
                                                                                        "<"
                                                                                ),
                                                                                new ValueExpression(new IntValue(100)),
                                                                                new ValueExpression(new IntValue(200))
                                                                        ),
                                                                        new CompoundStatement(
                                                                                new PrintStatement(new VariableExpression("v")),
                                                                                new CompoundStatement(
                                                                                        // v=((rh(b)-2)>rh(a))?100:200
                                                                                        new ConditionalAssignmentStatement(
                                                                                                "v",
                                                                                                new RelationalExpression(
                                                                                                        new ArithmeticExpression(
                                                                                                                new HeapReadExpression(new VariableExpression("b")),
                                                                                                                new ValueExpression(new IntValue(2)),
                                                                                                                "-"
                                                                                                        ),
                                                                                                        new HeapReadExpression(new VariableExpression("a")),
                                                                                                        ">"
                                                                                                ),
                                                                                                new ValueExpression(new IntValue(100)),
                                                                                                new ValueExpression(new IntValue(200))
                                                                                        ),
                                                                                        new PrintStatement(new VariableExpression("v"))
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
        programList.add(new ProgramDefinition(
                "Example 11: Conditional Assignment",
                ex11.toString(),
                ex11,
                "gui_conditional_assignment_log.txt"
        ));

        // Example 12: CountDownLatch
        // Ref int v1; Ref int v2; Ref int v3; int cnt;
        // new(v1,2); new(v2,3); new(v3,4); newLatch(cnt,rH(v2));
        // fork(wH(v1,rH(v1)*10); print(rH(v1)); countDown(cnt);
        //   fork(wH(v2,rH(v2)*10); print(rH(v2)); countDown(cnt);
        //     fork(wH(v3,rH(v3)*10); print(rH(v3)); countDown(cnt))
        //   )
        // );
        // await(cnt);
        // print(100);
        // countDown(cnt);
        // print(100)
        IStatement ex12 = new CompoundStatement(
                new DeclarationStatement("v1", new RefType(new IntType())),
                new CompoundStatement(
                        new DeclarationStatement("v2", new RefType(new IntType())),
                        new CompoundStatement(
                                new DeclarationStatement("v3", new RefType(new IntType())),
                                new CompoundStatement(
                                        new DeclarationStatement("cnt", new IntType()),
                                        new CompoundStatement(
                                                new NewStatement("v1", new ValueExpression(new IntValue(2))),
                                                new CompoundStatement(
                                                        new NewStatement("v2", new ValueExpression(new IntValue(3))),
                                                        new CompoundStatement(
                                                                new NewStatement("v3", new ValueExpression(new IntValue(4))),
                                                                new CompoundStatement(
                                                                        new NewLatchStatement("cnt", new HeapReadExpression(new VariableExpression("v2"))),
                                                                        new CompoundStatement(
                                                                                new ForkStatement(
                                                                                        new CompoundStatement(
                                                                                                new HeapWriteStatement("v1", new ArithmeticExpression(
                                                                                                        new HeapReadExpression(new VariableExpression("v1")),
                                                                                                        new ValueExpression(new IntValue(10)),
                                                                                                        "*"
                                                                                                )),
                                                                                                new CompoundStatement(
                                                                                                        new PrintStatement(new HeapReadExpression(new VariableExpression("v1"))),
                                                                                                        new CompoundStatement(
                                                                                                                new CountDownStatement("cnt"),
                                                                                                                new ForkStatement(
                                                                                                                        new CompoundStatement(
                                                                                                                                new HeapWriteStatement("v2", new ArithmeticExpression(
                                                                                                                                        new HeapReadExpression(new VariableExpression("v2")),
                                                                                                                                        new ValueExpression(new IntValue(10)),
                                                                                                                                        "*"
                                                                                                                                )),
                                                                                                                                new CompoundStatement(
                                                                                                                                        new PrintStatement(new HeapReadExpression(new VariableExpression("v2"))),
                                                                                                                                        new CompoundStatement(
                                                                                                                                                new CountDownStatement("cnt"),
                                                                                                                                                new ForkStatement(
                                                                                                                                                        new CompoundStatement(
                                                                                                                                                                new HeapWriteStatement("v3", new ArithmeticExpression(
                                                                                                                                                                        new HeapReadExpression(new VariableExpression("v3")),
                                                                                                                                                                        new ValueExpression(new IntValue(10)),
                                                                                                                                                                        "*"
                                                                                                                                                                )),
                                                                                                                                                                new CompoundStatement(
                                                                                                                                                                        new PrintStatement(new HeapReadExpression(new VariableExpression("v3"))),
                                                                                                                                                                        new CountDownStatement("cnt")
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
                                                                                ),
                                                                                new CompoundStatement(
                                                                                        new AwaitStatement("cnt"),
                                                                                        new CompoundStatement(
                                                                                                new PrintStatement(new ValueExpression(new IntValue(100))),
                                                                                                new CompoundStatement(
                                                                                                        new CountDownStatement("cnt"),
                                                                                                        new PrintStatement(new ValueExpression(new IntValue(100)))
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
        programList.add(new ProgramDefinition(
                "Example 12: CountDownLatch",
                ex12.toString(),
                ex12,
                "gui_countdownlatch_log.txt"
        ));

        return programList;
    }

    private void setupUI() {
        // main container
        VBox mainLayout = new VBox(15);
        mainLayout.setPadding(new Insets(20));
        mainLayout.setAlignment(Pos.TOP_CENTER);

        // title
        Label titleLabel = new Label("ToyLanguage Interpreter");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // instruction label
        Label instructionLabel = new Label("Select a program to execute:");

        // program list
        programListView = new ListView<>();
        ObservableList<String> programNames = FXCollections.observableArrayList();
        for (ProgramDefinition prog : programs) {
            programNames.add(prog.name);
        }
        programListView.setItems(programNames);
        programListView.setPrefHeight(120);
        programListView.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal) -> {
            onSelectionChanged();
        });

        // program details area
        Label detailsLabel = new Label("Program Details:");
        programDetailsArea = new TextArea();
        programDetailsArea.setEditable(false);
        programDetailsArea.setWrapText(true);
        programDetailsArea.setPrefHeight(200);
        programDetailsArea.setStyle("-fx-font-family: monospace;");
        VBox.setVgrow(programDetailsArea, Priority.ALWAYS);

        // buttons
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);

        Button runButton = new Button("Run Selected Program");
        runButton.setStyle("-fx-font-size: 14px; -fx-padding: 10 20;");
        runButton.setOnAction(e -> onRunClicked());

        Button exitButton = new Button("Exit");
        exitButton.setStyle("-fx-font-size: 14px; -fx-padding: 10 20;");
        exitButton.setOnAction(e -> stage.close());

        buttonBox.getChildren().addAll(runButton, exitButton);

        // add all components to main layout
        mainLayout.getChildren().addAll(
                titleLabel,
                instructionLabel,
                programListView,
                detailsLabel,
                programDetailsArea,
                buttonBox
        );

        // create scene and set stage
        Scene scene = new Scene(mainLayout, 700, 600);
        stage.setScene(scene);
        stage.setTitle("ToyLanguage Interpreter - Program Selection");
        stage.setOnCloseRequest(e -> System.exit(0));

        // select first program by default
        if (!programs.isEmpty()) {
            programListView.getSelectionModel().selectFirst();
        }
    }

    private void onSelectionChanged() {
        int selectedIndex = programListView.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0 && selectedIndex < programs.size()) {
            ProgramDefinition selected = programs.get(selectedIndex);
            programDetailsArea.setText(selected.description);
        }
    }

    private void onRunClicked() {
        int selectedIndex = programListView.getSelectionModel().getSelectedIndex();
        if (selectedIndex < 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Program Selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select a program to run.");
            alert.showAndWait();
            return;
        }

        ProgramDefinition selected = programs.get(selectedIndex);

        try {
            // create repository and controller
            IRepository repository = new InMemoryRepository(selected.logFile);
            Controller controller = new Controller(selected.program, repository, true);

            // create and show main window
            MainWindow mainWindow = new MainWindow(controller, selected.name);
            mainWindow.show();

            // close selection window
            stage.close();
        } catch (TypeException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Type Error");
            alert.setHeaderText("Program has type checking errors");
            alert.setContentText(ex.getMessage());
            alert.showAndWait();
        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Failed to initialize program");
            alert.setContentText(ex.getMessage());
            alert.showAndWait();
        }
    }

    public void show() {
        stage.show();
    }
}
