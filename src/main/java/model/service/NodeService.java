package model.service;

import model.pojo.MapNode;

import java.util.ArrayList;

/**
 * Description:
 */
public interface NodeService {
    void addNode(Integer parentNodeId, Integer sonNodeId,MapNode sonNode);
    void deleteNode(Integer id);
    MapNode getNodeById(Integer id);
    ArrayList<Integer> getChildrenIdById(Integer id);
    Integer getParentIdById(Integer id);
//    int updateNode(Integer node);

//    ArrayList<MapNode> queryMapByString(String string);
//    MapNode getSelectedNode();//不知道给什么参数,未知如何实现
//    int unfoldChildrenList();
//    int foldChildrenList();
//    int moveNode();
//    int copyNode();
//    int pasteNode();


}
