package model.utils;

import com.mongodb.client.MongoDatabase;
import org.junit.Test;

import static org.junit.Assert.*;

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