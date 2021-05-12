package control;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.pojo.MapNode;
import model.service.impl.NodeServiceImpl;
import model.service.impl.TreeServiceImpl;
import view.Generator;

public class Main extends Application { // 创建服务
    static NodeServiceImpl nodeService = new NodeServiceImpl();
    static TreeServiceImpl treeService = new TreeServiceImpl(nodeService);
    static Generator generator;
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("../EditMindMap.fxml"));
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


        // 重新计算坐标
        treeService.getTree().setLayout("right");
//        nodeService.setSCALE(50);//TODO 放缩
        nodeService.addNode(sonNode2.getId(),gransonNode1.getId(),gransonNode1);
        treeService.updateLayout();
        generator = new Generator(nodeService,treeService,root);
//        treeService.saveToCloud();

        nodeService.getNodeList().forEach((key,value)->{
            generator.showNode((MapNode) value);
        });
        generator.drawLine();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
