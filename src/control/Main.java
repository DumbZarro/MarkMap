package control;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import model.pojo.MapNode;

import java.util.Collection;

public class Main extends Application {

    double x1;
    double y1;
    double x_stage;
    double y_stage;

    double mouseStartX=0;
    double mouseStartY=0;
    double nodeStartX=0;
    double nodeStartY=0;

    @Override
    public void start(Stage primaryStage) throws Exception{
//        Parent root = FXMLLoader.load(getClass().getResource("../view/source/sample.fxml"));
        Group root = new Group();
        primaryStage.setTitle("Hello World!control");
        Scene scene = new Scene(root, 300, 275);
        primaryStage.setScene(scene);
        primaryStage.show();


        MapNode node = new MapNode(1,50,50);
        Rectangle r = new Rectangle();
        r.setHeight(node.getHeight());
        r.setWidth(node.getWidth());
        r.setX(node.getLeftX());
        r.setY(node.getTopY());
        r.setId(node.getId()+"");
        root.getChildren().add(r);



        r.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                r.setX(nodeStartX+mouseEvent.getScreenX()-mouseStartX);
                r.setY(nodeStartY+mouseEvent.getScreenY()-mouseStartY);
//                System.out.println("确认存活");
            }
        });

        r.setOnDragEntered(null);
        r.setOnMousePressed(new EventHandler<MouseEvent>() {


            @Override
            public void handle(MouseEvent mouseEvent) {
                //按下鼠标后，记录当前鼠标的坐标
//                System.out.println("mouseEvent.getScreenX(): "+mouseEvent.getScreenX());
//                System.out.println("mouseEvent.getSceneY(): "+mouseEvent.getSceneY());
//                System.out.println("mouseEvent.getX(): "+mouseEvent.getX());

                mouseStartX = mouseEvent.getScreenX();
                mouseStartY = mouseEvent.getScreenY();
                nodeStartX = r.getX();
                nodeStartY = r.getY();
            }
        });







//        // scene跟随鼠标移动
//        scene.setOnMouseDragged(new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent m) {
//                //计算
//                primaryStage.setX(x_stage + m.getScreenX() - x1);
//                primaryStage.setY(y_stage + m.getScreenY() - y1);
//                System.out.println(this.toString());
//            }
//        });
//        scene.setOnDragEntered(null);
//        scene.setOnMousePressed(new EventHandler<MouseEvent>() {
//
//            @Override
//            public void handle(MouseEvent m) {
//                //按下鼠标后，记录当前鼠标的坐标
//                x1 = m.getScreenX();
//                y1 = m.getScreenY();
//                x_stage = primaryStage.getX();
//                y_stage = primaryStage.getY();
//            }
//        });




    }


    public static void main(String[] args) {
        launch(args);
    }
}
