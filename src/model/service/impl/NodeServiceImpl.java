package model.service.impl;

import model.pojo.MapNode;
import model.service.NodeService;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Description:
 */
public class NodeServiceImpl implements NodeService {
    private HashMap<Integer, MapNode> nodeList;

    public NodeServiceImpl() {
        nodeList = new HashMap<Integer, MapNode>();
        MapNode testNode = new MapNode(1,0,0);
        testNode.setContent("中心节点");
        nodeList.put(testNode.getId(), testNode);
    }

    public NodeServiceImpl(HashMap<Integer, MapNode> nodeList) {
        this.nodeList = nodeList;
    }

    public HashMap<Integer, MapNode> getNodeList() {
        return nodeList;
    }

    public void setNodeList(HashMap<Integer, MapNode> nodeList) {
        this.nodeList = nodeList;
    }

    private void addNodeToMap(Integer id, MapNode node) {
        nodeList.put(id, node);
    }


    private void addParentToSon(Integer parentId, Integer sonId) {
        getChildrenIdById(parentId).add(sonId);
        getNodeById(sonId).setParentId(parentId);
    }

    @Override
    public void addNode(Integer parentNodeId, Integer sonNodeId, MapNode sonNode) {
        addNodeToMap(sonNodeId, sonNode);
        addParentToSon(parentNodeId, sonNodeId);
    }

    @Override
    public void deleteNode(Integer id) {
        nodeList.remove(id);//删除自己
        getChildrenIdById(getParentIdById(id)).remove(id);//删除父节点记录
        for (Integer childrenId : getChildrenIdById(id)) {//循环删除子节点
            deleteNode(childrenId);
        }
        ;
    }

    @Override
    public MapNode getNodeById(Integer id) {
        return nodeList.get(id);
    }


    //1. get id by id
    @Override
    public ArrayList<Integer> getChildrenIdById(Integer id) {
        if(getNodeById(id).getChildrensId() == null) {
            ArrayList<Integer> childNodeList = new ArrayList<Integer>();
            getNodeById(id).setChildrensId(childNodeList);
        }
        return getNodeById(id).getChildrensId();
    }

    @Override
    public Integer getParentIdById(Integer id) {
        return getNodeById(id).getParentId();
    }


    //2. get node by node
    public ArrayList<MapNode> getChildrenNodeByNode(MapNode node) {
        ArrayList<MapNode> childNodeList = new ArrayList<MapNode>();
        for (Integer id : node.getChildrensId()) {
            childNodeList.add(getNodeById(id));
        }
        return childNodeList;
    }

    public MapNode getParentNodeByNode(MapNode node) {
        return getNodeById(node.getParentId());
    }


    //3. get node by id
    public ArrayList<MapNode> getChildrenNodeById(Integer id) {
        return getChildrenNodeByNode(getNodeById(id));
    }

    public MapNode getParentNodeById(Integer id) {
        return getNodeById(getParentIdById(id));
    }


}
