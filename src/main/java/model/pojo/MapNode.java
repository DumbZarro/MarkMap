package model.pojo;

import java.util.ArrayList;

/**
 * Description: 思维导图中节点的类
 */
public class MapNode {
    private int Id; // 唯一标识符    [不初始化]
    private String content; // 显示的文本
    private String note;    // 注释
    private ArrayList<Integer> childrenId;   // 子节点Id列表
    private Integer parentId;   // 父节点的Id
    private Integer level;   // 节点的级别 也就是深度(以树的角度来看) [不初始化]
    private Double topY;    // 节点左上角的Y坐标    [不初始化]
    private Double leftX;   // 节点左上角的X坐标    [不初始化]
    private Double height=0.;  // 节点的高度,一般都是一样高,超过最大宽度则变成两行高  [不初始化]
    private Double width=0.;   // 节点的宽度 [不初始化]
    private Integer blockHeight;   // 块高    [不初始化]
    private Integer blockWidth;     // 块宽   [不初始化]
    private String cssClass;    // 节点的样式
    private Integer counter;    //用于计数
    private Boolean haveBlock;  //是否有外框
    private Boolean flag;   //标志位,主要用于判断节点是否计算了blockWidth
    private Boolean isVisible; // 节点是否显示 懒加载有的节点加载而不显示
    private Boolean isNoted;    // 节点是否有注释
    private Boolean isNoteVisible;//节点的注释框是否可见
    private Boolean isSonDisplay;  // 子节点是否显示 不可用子节点列表为空判断->可能只是没加载
    private Boolean isSelected; // 是否被选中
    private ArrayList<MapNode> extraEdge;   //除了父子间的线以外的关系线
    private Integer scale; //默认高度 长宽高的单位 用于缩放导图 [不初始化]

    public MapNode() {
        this.content = "新建节点";
        this.note = null;
        this.childrenId = new ArrayList<>();//节点列表要初始化
        this.blockHeight = 0;
        this.cssClass = "default";
        this.counter = 0;
        this.isVisible = true;
        this.isNoted = false;
        this.isSelected = false; // 默认被创建时选中
        this.isSonDisplay = false;//新创节点无子则不可见
        this.extraEdge = null;
        this.leftX = 0.;
        this.topY = 0.;
        this.level = 0;
        this.flag = false;
        this.haveBlock = false;
        this.isNoteVisible = false;
    }


    public MapNode(int Id, double height, double width, int scale) {
        this();
        this.Id = Id;
        this.height = height;
        this.width = width;
        this.scale = scale;
    }

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
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

    public Boolean getFlag() {
        return flag;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }

    public Boolean getVisible() {
        return isVisible;
    }

    public void setVisible(Boolean visible) {
        isVisible = visible;
    }

    public Boolean getNoted() {
        return isNoted;
    }

    public void setNoted(Boolean noted) {
        isNoted = noted;
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

    public Boolean getNoteVisible() {
        return isNoteVisible;
    }

    public void setNoteVisible(Boolean noteVisible) {
        isNoteVisible = noteVisible;
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

    public Boolean getHaveBlock() {
        return haveBlock;
    }

    public void setHaveBlock(Boolean haveBlock) {
        this.haveBlock = haveBlock;
    }

    public Integer getScale() {
        return scale;
    }

    public void setScale(Integer scale) {
        this.scale = scale;
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
                ", haveBlock=" + haveBlock +
                ", flag=" + flag +
                ", isVisible=" + isVisible +
                ", isNoted=" + isNoted +
                ", isSonDisplay=" + isSonDisplay +
                ", isSelected=" + isSelected +
                ", extraEdge=" + extraEdge +
                ", scale=" + scale +
                '}';
    }
}
