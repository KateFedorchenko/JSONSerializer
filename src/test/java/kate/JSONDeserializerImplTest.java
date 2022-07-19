package kate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class JSONDeserializerImplTest {
    JSONDeserializer deserializer;

    @Override
    public String toString() {
        return super.toString();
    }

    @EqualsAndHashCode
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    static class DataWithString {
        String x;
    }

    @EqualsAndHashCode
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    static class DataWithByte {
        byte x;
    }

    @EqualsAndHashCode
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    static class DataWithFloat {
        float x;
    }

    @EqualsAndHashCode
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    static class DataWithDouble {
        double x;
    }

    @EqualsAndHashCode
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    static class DataWithInt {
        int x;
    }

    @EqualsAndHashCode
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    static class DataWithChar {
        char x;
    }

    @EqualsAndHashCode
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    static class DataWithShort {
        short x;
    }

    @EqualsAndHashCode
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    static class DataWithBoxedInt {
        Integer x;
    }

    @EqualsAndHashCode
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    static class DataWithBoxedByte {
        Byte x;
    }

    @EqualsAndHashCode
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    static class DataWithBoxedShort {
        Short x;
    }

    @EqualsAndHashCode
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    static class DataWithBoxedLong {
        Long x;
    }

    @EqualsAndHashCode
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    static class DataWithBoxedFloat {
        Float x;
    }

    @EqualsAndHashCode
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    static class DataWithBoxedCharacter {
        Character x;
    }

    @EqualsAndHashCode
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    static class DataWithBoxedDouble {
        Double x;
    }

    @EqualsAndHashCode
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    static class DataWithBoxedBoolean {
        Boolean x;
    }

    @EqualsAndHashCode
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    static class DataWithBoolean {
        boolean x;
    }

    @EqualsAndHashCode
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    static class DataWithLong {
        long x;
    }

    @EqualsAndHashCode
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    static class DataWithBigDecimal {
        BigDecimal x;
    }

    @EqualsAndHashCode
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    static class DataWithNestedData {
        DataWithString d;
    }

    @EqualsAndHashCode
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    static class SuperNestedData {
        DataWithNestedData dd;
    }

    @BeforeEach
    void setUp() {
        deserializer = new JSONDeserializerImpl();
    }

    static Stream<Arguments> objectsAndJsons() {
        //TODO add test for each other primitive type! and their Boxed types
        return Stream.of(

//                arguments(
//                        new DataWithNestedData(new DataWithString("foo")), """
//                                {"d": { "x":"foo" } }"""
//                ),
//                arguments(
//                        new SuperNestedData(new DataWithNestedData(new DataWithString("foo"))), """
//                                { "dd": {"d": { "x":"foo" } } }"""
//                ),
//            arguments(List.of(1, 2, 3, 4), "[1,2,3,4]")//todo watch next
// todo fix work with all standard collection interfaces Set, Queue
//                arguments(List.of(1L, 2L, 3L, 4L), "[1,2,3,4]")
//            arguments(// todo introduce generic type
//                List.of(new DataWithString("foo"), new DataWithString("bar"), new DataWithString("xyz")), """
//                [ {"x":"foo"}, {"x":"bar"}, {"x":"xyz"} ]
//                """
//            )
        );
    }

    static Stream<Arguments> primitivesAndBoxedTypes() {
        return Stream.of(
                arguments(new DataWithByte((byte) 2), "{\"x\":2}"),         // problem here. new DataWithByte(2) throws Exception
                arguments(new DataWithBoxedByte((byte) 1), "{\"x\":1}"),    // problem here. new DataWithBoxedByte(1) throws Exception
                arguments(new DataWithFloat(2.7F), "{\"x\":2.7}"),
                arguments(new DataWithBoxedFloat(1.3F), "{\"x\":1.3}"),
                arguments(new DataWithDouble(2.73), "{\"x\":2.73}"),
                arguments(new DataWithBoxedDouble(2.73), "{\"x\":2.73}"),
                arguments(new DataWithChar('a'), "{\"x\":\"a\"}"),
                arguments((new DataWithBoxedCharacter('1')).toString(), "{\"x\":\"1\"}"),
                arguments(new DataWithShort((short) 2), "{\"x\":2}"),       // problem here. new DataWithShort(2) throws Exception
                arguments(new DataWithBoxedShort((short) 1), "{\"x\":1}"),   // problem here. new DataWithBoxedShort(1) throws Exception
                arguments(new DataWithBoolean(true), "{\"x\":true}"),
                arguments(new DataWithBoxedBoolean(false), "{\"x\":false}"),
                arguments(new DataWithLong(42), "{\"x\":42}"),
                arguments(new DataWithBoxedLong(1L), "{\"x\":1}"),
                arguments(new DataWithInt(42), "{\"x\":42}"),
                arguments(new DataWithBoxedInt(42), "{\"x\":42}"),
                arguments(new DataWithString("foo"), "{\"x\":\"foo\"}"),
                arguments(new DataWithBigDecimal(new BigDecimal("42.0")), "{\"x\":42}"),
                arguments(new DataWithBigDecimal(new BigDecimal("3.14")), "{\"x\":3.14}")
        );
    }

    @ParameterizedTest
    @MethodSource({"objectsAndJsons", "primitivesAndBoxedTypes"})
    public void testJsonToObject(Object expected, String json) {
        Object actual = deserializer.read(json, expected.getClass());
        assertEquals(expected, actual);
    }
}


// class Yylex