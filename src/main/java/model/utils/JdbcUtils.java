package model.utils;

import com.mongodb.*;
import com.mongodb.client.*;
import com.mongodb.MongoClientSettings;
import com.mongodb.ConnectionString;
import com.mongodb.ServerAddress;
import com.mongodb.MongoCredential;
import model.pojo.MapNode;
import org.bson.Document;

import java.util.Arrays;

/**
 * Description: 连接MongoDB
 */
public class JdbcUtils {
    private static MongoClient client = null;

    static {
        //连接数据库
        // 用户认证信息
        final String uriString = "mongodb://localhost:27017/testCollection";
        client = MongoClients.create(uriString);//url版

//        String user="dumbzarro"; // 用户名
//        String database="mindMap"; // 要连接的数据库
//        char[] password="dumbzarro".toCharArray(); // 密码(字符列表)
//        ServerAddress address = new ServerAddress("1.15.135.81",27017);//服务器地址
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
    public static MongoDatabase getDatabase(String dbName){
        return client.getDatabase(dbName);
    }
    //获取数据库中的集合
    public static MongoCollection<Document> getCollection(String dbName, String collName){
        MongoDatabase database =getDatabase(dbName);
        return database.getCollection(collName);
    }
}
