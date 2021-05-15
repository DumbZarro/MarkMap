package model.service.impl;

import model.dao.impl.MindMapDaoImpl;
import model.pojo.MapNode;
import model.service.NodeService;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Description:
 */
public class NodeServiceImpl implements NodeService {
    private HashMap<Integer, MapNode> nodeList = new HashMap<Integer, MapNode>();
    MindMapDaoImpl dataBaseService;
    private Integer SCALE = 100;

    public NodeServiceImpl() {
        MapNode centerNode = new MapNode(1, getDefaultHeight(), getDefaultWidth(), getSCALE());
        centerNode.setContent("中心节点");
        nodeList.put(centerNode.getId(), centerNode);
    }

    public NodeServiceImpl(MindMapDaoImpl dataBaseService) {// 数据库服务的初始化方式
        this.dataBaseService = dataBaseService;
        if (dataBaseService.getColl().countDocuments() == 0) {
            MapNode centerNode = new MapNode(1, getDefaultHeight(), getDefaultWidth(), getSCALE());
            centerNode.setContent("中心节点");
            nodeList.put(centerNode.getId(), centerNode);
        } else {
            dataBaseService.loadMap(this.nodeList);
            this.SCALE = nodeList.get(1).getScale();
            System.out.println(this.nodeList);
        }

    }

    public HashMap<Integer, MapNode> getNodeList() {
        return nodeList;
    }

    public Integer getSCALE() {
        return SCALE;
    }

    public void setSCALE(Integer SCALE) {
        this.SCALE = SCALE;
    }

    public void setNodeList(HashMap<Integer, MapNode> nodeList) {
        this.nodeList = nodeList;
    }

    private void addNodeToMap(Integer id, MapNode node) {
        nodeList.put(id, node);
    }


    private void addParentToSon(Integer parentId, Integer sonId) {
        MapNode parent = getNodeById(parentId);
        parent.setSonDisplay(true);
        parent.getChildrenId().add(sonId);//把子节点添加到父节点的子节点列表中

        MapNode children = getNodeById(sonId);
        children.setParentId(parentId);//为子节点增加父节点id
        children.setSonDisplay(false);
    }

    @Override
    public void addNode(Integer parentNodeId, Integer sonNodeId, MapNode sonNode) {
        addNodeToMap(sonNodeId, sonNode);
        addParentToSon(parentNodeId, sonNodeId);
    }

    @Override
    public void deleteNode(Integer id) {
        MapNode parentNode = getParentNodeById(id);
        ArrayList<Integer> childrenIdList = parentNode.getChildrenId();//删除父节点记录
        childrenIdList.remove(id);

        if (childrenIdList.size() == 0) {
            parentNode.setSonDisplay(false);
            nodeList.remove(id);
            return;
        }
        for (Integer childrenId : getChildrenIdById(id)) {//循环删除子节点
            releaseNode(childrenId);
        }
        nodeList.remove(id);//最后再删除自己
        System.out.println(nodeList);
    }

    public void releaseNode(Integer id) {
        if (getChildrenIdById(id).size() == 0) {
            nodeList.remove(id);
            return;
        }
        for (Integer childrenId : getChildrenIdById(id)) {//循环删除子节点
            releaseNode(childrenId);
        }
        nodeList.remove(id);//最后再删除自己
    }

    @Override
    public MapNode getNodeById(Integer id) {
        return nodeList.get(id);
    }


    //1. get id by id
    @Override
    public ArrayList<Integer> getChildrenIdById(Integer id) {
        if (getNodeById(id).getChildrenId() == null) {
            ArrayList<Integer> childNodeList = new ArrayList<Integer>();
            getNodeById(id).setChildrenId(childNodeList);
        }
        return getNodeById(id).getChildrenId();
    }

    @Override
    public Integer getParentIdById(Integer id) {
        return getNodeById(id).getParentId();
    }


    //2. get node by node
    public ArrayList<MapNode> getChildrenNodeByNode(MapNode node) {
        ArrayList<MapNode> childNodeList = new ArrayList<MapNode>();
        for (Integer id : node.getChildrenId()) {
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


    public Integer getDefaultHeight() {
        return SCALE;
    }

    public Integer getDefaultWidth() {
        return getDefaultHeight() * 2;
    }

    public void changeNodeSize(Integer scale) {
        setSCALE(scale);
        for (MapNode node : nodeList.values()) {
            Integer preScale = node.getScale();
            Double nowHeight = (node.getHeight() * scale) / preScale;
            node.setHeight(nowHeight);
            Double nowWidth = (node.getWidth() * scale) / preScale;
            node.setWidth(nowWidth);
            node.setScale(scale);
        }
    }

    public void setVisible(Integer id, boolean flag) {
        MapNode node = getNodeById(id);
        node.setVisible(flag);

        ArrayList<Integer> child = node.getChildrenId();
        if (child.size() == 0) {
            return;
        }
        for (Integer son : node.getChildrenId()) {
            setVisible(son, flag);
        }
    }

}
