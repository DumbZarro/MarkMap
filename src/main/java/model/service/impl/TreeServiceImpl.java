package model.service.impl;

import model.pojo.MapNode;
import model.pojo.MapTree;
import model.service.TreeService;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * Description:
 */
public class TreeServiceImpl implements TreeService {
    private MapTree tree;   // 包含布局信息和根节点信息
    private ArrayBlockingQueue<MapNode> CounterQueue;   // 计算块大小时用的队列
    private MapNode rootNode;   //根节点
    private NodeServiceImpl nodeService;

    public MapNode getRootNode() {
        return rootNode;
    }

    public TreeServiceImpl() {
        this.nodeService = new NodeServiceImpl();
        //测试用例,右布局
        this.tree = new MapTree(1);
        this.rootNode = nodeService.getNodeById(tree.getRootId());
        this.rootNode.setSelected(true);
    }

    public TreeServiceImpl(MapTree tree) {
        this.nodeService = new NodeServiceImpl();
        this.tree = tree;
        this.rootNode = nodeService.getNodeById(tree.getRootId());
        this.rootNode.setSelected(true);
    }

    public  TreeServiceImpl(NodeServiceImpl nodeService){
        this.nodeService = nodeService;
        this.tree  = new MapTree(1);
        this.rootNode = nodeService.getNodeList().get(1);
        this.rootNode.setSelected(true);
    }

    public MapTree getTree() {
        return tree;
    }

    public void setTree(MapTree tree) {
        this.tree = tree;
    }


    @Override
    public int drawMap(MapTree tree) {
        return 0;
    }

    public void initBlockCounter() {//找出叶子节点并blockSize赋初值
        CounterQueue = new ArrayBlockingQueue<MapNode>(20);
        for (MapNode node : nodeService.getNodeList().values()) {
            node.setCounter(0);
            if (node.getId()==getTree().getRootId()){ // 根节点初始size永远为0
                node.setBlockSize(0);
                continue;
            }
            // 子节点不可见而自己可见(叶子节点)
            if (!node.getSonDisplay()&&node.getVisible()) {
                // TODO 封装合适的间距
                node.setBlockSize(60);//这里的大小决定间距的大小
                CounterQueue.add(node);
            }
            else {
                node.setBlockSize(0);
            }

        }
    }

    public void toCountBlock() {
        initBlockCounter();
        while (!CounterQueue.isEmpty()) {
            MapNode node = CounterQueue.poll();
            MapNode parentNode = nodeService.getNodeList().get(node.getParentId());
            parentNode.setBlockSize(parentNode.getBlockSize() + node.getBlockSize());
            parentNode.setCounter(parentNode.getCounter() + 1);
            if (parentNode.getCounter() == parentNode.getChildrenId().size()) {
                if (tree.getRootId().equals(parentNode.getId())) {
                    break;
                }
                CounterQueue.offer(parentNode);
            }
        }
    }

    public void setLayout() {
        toCountBlock();
        // TODO 计算根节点位置
        // 设置根节点位置
        rootNode.setLeftX(400.);
        rootNode.setTopY(300.);
        switch (tree.getLayout()) {
            case "default" -> defaultLayout();
            case "right" -> rightLayout();
            case "left" -> leftLayout();
        }
    }

    private void rightLayout() {
        computeAllCoordinate(rootNode.getId(), 1);
    }

    private void leftLayout() {
        computeAllCoordinate(rootNode.getId(), -1);
    }

    private void defaultLayout() {
        rightLayout();
        // TODO 中心分布 划分中心节点
//        MapNode right =  new MapNode();
//        computeAllCoordinate(rootNode.getId());
    }

    private void computeAllCoordinate(Integer now_id, Integer flag) { //根据flag动态生成左树或者右树(从中心节点向外计算坐标)
        MapNode nowNode = nodeService.getNodeById(now_id);
        double childX = nowNode.getLeftX() + flag * nowNode.getWidth() + 50;  //x轴 flag  左树累加 右树累减
        // 第一个子节点的位置
        Integer delta = nowNode.getBlockSize();
        Double nowY = nowNode.getTopY();
        if(nowNode.getChildrenId()==null){     //叶子节点直接返回,不继续递推
            return;
        }
        for (Integer childId : nowNode.getChildrenId()) {
            MapNode childNode = nodeService.getNodeById(childId);
            //算x
            childNode.setLeftX(childX);
            //算y
            delta -= childNode.getBlockSize();
            Double y = nowY + delta;
            childNode.setTopY(y);
            delta -= childNode.getBlockSize();
            //递推调用
            computeAllCoordinate(childId, flag);
        }

        // 其余子节点的位置
    }

    @Override
    public int changeLayout() {
        return 0;
    }

    @Override
    public int saveToCloud() {
        return 0;
    }

    @Override
    public int saveToLocal() {
        return 0;
    }

    @Override
    public int readFromCloud() {
        return 0;
    }

    @Override
    public int readFromLocal() {
        return 0;
    }

    @Override
    public int exportAsImage() {
        return 0;
    }
}
