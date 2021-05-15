package view;

import control.Controller;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import model.pojo.MapNode;
import model.service.impl.NodeServiceImpl;
import model.service.impl.TreeServiceImpl;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class Generator {
    private NodeServiceImpl nodeService;
    private TreeServiceImpl treeService;
    private Parent root;
    private HashMap<Integer, Rectangle> nodeRectangleList = new HashMap<>();
    static public Integer selectedNodeNum = 1;
    AnchorPane MindMapPane;
    Stack<MapNode> notYetDrawPStack = new Stack<>();

    Color lineColor = Color.BLUE;

    public Generator(NodeServiceImpl nodeService, TreeServiceImpl treeService, Parent root) {
        this.nodeService = nodeService;
        this.treeService = treeService;
        this.root = root;
        this.MindMapPane = (AnchorPane) root.lookup("#mindMapPane");
    }

    //画图
    public void showMap() {
        MindMapPane.getChildren().clear();
        nodeService.getNodeList().forEach((key, value) -> {
            if (value.getVisible()) {
                showNode((MapNode) value);
            }
        });
        drawLine();
        treeView();
        ScrollPane scrollPane = (ScrollPane) root.lookup("#scrollPane");
        scrollPane.setVvalue(0.5);
        scrollPane.setHvalue(0.5);
    }

    //    展示showNode
    private void showNode(MapNode showNode) {
        MindMapPane = (AnchorPane) root.lookup("#mindMapPane");
        Rectangle nodeRectangle = nodeSelectedRectangle(showNode);//假若该节点被选中的长方形
        TextField text = nodeInputRectangle(showNode);
        Button sonVisibleNodeBut = storeButInit(showNode);
        Button annotaionBut = annotaionBut(showNode);//注释的按钮
        TextArea textArea = noteText(showNode);//注释框
        if (showNode.getExtraEdge().size() != 0)//当前显示节点有额外边的话调用画额外边的函数
            for (MapNode extraTargetNode : showNode.getExtraEdge()) {
                if (showNode.getVisible() && extraTargetNode.getVisible()) {
                    drawextraLine(showNode, extraTargetNode);
                    System.out.println(extraTargetNode.getId());
                }
            }

        //输入框的鼠标事件
        text.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                MapNode exChoosedNode = nodeService.getNodeById(selectedNodeNum);
                exChoosedNode.setSelected(false);
                if (Controller.extraLineButPressed) {
                    drawextraLine(exChoosedNode, showNode);
                    exChoosedNode.getExtraEdge().add(showNode);
                    Controller.extraLineButPressed = false;
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
                if (text.getLength() * 15 > text.getWidth()) {
                    text.setPrefWidth(text.getWidth() * 1.3);
                    showNode.setWidth(text.getWidth() * 1.3);
                    nodeRectangle.setWidth(text.getWidth() * 1.3 + 10);
                    treeService.updateLayout();
                    showMap();
                }
            }
        });

        MindMapPane.getChildren().add(nodeRectangle);
        MindMapPane.getChildren().add(text);
        if (showNode.getChildrenId().size() != 0)
            MindMapPane.getChildren().add(sonVisibleNodeBut);
        if (showNode.getNoted())
        MindMapPane.getChildren().add(annotaionBut);
        annotaionBut.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (showNode.getNoteVisible())
                    showNode.setNoteVisible(false);
                else showNode.setNoteVisible(true);
                showMap();
            }
        });
        textArea.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                showNode.setNote(textArea.getText());
            }
        });
        if (showNode.getNoteVisible())
        MindMapPane.getChildren().add(textArea);
    }

    //    画线
    public void drawLine() {
        MapNode rootNode = treeService.getRootNode();
        notYetDrawPStack.push(rootNode);
        if (!rootNode.getSonDisplay()) {//父节点没有可见子节点就不要划线了
            return;
        }
        String layoutString = treeService.getTree().getLayout();
        Integer layout = -100;
        switch (layoutString) {
            case "right":
                layout = 1;
                break;
            case "left":
                layout = -1;
                break;
            case "default":
                layout = 0;
                break;
        }
        while (!notYetDrawPStack.empty()) {
            MapNode notYetDrawP = notYetDrawPStack.pop();//有子节点的节点出栈
            ArrayList<Integer> childList = notYetDrawP.getChildrenId();//获取该节点的子节点列表

            ArrayList<Double> maxAndMinY = new ArrayList<>();//最大和最小的子节点y值
            if (notYetDrawP.getSonDisplay()) {
                switch (layout) {
                    case 1: {
                        maxAndMinY = rightDrawMapLine(childList);//右布局画线
                        Line parNodeLine = new Line(notYetDrawP.getLeftX() + notYetDrawP.getWidth(), notYetDrawP.getTopY() + notYetDrawP.getHeight() / 2, notYetDrawP.getLeftX() + notYetDrawP.getWidth() + (nodeService.getNodeById(childList.get(0)).getLeftX() - notYetDrawP.getLeftX() - notYetDrawP.getWidth()) / 2, notYetDrawP.getTopY() + notYetDrawP.getHeight() / 2);
                        parNodeLine.setStroke(Color.rgb(104, 151, 187));
                        parNodeLine.setStrokeWidth(6.0*nodeService.getSCALE()/100);
                        MindMapPane.getChildren().add(parNodeLine);
                        Line veriticalLineRight = new Line(notYetDrawP.getLeftX() + notYetDrawP.getWidth() + (nodeService.getNodeById(childList.get(0)).getLeftX() - notYetDrawP.getLeftX() - notYetDrawP.getWidth()) / 2, maxAndMinY.get(0) + notYetDrawP.getHeight() / 2, notYetDrawP.getLeftX() + notYetDrawP.getWidth() + (nodeService.getNodeById(childList.get(0)).getLeftX() - notYetDrawP.getLeftX() - notYetDrawP.getWidth()) / 2, maxAndMinY.get(1) + notYetDrawP.getHeight() / 2);//链接所有节点的竖线
                        veriticalLineRight.setStroke(Color.rgb(104, 151, 187));
                        veriticalLineRight.setStrokeWidth(6.0*nodeService.getSCALE()/100);
                        MindMapPane.getChildren().add(veriticalLineRight);
                        break;
                    }
                    case -1: {
                        maxAndMinY = leftDrawMapLine(childList);//左布局画线
                        Line parNodeLineleft = new Line(notYetDrawP.getLeftX(), notYetDrawP.getTopY() + notYetDrawP.getHeight() / 2, notYetDrawP.getLeftX() - (notYetDrawP.getLeftX() - (nodeService.getNodeById(childList.get(0))).getLeftX() - (nodeService.getNodeById(childList.get(0))).getWidth()) / 2, notYetDrawP.getTopY() + notYetDrawP.getHeight() / 2);//父节点伸出一小段线
                        parNodeLineleft.setStroke(Color.rgb(104, 151, 187));
                        parNodeLineleft.setStrokeWidth(6.0*nodeService.getSCALE()/100);
                        MindMapPane.getChildren().add(parNodeLineleft);
                        Line veriticalLineLeft = new Line(notYetDrawP.getLeftX() - (notYetDrawP.getLeftX() - (nodeService.getNodeById(childList.get(0))).getLeftX() - (nodeService.getNodeById(childList.get(0))).getWidth()) / 2, maxAndMinY.get(0) + notYetDrawP.getHeight() / 2, notYetDrawP.getLeftX() - (notYetDrawP.getLeftX() - (nodeService.getNodeById(childList.get(0))).getLeftX() - (nodeService.getNodeById(childList.get(0))).getWidth()) / 2, maxAndMinY.get(1) + notYetDrawP.getHeight() / 2);//链接所有节点的竖线
                        veriticalLineLeft.setStroke(Color.rgb(104, 151, 187));
                        veriticalLineLeft.setStrokeWidth(6.0*nodeService.getSCALE()/100);
                        MindMapPane.getChildren().add(veriticalLineLeft);
                        break;
                    }
                    case 0: {
                        Integer halfSize = childList.size() / 2;
                        ArrayList rightChildList;
                        ArrayList leftChildList;
                        if (childList.size()%2!=0) {
                            rightChildList = new ArrayList(childList.subList(0, halfSize + 1));
                            leftChildList = new ArrayList(childList.subList(halfSize + 1, childList.size()));
                        }
                        else
                        {
                            rightChildList = new ArrayList(childList.subList(0, halfSize));
                            leftChildList = new ArrayList(childList.subList(halfSize, childList.size()));
                        }
                        System.out.println(childList.size());
                        ArrayList<Double> maxAndMinY1 = leftDrawMapLine(leftChildList);
                        ArrayList<Double> maxAndMinY2 = rightDrawMapLine(rightChildList);
                        MapNode leftChildNode = nodeService.getNodeById((Integer) leftChildList.get(0));
                        MapNode rightChildNode = nodeService.getNodeById((Integer) rightChildList.get(0));
                        Double leftHalfDistance = (leftChildNode.getLeftX() + leftChildNode.getWidth() - notYetDrawP.getLeftX()) / 2;//这里用小的减大的.搞错了,所以下面得用负号
                        Double rightHalfDistance = (notYetDrawP.getLeftX() + notYetDrawP.getWidth() - rightChildNode.getLeftX()) / 2;
                        Line parNodeLineleft = new Line(notYetDrawP.getLeftX(), notYetDrawP.getTopY() + notYetDrawP.getHeight() / 2, notYetDrawP.getLeftX() + leftHalfDistance, notYetDrawP.getTopY() + notYetDrawP.getHeight() / 2);
                        parNodeLineleft.setStroke(Color.rgb(104, 151, 187));
                        parNodeLineleft.setStrokeWidth(6.0*nodeService.getSCALE()/100);
                        MindMapPane.getChildren().add(parNodeLineleft);//父节点的左延伸线
                        Line veriticalLineLeft = new Line(notYetDrawP.getLeftX() + leftHalfDistance, maxAndMinY1.get(0) + leftChildNode.getHeight() / 2, notYetDrawP.getLeftX() + leftHalfDistance, maxAndMinY1.get(1) + leftChildNode.getHeight() / 2);
                        veriticalLineLeft.setStroke(Color.rgb(104, 151, 187));
                        veriticalLineLeft.setStrokeWidth(6.0*nodeService.getSCALE()/100);
                        MindMapPane.getChildren().add(veriticalLineLeft);//竖线of左边子节点的
                        Line parNodeLine = new Line(notYetDrawP.getLeftX() + notYetDrawP.getWidth(), notYetDrawP.getTopY() + notYetDrawP.getHeight() / 2, notYetDrawP.getLeftX() + notYetDrawP.getWidth() - rightHalfDistance, notYetDrawP.getTopY() + notYetDrawP.getHeight() / 2);
                        parNodeLine.setStroke(Color.rgb(104, 151, 187));
                        parNodeLine.setStrokeWidth(6.0*nodeService.getSCALE()/100);
                        MindMapPane.getChildren().add(parNodeLine);//父节点的右延伸线
                        Line veriticalLineRight = new Line(notYetDrawP.getLeftX() + notYetDrawP.getWidth() - rightHalfDistance, maxAndMinY2.get(0) + rightChildNode.getHeight() / 2, notYetDrawP.getLeftX() + notYetDrawP.getWidth() - rightHalfDistance, maxAndMinY2.get(1) + rightChildNode.getHeight() / 2);
                        veriticalLineRight.setStroke(Color.rgb(104, 151, 187));
                        veriticalLineRight.setStrokeWidth(6.0*nodeService.getSCALE()/100);
                        MindMapPane.getChildren().add(veriticalLineRight);//竖线of右边子节点的
                        break;
                    }

                }
            }
            if (notYetDrawPStack.empty() == false) {
                if (notYetDrawP.getLeftX() > notYetDrawPStack.peek().getLeftX()&&layoutString=="default") {
                    layout = -1;
                } else if (notYetDrawP.getLeftX() < notYetDrawPStack.peek().getLeftX()&&layoutString=="default") {
                    layout = 1;
                }
            }
        }
    }

    //    右布局画线
    private ArrayList<Double> rightDrawMapLine(ArrayList<Integer> childList) {
        Double MAXY, MINY;
        MAXY = nodeService.getNodeById(childList.get(0)).getTopY();
        MINY = nodeService.getNodeById(childList.get(0)).getTopY();
        for (int i = 0; i < childList.size(); i++) {
            MapNode currentNode = nodeService.getNodeById(childList.get(i));
            if (!currentNode.getVisible())
                continue;
            if (currentNode.getTopY() > MAXY) {
                MAXY = currentNode.getTopY();
            }
            if (currentNode.getTopY() < MINY) {
                MINY = currentNode.getTopY();
            }
            if (nodeService.getChildrenNodeByNode(currentNode).size() != 0)
                notYetDrawPStack.push(currentNode);
            MapNode parentNode = nodeService.getParentNodeByNode(currentNode);
            Line nodeLine = new Line(currentNode.getLeftX() - (currentNode.getLeftX() - (parentNode.getLeftX() + parentNode.getWidth())) / 2, currentNode.getTopY() + currentNode.getHeight() / 2, currentNode.getLeftX(), currentNode.getTopY() + currentNode.getHeight() / 2);
            nodeLine.setStroke(Color.rgb(104, 151, 187));
            nodeLine.setStrokeWidth(6.0*nodeService.getSCALE()/100);
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
            if (!currentNode.getVisible())
                continue;
            if (currentNode.getTopY() > MAXY) {
                MAXY = currentNode.getTopY();
            }
            if (currentNode.getTopY() < MINY) {
                MINY = currentNode.getTopY();
            }
            if (nodeService.getChildrenNodeByNode(currentNode).size() != 0 && currentNode.getVisible())
                notYetDrawPStack.push(currentNode);
            MapNode parentNode = nodeService.getParentNodeByNode(currentNode);
            Line nodeLine = new Line(currentNode.getLeftX() + currentNode.getWidth(), currentNode.getTopY() + currentNode.getHeight() / 2, currentNode.getLeftX() + currentNode.getWidth() + (parentNode.getLeftX() - currentNode.getLeftX() - currentNode.getWidth()) / 2, currentNode.getTopY() + currentNode.getHeight() / 2);
            nodeLine.setStroke(Color.rgb(104, 151, 187));
            nodeLine.setStrokeWidth(6.0*nodeService.getSCALE()/100);
            MindMapPane.getChildren().add(nodeLine);
        }
        ArrayList<Double> maxAndMinY = new ArrayList<>();
        maxAndMinY.add(MAXY);
        maxAndMinY.add(MINY);
        return maxAndMinY;
    }

    public void drawextraLine(MapNode startNode, MapNode targetNode) {
        MoveTo moveTo = new MoveTo();
        moveTo.setX(startNode.getLeftX() + startNode.getWidth() / 2);//起始坐标
        moveTo.setY(startNode.getTopY());
        CubicCurveTo cubicTo = new CubicCurveTo();
        cubicTo.setControlX1(startNode.getLeftX() + startNode.getWidth() / 2 + 100);
        cubicTo.setControlY1(startNode.getTopY() - 250);
        cubicTo.setControlX2(startNode.getLeftX() + startNode.getWidth() / 2 + 100);
        cubicTo.setControlY2(startNode.getTopY() - 250);
        cubicTo.setX(targetNode.getLeftX() + targetNode.getWidth() / 2);//终点坐标
        cubicTo.setY(targetNode.getTopY());
        Path path = new Path(moveTo, cubicTo);
        path.setStroke(Color.rgb(255, 83, 140));
        path.setStrokeWidth(5);
        path.setStrokeLineCap(StrokeLineCap.ROUND);
        path.getStrokeDashArray().addAll(15d, 15d, 15d, 15d, 15d);
        path.setStrokeDashOffset(10);
        MindMapPane.getChildren().remove(Controller.path);
        MindMapPane.getChildren().add(path);
        Controller.extraLineButPressed = false;
    }

    public TextField nodeInputRectangle(MapNode showNode) {
        TextField text = new TextField();//节点输入框
        text.setText(showNode.getContent());
        text.setLayoutX(showNode.getLeftX());
        text.setLayoutY(showNode.getTopY());
        text.setPrefHeight(showNode.getHeight());
        text.setPrefWidth(showNode.getWidth());
        text.setPrefColumnCount(1);
        text.setStyle("-fx-font-size: 15px;-fx-border-radius: 15;\n" +
                "    -fx-background-radius: 15; -fx-font-weight: bold;");
        text.setVisible(true);
        text.setFont(new Font(10));
        return text;
    }

    public Rectangle nodeSelectedRectangle(MapNode showNode) {
        Rectangle nodeRectangle = new Rectangle(showNode.getWidth() + 10, showNode.getHeight() + 10);//节点被选中的浮出框
        nodeRectangle.setFill(Color.rgb(84, 87, 83));
        nodeRectangle.setLayoutY(showNode.getTopY() - 5);
        nodeRectangle.setLayoutX(showNode.getLeftX() - 5);
        nodeRectangle.setId("abc");
        nodeRectangle.setStroke(Color.rgb(73, 156, 84));//边框颜色
        nodeRectangle.setStrokeWidth(5);
        nodeRectangle.getStrokeDashArray().addAll(15d, 15d, 15d, 15d, 15d);
        nodeRectangle.setStrokeDashOffset(10);
        nodeRectangleList.put(showNode.getId(), nodeRectangle);
        if (!showNode.getSelected())//节点被选中即显示浮出框
            nodeRectangle.setVisible(false);
        else
            nodeRectangle.setVisible(true);
        return nodeRectangle;
    }

    private Button storeButInit(MapNode showNode) {
        Button storeBut = new Button();
        storeBut.setStyle("-fx-background-color: rgb(255, 199, 0);\n" +
                "    -fx-border-radius: 10px;\n" +
                "    -fx-background-radius: 10px;\n" +
                "    -fx-effect: dropshadow(one-pass-box, #72b9da, 10.0,0, 0, 0);\n" +
                "    -fx-pref-width: 20px;\n" +
                "    -fx-pref-height: 20px;");
        storeBut.setLayoutX(showNode.getLeftX() + showNode.getWidth() + 10);
        storeBut.setLayoutY(showNode.getTopY() + showNode.getHeight() / 2);
        storeBut.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (showNode.getChildrenId().size() != 0) {
                    if (showNode.getSonDisplay()) {
                        showNode.setSonDisplay(false);
                        for (Integer sonNodeID :
                                showNode.getChildrenId()) {
                            nodeService.setVisible(sonNodeID, false);
                        }
                    } else {
                        showNode.setSonDisplay(true);
                        for (Integer sonNodeID :
                                showNode.getChildrenId()) {
                            nodeService.setVisible(sonNodeID, true);
                        }
                    }
                    treeService.updateLayout();
                    showMap();
                }
            }
        });
        return storeBut;
    }

    private void treeView() {
        MapNode rootNode = treeService.getRootNode();
        TreeView<String> treeView = (TreeView) root.lookup("#treeView");
        HashMap<Integer, TreeItem> searchMap = new HashMap<>();
        nodeService.getNodeList().forEach((key, value) -> {
            TreeItem<String> item = new TreeItem<>(value.getContent());
            item.setExpanded(true);
            searchMap.put(value.getId(), item);
        });
        nodeService.getNodeList().forEach((key, value) -> {
            TreeItem item = searchMap.get(value.getId());
            if (value.getParentId() != null) {
                TreeItem parentItem = searchMap.get(value.getParentId());
                parentItem.getChildren().add(item);
            }
        });
        treeView.setRoot(searchMap.get(1));
    }
    private Button annotaionBut(MapNode showNode){
        ImageView imageView = new ImageView(new Image("image/概要.png"));
        imageView.setFitWidth(5);
        imageView.setFitHeight(5);
        Button annotaionBut = new Button(new String(),imageView);
        annotaionBut.setId("#annotaionBut");
        annotaionBut.setLayoutY(showNode.getTopY()+35);
        annotaionBut.setLayoutX(showNode.getLeftX()+showNode.getContent().length()*15+15);
        return annotaionBut;
    }
    private TextArea noteText(MapNode showNode){
        TextArea textField = new TextArea();
        textField.setText(showNode.getNote());
        textField.setPrefHeight(nodeService.getSCALE()*2);
        textField.setPrefWidth(nodeService.getSCALE()*4);
        textField.setLayoutX(showNode.getLeftX()-nodeService.getSCALE());
        textField.setLayoutY(showNode.getTopY()-10-nodeService.getSCALE()*2);
        return textField;
    }
}
