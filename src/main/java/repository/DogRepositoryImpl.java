package repository;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import entity.Dog;
import repository.base.MongoCommand;

import java.util.ArrayList;
import java.util.List;

/*
 *@created 30/10/2022
 *@author DELL
*/

public class DogRepositoryImpl extends MongoCommand<Dog> implements DogRepository {

    @Override
    public List<Dog> findByName(String name) {
        return (List<Dog>) startMongoOperation()
                .filter(() -> new BasicDBObject("name", name))
                .and(o -> o.append("color", "red"))
                .project(this::createProject)
                .group(this::createGroup)
                .skip(1)
                .take(3)
                .find(Dog.class, ArrayList::new, () -> {
                    //TODO handle when execute command success
                });
    }

    private DBObject createGroup() {
        return null;
    }

    private BasicDBObject createProject() {
        return null;
    }

    @Override
    public long count(String name) {
        return 0;
    }

    @Override
    protected Class<Dog> getEntityClass() {
        return Dog.class;
    }
}
