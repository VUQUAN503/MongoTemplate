package repository.base;

import com.mongodb.*;
import entity.AbstractEntity;
import entity.Entity;
import mongo.MongoManager;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static util.DBObjectUtil.*;

/*
 *@created 13/08/2022
 *@author DELL
*/

public abstract class MongoCommand<T extends AbstractEntity> extends BaseRepository implements CRUD<T, String> {

    Runnable doOnSuccess;
    Supplier<BasicDBObject> query = BasicDBObject::new;
    Function<BasicDBObject, BasicDBObject> andFilter = t -> query.get();
    Supplier<DBObject> set = BasicDBObject::new;
    List<DBObject> pipeline = new ArrayList<>();
    int take;
    int skip;
    boolean isUpsert = false;
    boolean isMulti = false;
    private final Entity entity = getEntityClass().getAnnotation(Entity.class);

    @Override
    public void insert(T data) {
        getCollection().insert(toDBObject(data));
    }

    void update() {
        BasicDBObject query = andFilter.apply(this.query.get());
        getCollection().update(query, set.get(), isUpsert, isMulti);
    }

    long count() {
        BasicDBObject query = andFilter.apply(this.query.get());
        return getCollection().count(query);
    }

    <R> Collection<R> find(Class<R> returnType, Supplier<Collection<R>> toCollection) {
        BasicDBObject query = andFilter.apply(this.query.get());
        try (DBCursor dbCursor = getCollection().find(query)
                .skip(skip).limit(take)) {
            return convertDataToCollection(dbCursor, returnType, toCollection);
        }
    }

    <K, V> Map<K, V> find(String fieldKey, String fieldValue, Class<V> vClass) {
        BasicDBObject query = andFilter.apply(this.query.get());
        try (Cursor cursor = getCollection().find(query)) {
            return convertDataToMap(fieldKey, fieldValue, vClass, cursor);
        }
    }

    <K, V> Map<K, Collection<V>> find(String fieldKey, String fieldValue, Class<V> vClass
            , Supplier<Collection<V>> toCollectionValue) {
        BasicDBObject query = andFilter.apply(this.query.get());
        try (Cursor cursor = getCollection().find(query)) {
            return convertDataToMap(fieldKey, fieldValue, vClass, toCollectionValue, cursor);
        }
    }

    <R> Collection<R> aggregate(Class<R> returnType, Supplier<Collection<R>> toCollection) {
        try (Cursor cursor = getCollection().aggregate(pipeline, AggregationOptions.builder().build())) {
            return convertDataToCollection(cursor, returnType, toCollection);
        }
    }

    <K, V> Map<K, V> aggregate(String fieldKey, String fieldValue, Class<V> vClass) {
        try (Cursor cursor = getCollection().aggregate(pipeline, AggregationOptions.builder().build())) {
            return convertDataToMap(fieldKey, fieldValue, vClass, cursor);
        }
    }

    <K, V> Map<K, Collection<V>> aggregate(String fieldKey, String fieldValue, Class<V> vClass
            , Supplier<Collection<V>> toCollectionValue) {
        try (Cursor cursor = getCollection().aggregate(pipeline, AggregationOptions.builder().build())) {
            return convertDataToMap(fieldKey, fieldValue, vClass, toCollectionValue, cursor);
        }
    }

    @SuppressWarnings("unchecked")
    public <R> List<R> distinct(String fieldName) {
        BasicDBObject query = andFilter.apply(this.query.get());
        return (List<R>) getCollection().distinct(fieldName, query);
    }

    private <R> Collection<R> convertDataToCollection(Cursor dbCursor, Class<R> rClass
            , Supplier<Collection<R>> toCollection) {
        Collection<R> result = toCollection.get();
        while (dbCursor.hasNext()) {
            DBObject dbObject = dbCursor.next();
            result.add(toEntity(dbObject, rClass));
        }
        if (Objects.nonNull(doOnSuccess))
            doOnSuccess.run();
        return result;
    }

    private <K, V> Map<K, V> convertDataToMap(String fieldKey, String fieldValue
            , Class<V> vClass, Cursor cursor) {
        Map<K, V> result = new HashMap<>();
        while (cursor.hasNext()) {
            DBObject dbObject = cursor.next();
            result.put(getSingleValue(dbObject, fieldKey), getValue(dbObject, fieldValue, vClass));
        }
        if (Objects.nonNull(this.doOnSuccess)) doOnSuccess.run();
        return result;
    }

    private <K, V> Map<K, Collection<V>> convertDataToMap(String fieldKey, String fieldValue, Class<V> vClass
            , Supplier<Collection<V>> toCollectionValue, Cursor cursor) {
        Map<K, Collection<V>> result = new HashMap<>();
        while (cursor.hasNext()) {
            DBObject dbObject = cursor.next();
            if (!(dbObject.get(fieldValue) instanceof BasicDBList basicDBList)) {
                throw new RuntimeException("");
            }
            result.put(getSingleValue(dbObject, fieldKey), basicDBList.stream()
                    .map(o -> getValue(vClass, o))
                    .collect(Collectors.toCollection(toCollectionValue)));
        }
        if(Objects.nonNull(this.doOnSuccess)) this.doOnSuccess.run();
        return result;
    }


    protected MongoOperation<T> startMongoOperation() {
        return new MongoOperation<>(this);
    }

    @Override
    DBCollection getCollection() {
        return getDatabase().getCollection(entity.collectionName());
    }

    @Override
    DB getDatabase() {
        return MongoManager.getDataBase(entity.databaseName());
    }

    protected abstract Class<T> getEntityClass();
}
