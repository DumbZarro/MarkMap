package view;

import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import model.pojo.MapNode;
import model.service.NodeService;
import model.service.impl.NodeServiceImpl;

public class Generator {
    public void showNode(Parent root, MapNode showNode){
        AnchorPane MindMapPane = (AnchorPane)root.lookup("#mindMapPane");
        Rectangle nodeRectangle = new Rectangle(showNode.getWidth(),showNode.getHeight());
        nodeRectangle.setArcHeight(40);
        nodeRectangle.setArcWidth(40);
        nodeRectangle.setLayoutY(showNode.getTopY());
        nodeRectangle.setLayoutX(showNode.getLeftX());
        nodeRectangle.setId("abc");

        TextField text = new TextField();
        text.setText(showNode.getContent());
        text.setLayoutX(showNode.getLeftX());
        text.setLayoutY(showNode.getTopY());
        text.setPrefHeight(100);
        text.setPrefWidth(200);
        text.setStyle("-fx-font-size: 12px;-fx-border-radius: 20;\n" +
                "    -fx-background-radius: 20; ");
        text.setVisible(false);

        text.setFont(new Font(1));

        Text text1 = new Text();
        text1.setText(showNode.getContent());
        text1.setLayoutX(showNode.getLeftX()+showNode.getWidth()/4);
        text1.setLayoutY(showNode.getTopY()+showNode.getHeight()/2);
        text1.setStyle("-fx-font-size: 12px;");
        text1.setWrappingWidth(100);
        text1.setVisible(true);
        text1.setFont(new Font(1));

        text1.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                text.setVisible(true);
                text1.setVisible(false);
                showNode.setContent(text.getText());

            }
        });

        text.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                text.setVisible(false);
                text1.setVisible(true);
                text1.setText(text.getText());
            }
        });



        Color color =Color.WHITE;
        nodeRectangle.setFill(color);
        nodeRectangle.setStyle("");
        MindMapPane.getChildren().add(nodeRectangle);
        MindMapPane.getChildren().add(text);
        MindMapPane.getChildren().add(text1);

        NodeServiceImpl getUtils = new NodeServiceImpl();
        MapNode parentNode = getUtils.getParentNodeByNode(showNode);
        if (parentNode != null){
            Path path = new Path();
            path.getElements().add(new MoveTo(parentNode.getLeftX()+ parentNode.getWidth(), parentNode.getTopY()+ parentNode.getHeight()/2));
            path.getElements().add(new LineTo(showNode.getLeftX(), showNode.getTopY()+ showNode.getHeight()/2));
            MindMapPane.getChildren().add(path);
        }

    }
}
