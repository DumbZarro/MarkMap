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

public class Main extends Application { // 创建服务
    NodeServiceImpl nodeService = new NodeServiceImpl();
    TreeServiceImpl treeService = new TreeServiceImpl(nodeService);

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("../view/source/EditMindMap.fxml"));
        NodeServiceImpl impl = new NodeServiceImpl();
        Generator TEST = new Generator();
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
        sonNode0.setContent("11111");
        sonNode1.setContent("22222");
        sonNode2.setContent("33333");
        // 添加节点
        nodeService.addNode(parentId,sonNode0.getId(),sonNode0);
        nodeService.addNode(parentId,sonNode1.getId(),sonNode1);
        nodeService.addNode(parentId,sonNode2.getId(),sonNode2);
        // 重新计算坐标
        treeService.getTree().setLayout("right");
        treeService.setLayout();


        Generator TEST = new Generator(nodeService);
        HashMap nodeMap = nodeService.getNodeList();
        nodeMap.forEach((key,value)->{
            TEST.showNode(root,(MapNode) value);
        });
    }


    public static void main(String[] args) {
        launch(args);
    }
}
