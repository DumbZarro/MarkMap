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
import model.pojo.MapNode;
import view.Generator;

import java.util.HashMap;

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
        Integer nodeId =  (int)(Math.random()*100);
        Integer parentId;
        if (Generator.selectedNodeNum!=1)
            parentId = Main.nodeService.getParentNodeById(Generator.selectedNodeNum).getId();//当选中节点不是中心节点时，给选中节点添加兄弟节点
        else
            parentId = Generator.selectedNodeNum;//当选中节点为中心节点，给中心节点添加子节点
        MapNode newNode = new MapNode(nodeId);
        Main.nodeService.addNode(parentId, nodeId,newNode);
        Main.treeService.setLayout();
        mindMapPane.getChildren().clear();
        Main.nodeService.getNodeList().forEach((key,value)->{
            Main.generator.showNode((MapNode) value);
        });
        Main.generator.drawLine();
    }
}
