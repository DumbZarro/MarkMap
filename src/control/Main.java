package control;

import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.pojo.MapNode;
import model.service.impl.NodeServiceImpl;
import model.service.impl.TreeServiceImpl;
import view.Generator;

import java.awt.*;
import java.util.HashMap;

public class Main extends Application { // 创建服务
    static NodeServiceImpl nodeService = new NodeServiceImpl();
    static TreeServiceImpl treeService = new TreeServiceImpl(nodeService);
    static Generator generator;
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("../view/source/EditMindMap.fxml"));
        primaryStage.setTitle("思维导图");
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();

        MapNode parentNode = treeService.getRootNode();
        // 创建新的节点,以及找到父节点
        Integer parentId = treeService.getTree().getRootId();
        MapNode sonNode0 = new MapNode(2);
        MapNode sonNode1 = new MapNode(3);
        MapNode sonNode2 = new MapNode(4);
        MapNode gransonNode =  new MapNode(5);
        MapNode gransonNode1 =  new MapNode(6);
        sonNode0.setContent("11111");
        sonNode1.setContent("22222");
        sonNode2.setContent("33333");

        // 添加节点
        nodeService.addNode(parentId,sonNode0.getId(),sonNode0);
        nodeService.addNode(parentId,sonNode1.getId(),sonNode1);
        nodeService.addNode(parentId,sonNode2.getId(),sonNode2);
        nodeService.addNode(sonNode2.getId(),gransonNode.getId(),gransonNode);
        nodeService.addNode(sonNode2.getId(),gransonNode1.getId(),gransonNode1);

        // 重新计算坐标
        treeService.getTree().setLayout("right");
        treeService.setLayout();


        generator = new Generator(nodeService,treeService,root);
        generator.showMap();

        Button OutlineToMap = (Button) root.lookup("#OutlineToMap");
        Button MapToOutline = (Button) root.lookup("#MapToOutline");
        OutlineToMap.setStyle("-fx-background-color: rgb(230, 230, 231);\n" +
                "    -fx-text-fill: black;\n" +
                "    -fx-font-size: 25px;\n" +
                "    -fx-border-width: 2px;\n" +
                "    -fx-border-radius: 30px;\n" +
                "    -fx-background-radius: 30px; -fx-font-weight: bold");
        MapToOutline.setStyle("-fx-background-color: rgb(244, 244, 244);\n" +
                "    -fx-text-fill: rgb(125, 125, 125);\n" +
                "    -fx-font-size: 25px;\n" +
                "    -fx-border-width: 2px;\n" +
                "    -fx-border-radius: 30px;\n" +
                "    -fx-background-radius: 30px; ");
    }


    public static void main(String[] args) {
        launch(args);
    }
}
