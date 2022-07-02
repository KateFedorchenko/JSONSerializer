package kate;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class JSONSerializerImplTest {
    public static class Data{
        int x;

        public Data(int x){
            this.x = x;
        }
    }
    @ParameterizedTest
    @MethodSource("objectsAndJSONs")
    public void shouldWriteAsString(Object o, String expected) {
        JSONSerializerImpl copy = new JSONSerializerImpl();
        String actual = copy.writeAsString(o);
        assertEquals(expected, actual);
    }

    public static Stream<Arguments> objectsAndJSONs() {
        return Stream.of(
                Arguments.of(new Object(), "{}"),
                Arguments.of(new String[0], "[]"),
                Arguments.of(new ArrayList<String>(),"[]"),
                Arguments.of(new ArrayList<Integer>(),"[]"),
                Arguments.of(new HashSet<String>(),"[]"),
                Arguments.of(new Data(1),"{\"x\":1}"),
                Arguments.of(new int[]{1,2},"[1,2]"),
                Arguments.of(List.of(1,2,3),"[1,2,3]"),
                Arguments.of(null,null),
                Arguments.of(List.of(new Data(1),new Data(2)), "[{\"x\":1},{\"x\":2}]")
        );
    }
}