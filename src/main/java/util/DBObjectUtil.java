package util;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import entity.FieldName;
import org.bson.types.ObjectId;

import java.lang.reflect.Field;

/*
 *@created 29/10/2022
 *@author DELL
*/

public class DBObjectUtil {

    @SuppressWarnings("unchecked")
    static public <R> R toEntity(DBObject dbObject, Class<R> tClass) {
        R r = null;
        try {
            r = tClass.newInstance();
            for (Field field : tClass.getDeclaredFields()) {
                FieldName fieldName = field.getAnnotation(FieldName.class);
                Object object;
                if (fieldName == null) {
                    object = dbObject.get(field.getName());
                } else {
                    object = dbObject.get(fieldName.name());
                }
                Object data = getData(object, field);
                field.setAccessible(true);
                if (field.getType().isEnum()) {
                    field.set(r, Enum.valueOf((Class<? extends Enum>) field.getType(), (String) data));
                } else {
                    field.set(r, data);
                }
            }
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return r;
    }

    static public <R> BasicDBObject toDBObject(R data) {
        try {
            BasicDBObject dbObject = new BasicDBObject();
            for (Field field : data.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                if (field.isAnnotationPresent(FieldName.class)) {
                    FieldName fieldName = field.getAnnotation(FieldName.class);
                    if (!fieldName.generic().equals(Object.class)) {
                        dbObject.append(fieldName.name().equals("") ? field.getName() : fieldName.name(), toDBObject(field.get(data)));
                    } else {
                        dbObject.append(fieldName.name().equals("") ? field.getName() : fieldName.name(), field.get(data));
                    }
                } else {
                    dbObject.append(field.getName(), field.get(data));
                }
            }
            return dbObject;
        } catch (IllegalAccessException e) {
            throw new RuntimeException();
        }
    }

    static  Object getData(Object object, Field field) {
        if (object == null) {
            return null;
        }
        if (object instanceof ObjectId) {
            return object.toString();
        } else if (object instanceof BasicDBObject dbObject) {
            return toEntity(dbObject, field.getType());
        } else if (object instanceof BasicDBList basicDBList) {
            return basicDBList.stream()
                    .map(o -> getData(o, field))
                    .toList();
        }
        return object;
    }
    @SuppressWarnings("unchecked")
    public static  <V> V getValue(Class<V> vClass, Object o) {
        if (o instanceof BasicDBObject basicDBObject) {
            return toEntity(basicDBObject, vClass);
        }
        return (V) o;
    }

    @SuppressWarnings("unchecked")
    public static  <V> V getValue(DBObject dbObject, String fieldValue, Class<V> vClass) {
        Object object = dbObject.get(fieldValue);
        if (object == null) {
            return null;
        }
        if (object instanceof ObjectId) {
            if (vClass.equals(ObjectId.class)) {
                return (V) object;
            }
            return (V) object.toString();
        }
        if (object instanceof BasicDBObject basicDBObject) {
            return toEntity(basicDBObject, vClass);
        }
        return (V) object;
    }

    @SuppressWarnings("unchecked")
    public static  <R> R getSingleValue(DBObject dbObject, String fieldName) {
        return (R) dbObject.get(fieldName);
    }

    private static String toSnakeCase(String key) {
        return key.replaceAll("[A-Z]", "_");
    }

    public static void main(String[] args) {
        System.out.println(toSnakeCase("helloWorld"));
    }
}
