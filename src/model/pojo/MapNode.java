package model.pojo;

import java.util.ArrayList;

/**
 * Description: 思维导图中节点的类
 */
public class MapNode {
    private int Id; // 唯一标识符    [由MapTree来决定]
    private String content; // 显示的文本
    private String note;    // 注释
    private ArrayList<Integer> childrensId;   // 子节点Id列表
    private Integer parentId;   // 父节点的Id
    private Integer level;   // 节点的级别 也就是深度(以树的角度来看) [由MapTree来决定] ********
    private Double topY;    // 节点左上角的Y坐标    [由MapTree来决定]
    private Double leftX;   // 节点左上角的X坐标    [由MapTree来决定]
    private Double height;  // 节点的高度,一般都是一样高,超过最大宽度则变成两行高
    private Double width;   // 节点的宽度
    private Integer blockSize;   // 块大小
    private String cssClass;    // 节点的样式
    private Integer counter;    //用于计数
    private Boolean isVisible;  // 节点是否显示 懒加载有的节点加载而不显示  ********
    private Boolean isAlong;    // 孤立 无父无子  [由MapTree来决定]
    private Boolean isSonDisplay;  // 子节点是否显示 不可用子节点列表为空判断->可能只是没加载
    private Boolean isSelected; // 是否被选中
    private ArrayList<MapNode> extraEdge;   //除了父子间的线以外的关系线  将当前节点与列表中的节点分别连一根线
    static Integer SCALE = 100; //长宽高的单位 用于缩放导图

    public MapNode() {
        this.content = "";
        this.note = null;
        this.childrensId = null;
        this.height = SCALE*1.;
        this.width = 2 * height;
        this.blockSize = 0;
        this.cssClass = "default";
        this.counter = 0;
        this.isVisible = true;
        this.isSelected = false; // 默认被创建时选中
        this.isSonDisplay = true;
        this.extraEdge = null;
        this.leftX=0.;
        this.topY=0.;
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

    public ArrayList<Integer> getChildrensId() {
        return childrensId;
    }

    public void setChildrensId(ArrayList<Integer> childrensId) {
        this.childrensId = childrensId;
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

    public Integer getBlockSize() {
        return blockSize;
    }

    public void setBlockSize(Integer blockSize) {
        this.blockSize = blockSize;
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
        return extraEdge;
    }

    public void setExtraEdge(ArrayList<MapNode> extraEdge) {
        this.extraEdge = extraEdge;
    }

    public static Integer getSCALE() {
        return SCALE;
    }

    public static void setSCALE(Integer SCALE) {
        MapNode.SCALE = SCALE;
    }

    @Override
    public String toString() {
        return "MapNode{" +
                "Id=" + Id +
                ", content='" + content + '\'' +
                ", note='" + note + '\'' +
                ", childrensId=" + childrensId +
                ", parentId=" + parentId +
                ", level=" + level +
                ", topY=" + topY +
                ", leftX=" + leftX +
                ", height=" + height +
                ", width=" + width +
                ", blockSize=" + blockSize +
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
