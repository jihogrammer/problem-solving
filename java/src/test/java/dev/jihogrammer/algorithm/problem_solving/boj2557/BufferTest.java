package dev.jihogrammer.algorithm.problem_solving.boj2557;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

public class BufferTest {

    @Test
    public void makeHelloWorldString() {
        String helloWorld = "Hello World!";
        List<Integer> intStream = new ArrayList<>();

        helloWorld.chars().forEach(ch -> intStream.add(ch));
        System.out.println(intStream.toString());

        assertArrayEquals(
                new Object[] { 72, 101, 108, 108, 111, 32, 87, 111, 114, 108, 100, 33 },
                intStream.toArray());
    }

}
