package model.utils;

import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.swing.*;

public class ViewUtils {
    public static String username = null;
    public static String map = null;

    public static void displayMessage(String title, String message, String buttonStirng) {
        Stage window = new Stage();
        window.setTitle(title);
        // modality要使用Modality.APPLICATION_MODEL
        window.initModality(Modality.APPLICATION_MODAL);
        window.setMinWidth(300);
        window.setMinHeight(150);

        Button button = new Button(buttonStirng);
        button.setOnAction(e -> window.close());

        Label label = new Label(message);

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, button);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        // 使用showAndWait()先处理这个窗口，而如果不处理，main中的那个窗口不能响应
        window.showAndWait();
    }

    public static void openBox() {
        Stage window = new Stage();
        window.setTitle("打开导图");
        // modality要使用Modality.APPLICATION_MODEL
        window.initModality(Modality.APPLICATION_MODAL);
        window.setMinWidth(220);
        window.setMinHeight(140);


        Label label = new Label("用户名: ");
        TextField inputID = new TextField();
        inputID.setPromptText("username");

        Label label2 = new Label("导图名: ");
        TextField inputID2 = new TextField();
        inputID2.setPromptText("map");


        Button button = new Button("打开");
        button.setOnAction((ActionEvent e) -> {
//            System.out.println(inputID.getText());
            username = inputID.getText();
            map = inputID2.getText();
            if (username == null || map == null) {
                displayMessage("警告", "非法输入人", "确认");
            } else {
//                System.out.println(username);
//                System.out.println(map);
                window.close();
            }
        });

        HBox line1 = new HBox(label, inputID);
        HBox line2 = new HBox(label2, inputID2);
        HBox line3 = new HBox(button);
        line1.setAlignment(Pos.CENTER);
        line2.setAlignment(Pos.CENTER);
        line3.setAlignment(Pos.CENTER_RIGHT);
        VBox layout = new VBox(10);
        layout.getChildren().addAll(line1, line2, line3);
        layout.setStyle("-fx-padding: 5;");

        Scene scene = new Scene(layout);
        window.setScene(scene);
        // 使用showAndWait()先处理这个窗口，而如果不处理，main中的那个窗口不能响应
        window.showAndWait();
    }

    public static String openPath() {
        JFileChooser fileChooser = new JFileChooser("D:\\");
        String filePath = "D:\\";
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int returnVal = fileChooser.showOpenDialog(fileChooser);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            filePath = fileChooser.getSelectedFile().getAbsolutePath();//这个就是你选择的文件夹的
            System.out.println(filePath);
        }
        return filePath;
    }
}
