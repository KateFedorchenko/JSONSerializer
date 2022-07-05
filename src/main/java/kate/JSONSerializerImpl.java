package kate;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Collection;

public class JSONSerializerImpl implements JSONSerializer {
    private String writeAsString0(Object o) throws IllegalAccessException {
        if (o == null) {
            return null;
        }
        Class<?> clazz = o.getClass();
        if (clazz.equals(Integer.class)) {                 //Number.class ???
            return "[" + o + "]";
        }
        if (clazz.equals(String.class)) {
            return "[\"" + o + "\"]";
        }
        if (clazz.isArray()) {
            return writeArrayAsString(o);
        } else if (Collection.class.isAssignableFrom(clazz)) {
            return writeCollectionAsString(o);
        } else {
            JSONObject jsonObj = new JSONObject();
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                jsonObj.put(field.getName(), field.get(o));
            }
            return jsonObj.toJSONString();
        }
    }

    @Override
    public String writeAsString(Object o) {
        try {
            return writeAsString0(o);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public String writeArrayAsString(Object o) {
        int size = Array.getLength(o);
        JSONArray jsonArr = new JSONArray();
        for (int i = 0; i < size; i++) {
            jsonArr.add(Array.get(o, i));
        }
        return jsonArr.toJSONString();
    }

    public String writeCollectionAsString(Object o) throws IllegalAccessException {
        JSONArray jsonArr = new JSONArray();
        Collection<?> collection = (Collection<?>) o;
        for (Object o1 : collection) {
            JSONObject jsonObj = new JSONObject();
            if (o1.getClass().equals(Integer.class) || o1.getClass().equals(String.class)) {
                jsonArr.add(o1);
            } else {
                Field[] declaredFields = o1.getClass().getDeclaredFields();
                for (Field field : declaredFields) {
                    jsonObj.put(field.getName(), field.get(o1));
                }
                jsonArr.add(jsonObj);
            }
        }
        return jsonArr.toJSONString();
    }
}
