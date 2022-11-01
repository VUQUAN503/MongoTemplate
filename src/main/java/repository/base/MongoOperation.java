package repository.base;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import entity.AbstractEntity;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

/*
 *@created 27/10/2022
 *@author DELL
*/


public class MongoOperation<T extends AbstractEntity> {

    MongoCommand<T> command;

    public MongoOperation(MongoCommand<T> command) {
        this.command = command;
    }

    public MongoOperation<T> filter(Supplier<BasicDBObject> query) {
        command.query = query;
        return this;
    }

    public MongoOperation<T> and(Function<BasicDBObject, BasicDBObject> query) {
        if (command.andFilter == null) {
            command.andFilter = query;
        } else command.andFilter = command.andFilter.andThen(query);
        return this;
    }

    public MongoOperation<T> or(Function<BasicDBObject, BasicDBObject> query) {
        if (command.andFilter == null) {
            command.andFilter = query;
        } else command.andFilter = command.andFilter.andThen(query);
        return this;
    }

    public MongoOperation<T> inCrease(Supplier<BasicDBObject> increase) {
        return this;
    }

    public MongoOperation<T> map(Function<T, BasicDBObject> map) {
        return this;
    }

    public MongoOperation<T> set(Supplier<DBObject> set) {
        command.set = set;
        return this;
    }

    public MongoOperation<T> skip(int skip) {
        command.skip = skip;
        return this;
    }

    public MongoOperation<T> take(int take) {
        command.take = take;
        return this;
    }

    public MongoOperation<T> group(Supplier<DBObject> group) {
        command.pipeline.add(new BasicDBObject("$group", group.get()));
        return this;
    }

    public MongoOperation<T> match(Supplier<BasicDBObject> match) {
        command.pipeline.add(new BasicDBObject("$match", command.query.get()));
        return this;
    }

    public MongoOperation<T> project(Supplier<BasicDBObject> project) {
        command.pipeline.add(new BasicDBObject("$project", project.get()));
        return this;
    }

    public MongoOperation<T> isUpsert(boolean isUpsert) {
        command.isUpsert = isUpsert;
        return this;
    }

    public MongoOperation<T> isMulti(boolean isMulti) {
        command.isMulti = isMulti;
        return this;
    }

    public MongoOperation<T> doOnSuccess(Runnable runnable) {
        command.doOnSuccess = runnable;
        return this;
    }

    public <R> R execute(Function<MongoCommand<T>, R> function, Runnable doOnSuccess) {
        command.doOnSuccess = doOnSuccess;
        return function.apply(command);
    }

    public <R> Collection<R> find(Class<R> rClass, Supplier<Collection<R>> toCollection) {
        return command.find(rClass, toCollection);
    }

    public <R> Collection<R> find(Class<R> rClass, Supplier<Collection<R>> toCollection, Runnable doOnSuccess) {
        command.doOnSuccess = doOnSuccess;
        return command.find(rClass, toCollection);
    }

    public <K, V> Map<K, V> find(String fieldKey, String fieldValue, Class<V> vClass) {
        return command.find(fieldKey, fieldValue, vClass);
    }

    public <K, V> Map<K, Collection<V>> find(String fieldKey, String fieldValue, Class<V> vClass, Supplier<Collection<V>> toCollection) {
        return command.find(fieldKey, fieldValue, vClass, toCollection);
    }

    public long count() {
        return command.count();
    }

    public void update() {
        command.update();
    }

    public <R> List<R> distinct(String fieldName) {
        return command.distinct(fieldName);
    }

    public <R> Collection<R> aggregate(Class<R> rClass, Supplier<Collection<R>> toCollection) {
        return command.aggregate(rClass, toCollection);
    }

    public <R> Collection<R> aggregate(Class<R> rClass, Supplier<Collection<R>> toCollection, Runnable doOnSuccess) {
        command.doOnSuccess = doOnSuccess;
        return command.aggregate(rClass, toCollection);
    }

    public <K, V> Map<K, V> aggregate(String fieldKey, String fieldValue, Class<V> vClass) {
        return command.aggregate(fieldKey, fieldValue, vClass);
    }

    public <K, V> Map<K, Collection<V>> aggregate(String fieldKey, String fieldValue, Class<V> vClass
            , Supplier<Collection<V>> toCollectionValue) {
        return command.aggregate(fieldKey, fieldValue, vClass, toCollectionValue);
    }
}
