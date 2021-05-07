package control;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.pojo.MapNode;
import model.service.impl.NodeServiceImpl;
import model.service.impl.TreeServiceImpl;
import view.Generator;

import java.util.HashMap;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("../view/source/EditMindMap.fxml"));
        NodeServiceImpl impl = new NodeServiceImpl();
        Generator TEST = new Generator();
        primaryStage.setTitle("思维导图");
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
        MapNode centerNode = new MapNode(100,300,400);
        MapNode centerNode1 = new MapNode(111,500,500);
        centerNode.setContent("两只老婆爱跳舞");
        impl.addNode(1,100,centerNode);
        impl.addNode(1,111,centerNode1);

        HashMap nodeMap = impl.getNodeList();
        nodeMap.forEach((key,value)->{
            TEST.showNode(root,(MapNode) value);
        });
    }


    public static void main(String[] args) {
        launch(args);
    }
}
