package kate;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

public class oldJSONDeserializerImpl implements JSONDeserializer {
    @Override
    public <T> T read(String json, Class<T> clazz) {
        try {
            return read0(json, clazz);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private <T> T read0(String json, Class<T> clazz) throws Exception {
        JSONParser parser = new JSONParser();
        Object parse = parser.parse(json);
        Constructor<T> ctor = clazz.getConstructor();
        T newInstance = ctor.newInstance();

        Object resultObj = convertToValidType(parse, newInstance);
        return (T) resultObj;
    }

    private <T> T convertToValidType(Object parse, T newInstance) throws Exception {
        Class<?> type = newInstance.getClass();
        if (type.isPrimitive() || newInstance instanceof Number || newInstance instanceof String) {

        } else if (newInstance instanceof Collection) {

        } else {
            Object o = fromJSONToObject(parse, newInstance);

        }
        return null;
    }

    private List<?> fromJSONToCollection(Object parse, Class<?> clazz) throws Exception {
        return null;
    }

    private <T> Object fromJSONToObject(Object jsonObject, T newInstance) throws Exception {
        Class<?> clazz = newInstance.getClass();
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field df : declaredFields) {
            Object val = ((JSONObject) jsonObject).get(df.getName());
            df.set(newInstance, convertToValidType(val, clazz));
        }
        return newInstance;
    }

    private int fromJSONToNumber(Long l) {
        return l.intValue();
    }


    private boolean isCollection(Class<?> clazz) {
        return Collection.class.isAssignableFrom(clazz);
    }

    private boolean isPrimitive(Class<?> clazz) {
        return clazz.isPrimitive() || clazz.equals(String.class);
    }

}
