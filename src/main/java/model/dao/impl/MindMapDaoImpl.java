package model.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import model.dao.BaseDao;
import model.pojo.MapNode;
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
        this.coll = JdbcUtils.getCollection("map"); //默认这个导图
    }

    public MindMapDaoImpl(String collName) {
        this.coll = JdbcUtils.getCollection(collName);//默认导图
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

    public void saveMapToCloud(HashMap<Integer, MapNode> nodeList){
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
        System.out.println("去除 _id");
        for (Document document : findIterable) {     // 优化
            JSONObject jsonObject= JSONObject.parseObject(document.toJson());
            jsonObject.remove("_id");
            Document document2 = Document.parse(jsonObject.toJSONString());
            System.out.println(document2);
        }

    }

    public void loadMap(HashMap<Integer, MapNode> nodeList){
//        System.out.println("Document ---> JavaBean");
        FindIterable<Document> findIterable = coll.find();
        for (Document document : findIterable) {
            JSONObject jsonObject= JSONObject.parseObject(document.toJson());
            jsonObject.remove("_id");
            Document document2 = Document.parse(jsonObject.toJSONString());
            MapNode node = JSONObject.parseObject(document2.toJson(), MapNode.class);
            nodeList.put(node.getId(), node);
        }
//        System.out.println(JSONObject.toJSONString(nodeList));
    }

    // TODO 本地读取
//    public void readMapFromLocal()throws IOException{
//        JFileChooser fileChooser = new JFileChooser("D:\\");
//
//        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
//
//        int returnVal = fileChooser.showOpenDialog(fileChooser);
//
//        if (returnVal == JFileChooser.APPROVE_OPTION) {
//            String filePath = fileChooser.getSelectedFile().getAbsolutePath();//这个就是你选择的文件夹的
//            System.out.println(filePath);
//            File file = new File(filePath);
//            InputStream in = new FileInputStream(file);
//            in.readAllBytes();
//        }
//    }
    // TODO 本地存储
//    public void saveMapToLocal()throws IOException{
//        JFileChooser fileChooser = new JFileChooser("D:\\");
//
//        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
//
//        int returnVal = fileChooser.showOpenDialog(fileChooser);
//
//        if (returnVal == JFileChooser.APPROVE_OPTION) {
//            String filePath = fileChooser.getSelectedFile().getAbsolutePath();//这个就是你选择的文件夹的
//            System.out.println(filePath);
//            File file = new File(filePath);
//            OutputStream out = new FileOutputStream(file);
//        }
//    }
}
