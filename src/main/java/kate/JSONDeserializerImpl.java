package kate;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

public class JSONDeserializerImpl implements JSONDeserializer {
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
        Object resultObj = convertToValidObject(parse, clazz);
        return (T) resultObj;
//        if (parse instanceof JSONObject) {
//            Constructor<T> ctor = clazz.getConstructor();
//            T t = ctor.newInstance();
//
//            Field[] declaredFields = clazz.getDeclaredFields();
//            for (Field df : declaredFields) {
//                Object val = ((JSONObject) parse).get(df.getName());
//                df.set(t, val);
//            }
//            return t;
//        }
//        return (T) parse;
    }

    private <T> T convertToValidObject(Object parse, Class<T> clazz) throws Exception {
        T resConvert = null;
        if (parse instanceof Number) {
//            resConvert = fromJSONToNumber(parse);
        } else if (isPrimitive(clazz)) {
            resConvert = (T) fromJSONToObject(parse, clazz);
        } else if (parse instanceof JSONObject) {
            resConvert = (T) fromJSONToObject(parse, clazz);
        }
        return resConvert;
    }

    private Object fromJSONToObject(Object jsonObject, Class<?> clazz) throws Exception {
        Constructor<?> ctor = clazz.getConstructor();
        Object t = ctor.newInstance();

        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field df : declaredFields) {
            Object val = ((JSONObject) jsonObject).get(df.getName());
            df.set(t, convertToValidObject(val, clazz));
        }
        return t;
    }

    private int fromJSONToNumber(Long l) {
        return l.intValue();
    }

    private boolean isPrimitive(Class<?> clazz) {
        return clazz.isPrimitive() || clazz.equals(String.class);
    }

}
