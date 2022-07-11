package kate;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class JSONDeserializerImplTest {
    public static class DataString {
        String str;

        public DataString(String str) {
            this.str = str;
        }

        public DataString() {
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            DataString data = (DataString) o;
            return Objects.equals(str, data.str);
        }

        @Override
        public int hashCode() {
            return Objects.hash(str);
        }
    }
    public static class DataInteger {
        int param1;

        public DataInteger(int param1) {
            this.param1 = param1;
        }

        public DataInteger() {
        }
    }

    @ParameterizedTest
    @MethodSource({"testSimple", "objectsWithFields", "nestedObjects", "collectionWithObjects", "simpleCollection"})
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

    static Stream<Arguments> testSimple() {
        return Stream.of(
                Arguments.of("1", 1L),
                Arguments.of("\"foo\"", "foo"),
                Arguments.of("null", null),
                Arguments.of("true", true)
        );
    }
    static Stream<Arguments> objectsWithFields() {
        return Stream.of(
                Arguments.of("{\"str\":\"foo\"}", new DataString("foo")),
                Arguments.of("{\"str\":\"foo\"}", new DataString("foo")),
                /**
                 * new test
                 **/
                Arguments.of("{\"param1\":1}", new DataInteger(1))
        );
    }

    /**
     * Test for collections with objects
     **/
    public static Stream<Arguments> collectionWithObjects() {
        return Stream.of(
                Arguments.of(List.of("[{\"str\":\"params\"},{\"str\":\"logs\"}]", new DataString("params"), new DataString("logs"))),
                Arguments.of(List.of("[{\"param1\":1},{\"param1\":2}]", new DataInteger(1), new DataInteger(2)))
        );
    }

    /**
     * Test for simple collection with data types: String, int
     **/
    public static Stream<Arguments> simpleCollection() {
        return Stream.of(
                Arguments.of("[1,2,3]", List.of(1, 2, 3)),
                Arguments.of("[\"foo\",\"bar\"]", List.of("foo", "bar"))
        );
    }

    /**
     * Test for nested objects
     **/
    public static Stream<Arguments> nestedObjects() {
        class DataComplex {
            DataString dataString;

            public DataComplex(DataString dataString) {
                this.dataString = dataString;
            }
        }
        return Stream.of(
                Arguments.of("{\"dataString\":{\"str\":\"results\"}}", new DataComplex(new DataString("results")))
        );
    }
}

// collection + !!!!nested Objects + !!!L issue (Number thing) - separate method
// recursive for nested Object (obj with collections) // obj - obj - obj -> sanity check // alike Serializer // lombok for test to add