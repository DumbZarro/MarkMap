package model.dao.impl;

import com.mongodb.client.MongoDatabase;
import model.pojo.MapNode;
import model.service.impl.NodeServiceImpl;
import model.service.impl.TreeServiceImpl;
import model.utils.JdbcUtils;
import org.junit.Test;
import view.Generator;

import java.util.HashMap;

import static org.junit.Assert.*;

/**
 * Description:
 */
public class MindMapDaoImplTest {
    static NodeServiceImpl nodeService = new NodeServiceImpl();
    static TreeServiceImpl treeService = new TreeServiceImpl(nodeService);

    static{
        MapNode parentNode = treeService.getRootNode();
        // 创建新的节点,以及找到父节点
        Integer parentId = treeService.getTree().getRootId();
        MapNode sonNode0 = new MapNode(2,nodeService.getDefaultHeight(),nodeService.getDefaultWidth());
        MapNode sonNode1 = new MapNode(3,nodeService.getDefaultHeight(),nodeService.getDefaultWidth());
        MapNode sonNode2 = new MapNode(4,nodeService.getDefaultHeight(),nodeService.getDefaultWidth());
        MapNode gransonNode =  new MapNode(5,nodeService.getDefaultHeight(),nodeService.getDefaultWidth());
        MapNode gransonNode1 =  new MapNode(6,nodeService.getDefaultHeight(),nodeService.getDefaultWidth());
        sonNode0.setContent("66666");
        sonNode1.setContent("22222");
        sonNode2.setContent("333412433");

        // 添加节点
        nodeService.addNode(parentId,sonNode0.getId(),sonNode0);
        nodeService.addNode(parentId,sonNode1.getId(),sonNode1);
        nodeService.addNode(parentId,sonNode2.getId(),sonNode2);
        nodeService.addNode(sonNode2.getId(),gransonNode.getId(),gransonNode);

        // 重新计算坐标
        treeService.getTree().setLayout("right");
        nodeService.addNode(sonNode2.getId(),gransonNode1.getId(),gransonNode1);
        treeService.updateLayout();
    }

    @Test
    public void saveMap() {
        MongoDatabase database = JdbcUtils.getDatabase("testCollection");
        MindMapDaoImpl db = new MindMapDaoImpl("testCollection","myCollection");
        System.out.println(database.getName());
        System.out.println(database.getCollection("myCollection").countDocuments());

        db.saveMap(nodeService.getNodeList());
        System.out.println("存储结束");
        System.out.println(database.getCollection("myCollection").countDocuments());
    }

    @Test
    public void findAll() {
        MongoDatabase database = JdbcUtils.getDatabase("testCollection");
        MindMapDaoImpl db = new MindMapDaoImpl("testCollection","myCollection");
        System.out.println(database.getName());
        System.out.println(database.getCollection("myCollection").countDocuments());

        db.findAll();
    }

    @Test
    public void loadMap() {
        MongoDatabase database = JdbcUtils.getDatabase("testCollection");
        MindMapDaoImpl db = new MindMapDaoImpl("testCollection","myCollection");
        System.out.println(database.getName());
        System.out.println(database.getCollection("myCollection").countDocuments());

        HashMap<Integer, MapNode> nodeList = new HashMap<>();
        db.loadMap(nodeList);
    }
}