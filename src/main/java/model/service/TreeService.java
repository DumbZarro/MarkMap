package model.service;

import model.pojo.MapTree;

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
