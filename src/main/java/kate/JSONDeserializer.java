package kate;

public interface JSONDeserializer {
    <T> T read(String json, Class<T> clazz);
}
