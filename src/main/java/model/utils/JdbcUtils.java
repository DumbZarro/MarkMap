package model.utils;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

/**
 * Description: 连接MongoDB
 */
public class JdbcUtils {
    private static MongoClient client = null;
    private static final String dbName = ViewUtils.username;  //从导入界面获取连接的数据库名

    static {
        //连接数据库
        // 用户认证信息
//        final String uriString = "mongodb://localhost:27017";
        final String uriString = "mongodb://mapUser:201925310306@mongodb.dumbzarro.top:27017/" + dbName;//要预先填数据库,否则认证会失败
        client = MongoClients.create(uriString);//url版

        //我*@$%&#^&$%^&$,下面这个配置不行,直接有上面的url就行,我!#$!%$@#$%^#!@#$
//        String user="dumbzarro"; // 用户名
//        String database="username"; // 要连接的数据库
//        char[] password="dumbzarro".toCharArray(); // 密码(字符列表)
//        ServerAddress address = new ServerAddress("mongodb.dumbzarro.top",27017);//服务器地址
//
//        MongoCredential credential = MongoCredential.createCredential(user,database,password);
//
//        MongoClientSettings settings = MongoClientSettings.builder()
//                .credential(credential)
//                .applyToSslSettings(builder -> builder.enabled(true))
//                .applyToClusterSettings(builder ->
//                        builder.hosts(Arrays.asList(address)))
//                .build();
//        client = MongoClients.create(settings);
//        System.out.println("数据库用户初始化!");

    }

    //获取数据库
    public static MongoDatabase getDatabase() {
        return client.getDatabase(dbName);
    }

    //获取数据库中的集合
    public static MongoCollection<Document> getCollection(String collName) {
        MongoDatabase database = client.getDatabase(dbName);
        return database.getCollection(collName);
    }
}
