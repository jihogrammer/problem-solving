package dev.jihogrammer.problem.solving.boj.boj13460;

import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

class TestCase {

    static Stream<Arguments> arguments() {
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
