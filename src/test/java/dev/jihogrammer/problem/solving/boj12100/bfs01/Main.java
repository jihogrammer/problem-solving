package dev.jihogrammer.problem.solving.boj12100.bfs01;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayDeque;

public class Main {

    public static void main(final String[] args) throws IOException {
        System.out.print(solve(System.in));
    }

    public static int solve(final InputStream inputStream) throws IOException {
        var reader = new Reader(inputStream);

        var size = reader.nextInt();
        var board = reader.readBoard(size);

        var solver = new Solver(size);

        return solver.bfs(board);
    }

}

class Solver {

    private final int size;

    private int maxValue;

    Solver(final int size) {
        this.size = size;
    }

    int bfs(final int[][] board) {
        for (int y = 0; y < this.size; y++) {
            for (int x = 0; x < this.size; x++) {
                this.maxValue = Math.max(board[y][x], this.maxValue);
            }
        }

        var queue = new ArrayDeque<int[][]>();
        queue.offer(board);

        for (var depth = 0; depth < 5; depth++) {
            var queueSize = queue.size();

            while (queueSize-- > 0) {
                var currentBoard = queue.poll();

                queue.add(moveUp(currentBoard));
                queue.add(moveDown(currentBoard));
                queue.add(moveLeft(currentBoard));
                queue.add(moveRight(currentBoard));
            }
        }

        return this.maxValue;
    }

    int[][] moveUp(final int[][] board) {
        var nextBoard = new int[this.size][this.size];

        for (var x = 0; x < this.size; x++) {
            var lastTile = 0;
            var ky = 0;

            for (var y = 0; y < this.size; y++) {
                var tile = board[y][x];

                if (tile > 0) {
                    if (tile == lastTile) {
                        var nextTile = tile << 1;

                        nextBoard[ky - 1][x] = nextTile;
                        this.maxValue = Math.max(nextTile, this.maxValue);
                        lastTile = 0;
                    } else {
                        nextBoard[ky++][x] = lastTile = tile;
                    }
                }
            }
        }

        return nextBoard;
    }

    int[][] moveDown(final int[][] board) {
        var nextBoard = new int[this.size][this.size];

        for (var x = 0; x < this.size; x++) {
            var lastTile = 0;
            var ky = this.size - 1;

            for (var y = this.size - 1; y >= 0; y--) {
                var tile = board[y][x];

                if (tile > 0) {
                    if (tile == lastTile) {
                        var nextTile = tile << 1;

                        nextBoard[ky + 1][x] = nextTile;
                        this.maxValue = Math.max(nextTile, this.maxValue);
                        lastTile = 0;
                    } else {
                        nextBoard[ky--][x] = lastTile = tile;
                    }
                }
            }
        }

        return nextBoard;
    }

    int[][] moveLeft(final int[][] board) {
        var nextBoard = new int[this.size][this.size];

        for (var y = 0; y < this.size; y++) {
            var lastTile = 0;
            var kx = 0;

            for (var x = 0; x < this.size; x++) {
                var tile = board[y][x];

                if (tile > 0) {
                    if (tile == lastTile) {
                        var nextTile = tile << 1;

                        nextBoard[y][kx - 1] = nextTile;
                        this.maxValue = Math.max(nextTile, this.maxValue);
                        lastTile = 0;
                    } else {
                        nextBoard[y][kx++] = lastTile = tile;
                    }
                }
            }
        }

        return nextBoard;
    }

    int[][] moveRight(final int[][] board) {
        var nextBoard = new int[this.size][this.size];

        for (var y = 0; y < this.size; y++) {
            var lastTile = 0;
            var kx = this.size - 1;

            for (var x = this.size - 1; x >= 0; x--) {
                var tile = board[y][x];

                if (tile > 0) {
                    if (tile == lastTile) {
                        var nextTile = tile << 1;

                        nextBoard[y][kx + 1] = nextTile;
                        this.maxValue = Math.max(nextTile, this.maxValue);
                        lastTile = 0;
                    } else {
                        nextBoard[y][kx--] = lastTile = tile;
                    }
                }
            }
        }

        return nextBoard;
    }

}

class Reader {

    private static final int NUMBER_MASK = 15;

    private static final int LAST_SPECIAL_CHAR = 32;

    private final InputStream is;

    Reader(final InputStream is) {
        this.is = is;
    }

    int nextInt() throws IOException {
        var c = 0;
        var n = this.is.read() & NUMBER_MASK;

        while (LAST_SPECIAL_CHAR < (c = this.is.read())) {
            n = (n << 3) + (n << 1) + (c & NUMBER_MASK);
        }

        return n;
    }

    int[][] readBoard(final int size) throws IOException {
        var board = new int[size][size];

        for (var y = 0; y < size; y++) {
            for (var x = 0; x < size; x++) {
                board[y][x] = this.nextInt();
            }
        }

        return board;
    }

}
