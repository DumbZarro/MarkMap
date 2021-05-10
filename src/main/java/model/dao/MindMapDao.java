package model.dao;

import model.pojo.MapTree;
import java.util.ArrayList;

/**
 * Description:思维导图的Dao
 */
public interface MindMapDao {
    public int addMap(MapTree node);

    public int deleteMapById(int id);

    public int updateMap(MapTree book);

    public MapTree queryMapById(int id);

    public ArrayList<MapTree> queryMap();


}
