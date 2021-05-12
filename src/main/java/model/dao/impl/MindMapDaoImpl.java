package model.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import model.dao.BaseDao;
import model.pojo.MapNode;
import model.service.impl.NodeServiceImpl;
import model.utils.JdbcUtils;
import org.bson.Document;

import java.util.HashMap;

/**
 * Description:
 */
public class MindMapDaoImpl extends BaseDao {
    private MongoDatabase db;
    private MongoCollection<Document> coll;

    public MindMapDaoImpl() {//一个用户对应一个数据库 一个collection对应一个思维导图
//        this.coll = JdbcUtils.getCollection("mindMap","map");//云端
        this.coll = JdbcUtils.getCollection("testCollection","myCollection");
    }


    public MindMapDaoImpl(String dbName) {
        this.coll = JdbcUtils.getCollection(dbName,"map");//默认导图
    }

    public MindMapDaoImpl(String dbName,String collName) {
        this.coll = JdbcUtils.getCollection(dbName,collName);//默认导图
    }

    public void setDb(MongoDatabase db) {
        this.db = db;
    }

    public MongoCollection<Document> getColl() {
        return coll;
    }

    public void setColl(MongoCollection<Document> coll) {
        this.coll = coll;
    }

    public MongoDatabase getDb() {
        return db;
    }

    public void saveMap(HashMap<Integer, MapNode> nodeList){
        if(coll.countDocuments()!=0){   //先清空再重新加入
            coll.drop();
        }
        // 遍历加入
        for (Integer id:nodeList.keySet()) {
            MapNode node = nodeList.get(id);
            Document document = Document.parse(JSONObject.toJSONString(node));
            coll.insertOne(document);
        }
    }

    public void findAll() {
        // 检索所有文档
        System.out.println("检索所有文档:");
        FindIterable<Document> findIterable = coll.find();  //获取迭代器FindIterable<Document>
//        MongoCursor<Document> mongoCursor = findIterable.iterator();    //获取游标MongoCursor<Document>
//        while (mongoCursor.hasNext()) {     //通过游标遍历检索出的文档集合
//            System.out.println(mongoCursor.next());
//        }
        System.out.println("去除 _id");
        for (Document document : findIterable) {     // 优化
            JSONObject jsonObject= JSONObject.parseObject(document.toJson());
            jsonObject.remove("_id");
            Document document2 = Document.parse(jsonObject.toJSONString());
            System.out.println(document2);
        }

    }

    public void loadMap(HashMap<Integer, MapNode> nodeList){
        System.out.println("Document ---> JavaBean");
        FindIterable<Document> findIterable = coll.find();
        for (Document document : findIterable) {
            JSONObject jsonObject= JSONObject.parseObject(document.toJson());
            jsonObject.remove("_id");
            Document document2 = Document.parse(jsonObject.toJSONString());
            MapNode node = JSONObject.parseObject(document2.toJson(), MapNode.class);
            nodeList.put(node.getId(), node);
        }
        System.out.println(JSONObject.toJSONString(nodeList));
    }





}
