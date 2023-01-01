package dev.jihogrammer.algorithm.problem_solving.boj2557;

import java.io.IOException;

public class Buffer {

    private final byte[] HELLO_WORLD_BUFFER = { 72, 101, 108, 108, 111, 32, 87, 111, 114, 108, 100, 33 };

    public void solve() throws IOException {
        System.out.write(HELLO_WORLD_BUFFER);
    }

}
