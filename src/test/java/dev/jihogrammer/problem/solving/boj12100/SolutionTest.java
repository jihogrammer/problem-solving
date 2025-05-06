package dev.jihogrammer.problem.solving.boj12100;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class SolutionTest {

    @ParameterizedTest
    @MethodSource("testCase")
    void bfs01(final String input, final int expected) throws IOException {
        // given
        var inputStream = new ByteArrayInputStream(input.getBytes());

        // when
        var result = dev.jihogrammer.problem.solving.boj12100.dfs01.Main.solve(inputStream);

        // then
        assertThat(result).isEqualTo(expected);
    }

    private static Stream<Arguments> testCase() {
        return Stream.of(
                Arguments.of("""
                        3
                        2 2 2
                        4 4 4
                        8 8 8
                        """, 16),
                Arguments.of("""
                        3
                        2 2 2
                        0 0 0
                        0 0 0
                        """, 4),
                Arguments.of("""
                        2
                        0 0
                        0 4
                        """, 4),
                Arguments.of("""
                        3
                        2 32 256
                        128 1024 4
                        512 64 8
                        """, 1024),
                Arguments.of("""
                        4
                        0 0 2 0
                        0 0 0 0
                        2 0 0 0
                        0 0 0 0
                        """, 4),
                Arguments.of("""
                        4
                        4 2 0 0
                        0 0 0 0
                        0 0 0 0
                        2 0 0 0
                        """, 8),
                Arguments.of("""
                        4
                        2 0 2 8
                        0 0 2 2
                        0 0 0 0
                        0 0 0 0
                        """, 16),
                Arguments.of("""
                        4
                        2 4 16 8
                        8 4 0 0
                        16 8 2 0
                        2 8 2 0
                        """, 32),
                Arguments.of("""
                        4
                        2 0 0 0
                        2 2 0 0
                        2 0 0 0
                        0 0 0 0
                        """, 8),
                Arguments.of("""
                        3
                        2 0 2
                        0 0 0
                        0 0 0
                        """, 4),
                Arguments.of("""
                        10
                        1 0 0 0 0 0 0 0 0 1
                        1 0 0 0 0 0 0 0 0 1
                        1 0 0 0 0 0 0 0 0 1
                        1 0 0 0 0 0 0 0 0 1
                        1 0 0 0 0 0 0 0 0 1
                        1 0 0 0 0 0 0 0 0 1
                        1 0 0 0 0 0 0 0 0 1
                        1 0 0 0 0 0 0 0 0 1
                        1 0 0 0 0 0 0 0 0 1
                        1 0 0 0 0 0 0 0 0 1
                        """, 16),
                Arguments.of("""
                        4
                        0 4 4 32
                        4 0 2 64
                        8 8 0 0
                        0 16 64 4
                        """, 64),
                Arguments.of("""
                        4
                        32 64 0 0
                        4 128 256 0
                        128 4 16 2
                        4 128 32 32
                        """, 256)
        );
    }

}
