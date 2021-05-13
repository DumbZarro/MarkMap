package view;

import com.sun.source.tree.Tree;
import control.Controller;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import model.pojo.MapNode;
import model.service.NodeService;
import model.service.impl.NodeServiceImpl;
import model.service.impl.TreeServiceImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class Generator {
    private NodeServiceImpl nodeService;
    private TreeServiceImpl treeService;
    private Parent root;
    private HashMap<Integer,Rectangle> nodeRectangleList = new HashMap<>();
    static public Integer selectedNodeNum = 1;
    AnchorPane MindMapPane;
    Stack<MapNode> notYetDrawPStack = new Stack<>();

    Color lineColor = Color.BLUE;

    public Generator(NodeServiceImpl nodeService,TreeServiceImpl treeService,Parent root) {
        this.nodeService=nodeService;
        this.treeService=treeService;
        this.root = root;
        this.MindMapPane = (AnchorPane)root.lookup("#mindMapPane");
    }
    //画图
    public void showMap(){
        nodeService.getNodeList().forEach((key,value)->{
            showNode((MapNode) value);
        });
        drawLine();

    }
    //    展示showNode
    private void showNode(MapNode showNode){
        MindMapPane = (AnchorPane)root.lookup("#mindMapPane");
        Rectangle nodeRectangle = nodeSelectedRectangle(showNode);//假若该节点被选中的长方形
        TextField text = nodeInputRectangle(showNode);

        if (showNode.getExtraEdge()!=null)//当前显示节点有额外边的话调用画额外边的函数
            for (MapNode extraTargetNode: showNode.getExtraEdge()){
                drawextraLine(showNode,extraTargetNode);
            }

        //输入框的鼠标事件
        text.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                MapNode exChoosedNode =  nodeService.getNodeById(selectedNodeNum);
                exChoosedNode.setSelected(false);
                if (Controller.extraLineButPressed){
                    drawextraLine(exChoosedNode,showNode);
                    exChoosedNode.getExtraEdge().add(showNode);
                }
                showNode.setSelected(true);
                nodeRectangleList.get(selectedNodeNum).setVisible(false);
                nodeRectangle.setVisible(true);
                selectedNodeNum = showNode.getId();
            }
        });
        text.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                showNode.setContent(text.getText());
            }
        });
        text.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {

            }
        });

        MindMapPane.getChildren().add(nodeRectangle);
        MindMapPane.getChildren().add(text);

    }
    //    画线
    private void drawLine(){
        notYetDrawPStack.push(nodeService.getNodeById(1));
    public void drawLine(){
        MapNode rootNode = treeService.getRootNode();
        notYetDrawPStack.push(rootNode);
        if(!rootNode.getSonDisplay()){//父节点没有可见子节点就不要划线了
            return;
        }
        String layout = treeService.getTree().getLayout();
        while(!notYetDrawPStack.empty()){
            MapNode notYetDrawP = notYetDrawPStack.pop();//有子节点的节点出栈
            ArrayList<Integer> childList = notYetDrawP.getChildrenId();//获取该节点的子节点列表
            ArrayList<Double> maxAndMinY = new ArrayList<>();//最大和最小的子节点y值
            switch (layout){
                case "right":maxAndMinY = rightDrawMapLine(childList);//右布局画线
                    Line parNodeLine = new Line(notYetDrawP.getLeftX()+notYetDrawP.getWidth(),notYetDrawP.getTopY()+notYetDrawP.getHeight()/2,notYetDrawP.getLeftX()+notYetDrawP.getWidth()+(nodeService.getNodeById(childList.get(0)).getLeftX()-notYetDrawP.getLeftX()-notYetDrawP.getWidth())/2,notYetDrawP.getTopY()+notYetDrawP.getHeight()/2);
                    parNodeLine.setStroke(Color.rgb(104, 151, 187));
                    parNodeLine.setStrokeWidth(6);
                    MindMapPane.getChildren().add(parNodeLine);
                    Line veriticalLineRight = new Line(notYetDrawP.getLeftX()+notYetDrawP.getWidth()+20,maxAndMinY.get(0)+notYetDrawP.getHeight()/2,notYetDrawP.getLeftX()+notYetDrawP.getWidth()+20,maxAndMinY.get(1)+notYetDrawP.getHeight()/2);//链接所有节点的竖线
                    veriticalLineRight.setStroke(Color.rgb(104, 151, 187));
                    veriticalLineRight.setStrokeWidth(6);
                    MindMapPane.getChildren().add(veriticalLineRight);

                    break;
                case "left":maxAndMinY = leftDrawMapLine(childList);//左布局画线
                    Line parNodeLineleft = new Line(notYetDrawP.getLeftX(),notYetDrawP.getTopY()+notYetDrawP.getHeight()/2,notYetDrawP.getLeftX()-(notYetDrawP.getLeftX()-(nodeService.getNodeById(childList.get(0))).getLeftX()-(nodeService.getNodeById(childList.get(0))).getWidth())/2,notYetDrawP.getTopY()+notYetDrawP.getHeight()/2);//父节点伸出一小段线
                    parNodeLineleft.setStroke(Color.rgb(104, 151, 187));
                    parNodeLineleft.setStrokeWidth(6);
                    MindMapPane.getChildren().add(parNodeLineleft);
                    Line veriticalLineLeft = new Line(notYetDrawP.getLeftX()-20,maxAndMinY.get(0)+notYetDrawP.getHeight()/2,notYetDrawP.getLeftX()-20,maxAndMinY.get(1)+notYetDrawP.getHeight()/2);//链接所有节点的竖线
                    veriticalLineLeft.setStroke(Color.rgb(104, 151, 187));
                    veriticalLineLeft.setStrokeWidth(6);
                    MindMapPane.getChildren().add(veriticalLineLeft);
                    break;
            }

        }
    }
    //    右布局画线
    private ArrayList<Double> rightDrawMapLine(ArrayList<Integer> childList){
        Double MAXY,MINY;
        MAXY = nodeService.getNodeById(childList.get(0)).getTopY();
        MINY = nodeService.getNodeById(childList.get(0)).getTopY();
        for (int i = 0; i<childList.size();i++){
            MapNode currentNode = nodeService.getNodeById(childList.get(i));
            if (currentNode.getTopY()>MAXY){
                MAXY = currentNode.getTopY();
            }
            if (currentNode.getTopY()<MINY){
                MINY = currentNode.getTopY();
            }
            if(nodeService.getChildrenNodeByNode(currentNode).size()!=0)
                notYetDrawPStack.push(currentNode);
            MapNode parentNode = nodeService.getParentNodeByNode(currentNode);
            Line nodeLine= new Line(currentNode.getLeftX()-(currentNode.getLeftX()-(parentNode.getLeftX()+ parentNode.getWidth()))/2,currentNode.getTopY()+currentNode.getHeight()/2,currentNode.getLeftX(),currentNode.getTopY()+currentNode.getHeight()/2);
            nodeLine.setStroke(Color.rgb(104, 151, 187));
            nodeLine.setStrokeWidth(6);
            MindMapPane.getChildren().add(nodeLine);
        }
        ArrayList<Double> maxAndMinY = new ArrayList<>();
        maxAndMinY.add(MAXY);
        maxAndMinY.add(MINY);
        return maxAndMinY;
    }
    //左布局画线
    private ArrayList<Double> leftDrawMapLine(ArrayList<Integer> childList) {
        Double MAXY, MINY;
        MAXY = nodeService.getNodeById(childList.get(0)).getTopY();
        MINY = nodeService.getNodeById(childList.get(0)).getTopY();
        for (int i = 0; i < childList.size(); i++) {
            MapNode currentNode = nodeService.getNodeById(childList.get(i));
            if (currentNode.getTopY() > MAXY) {
                MAXY = currentNode.getTopY();
            }
            if (currentNode.getTopY() < MINY) {
                MINY = currentNode.getTopY();
            }
            if (nodeService.getChildrenNodeByNode(currentNode).size() != 0)
                notYetDrawPStack.push(currentNode);
            MapNode parentNode = nodeService.getParentNodeByNode(currentNode);
            Line nodeLine = new Line(currentNode.getLeftX()+currentNode.getWidth(), currentNode.getTopY() + currentNode.getHeight() / 2, currentNode.getLeftX()+currentNode.getWidth()+(parentNode.getLeftX()-currentNode.getLeftX()-currentNode.getWidth())/2, currentNode.getTopY() + currentNode.getHeight() / 2);
            nodeLine.setStroke(Color.rgb(104, 151, 187));
            nodeLine.setStrokeWidth(6);
            MindMapPane.getChildren().add(nodeLine);
        }
        ArrayList<Double> maxAndMinY = new ArrayList<>();
        maxAndMinY.add(MAXY);
        maxAndMinY.add(MINY);
        return maxAndMinY;
    }
    public void drawextraLine(MapNode startNode,MapNode targetNode){
        MoveTo moveTo = new MoveTo();
        moveTo.setX(startNode.getLeftX()+ startNode.getWidth()/2);//起始坐标
        moveTo.setY(startNode.getTopY());
        CubicCurveTo cubicTo = new CubicCurveTo();
        cubicTo.setControlX1( startNode.getLeftX()+ startNode.getWidth()/2+100);
        cubicTo.setControlY1( startNode.getTopY()-250);
        cubicTo.setControlX2( startNode.getLeftX()+ startNode.getWidth()/2+100);
        cubicTo.setControlY2( startNode.getTopY()-250);
        cubicTo.setX(targetNode.getLeftX()+targetNode.getWidth()/2);//终点坐标
        cubicTo.setY(targetNode.getTopY());
        Path path = new Path(moveTo,cubicTo);
        path.setStroke(Color.rgb(255, 83, 140));
        path.setStrokeWidth(5);
        path.setStrokeLineCap(StrokeLineCap.ROUND);
        path.getStrokeDashArray().addAll(15d, 15d, 15d, 15d, 15d);
        path.setStrokeDashOffset(10);
        MindMapPane.getChildren().remove(Controller.path);
        MindMapPane.getChildren().add(path);
        Controller.extraLineButPressed = false;
    }
    public TextField nodeInputRectangle(MapNode showNode){
        TextField text = new TextField();//节点输入框
        text.setText(showNode.getContent());
        text.setLayoutX(showNode.getLeftX());
        text.setLayoutY(showNode.getTopY());
        text.setPrefHeight(nodeService.getSCALE().doubleValue());
        text.setPrefWidth(nodeService.getSCALE().doubleValue());
        text.setStyle("-fx-font-size: 15px;-fx-border-radius: 15;\n" +
                "    -fx-background-radius: 15; -fx-font-weight: bold;");
        text.setVisible(true);
        text.setFont(new Font(10));
        return text;
    }
    public Rectangle nodeSelectedRectangle(MapNode showNode){
        Rectangle nodeRectangle = new Rectangle(nodeService.getSCALE().doubleValue()+nodeService.getSCALE().doubleValue()/3,nodeService.getSCALE().doubleValue()+nodeService.getSCALE().doubleValue()/3);//节点被选中的浮出框
        nodeRectangle.setFill(Color.rgb(84, 87, 83));
        nodeRectangle.setLayoutY(showNode.getTopY()-nodeService.getSCALE().doubleValue()/6);
        nodeRectangle.setLayoutX(showNode.getLeftX()-nodeService.getSCALE().doubleValue()/6);
        nodeRectangle.setId("abc");
        nodeRectangle.setStroke(Color.rgb(73, 156, 84));//边框颜色
        nodeRectangle.setStrokeWidth(5);
        nodeRectangle.getStrokeDashArray().addAll(15d, 15d, 15d, 15d, 15d);
        nodeRectangle.setStrokeDashOffset(10);
        nodeRectangleList.put(showNode.getId(),nodeRectangle);
        if (!showNode.getSelected())//节点被选中即显示浮出框
            nodeRectangle.setVisible(false);
        else
            nodeRectangle.setVisible(true);
        return nodeRectangle;
    }
}
