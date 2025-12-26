package view;

import javafx.application.Application;
import javafx.stage.Stage;

public class InterpreterGUI extends Application {
    @Override
    public void start(Stage primaryStage) {
        ProgramSelectionWindow selectionWindow = new ProgramSelectionWindow();
        selectionWindow.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
