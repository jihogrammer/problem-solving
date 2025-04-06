package dev.jihogrammer.problem.solving.boj13460;

import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

public class TestCase {

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

    public static int[][] debug(final int[][] board, final int red, final int blue) {
        final var BIT = 4;
        final var MASK = ~(-1 << BIT);
        final var EMPTY = 0;
        final var WALL = 1;
        final var RED = 3;
        final var BLUE = 4;

        var newBoard = new int[board.length][board[0].length];

        var rx = red & MASK;
        var ry = red >> BIT;
        var bx = blue & MASK;
        var by = blue >> BIT;

        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board[y].length; x++) {
                newBoard[y][x] = WALL == board[y][x] ? WALL : EMPTY;
            }
        }

        newBoard[ry][rx] = RED;
        newBoard[by][bx] = BLUE;

        return newBoard;
    }

}
