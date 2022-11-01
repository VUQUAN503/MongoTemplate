package repository.base;

import com.mongodb.DB;
import com.mongodb.DBCollection;

/*
 *@created 14/08/2022
 *@author DELL
*/

public abstract class BaseRepository {

    abstract DBCollection getCollection();

    abstract DB getDatabase();
}
