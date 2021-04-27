package model.service;

import model.pojo.MapNode;
import model.pojo.MapTree;

import java.util.ArrayList;

/**
 * Description:
 */
public interface TreeService {


    int drawMap(MapTree tree);
    int changeLayout();
    int saveToCloud();
    int saveToLocal();
    int readFromCloud();
    int readFromLocal();
    int exportAsImage();
}
