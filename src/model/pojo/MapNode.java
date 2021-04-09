package model.pojo;

import java.util.ArrayList;

/**
 * Description: 思维导图中节点的类
 */
public class MapNode {
    private int Id; // 唯一标识符    [由MapTree来决定]
    private String content; // 显示的文本
    private String note;    // 注释
    private ArrayList<MapNode> childrens;   // 子节点列表
    private String level;   // 节点的级别 也就是深度(以树的角度来看) [由MapTree来决定]
    private double topY;    // 节点左上角的Y坐标    [由MapTree来决定]
    private double leftX;   // 节点左上角的X坐标    [由MapTree来决定]
    private double height;  // 节点的高度,一般都是一样高,超过最大宽度则变成两行高
    private double width;   // 节点的宽度
    private String cssClass;    // 节点的样式
    private boolean isVisible;  // 节点是否显示 懒加载有的节点加载而不显示
    private boolean isAlong;    // 孤立 无父无子  [由MapTree来决定]
    private boolean isSonless;  // 无子节点 不可用子节点列表为空判断->可能只是没加载
    private boolean isSelected; // 是否被选中
    private ArrayList<MapNode> extraEdge;   //除了父子间的线以外的关系线  将当前节点与列表中的节点分别连一根线
    static double SCALE=100; //长宽高的单位 用于缩放导图

    public MapNode() {
        this.content = "";
        this.note=null;
        this.childrens = null;
        this.height = SCALE;
        this.width = 2*height;
        this.cssClass = "default";
        this.isVisible = true;
        this.isSelected = true; // 默认被创建时选中
        this.isSonless = true;
        this.extraEdge = null;
    }

    public MapNode(int Id,double leftX,double topY) {
        this();
        this.Id=Id;
        this.leftX=leftX;
        this.topY = topY;

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

    public ArrayList<MapNode> getChildrens() {
        return childrens;
    }

    public void setChildrens(ArrayList<MapNode> childrens) {
        this.childrens = childrens;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public double getTopY() {
        return topY;
    }

    public void setTopY(double topY) {
        this.topY = topY;
    }

    public double getLeftX() {
        return leftX;
    }

    public void setLeftX(double leftX) {
        this.leftX = leftX;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public String getCssClass() {
        return cssClass;
    }

    public void setCssClass(String cssClass) {
        this.cssClass = cssClass;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    public boolean isAlong() {
        return isAlong;
    }

    public void setAlong(boolean along) {
        isAlong = along;
    }

    public boolean isSonless() {
        return isSonless;
    }

    public void setSonless(boolean sonless) {
        isSonless = sonless;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public ArrayList<MapNode> getExtraEdge() {
        return extraEdge;
    }

    public void setExtraEdge(ArrayList<MapNode> extraEdge) {
        this.extraEdge = extraEdge;
    }

    public static double getSCALE() {
        return SCALE;
    }

    public static void setSCALE(double SCALE) {
        MapNode.SCALE = SCALE;
    }

    @Override
    public String toString() {
        return "MapNode{" +
                "Id=" + Id +
                ", content='" + content + '\'' +
                ", note='" + note + '\'' +
                ", childrens=" + childrens +
                ", level='" + level + '\'' +
                ", topY=" + topY +
                ", leftX=" + leftX +
                ", height=" + height +
                ", width=" + width +
                ", cssClass='" + cssClass + '\'' +
                ", isVisible=" + isVisible +
                ", isAlong=" + isAlong +
                ", isSonless=" + isSonless +
                ", isSelected=" + isSelected +
                '}';
    }
}
