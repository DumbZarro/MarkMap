package control;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
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
    private Parent root;

    @Override
    public void start(Stage primaryStage) throws Exception {
        root = FXMLLoader.load(getClass().getResource("../EditMindMap.fxml"));
        primaryStage.setTitle("思维导图");
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.show();
        initialize();
        addButton();
        stageSelfAdaption(primaryStage);

    }

    private void stageSelfAdaption(Stage primaryStage) {
        primaryStage.widthProperty().addListener(new ChangeListener<Number>() {
        @Override
        public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    Scene scene = primaryStage.getScene();

                    AnchorPane allPane = (AnchorPane) scene.getRoot().lookup("#allPane");
                    ScrollPane scrollPane = (ScrollPane) scene.getRoot().lookup("#scrollPane");
                    Pane topPane = (Pane) scene.getRoot().lookup("#topPane");
                    Pane bottomPane = (Pane) scene.getRoot().lookup("#bottomPane");
                    Pane butPane = (Pane) scene.getRoot().lookup("#butPane");
                    Pane butBox = (Pane) scene.getRoot().lookup("#butBox");
                    Pane layoutPane = (Pane) scene.getRoot().lookup("#layoutPane");

                    allPane.setMaxHeight(primaryStage.getHeight());
                    allPane.setMaxWidth(primaryStage.getWidth());
                    topPane.setPrefSize(allPane.getWidth(), 0.04 * allPane.getHeight());
                    bottomPane.setPrefSize(allPane.getWidth(), 0.96 * allPane.getHeight());
                    scrollPane.setPrefHeight(allPane.getHeight() * 5 / 6);
                    scrollPane.setPrefWidth(allPane.getWidth());
                    butPane.setLayoutX(allPane.getWidth() / 2 - 204);
                    butBox.setLayoutX(allPane.getWidth() - 165);
                    butPane.setLayoutY(-8);
                    layoutPane.setLayoutX(allPane.getWidth() - 191);
                }
            });
        }
        }
        );
    }

    private void initialize() {
        MapNode parentNode = treeService.getRootNode();
        // 创建新的节点,以及找到父节点
        Integer parentId = treeService.getTree().getRootId();
        MapNode sonNode0 = new MapNode(2);
        MapNode sonNode1 = new MapNode(3);
        MapNode sonNode2 = new MapNode(4);
        sonNode0.setContent("11111");
        sonNode1.setContent("22222");
        sonNode2.setContent("33333");

        nodeService.setSCALE(50);
        // 添加节点
        nodeService.addNode(parentId, sonNode0.getId(), sonNode0);
        nodeService.addNode(parentId, sonNode1.getId(), sonNode1);
        nodeService.addNode(parentId, sonNode2.getId(), sonNode2);

        // 重新计算坐标
        treeService.getTree().setLayout("left");
        treeService.updateLayout();

        generator = new Generator(nodeService, treeService, root);

        generator.showMap();

        ScrollPane scrollPane = (ScrollPane) root.lookup("#scrollPane");
        scrollPane.setVvalue(0.5);
        scrollPane.setHvalue(0.5);

    }

    private void addButton() {
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
