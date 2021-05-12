package model.service.impl;

import model.pojo.MapNode;
import model.pojo.MapTree;
import model.service.TreeService;

import java.util.Collections;
import java.util.Comparator;
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
            //每个节点都初始化
            node.setCounter(0);
            node.setBlockHeight(0);
            node.setBlockWidth(node.getWidth().intValue());//初始块宽为自己的宽度

            if (node.getId()==getTree().getRootId()){ // 根节点初始size永远为0
                continue;
            }
            // 子节点不可见而自己可见(叶子节点)
            if (!node.getSonDisplay()&&node.getVisible()) {
                Integer Y_bias= nodeService.getSCALE()/5;//设置合适的纵向间距
                Integer defaultSize = nodeService.getSCALE()+Y_bias;
                node.setBlockHeight(defaultSize);//这里的大小决定纵向间距的大小
                CounterQueue.add(node);
            }


        }
    }

    public void toCountBlock() {
        initBlockCounter();
        while (!CounterQueue.isEmpty()) {
            MapNode node = CounterQueue.poll();
            MapNode parentNode = nodeService.getNodeList().get(node.getParentId());
            parentNode.setBlockHeight(parentNode.getBlockHeight() + node.getBlockHeight());//计算块高

            if(parentNode.getCounter()==0){// 父节点只计算一块宽,第一次遍历到其子节点的时候计算
                Integer X_bias = nodeService.getSCALE()/2; //设置合适的横向间距 TODO 统一X_bias
                // 高级实现Comparetor的高级写法
                MapNode maxChild = Collections.max(nodeService.getChildrenNodeByNode(parentNode), Comparator.comparingInt(MapNode::getBlockWidth));
//            Object obj = Collections.max(nodeService.getChildrenNodeByNode(parentNode), (o1, o2) -> o1.getBlockWidth()-o2.getBlockWidth());

//            Object obj = Collections.max(nodeService.getChildrenNodeByNode(parentNode), new Comparator<MapNode>() {
//                @Override
//                public int compare(MapNode o1, MapNode o2) {
//                    return o1.getBlockWidth()-o2.getBlockWidth();
//                }
//            });
                parentNode.setBlockWidth(parentNode.getBlockWidth() + maxChild.getBlockWidth()+X_bias);//计算块宽
            }


            parentNode.setCounter(parentNode.getCounter() + 1);
            if (parentNode.getCounter() == parentNode.getChildrenId().size()) {
                if (tree.getRootId().equals(parentNode.getId())) {
                    break;
                }
                CounterQueue.offer(parentNode);
            }
        }
    }

    public void updateLayout() {
        toCountBlock();
        // 设置根节点位置
        rootNode.setLeftX(rootNode.getBlockWidth().doubleValue());//TODO 寻找合适的横坐标值
        rootNode.setTopY(rootNode.getBlockHeight().doubleValue());
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

        if(nowNode.getChildrenId()==null){     //叶子节点直接返回,不继续递推
            return;
        }
        if(now_id==rootNode.getId()){
            nowNode.setLevel(0);
        }

        // 设定所有子节点的横坐标
        Integer X_bias = nodeService.getSCALE()/2; //设置合适的横向间距
        double childX = nowNode.getLeftX() + flag * (nowNode.getWidth() + X_bias);  //x轴 通过flag实现:左树累加、右树累减

        // 第一个子节点的位置
        Integer delta = nowNode.getBlockHeight()/2;
        Double nowY = nowNode.getTopY();

        for (Integer childId : nowNode.getChildrenId()) {
            MapNode childNode = nodeService.getNodeById(childId);
            //算x
            childNode.setLeftX(childX);
            //算y
            delta -= childNode.getBlockHeight()/2;
            Double y = nowY + delta;
            childNode.setTopY(y);
            delta -= childNode.getBlockHeight()/2;
            //算level
            childNode.setLevel(nowNode.getLevel()+1);
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
