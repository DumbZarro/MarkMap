package model.service.impl;

import model.pojo.MapNode;

/**
 * Description: 测试TreeService
 */
public class TreeServiceImplTest {

    // 创建服务
    NodeServiceImpl nodeService = new NodeServiceImpl();
    TreeServiceImpl treeService = new TreeServiceImpl(nodeService);

    @org.junit.Test
    public void setLayout() {
        // 创建新的节点,以及找到父节点
        Integer parentId = treeService.getTree().getRootId();
        MapNode sonNode = new MapNode(2,nodeService.getDefaultHeight(),nodeService.getDefaultWidth());
        MapNode sonNode1 = new MapNode(3,nodeService.getDefaultHeight(),nodeService.getDefaultWidth());
        sonNode.setContent("两只老婆爱跳舞");
        sonNode1.setContent("fxhSB");
        // 添加节点
        nodeService.addNode(parentId, sonNode.getId(), sonNode);
        nodeService.addNode(parentId, sonNode1.getId(), sonNode1);
        // 重新计算坐标
        treeService.updateLayout();
    }

}