package model.utils;

import org.junit.Test;

/**
 * Description:
 */
public class JdbcUtilsTest {

    @Test
    public void getDatabase() {

        System.out.println(JdbcUtils.getCollection("map").countDocuments());

    }

    @Test
    public void getCollection() {
    }
}