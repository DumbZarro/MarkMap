package control;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TreeView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.stage.StageStyle;
import model.dao.impl.MindMapDaoImpl;
import model.pojo.MapNode;
import model.service.impl.NodeServiceImpl;
import model.service.impl.TreeServiceImpl;
import view.Generator;

import java.awt.*;
import java.util.HashMap;
import java.util.Objects;


public class Main extends Application {
    // 创建服务
    public static String mapName = "map1";
    static MindMapDaoImpl dataBaseService = new MindMapDaoImpl(mapName);  //打开思维导图
    static NodeServiceImpl nodeService = new NodeServiceImpl(dataBaseService);
    static TreeServiceImpl treeService = new TreeServiceImpl(nodeService);
    static Generator generator;
    static public AnchorPane mindMapPane;
    private Parent root;

    @Override
    public void start(Stage primaryStage) throws Exception {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../EditMindMap.fxml")));
        primaryStage.setTitle("思维导图");
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.show();
        mindMapPane = (AnchorPane) root.lookup("#mindMapPane");
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
                                                                 Pane treePane = (Pane) scene.getRoot().lookup("#treePane");
                                                                 SplitPane splitPane = (SplitPane) scene.getRoot().lookup("#splitPane");
                                                                 TreeView<String> treeView = (TreeView) scene.getRoot().lookup("#treeView");

                                                                 allPane.setMaxHeight(primaryStage.getHeight());//设置最底下的平面
                                                                 allPane.setMaxWidth(primaryStage.getWidth());
                                                                 topPane.setPrefSize(allPane.getWidth(), 0.04 * allPane.getHeight());//设置上方平面
                                                                 bottomPane.setPrefSize(allPane.getWidth(), 0.96 * allPane.getHeight());//设置下方平面
                                                                 splitPane.setPrefHeight(allPane.getHeight() * 5 / 6);//设置分割的平面
                                                                 splitPane.setPrefWidth(allPane.getWidth());
                                                                 scrollPane.setPrefHeight(allPane.getHeight() * 5 / 6);
                                                                 scrollPane.setPrefWidth(allPane.getWidth() * 3 / 4);
                                                                 treePane.setPrefHeight(allPane.getHeight() * 5 / 6);
                                                                 treePane.setPrefWidth(allPane.getWidth() * 1 / 4);
                                                                 treeView.setPrefHeight(allPane.getHeight() * 5 / 6);
                                                                 treeView.setPrefWidth(allPane.getWidth() * 1 / 4);
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
        treeService.getTree().setLayout("right");
        nodeService.changeNodeSize(60);//能缩放了
        for (MapNode node : nodeService.getNodeList().values()) {
            node.setSelected(false);
        }
        treeService.updateLayout();

        generator = new Generator(nodeService, treeService, root);

        generator.showMap();

        ScrollPane scrollPane = (ScrollPane) root.lookup("#scrollPane");
        scrollPane.setVvalue(0.5);
        scrollPane.setHvalue(0.5);

    }

    private void addButton() {
        Button OutlineToMap = (Button) root.lookup("#OutlineToMap");
        OutlineToMap.setStyle("-fx-background-color: rgb(230, 230, 231);\n" +
                "    -fx-text-fill: black;\n" +
                "    -fx-font-size: 25px;\n" +
                "    -fx-border-width: 2px;\n" +
                "    -fx-border-radius: 30px;\n" +
                "    -fx-background-radius: 30px; -fx-font-weight: bold");

    }

    public static void main(String[] args) {
        launch(args);
    }
}
