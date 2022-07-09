package kate;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class JSONSerializerImplTest {
    public static class Data {
        int x;

        public Data(int x) {
            this.x = x;
        }
    }
    public static class Data0 {
        int x;
        String str;

        public Data0(int x, String str) {
            this.x = x;
            this.str = str;
        }
    }

    @ParameterizedTest
    @MethodSource({"simpleObjects", "simpleCollection", "objectsWithFields", "collectionWithObjects", "objectsWithComplexFields"})
    public void shouldWriteAsString(Object o, String expected) {
        JSONSerializerImpl copy = new JSONSerializerImpl();
        String actual = copy.writeAsString(o);
        assertEquals(expected, actual);
    }

    public static Stream<Arguments> simpleObjects() {
        return Stream.of(
                Arguments.of(null, null),
                Arguments.of(1, "1"),
                Arguments.of("foo", "foo"),
                Arguments.of(new Object(), "{}")
        );
    }
    public static Stream<Arguments> objectsWithFields() {
        class DataWithString {
            String str;

            public DataWithString(String str) {
                this.str = str;
            }
        }
        class DataWithIntAndString {
            String str;
            int x;

            public DataWithIntAndString(String str, int x) {
                this.str = str;
                this.x = x;
            }
        }
        return Stream.of(
                Arguments.of(new Data(1), "{\"x\":1}"),
                Arguments.of(new DataWithString("foo"), "{\"str\":\"foo\"}"),
                Arguments.of(new DataWithIntAndString("foo", 1), "{\"str\":\"foo\",\"x\":1}")
        );
    }
    public static Stream<Arguments> simpleCollection() {
        return Stream.of(
                Arguments.of(new String[0], "[]"),
                Arguments.of(new ArrayList<String>(), "[]"),
                Arguments.of(new ArrayList<Integer>(), "[]"),
                Arguments.of(new HashSet<String>(), "[]"),
                Arguments.of(new int[]{1, 2}, "[1,2]"),
                Arguments.of(List.of(1, 2, 3), "[1,2,3]"),
                Arguments.of(List.of("foo", "bar"), "[\"foo\",\"bar\"]")
        );
    }
    public static Stream<Arguments> collectionWithObjects() {
        return Stream.of(
                Arguments.of(List.of(new Data(1), new Data(2)), "[{\"x\":1},{\"x\":2}]"),
                Arguments.of(List.of(new Data(1), 2), "[{\"x\":1},2]"),
                Arguments.of(List.of(new Data0(1, "foo"), new Data0(2, "bar")), "[{\"str\":\"foo\",\"x\":1},{\"str\":\"bar\",\"x\":2}]")
        );
    }
    public static Stream<Arguments> objectsWithComplexFields() {
        class DataComplex {
            Data d;

            public DataComplex(Data d) {
                this.d = d;
            }
        }
        return Stream.of(
                Arguments.of(new DataComplex(new Data(2)), "{\"d\":{\"x\":2}}")
        );
    }

}