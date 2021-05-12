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
        MongoDatabase database = JdbcUtils.getDatabase("testCollection");

        System.out.println(database.getName());
        System.out.println(database.getCollection("myCollection").countDocuments());

    }

    @Test
    public void getCollection() {
    }
}