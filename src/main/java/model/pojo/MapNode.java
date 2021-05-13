package model.pojo;

import java.util.ArrayList;

/**
 * Description: 思维导图中节点的类
 */
public class MapNode {
    private int Id; // 唯一标识符    [由MapTree来决定]
    private String content; // 显示的文本
    private String note;    // 注释
    private ArrayList<Integer> childrenId;   // 子节点Id列表
    private Integer parentId;   // 父节点的Id
    private Integer level;   // 节点的级别 也就是深度(以树的角度来看) [由MapTree来决定] ********
    private Double topY;    // 节点左上角的Y坐标    [由MapTree来决定]
    private Double leftX;   // 节点左上角的X坐标    [由MapTree来决定]
    private Double height;  // 节点的高度,一般都是一样高,超过最大宽度则变成两行高
    private Double width;   // 节点的宽度
    private Integer blockHeight;   // 块高
    private Integer blockWidth;     // 块宽
    private String cssClass;    // 节点的样式
    private Integer counter;    //用于计数
    private Boolean isVisible;  // 节点是否显示 懒加载有的节点加载而不显示  ********
    private Boolean isAlong;    // 孤立 无父无子  [由MapTree来决定]
    private Boolean isSonDisplay;  // 子节点是否显示 不可用子节点列表为空判断->可能只是没加载
    private Boolean isSelected; // 是否被选中
    private ArrayList<MapNode> extraEdge;   //除了父子间的线以外的关系线  将当前节点与列表中的节点分别连一根线
//    static Integer SCALE = 100; //默认高度 长宽高的单位 用于缩放导图

    public MapNode() {
        this.content = "新建节点";
        this.note = null;
        this.childrenId = new ArrayList<>();//节点列表要初始化
        this.height = 100*1.;   // TODO 解决初始化高度
        this.width = 2 * height;
        this.blockHeight = 0;
        this.cssClass = "default";
        this.counter = 0;
        this.isVisible = true;
        this.isSelected = false; // 默认被创建时选中
        this.isSonDisplay = false;//新创节点无子则不可见
        this.extraEdge = null;
        this.leftX=0.;
        this.topY=0.;
        this.level=0;
    }

    public MapNode(int Id, double leftX, double topY) {
        this();
        this.Id = Id;
        this.leftX = leftX;
        this.topY = topY;

    }
    public MapNode(int Id) {
        this();
        this.Id = Id;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public ArrayList<Integer> getChildrenId() {
        return childrenId;
    }

    public void setChildrenId(ArrayList<Integer> childrenId) {
        this.childrenId = childrenId;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Double getTopY() {
        return topY;
    }

    public void setTopY(Double topY) {
        this.topY = topY;
    }

    public Double getLeftX() {
        return leftX;
    }

    public void setLeftX(Double leftX) {
        this.leftX = leftX;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public Double getWidth() {
        return width;
    }

    public void setWidth(Double width) {
        this.width = width;
    }

    public Integer getBlockHeight() {
        return blockHeight;
    }

    public void setBlockHeight(Integer blockHeight) {
        this.blockHeight = blockHeight;
    }

    public String getCssClass() {
        return cssClass;
    }

    public void setCssClass(String cssClass) {
        this.cssClass = cssClass;
    }

    public Integer getCounter() {
        return counter;
    }

    public void setCounter(Integer counter) {
        this.counter = counter;
    }

    public Boolean getVisible() {
        return isVisible;
    }

    public void setVisible(Boolean visible) {
        isVisible = visible;
    }

    public Boolean getAlong() {
        return isAlong;
    }

    public void setAlong(Boolean along) {
        isAlong = along;
    }

    public Boolean getSonDisplay() {
        return isSonDisplay;
    }

    public void setSonDisplay(Boolean sonDisplay) {
        isSonDisplay = sonDisplay;
    }

    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }

    public ArrayList<MapNode> getExtraEdge() {
        if (extraEdge == null)
            extraEdge = new ArrayList<MapNode>();
        return extraEdge;
    }

    public void setExtraEdge(ArrayList<MapNode> extraEdge) {
        this.extraEdge = extraEdge;
    }

    public Integer getBlockWidth() {
        return blockWidth;
    }

    public void setBlockWidth(Integer blockWidth) {
        this.blockWidth = blockWidth;
    }

    @Override
    public String toString() {
        return "MapNode{" +
                "Id=" + Id +
                ", content='" + content + '\'' +
                ", note='" + note + '\'' +
                ", childrenId=" + childrenId +
                ", parentId=" + parentId +
                ", level=" + level +
                ", topY=" + topY +
                ", leftX=" + leftX +
                ", height=" + height +
                ", width=" + width +
                ", blockHeight=" + blockHeight +
                ", blockWidth=" + blockWidth +
                ", cssClass='" + cssClass + '\'' +
                ", counter=" + counter +
                ", isVisible=" + isVisible +
                ", isAlong=" + isAlong +
                ", isSonDisplay=" + isSonDisplay +
                ", isSelected=" + isSelected +
                ", extraEdge=" + extraEdge +
                '}';
    }
}
