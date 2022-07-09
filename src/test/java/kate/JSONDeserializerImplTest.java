package kate;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Objects;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class JSONDeserializerImplTest {
    @ParameterizedTest
    @MethodSource({"test", "testComplex"})
    public void test(String json, Object expected) {
        JSONDeserializer deserializer = new JSONDeserializerImpl();
        Class<?> clazz;
        if (expected == null) {
            clazz = Object.class;
        } else {
            clazz = expected.getClass();
        }
        Object actual = deserializer.read(json, clazz);
        assertEquals(expected, actual);
    }

    static Stream<Arguments> test() {
        return Stream.of(
                Arguments.of("1", 1L),
                Arguments.of("\"foo\"", "foo"),
                Arguments.of("null", null),
                Arguments.of("true", true)
        );
    }

    static Stream<Arguments> testComplex() {
        class Data {
            String x;

            public Data(String x) {
                this.x = x;
            }

            public Data() {
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                Data data = (Data) o;
                return Objects.equals(x, data.x);
            }

            @Override
            public int hashCode() {
                return Objects.hash(x);
            }
        }
        return Stream.of(
                Arguments.of("{\"x\":\"foo\"}", new Data("foo")),
                Arguments.of("{\"x\":\"foo\"}", new Data("foo")) // collection + !!!!nested Objects + !!!L issue (Number thing) - separate method
                // recursive for nested Object (obj with collections) // obj - obj - obj -> sanity check // alike Serializer // lombok for test to add
        );
    }
}