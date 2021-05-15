package model.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;


public class ControllerUtils {
    /**
     * @Title: jumpFxml
     * @Description: 跳转模版
     * @param targetFile
     * @param pressedButton
     * @return: void
     */
    public void jumpFxml(String targetFile,Button pressedButton,boolean closeParent) {
        //获取目标界面
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("./source/" + targetFile));

        } catch (IOException e) {
            e.printStackTrace();
        }
        // 创建新舞台
        Scene scene = new Scene(root);
        Stage mainStage = new Stage();
        mainStage.setTitle("思维导图");
        mainStage.setScene(scene);// 设置界面

        if(closeParent) {//showType为True时
            // 删掉旧界面
            Stage stage = (Stage) pressedButton.getScene().getWindow();// 获取按钮所在的舞台
            stage.close();
            mainStage.show();
        }else {
            mainStage.showAndWait();
        }
    }

}
