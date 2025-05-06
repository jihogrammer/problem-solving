package dev.jihogrammer.problem.solving.boj13458;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class SolutionTest {

    @ParameterizedTest
    @MethodSource("testCases")
    void implementation01(final String input, final long expected) throws IOException {
        // given
        var inputStream = new ByteArrayInputStream(input.getBytes());

        // when
        var result = dev.jihogrammer.problem.solving.boj13458.implementation01.Main.solve(inputStream);

        // then
        assertThat(result).isEqualTo(expected);
    }

    static Stream<Arguments> testCases() {
        return Stream.of(
                Arguments.of("""
                        1
                        1
                        1 1
                        """, 1),
                Arguments.of("""
                        3
                        3 4 5
                        2 2
                        """, 7),
                Arguments.of("""
                        5
                        1000000 1000000 1000000 1000000 1000000
                        5 7
                        """, 714290),
                Arguments.of("""
                        5
                        10 9 10 9 10
                        7 20
                        """, 10),
                Arguments.of("""
                        5
                        10 9 10 9 10
                        7 2
                        """, 13)
        );
    }

}
