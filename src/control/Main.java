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

        // 创建新的节点,以及找到父节点
        Integer parentId = treeService.getTree().getRootId();
        MapNode sonNode = new MapNode(2);
        MapNode sonNode1 = new MapNode(3);
        sonNode.setContent("两只老婆爱跳舞");
        sonNode1.setContent("fxhSB");
        // 添加节点
        nodeService.addNode(parentId,sonNode.getId(),sonNode);
        nodeService.addNode(parentId,sonNode1.getId(),sonNode1);
        // 重新计算坐标
        treeService.setLayout();


        Generator TEST = new Generator();
        HashMap nodeMap = nodeService.getNodeList();
        nodeMap.forEach((key,value)->{
            TEST.showNode(root,(MapNode) value);
        });
    }


    public static void main(String[] args) {
        launch(args);
    }
}
