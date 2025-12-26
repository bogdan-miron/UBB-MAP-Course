package view;

import controller.Controller;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.exception.RepositoryException;
import model.state.*;
import model.value.IValue;

import java.util.*;
import java.util.stream.Collectors;

public class MainWindow {
    private Stage stage;
    private Controller controller;
    private String programName;

    // ui components
    private TextField prgStateCountField;           // (a) number of PrgStates
    private TableView<HeapEntry> heapTableView;     // (b) Heap table
    private ListView<String> outputListView;         // (c) Output
    private ListView<String> fileTableListView;      // (d) File table
    private ListView<Integer> prgStateIdListView;    // (e) PrgState IDs
    private TableView<SymbolEntry> symbolTableView;  // (f) Symbol table
    private ListView<String> exeStackListView;       // (g) Execution stack
    private Button runOneStepButton;
    private Label statusLabel;

    // helper classes for TableView data binding
    public static class HeapEntry {
        private final SimpleIntegerProperty address;
        private final SimpleStringProperty value;

        public HeapEntry(Integer address, String value) {
            this.address = new SimpleIntegerProperty(address);
            this.value = new SimpleStringProperty(value);
        }

        public int getAddress() {
            return address.get();
        }

        public String getValue() {
            return value.get();
        }

        public SimpleIntegerProperty addressProperty() {
            return address;
        }

        public SimpleStringProperty valueProperty() {
            return value;
        }
    }

    public static class SymbolEntry {
        private final SimpleStringProperty variableName;
        private final SimpleStringProperty value;

        public SymbolEntry(String variableName, String value) {
            this.variableName = new SimpleStringProperty(variableName);
            this.value = new SimpleStringProperty(value);
        }

        public String getVariableName() {
            return variableName.get();
        }

        public String getValue() {
            return value.get();
        }

        public SimpleStringProperty variableNameProperty() {
            return variableName;
        }

        public SimpleStringProperty valueProperty() {
            return value;
        }
    }

    public MainWindow(Controller controller, String programName) {
        this.controller = controller;
        this.programName = programName;
        this.stage = new Stage();
        setupUI();
        updateAllUIComponents();
    }

    private void setupUI() {
        // root layout
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        // TOP: Program info and state count
        HBox topPanel = createTopPanel();
        root.setTop(topPanel);

        // CENTER: Main content with all components
        VBox centerPanel = createCenterPanel();
        root.setCenter(centerPanel);

        // BOTTOM: Control buttons and status
        VBox bottomPanel = createBottomPanel();
        root.setBottom(bottomPanel);

        // create scene
        Scene scene = new Scene(root, 1200, 800);
        stage.setScene(scene);
        stage.setTitle("ToyLanguage Interpreter - " + programName);
        stage.setOnCloseRequest(e -> System.exit(0));
    }

    private HBox createTopPanel() {
        HBox topPanel = new HBox(20);
        topPanel.setPadding(new Insets(10));
        topPanel.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        topPanel.setStyle("-fx-background-color: #f0f0f0; -fx-border-color: #cccccc; -fx-border-width: 0 0 2 0;");

        Label countLabel = new Label("Active ProgramStates:");
        countLabel.setStyle("-fx-font-weight: bold;");

        prgStateCountField = new TextField("0");
        prgStateCountField.setEditable(false);
        prgStateCountField.setPrefWidth(60);
        prgStateCountField.setStyle("-fx-font-weight: bold;");

        topPanel.getChildren().addAll(countLabel, prgStateCountField);
        return topPanel;
    }

    private VBox createCenterPanel() {
        VBox centerPanel = new VBox(10);
        centerPanel.setPadding(new Insets(10, 0, 10, 0));

        // top section: Heap and Output side by side
        SplitPane topSplit = new SplitPane();
        topSplit.setOrientation(Orientation.HORIZONTAL);
        topSplit.setPrefHeight(250);

        VBox heapSection = createHeapSection();
        VBox outputSection = createOutputSection();

        topSplit.getItems().addAll(heapSection, outputSection);
        topSplit.setDividerPositions(0.5);

        // middle section: FileTable and PrgState IDs side by side
        SplitPane middleSplit = new SplitPane();
        middleSplit.setOrientation(Orientation.HORIZONTAL);
        middleSplit.setPrefHeight(150);

        VBox fileTableSection = createFileTableSection();
        VBox prgStateIdSection = createPrgStateIdSection();

        middleSplit.getItems().addAll(fileTableSection, prgStateIdSection);
        middleSplit.setDividerPositions(0.5);

        // bottom section: Symbol table and execution stack
        SplitPane bottomSplit = new SplitPane();
        bottomSplit.setOrientation(Orientation.HORIZONTAL);

        VBox symbolTableSection = createSymbolTableSection();
        VBox exeStackSection = createExeStackSection();

        bottomSplit.getItems().addAll(symbolTableSection, exeStackSection);
        bottomSplit.setDividerPositions(0.5);

        VBox.setVgrow(bottomSplit, Priority.ALWAYS);

        centerPanel.getChildren().addAll(topSplit, middleSplit, bottomSplit);
        return centerPanel;
    }

    private VBox createHeapSection() {
        VBox section = new VBox(5);

        Label label = new Label("Heap Table");
        label.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        heapTableView = new TableView<>();
        heapTableView.setPlaceholder(new Label("Heap is empty"));

        TableColumn<HeapEntry, Integer> addressCol = new TableColumn<>("Address");
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        addressCol.setPrefWidth(100);

        TableColumn<HeapEntry, String> valueCol = new TableColumn<>("Value");
        valueCol.setCellValueFactory(new PropertyValueFactory<>("value"));
        valueCol.setPrefWidth(200);

        heapTableView.getColumns().addAll(addressCol, valueCol);
        heapTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        VBox.setVgrow(heapTableView, Priority.ALWAYS);
        section.getChildren().addAll(label, heapTableView);
        return section;
    }

    private VBox createOutputSection() {
        VBox section = new VBox(5);

        Label label = new Label("Output");
        label.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        outputListView = new ListView<>();
        outputListView.setPlaceholder(new Label("No output yet"));
        outputListView.setStyle("-fx-font-family: monospace;");

        VBox.setVgrow(outputListView, Priority.ALWAYS);
        section.getChildren().addAll(label, outputListView);
        return section;
    }

    private VBox createFileTableSection() {
        VBox section = new VBox(5);

        Label label = new Label("File Table");
        label.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        fileTableListView = new ListView<>();
        fileTableListView.setPlaceholder(new Label("No files open"));
        fileTableListView.setStyle("-fx-font-family: monospace;");

        VBox.setVgrow(fileTableListView, Priority.ALWAYS);
        section.getChildren().addAll(label, fileTableListView);
        return section;
    }

    private VBox createPrgStateIdSection() {
        VBox section = new VBox(5);

        Label label = new Label("ProgramState IDs");
        label.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        prgStateIdListView = new ListView<>();
        prgStateIdListView.setPlaceholder(new Label("No program states"));

        // add selection listener
        prgStateIdListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                updateSelectedProgramState();
            }
        });

        VBox.setVgrow(prgStateIdListView, Priority.ALWAYS);
        section.getChildren().addAll(label, prgStateIdListView);
        return section;
    }

    private VBox createSymbolTableSection() {
        VBox section = new VBox(5);

        Label label = new Label("Symbol Table (for selected ProgramState)");
        label.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        symbolTableView = new TableView<>();
        symbolTableView.setPlaceholder(new Label("Select a ProgramState to view its symbol table"));

        TableColumn<SymbolEntry, String> nameCol = new TableColumn<>("Variable Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("variableName"));
        nameCol.setPrefWidth(150);

        TableColumn<SymbolEntry, String> valueCol = new TableColumn<>("Value");
        valueCol.setCellValueFactory(new PropertyValueFactory<>("value"));
        valueCol.setPrefWidth(200);

        symbolTableView.getColumns().addAll(nameCol, valueCol);
        symbolTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        VBox.setVgrow(symbolTableView, Priority.ALWAYS);
        section.getChildren().addAll(label, symbolTableView);
        return section;
    }

    private VBox createExeStackSection() {
        VBox section = new VBox(5);

        Label label = new Label("Execution Stack (for selected ProgramState)");
        label.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        exeStackListView = new ListView<>();
        exeStackListView.setPlaceholder(new Label("Select a ProgramState to view its execution stack"));
        exeStackListView.setStyle("-fx-font-family: monospace;");

        VBox.setVgrow(exeStackListView, Priority.ALWAYS);
        section.getChildren().addAll(label, exeStackListView);
        return section;
    }

    private VBox createBottomPanel() {
        VBox bottomPanel = new VBox(10);
        bottomPanel.setPadding(new Insets(10));
        bottomPanel.setStyle("-fx-background-color: #f0f0f0; -fx-border-color: #cccccc; -fx-border-width: 2 0 0 0;");

        // buttons
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(javafx.geometry.Pos.CENTER);

        runOneStepButton = new Button("Run One Step");
        runOneStepButton.setStyle("-fx-font-size: 14px; -fx-padding: 10 30; -fx-font-weight: bold;");
        runOneStepButton.setOnAction(e -> onRunOneStepClicked());

        buttonBox.getChildren().add(runOneStepButton);

        // status label
        statusLabel = new Label("Ready to execute");
        statusLabel.setStyle("-fx-font-size: 12px;");

        bottomPanel.getChildren().addAll(buttonBox, statusLabel);
        return bottomPanel;
    }

    private void onRunOneStepClicked() {
        runOneStepButton.setDisable(true);
        statusLabel.setText("Executing one step...");
        statusLabel.setTextFill(Color.BLUE);

        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                List<ProgramState> prgList = controller.getRepo().getPrgList();
                if (!prgList.isEmpty()) {
                    // remove completed programs BEFORE executing
                    // this keeps the final state visible for output display
                    prgList = prgList.stream()
                            .filter(p -> p.isNotCompleted())
                            .collect(Collectors.toList());
                    controller.getRepo().setPrgList(prgList);

                    // only execute if there are still programs to run
                    if (!prgList.isEmpty()) {
                        controller.oneStepForAllPrg(prgList);
                    }
                }
                return null;
            }
        };

        task.setOnSucceeded(event -> Platform.runLater(() -> {
            updateAllUIComponents();

            // check if there are any incomplete programs
            List<ProgramState> prgList = controller.getRepo().getPrgList();
            long incompleteCount = prgList.stream().filter(p -> p.isNotCompleted()).count();

            if (incompleteCount == 0) {
                statusLabel.setText("All programs completed! Final output displayed above.");
                statusLabel.setTextFill(Color.GREEN);
                runOneStepButton.setDisable(true);
            } else {
                statusLabel.setText("Step executed successfully. " + incompleteCount + " program(s) remaining.");
                statusLabel.setTextFill(Color.BLACK);
                runOneStepButton.setDisable(false);
            }
        }));

        task.setOnFailed(event -> Platform.runLater(() -> {
            Throwable ex = task.getException();
            statusLabel.setText("Error: " + ex.getMessage());
            statusLabel.setTextFill(Color.RED);
            runOneStepButton.setDisable(false);

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Execution Error");
            alert.setHeaderText("An error occurred during execution");
            alert.setContentText(ex.getMessage());
            alert.showAndWait();
        }));

        new Thread(task).start();
    }

    private void updateAllUIComponents() {
        List<ProgramState> prgList = controller.getRepo().getPrgList();

        // (a) update PrgState count (only count incomplete programs)
        long incompleteCount = prgList.stream().filter(p -> p.isNotCompleted()).count();
        prgStateCountField.setText(String.valueOf(incompleteCount));

        if (prgList.isEmpty()) {
            // clear all displays if no programs (shouldn't happen normally)
            heapTableView.setItems(FXCollections.observableArrayList());
            outputListView.setItems(FXCollections.observableArrayList());
            fileTableListView.setItems(FXCollections.observableArrayList());
            prgStateIdListView.setItems(FXCollections.observableArrayList());
            symbolTableView.setItems(FXCollections.observableArrayList());
            exeStackListView.setItems(FXCollections.observableArrayList());
            return;
        }

        // (b) update Heap Table (shared across all states)
        ProgramState firstState = prgList.get(0);
        Map<Integer, IValue> heap = firstState.getHeap().getContent();
        ObservableList<HeapEntry> heapEntries = FXCollections.observableArrayList();
        for (Map.Entry<Integer, IValue> entry : heap.entrySet()) {
            heapEntries.add(new HeapEntry(entry.getKey(), entry.getValue().toString()));
        }
        heapTableView.setItems(heapEntries);

        // (c) update Output (shared across all states)
        List<IValue> output = firstState.getOutput().getOutput();
        ObservableList<String> outputStrings = FXCollections.observableArrayList();
        for (IValue value : output) {
            outputStrings.add(value.toString());
        }
        outputListView.setItems(outputStrings);

        // (d) update File Table (shared across all states)
        String fileTableStr = firstState.getFileTable().toString();
        ObservableList<String> fileTableLines = FXCollections.observableArrayList();
        if (fileTableStr.contains("{") && fileTableStr.contains("}")) {
            String content = fileTableStr.substring(fileTableStr.indexOf("{") + 1, fileTableStr.lastIndexOf("}")).trim();
            if (!content.isEmpty()) {
                String[] files = content.split(",");
                for (String file : files) {
                    fileTableLines.add(file.trim());
                }
            }
        }
        fileTableListView.setItems(fileTableLines);

        // (e) update PrgState IDs (show all, including completed)
        List<Integer> ids = prgList.stream().map(ProgramState::getId).collect(Collectors.toList());
        ObservableList<Integer> idList = FXCollections.observableArrayList(ids);
        Integer selectedId = prgStateIdListView.getSelectionModel().getSelectedItem();
        prgStateIdListView.setItems(idList);

        // re-select if still valid, otherwise select first
        if (selectedId != null && ids.contains(selectedId)) {
            prgStateIdListView.getSelectionModel().select(selectedId);
        } else if (!ids.isEmpty()) {
            prgStateIdListView.getSelectionModel().selectFirst();
        }

        // (f) & (g) will be updated by selection listener
        updateSelectedProgramState();
    }

    private void updateSelectedProgramState() {
        Integer selectedId = prgStateIdListView.getSelectionModel().getSelectedItem();
        if (selectedId == null) {
            symbolTableView.setItems(FXCollections.observableArrayList());
            exeStackListView.setItems(FXCollections.observableArrayList());
            return;
        }

        // find the program state with this ID
        List<ProgramState> prgList = controller.getRepo().getPrgList();
        ProgramState selectedState = null;
        for (ProgramState state : prgList) {
            if (state.getId() == selectedId) {
                selectedState = state;
                break;
            }
        }

        if (selectedState == null) {
            symbolTableView.setItems(FXCollections.observableArrayList());
            exeStackListView.setItems(FXCollections.observableArrayList());
            return;
        }

        // (f) update Symbol Table for selected state
        Map<String, IValue> symTable = selectedState.getSymTable().getContent();
        ObservableList<SymbolEntry> symbolEntries = FXCollections.observableArrayList();
        for (Map.Entry<String, IValue> entry : symTable.entrySet()) {
            symbolEntries.add(new SymbolEntry(entry.getKey(), entry.getValue().toString()));
        }
        symbolTableView.setItems(symbolEntries);

        // (g) update Execution Stack for selected state
        String exeStackStr = selectedState.getExeStack().toString();
        ObservableList<String> exeStackLines = FXCollections.observableArrayList();
        String[] lines = exeStackStr.split("\n");
        for (String line : lines) {
            if (!line.trim().isEmpty()) {
                exeStackLines.add(line);
            }
        }
        exeStackListView.setItems(exeStackLines);
    }

    public void show() {
        stage.show();
    }
}
