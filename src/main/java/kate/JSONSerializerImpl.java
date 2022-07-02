package kate;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class JSONSerializerImpl implements JSONSerializer {
    private String writeAsString0(Object o) throws IllegalAccessException {
        Class<?> clazz = o.getClass();
        if (clazz.isArray()) {
            int size = Array.getLength(o);
            JSONArray jsonArr = new JSONArray();
            for (int i = 0; i < size; i++) {
                jsonArr.add(Array.get(o, i));
            }
            return jsonArr.toJSONString();
        } else if (Collection.class.isAssignableFrom(clazz)) {
            JSONArray jsonArr = new JSONArray();
            Collection<?> collection = (Collection<?>) o;
            for (Object o1 : collection) {
                jsonArr.add(o1);
            }
            return jsonArr.toJSONString();
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
            //TODO own runtime exception
            throw new RuntimeException(e);
        }
    }
}
