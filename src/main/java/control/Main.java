package control;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.dao.impl.MindMapDaoImpl;
import model.pojo.MapNode;
import model.service.impl.NodeServiceImpl;
import model.service.impl.TreeServiceImpl;
import view.Generator;

public class Main extends Application {
    // 创建服务
    static MindMapDaoImpl dataBaseService = new MindMapDaoImpl("map");  //打开思维导图
    static NodeServiceImpl nodeService = new NodeServiceImpl(dataBaseService);
    static TreeServiceImpl treeService = new TreeServiceImpl(nodeService);
    static Generator generator;
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("../EditMindMap.fxml"));
        primaryStage.setTitle("思维导图");
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();

        // 重新计算坐标
        treeService.getTree().setLayout("right");
        nodeService.changeNodeSize(50);//能缩放了
        treeService.updateLayout();
        generator = new Generator(nodeService,treeService,root);

        nodeService.getNodeList().forEach((key,value)->{
            generator.showNode((MapNode) value);
        });
        generator.drawLine();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
