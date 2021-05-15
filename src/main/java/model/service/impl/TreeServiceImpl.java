package model.service.impl;

import control.Main;
import model.dao.impl.MindMapDaoImpl;
import model.pojo.MapNode;
import model.pojo.MapTree;
import model.service.TreeService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Description:
 */
public class TreeServiceImpl implements TreeService {
    private MapTree tree;   // 包含布局信息和根节点信息
    private ArrayBlockingQueue<MapNode> CounterQueue;   // 计算块大小时用的队列
    private final MapNode rootNode;   //根节点
    private final NodeServiceImpl nodeService;

    public MapNode getRootNode() {
        return rootNode;
    }


    public TreeServiceImpl(NodeServiceImpl nodeService) {
        this.nodeService = nodeService;
        this.tree = new MapTree(1);
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
        CounterQueue = new ArrayBlockingQueue<MapNode>(1000);
        for (MapNode node : nodeService.getNodeList().values()) {
            //每个节点都初始化
            node.setCounter(0);
            node.setBlockHeight(0);
            node.setBlockWidth(node.getWidth().intValue());//初始块宽为自己的宽度
            node.setFlag(false);

            if (node.getId() == getTree().getRootId()) { // 根节点初始size永远为0
                continue;
            }
            // 子节点不可见而自己可见(叶子节点)
            if (!node.getSonDisplay() && node.getVisible()) {
                Integer Y_bias = nodeService.getSCALE() / 5;//设置合适的纵向间距
                Integer defaultSize = nodeService.getSCALE() + Y_bias;
                node.setBlockHeight(defaultSize);//这里的大小决定纵向间距的大小
                CounterQueue.add(node);
            }


        }
    }

    public void toCountBlock() {
        initBlockCounter();
        while (!CounterQueue.isEmpty()) {
            MapNode node = CounterQueue.poll();
            node.setFlag(true);
            MapNode parentNode = nodeService.getNodeList().get(node.getParentId());
            parentNode.setBlockHeight(parentNode.getBlockHeight() + node.getBlockHeight());//计算块高

            //计算块宽
            ArrayList<MapNode> broNodes = nodeService.getChildrenNodeByNode(parentNode);
            boolean allChildFinish = true;
            for (MapNode broNode : broNodes) {
                if (!broNode.getFlag()) {   //如果有兄弟节点没有算完块宽
                    allChildFinish = false;
                    break;
                }
            }
            if (allChildFinish) {// 父节点只计算一块宽,遍历到最后一个子节点的时候计算
                Integer X_bias = nodeService.getSCALE() / 2; //设置合适的横向间距 TODO 统一X_bias
                // 高级实现Comparator的高级写法
                MapNode maxChild = Collections.max(broNodes, Comparator.comparingInt(MapNode::getBlockWidth));
                parentNode.setBlockWidth(parentNode.getBlockWidth() + maxChild.getBlockWidth() + X_bias);
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
        rootNode.setLeftX(Main.mindMapPane.getWidth()/2);//TODO 寻找合适的横坐标值
        rootNode.setTopY(Main.mindMapPane.getHeight()/2);
        switch (tree.getLayout()) {
            case "default" -> defaultLayout();
            case "right" -> rightLayout();
            case "left" -> leftLayout();
        }
    }

    private void rightLayout() {
        computeCoordinate(rootNode.getId(), 1);
    }

    private void leftLayout() {
        computeCoordinate(rootNode.getId(), -1);
    }

    private void defaultLayout() {
        computeCoordinate(rootNode.getId(), 0);
        // TODO 中心分布 划分中心节点
    }

    private void computeCoordinate(Integer now_id, Integer flag) { //根据flag动态生成左树或者右树(从中心节点向外计算坐标)
        MapNode nowNode = nodeService.getNodeById(now_id);
        if (nowNode.getChildrenId() == null) {     //叶子节点直接返回,不继续递推
            return;
        }
        if (now_id == rootNode.getId()) {
            nowNode.setLevel(0);
        }

        if(flag==0){//中心布局要特殊处理
            nowNode.setCounter(0);
            int half = (nowNode.getChildrenId().size()+1)/2;//加一是为了将编译器向下取整变成向上取整
            int leftSize = 0;
            int rightSize = 0;

            for (Integer childId : nowNode.getChildrenId()){//计算左右子树高度
                MapNode childNode = nodeService.getNodeById(childId);
                if(nowNode.getCounter()<half){ // 小于,左右平衡,
                    rightSize +=childNode.getBlockHeight();
                }else {
                    leftSize +=childNode.getBlockHeight();
                }
                //计数
                nowNode.setCounter(nowNode.getCounter()+1);
            }
            rightSize/=2;
            leftSize/=2;

            // 第一个子节点的位置
            Integer delta = nowNode.getBlockHeight() / 2;
            Double nowY = nowNode.getTopY();

            nowNode.setCounter(0);
            for (Integer childId : nowNode.getChildrenId()) {
                MapNode childNode = nodeService.getNodeById(childId);

                // 算y
                if(nowNode.getCounter()<half){//前一半右树  // 小于,和上面保持一致
                    flag=1;//右树
                    delta -= childNode.getBlockHeight() / 2;
                    Double y = nowY - delta+leftSize;//还要计算偏移
                    childNode.setTopY(y);
                    delta -= childNode.getBlockHeight() / 2;
                }else {
                    flag=-1;//左树
                    delta -= childNode.getBlockHeight() / 2;
                    Double y = nowY - delta-rightSize;//还要计算偏移
                    childNode.setTopY(y);
                    delta -= childNode.getBlockHeight() / 2;
                }

                //算x
                Integer X_bias = nodeService.getSCALE() / 2; //设置合适的横向间距
                double childX = nowNode.getLeftX() + flag * (nowNode.getWidth() + X_bias);  //x轴 通过flag实现:左树累加、右树累减
                childNode.setLeftX(childX);

                //计数
                nowNode.setCounter(nowNode.getCounter()+1);
                //算level
                childNode.setLevel(nowNode.getLevel() + 1);
                //递推调用
                computeCoordinate(childId, flag);
            }
        }else{  //左右单边的布局
            Integer X_bias = nodeService.getSCALE() / 2; //设置合适的横向间距
            double childX = nowNode.getLeftX() + flag * (nowNode.getWidth() + X_bias);  //x轴 通过flag实现:左树累加、右树累减

            // 第一个子节点的位置
            Integer delta = nowNode.getBlockHeight() / 2;
            Double nowY = nowNode.getTopY();

            for (Integer childId : nowNode.getChildrenId()) {
                MapNode childNode = nodeService.getNodeById(childId);
                //算x
                childNode.setLeftX(childX);
                //算y
                delta -= childNode.getBlockHeight() / 2;
                Double y = nowY - delta;    //注意正负号,javafx的坐标系和一般的不同
                childNode.setTopY(y);
                delta -= childNode.getBlockHeight() / 2;
                //算level
                childNode.setLevel(nowNode.getLevel() + 1);
                //递推调用
                computeCoordinate(childId, flag);
            }
        }
    }


    @Override
    public int changeLayout() {
        return 0;
    }

    @Override
    public int saveToCloud() {
        MindMapDaoImpl db =new MindMapDaoImpl(Main.mapName);
        db.saveMapToCloud(nodeService.getNodeList());
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
