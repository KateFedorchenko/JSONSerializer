package kate;

import org.json.simple.JSONArray;
import org.json.simple.JSONAware;
import org.json.simple.JSONObject;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Collection;

public class JSONSerializerImpl implements JSONSerializer {
    @Override
    public String writeAsString(Object o) {
        try {
            return writeAsString0(o);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private String writeAsString0(Object o) throws IllegalAccessException {
        if (o == null) {
            return null;
        }
        Object obj = convertToValidJSONElement(o);
        if(obj instanceof JSONAware){
            return ((JSONAware)obj).toJSONString();
        } else {
            return obj.toString();
        }

//        if (obj instanceof JSONArray) {
//            JSONArray obj1 = (JSONArray) obj;
//            return obj1.toJSONString();
//        } else if (obj instanceof JSONObject) {
//            JSONObject obj2 = (JSONObject) obj;
//            return obj2.toJSONString();
//        } else {
//            return obj.toString();
//        }
    }

    private Object convertToValidJSONElement(Object o) throws IllegalAccessException {
        Object json;
        if (isCollection(o.getClass())) {
            json = collectionToJSONArray((Collection<?>) o);
        } else if (o.getClass().isArray()) {
            json = arrayToJSONArray(o);
        } else if (isPrimitive(o.getClass())) {
            json = o;
        } else {
            json = toJSONObject(o);
        }
        return json;
    }

    private JSONObject toJSONObject(Object o) throws IllegalAccessException {
        JSONObject jsonObj = new JSONObject();
        Class<?> aClass = o.getClass();
        Field[] fields = aClass.getDeclaredFields();
        for (Field field : fields) {
            Object fieldVal = field.get(o);
            jsonObj.put(field.getName(), convertToValidJSONElement(fieldVal));
        }
        return jsonObj;
    }

    private JSONArray arrayToJSONArray(Object o) throws IllegalAccessException {
        int size = Array.getLength(o);
        JSONArray jsonArr = new JSONArray();
        for (int i = 0; i < size; i++) {
            Object element = Array.get(o, i);
            jsonArr.add(convertToValidJSONElement(element));
        }
        return jsonArr;
    }

    private JSONArray collectionToJSONArray(Collection<?> collection) throws IllegalAccessException {
        JSONArray jsonArr = new JSONArray();
        for (Object o : collection) {
            jsonArr.add(convertToValidJSONElement(o));
        }
        return jsonArr;
    }

    private boolean isCollection(Class<?> clazz) {
        return Collection.class.isAssignableFrom(clazz);
    }

    private boolean isPrimitive(Class<?> clazz) {
//        if(clazz.isPrimitive()){
//            return true;
//        }
//        if(Number.class.isAssignableFrom(clazz)){
//            return true;
//        }
//        if(clazz.equals(String.class)){
//            return true;
//        }
//        return false;
        return clazz.isPrimitive() || Number.class.isAssignableFrom(clazz) || clazz.equals(String.class);
    }
}
