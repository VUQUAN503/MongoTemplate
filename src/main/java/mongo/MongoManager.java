package mongo;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

/*
 *@created 13/08/2022
 *@author DELL
*/
public class MongoManager {
    private static final String URL = "mongodb://localhost:27017/";
    static MongoClient client;

    static {
        MongoClientURI clientURI = new MongoClientURI(URL);
        client = new MongoClient(clientURI);
    }

    public static DB getDataBase(String databaseName){
        return client.getDB(databaseName);
    }
}
