package kate;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

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
        /*JSONObject JSONArray null String ? extends Number*/
        Object parsedJson = parser.parse(json);

        return (T) convertToValue(parsedJson, clazz, null);
    }

    private Object convertToValue(Object jsonElement, Class<?> type, Type refType) throws Exception {
        if (type == null || String.class.equals(type)) {
            return jsonElement;
        }
        /**
         * added 2 methods(): isPrimitive() and JSONPrimitivesToObjects()
         */
        else if (isPrimitive(type)) {
            return JSONPrimitivesToObjects(jsonElement, type);
        } else if (List.class.isAssignableFrom(type)) {
            JSONArray jsonArray = (JSONArray) jsonElement;
            List<? super Object> list = new ArrayList<>();// PECS
            for (Object jsonEl : jsonArray) {
                list.add(convertToValue(jsonEl, null, null));
            }
            return list;
        } else {
            Object instance = createInstance(type);
            JSONObject jsonObject = (JSONObject) jsonElement;
            for (Field field : type.getDeclaredFields()) {
                field.set(
                        instance,
                        convertToValue(jsonObject.get(field.getName()), field.getType(), field.getGenericType())
                );
            }
            return instance;
        }
    }

    private boolean isPrimitive(Class<?> clazz) {
        return clazz.isPrimitive() || Number.class.isAssignableFrom(clazz) || Boolean.class.isAssignableFrom(clazz) || Character.class.isAssignableFrom(clazz) || BigDecimal.class.equals(clazz);
    }

    private Object JSONPrimitivesToObjects(Object jsonElement, Class<?> type) {
        if (int.class.equals(type) || Integer.class.equals(type)) {
            return ((Number) jsonElement).intValue();
        } else if (long.class.equals(type) || Long.class.equals(type)) {
            return ((Number) jsonElement).longValue();
        } else if (float.class.equals(type) || Float.class.equals(type)) {
            return ((Number) jsonElement).floatValue();
        } else if (double.class.equals(type) || Double.class.equals(type)) {
            return ((Number) jsonElement).doubleValue();
        } else if (byte.class.equals(type) || Byte.class.equals(type)) {
            return ((Number) jsonElement).byteValue();
        } else if (short.class.equals(type) || Short.class.equals(type)) {
            return ((Number) jsonElement).shortValue();
        } else if (char.class.equals(type) || Character.class.equals(type)) {
            String str = String.valueOf(jsonElement);
            return str;
        } else if (BigDecimal.class.equals(type)) {
            Number num = (Number) jsonElement;
            return BigDecimal.valueOf(num.doubleValue());
        }
        return jsonElement;
    }

    private <T> T createInstance(Class<T> clazz) throws Exception {
        Constructor<T> constructor = clazz.getDeclaredConstructor();
        return constructor.newInstance();
    }
}
