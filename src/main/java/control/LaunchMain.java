package control;
import javafx.application.Application;
/**
 * Description: 启动javafx的Main函数,这里是整个项目的入口
 * maven导入javafx会找不到主类,这和javafx设计有关,详情请看https://my.oschina.net/tridays/blog/2222909
 */
public class LaunchMain {
    public static void main(String[] args) {
        Application.launch(Main.class, args);
    }

}
