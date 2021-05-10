package view;

import com.sun.source.tree.Tree;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
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
import java.util.Stack;

public class Generator {
    private NodeServiceImpl nodeService;
    private TreeServiceImpl treeService;
    private Parent root;
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

    public void showNode(MapNode showNode){
        AnchorPane MindMapPane = (AnchorPane)root.lookup("#mindMapPane");
        Rectangle nodeRectangle = new Rectangle(showNode.getWidth(),showNode.getHeight());
        nodeRectangle.setLayoutY(showNode.getTopY());
        nodeRectangle.setLayoutX(showNode.getLeftX());
        Color color =Color.rgb(84, 87, 83);
        nodeRectangle.setFill(color);
        nodeRectangle.setId("abc");



        TextField text = new TextField();
        text.setText(showNode.getContent());
        text.setLayoutX(showNode.getLeftX());
        text.setLayoutY(showNode.getTopY());
        text.setPrefHeight(100);
        text.setPrefWidth(200);
        text.setStyle("-fx-font-size: 15px;-fx-border-radius: 15;\n" +
                    "    -fx-background-radius: 15; -fx-font-weight: bold;");
        text.setVisible(true);
        text.setFont(new Font(10));



        text.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                nodeService.getNodeById(selectedNodeNum).setSelected(false);
                showNode.setSelected(true);
                selectedNodeNum = showNode.getId();
            }
        });
        text.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                showNode.setContent(text.getText());
            }
        });

        MindMapPane.getChildren().add(nodeRectangle);
        MindMapPane.getChildren().add(text);

    }
    public void drawLine(){
        notYetDrawPStack.push(nodeService.getNodeById(1));
        String layout = treeService.getTree().getLayout();
        while(!notYetDrawPStack.empty()){
            MapNode notYetDrawP = notYetDrawPStack.pop();//有子节点的节点出栈
            ArrayList<Integer> childList = notYetDrawP.getChildrenId();//获取该节点的子节点列表
            ArrayList<Double> maxAndMinY = new ArrayList<>();//最大和最小的子节点y值
            switch (layout){
                case "right":maxAndMinY = rightDrawMapLine(childList);//右布局画线
                    Line parNodeLine = new Line(notYetDrawP.getLeftX()+notYetDrawP.getWidth(),notYetDrawP.getTopY()+notYetDrawP.getHeight()/2,notYetDrawP.getLeftX()+notYetDrawP.getWidth()+20,notYetDrawP.getTopY()+notYetDrawP.getHeight()/2);
                    parNodeLine.setStroke(Color.rgb(73, 156, 84));
                    parNodeLine.setStrokeWidth(6);
                    MindMapPane.getChildren().add(parNodeLine);
                    Line veriticalLineRight = new Line(notYetDrawP.getLeftX()+notYetDrawP.getWidth()+20,maxAndMinY.get(0)+notYetDrawP.getHeight()/2,notYetDrawP.getLeftX()+notYetDrawP.getWidth()+20,maxAndMinY.get(1)+notYetDrawP.getHeight()/2);//链接所有节点的竖线
                    veriticalLineRight.setStroke(Color.rgb(73, 156, 84));
                    veriticalLineRight.setStrokeWidth(6);
                    MindMapPane.getChildren().add(veriticalLineRight);

                break;
                case "left":maxAndMinY = leftDrawMapLine(childList);//左布局画线
                    Line parNodeLineleft = new Line(notYetDrawP.getLeftX(),notYetDrawP.getTopY()+notYetDrawP.getHeight()/2,notYetDrawP.getLeftX()-20,notYetDrawP.getTopY()+notYetDrawP.getHeight()/2);//父节点伸出一小段线
                    parNodeLineleft.setStroke(Color.rgb(73, 156, 84));
                    parNodeLineleft.setStrokeWidth(6);
                    MindMapPane.getChildren().add(parNodeLineleft);
                    Line veriticalLineLeft = new Line(notYetDrawP.getLeftX()-20,maxAndMinY.get(0)+notYetDrawP.getHeight()/2,notYetDrawP.getLeftX()-20,maxAndMinY.get(1)+notYetDrawP.getHeight()/2);//链接所有节点的竖线
                    veriticalLineLeft.setStroke(Color.rgb(73, 156, 84));
                    veriticalLineLeft.setStrokeWidth(6);
                    MindMapPane.getChildren().add(veriticalLineLeft);

                    break;
            }

        }
    }
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
            Line nodeLine= new Line(currentNode.getLeftX()-(currentNode.getLeftX()-(parentNode.getLeftX()+ parentNode.getWidth()+20)),currentNode.getTopY()+currentNode.getHeight()/2,currentNode.getLeftX(),currentNode.getTopY()+currentNode.getHeight()/2);
            nodeLine.setStroke(Color.rgb(73, 156, 84));
            nodeLine.setStrokeWidth(6);
            MindMapPane.getChildren().add(nodeLine);
        }
        ArrayList<Double> maxAndMinY = new ArrayList<>();
        maxAndMinY.add(MAXY);
        maxAndMinY.add(MINY);
        return maxAndMinY;
    }
    private ArrayList<Double> leftDrawMapLine(ArrayList<Integer> childList){
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
            Line nodeLine= new Line(parentNode.getLeftX()-20,currentNode.getTopY()+currentNode.getHeight()/2,currentNode.getLeftX()+currentNode.getWidth(),currentNode.getTopY()+currentNode.getHeight()/2);
            nodeLine.setStroke(Color.rgb(73, 156, 84));
            nodeLine.setStrokeWidth(6);
            MindMapPane.getChildren().add(nodeLine);
        }
        ArrayList<Double> maxAndMinY = new ArrayList<>();
        maxAndMinY.add(MAXY);
        maxAndMinY.add(MINY);
        return maxAndMinY;
    }
}
