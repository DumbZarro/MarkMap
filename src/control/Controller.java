package control;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;



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
    void MapToOutline(MouseEvent event) {
        System.out.println(111);
    }

    @FXML
    void genrateTopic(ActionEvent event) {

    }

}
