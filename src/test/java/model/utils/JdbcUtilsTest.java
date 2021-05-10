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
        MongoDatabase database = JdbcUtils.getDatabase("mindMap");
        System.out.println(database.getName());
    }

    @Test
    public void getCollection() {
    }
}