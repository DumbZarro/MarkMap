package model.pojo;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Description:思维导图树的类,实际上就是节点列表加上布局
 */
public class MapTree {
    private final Integer rootId;
    private String layout;

    public MapTree(Integer rootId) {   // 通过节点列表构造
        this.rootId = rootId;
        this.layout = "left";
    }

    public Integer getRootId() {
        return rootId;
    }

    public String getLayout() {
        return layout;
    }

    public void setLayout(String layout) {
        this.layout = layout;
    }
}
