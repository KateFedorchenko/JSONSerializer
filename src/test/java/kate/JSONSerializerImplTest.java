package kate;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class JSONSerializerImplTest {
    public static class Data{
        int x;

        public Data(int x){
            this.x = x;
        }
    }

    public static class Data0{
        int x;
        String str;

        public Data0(int x, String str){
            this.x = x;
            this.str = str;
        }
    }

    public static class Data1{
        Data d;

        public Data1(Data d){
            this.d = d;
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
//                Arguments.of(new Object(), "{}"),
//                Arguments.of(new String[0], "[]"),
//                Arguments.of(new ArrayList<String>(),"[]"),
//                Arguments.of(new ArrayList<Integer>(),"[]"),
//                Arguments.of(new HashSet<String>(),"[]"),
//                Arguments.of(new Data(1),"{\"x\":1}"),
//                Arguments.of(new int[]{1,2},"[1,2]"),
//                Arguments.of(List.of(1,2,3),"[1,2,3]"),
//                Arguments.of(List.of("foo","bar"),"[\"foo\",\"bar\"]"),
//                Arguments.of(null,null),
//                Arguments.of(1,"1"),
//                Arguments.of("foo","foo"),
//                Arguments.of(List.of(new Data(1),new Data(2)), "[{\"x\":1},{\"x\":2}]"),
//                Arguments.of(List.of(new Data(1),2), "[{\"x\":1},2]"),
//                Arguments.of(List.of(new Data0(1,"foo"),new Data0(2,"bar")), "[{\"str\":\"foo\",\"x\":1},{\"str\":\"bar\",\"x\":2}]"),
                Arguments.of(new Data1(new Data(2)), "")
        );
    }
}