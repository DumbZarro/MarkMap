package control;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.Stage;
import model.pojo.MapNode;
import model.utils.ViewUtils;
import view.Generator;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class Controller {

    @FXML
    private ScrollPane scrollPane;
    @FXML
    private AnchorPane mindMapPane;

    @FXML
    private MenuBar LeftMenuBar;

    @FXML
    private Pane butBox;

    @FXML
    private Button MapToOutline;

    @FXML
    private Button OutlineToMap;


    @FXML
    private MenuItem mapInputButton;

    @FXML
    private MenuItem saveMapButton;

    @FXML
    private AnchorPane allPane;

    private Integer lineNum = 0;
    static public Path path;
    private Double mouseX;
    private Double mouseY;
    static public Boolean extraLineButPressed = false;


    @FXML
    void close(Event event) {
//        Main.treeService.saveToCloud();//结束时保存
        System.exit(0);
    }

    @FXML
    void maxmize(Event event) {
        Stage stage = (Stage) mindMapPane.getScene().getWindow();
        Scene scene = (Scene) mindMapPane.getScene();
        stage.setMaximized(!stage.isMaximized());
    }

    @FXML
    void minimize(Event event) {
        Stage stage = (Stage) mindMapPane.getScene().getWindow();
        Scene scene = (Scene) mindMapPane.getScene();
        stage.setIconified(true);
    }

    @FXML
    void MapToOutline(Event event) {
        mindMapPane.setVisible(false);
        MapToOutline.setStyle("-fx-background-color: rgb(230, 230, 231);\n" +
                "    -fx-text-fill: black;\n" +
                "    -fx-font-size: 25px;\n" +
                "    -fx-border-width: 2px;\n" +
                "    -fx-border-radius: 30px;\n" +
                "    -fx-background-radius: 30px; -fx-font-weight: bold");
        OutlineToMap.setStyle("-fx-background-color: rgb(244, 244, 244);\n" +
                "    -fx-text-fill:rgb(125, 125, 125);\n" +
                "    -fx-font-size: 25px;\n" +
                "    -fx-border-width: 2px;\n" +
                "    -fx-border-radius: 30px;\n" +
                "    -fx-background-radius: 30px; ");
    }

    @FXML
    void OutlineToMap(Event event) {
        mindMapPane.setVisible(true);
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
                "    -fx-background-radius: 30px;");
    }

    @FXML
    void newNode(Event event) {
        int k;
        while (Main.nodeService.getNodeList().get(k = (int)(Math.random()*10000)) ==null)
            break;
        Integer nodeId =  k;
        Integer parentId;
        if (Generator.selectedNodeNum!=1)
            parentId = Main.nodeService.getParentNodeById(Generator.selectedNodeNum).getId();//当选中节点不是中心节点时，给选中节点添加兄弟节点
        else
            parentId = Generator.selectedNodeNum;//当选中节点为中心节点，给中心节点添加子节点
        MapNode newNode = new MapNode(nodeId,Main.nodeService.getDefaultHeight(),Main.nodeService.getDefaultWidth(),Main.nodeService.getSCALE());
        Main.nodeService.addNode(parentId, nodeId,newNode);

        if (Main.treeService.getRootNode().getBlockHeight()*2>mindMapPane.getHeight()){
            Main.treeService.getRootNode().setTopY(mindMapPane.getHeight()-Main.treeService.getRootNode().getHeight()/2);
            mindMapPane.setPrefHeight(mindMapPane.getHeight()*2);
        }
        ///////////
        if (Main.treeService.getRootNode().getBlockWidth()*2>mindMapPane.getWidth()){
            mindMapPane.setPrefWidth(mindMapPane.getWidth()*2);
        }
        //////////
        Main.treeService.updateLayout();
        mindMapPane.getChildren().clear();
        Main.generator.showMap();
    }
    @FXML
    void newChildNode(Event event){
        Integer parentId = Generator.selectedNodeNum;
        int k;
        while (Main.nodeService.getNodeList().get(k = (int)(Math.random()*10000)) ==null)
            break;
        Integer nodeId =  k;
        MapNode newNode = new MapNode(nodeId,Main.nodeService.getDefaultHeight(),Main.nodeService.getDefaultWidth(),Main.nodeService.getSCALE());
        Main.nodeService.addNode(parentId, nodeId,newNode);

        if (Main.treeService.getRootNode().getBlockHeight()*2>mindMapPane.getHeight()){
            Main.treeService.getRootNode().setTopY(mindMapPane.getHeight()-Main.nodeService.getNodeById(1).getHeight()/2);
            mindMapPane.setPrefHeight(mindMapPane.getHeight()*2);
        }
        //////
        if (Main.treeService.getRootNode().getBlockWidth()*2>mindMapPane.getWidth()){
            mindMapPane.setPrefWidth(mindMapPane.getWidth()*2);
        }
        //////
        Main.treeService.updateLayout();
        mindMapPane.getChildren().clear();
        Main.generator.showMap();
    }
    @FXML
    void extraLine(Event event){
        extraLineButPressed = true;
    }
    @FXML
    void checkMousePosition(MouseEvent event){
        if (extraLineButPressed){
            this.mouseX = event.getX();
            this.mouseY = event.getY();
            MapNode startNode = Main.nodeService.getNodeById(Generator.selectedNodeNum);
            MoveTo moveTo = new MoveTo();
            moveTo.setX(startNode.getLeftX()+startNode.getWidth()/2);
            moveTo.setY(startNode.getTopY());

            CubicCurveTo cubicTo = new CubicCurveTo();
            cubicTo.setControlX1(startNode.getLeftX()+startNode.getWidth()/2);
            cubicTo.setControlY1(startNode.getTopY()-250);
            cubicTo.setControlX2(startNode.getLeftX()+startNode.getWidth()/2);
            cubicTo.setControlY2(startNode.getTopY()-250);
            if (mouseY<startNode.getTopY()-250){
                cubicTo.setX(startNode.getLeftX()+startNode.getWidth()/2+this.mouseX - event.getX());
                cubicTo.setY(startNode.getTopY()-250+this.mouseY -event.getY());
                System.out.println(mouseX);
            }
            else {
                cubicTo.setX(mouseX);
                cubicTo.setY(mouseY);
            }

            Path path = new Path(moveTo,cubicTo);
            path.setStroke(Color.rgb(255, 83, 140));
            path.setStrokeWidth(5);
            path.setStrokeLineCap(StrokeLineCap.ROUND);
            path.getStrokeDashArray().addAll(15d, 15d, 15d, 15d, 15d);
            path.setStrokeDashOffset(10);

            if (this.path != null){
                mindMapPane.getChildren().remove(this.path);
            }
            this.path = path ;
            mindMapPane.getChildren().add(path);

        }
    }
    @FXML
    public void outputPNG(Event event) throws IOException {
        WritableImage outputImage = mindMapPane.snapshot(new SnapshotParameters(),null);
        String path = ViewUtils.openPath();
//        File outputFile = new File(System.getProperty("user.dir")+File.separator+"node.png");
        File outputFile = new File(path+File.separator+"node.png");
        ViewUtils.displayMessage("信息", "保存成功", "确定");
        ImageIO.write(SwingFXUtils.fromFXImage(outputImage,null),"png",outputFile);
    }
    @FXML
    public void deleteNode(Event event){
        Integer deleteID= Generator.selectedNodeNum;
        Generator.selectedNodeNum = Main.nodeService.getNodeById(deleteID).getParentId();
        Main.nodeService.deleteNode(deleteID);
        Main.treeService.updateLayout();
        mindMapPane.getChildren().clear();
        Main.generator.showMap();

    }
    @FXML
    public void outLineBorder(){
        Integer choosedId = Generator.selectedNodeNum;
        MapNode choosedNode = Main.nodeService.getNodeById(choosedId);
        Rectangle choosedRectangle = new Rectangle();
        choosedRectangle.setWidth(choosedNode.getBlockWidth()+10);
        choosedRectangle.setHeight(choosedNode.getBlockHeight()+10);
        choosedRectangle.setLayoutX(choosedNode.getLeftX());
        choosedRectangle.setLayoutY((choosedNode.getHeight()-choosedNode.getBlockHeight())/2+choosedNode.getTopY()-5);
        choosedRectangle.setFill(Color.TRANSPARENT);
        choosedRectangle.setStroke(Color.RED);
        choosedRectangle.setStrokeWidth(5);
        choosedRectangle.setStyle("-fx-scale-z: -1");
        choosedNode.setHaveBlock(true);
        mindMapPane.getChildren().add(choosedRectangle);
    }
    @FXML
    void setLeftLayout(Event event) {
        Main.treeService.getTree().setLayout("left");
        Main.treeService.updateLayout();
        Main.generator.showMap();
    }

    @FXML
    void setRightLayout(Event event) {
        Main.treeService.getTree().setLayout("right");
        Main.treeService.updateLayout();
        Main.generator.showMap();
    }

    @FXML
    void setDefaultLayout(Event event) {
        Main.treeService.getTree().setLayout("default");
        Main.treeService.updateLayout();
        Main.generator.showMap();
    }
    @FXML
    void keyEvenHandler(Event event) {
      System.out.println(event.getEventType().getName());
    }

    @FXML
    void mapInput(Event event) {
        ViewUtils.openBox();
    }

    @FXML
    void saveMap(Event event) {
        Main.treeService.saveToCloud();//结束时保存
        ViewUtils.displayMessage("信息", "保存成功", "确定");
    }

    @FXML
    void subScale(Event event) {
        int scale =Main.nodeService.getSCALE();
        Main.nodeService.changeNodeSize(scale-10);
        Main.treeService.updateLayout();
        Main.generator.showMap();
    }

    @FXML
    void addScale(Event event) {
        int scale =Main.nodeService.getSCALE();
        Main.nodeService.changeNodeSize(scale+10);//能缩放了
        Main.treeService.updateLayout();
        Main.generator.showMap();
    }
    @FXML
    void notedNode(Event event){
        Main.nodeService.getNodeById(Generator.selectedNodeNum).setNoted(true);
        Main.generator.showMap();
    }
}
