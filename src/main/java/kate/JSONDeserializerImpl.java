package kate;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

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
        // Number, String, Boolean, JSONArray, JSONObject
        if (parse instanceof JSONObject) {
            Constructor<T> ctor = clazz.getConstructor();
            T t = ctor.newInstance();

            Field[] declaredFields = clazz.getDeclaredFields();
            for (Field df : declaredFields) {
                Object val = ((JSONObject) parse).get(df.getName());
                df.set(t, val);
            }
            return t;
        }
        return (T) parse;

    }
}
