package dev.jihogrammer.problem.solving.boj13460;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class BOJ13460Test {

    @ParameterizedTest
    @MethodSource("testCase")
    void dfs01(final String input, final int expected) {
        // given
        var inputStream = new ByteArrayInputStream(input.getBytes());

        // when
        int result = dev.jihogrammer.problem.solving.boj13460.dfs01.Agent.solve(inputStream);

        // then
        assertThat(result).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("testCase")
    void dfs02(final String input, final int expected) throws IOException {
        // given
        var inputStream = new ByteArrayInputStream(input.getBytes());

        // when
        var result = dev.jihogrammer.problem.solving.boj13460.dfs02.Agent.solve(inputStream);

        // then
        assertThat(result).isEqualTo(expected);
    }

    static Stream<Arguments> testCase() {
        return Stream.of(
                Arguments.of("""
                        5 5
                        #####
                        #..B#
                        #.#.#
                        #RO.#
                        #####""", 1),
                Arguments.of("""
                        7 7
                        #######
                        #...RB#
                        #.#####
                        #.....#
                        #####.#
                        #O....#
                        #######""", 5),
                Arguments.of("""
                        7 7
                        #######
                        #..R#B#
                        #.#####
                        #.....#
                        #####.#
                        #O....#
                        #######""", 5),
                Arguments.of("""
                        10 10
                        ##########
                        #R#...##B#
                        #...#.##.#
                        #####.##.#
                        #......#.#
                        #.######.#
                        #.#....#.#
                        #.#.#.#..#
                        #...#.O#.#
                        ##########""", -1),
                Arguments.of("""
                        3 7
                        #######
                        #R.O.B#
                        #######""", 1),
                Arguments.of("""
                        10 10
                        ##########
                        #R#...##B#
                        #...#.##.#
                        #####.##.#
                        #......#.#
                        #.######.#
                        #.#....#.#
                        #.#.##...#
                        #O..#....#
                        ##########""", 7),
                Arguments.of("""
                        3 10
                        ##########
                        #.O....RB#
                        ##########""", -1));
    }

}
