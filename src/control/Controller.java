package control;

import javafx.application.Application;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Controller {

    @FXML
    private AnchorPane mindMapPane;

    @FXML
    private MenuBar LeftMenuBar;

    @FXML
    private Pane butBox;

    @FXML
    private Button MapToOutline;

    @FXML
    void close(Event event) {
        System.exit(0);
    }

    @FXML
    void maxmize(Event event) {
        Stage stage = (Stage) mindMapPane.getScene().getWindow();
        Scene scene = (Scene) mindMapPane.getScene();
        stage.setMaximized(true);

    }

    @FXML
    void minimize(Event event) {
        Stage stage = (Stage) mindMapPane.getScene().getWindow();
        Scene scene = (Scene) mindMapPane.getScene();
        stage.setAlwaysOnTop(false);
    }

    @FXML
    void OutlineToMap(Event event) {
        mindMapPane.setVisible(false);
    }

    @FXML
    void MapToOutline(Event event) {
        mindMapPane.setVisible(true);
    }

    @FXML
    void newNode(Event event) {

    }


}
