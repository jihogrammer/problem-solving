package dev.jihogrammer.problem.solving.boj12100.dfs01;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Objects;

public class Main {

    public static void main(final String[] args) throws IOException {
        System.out.print(solve(System.in));
    }

    public static int solve(final InputStream inputStream) throws IOException {
        var reader = new Reader(inputStream);

        var size = reader.nextInt();
        var board = reader.readBoard(size);

        var solver = new Solver(board);

        return solver.solve();
    }

}

class Solver {

    private static final int MAX_ROUND = 5;

    private static final int[] DX = {0, 0, -1, 1};

    private static final int[] DY = {-1, 1, 0, 0};

    private int[][] board;

    private int maxValue;

    Solver(final int[][] board) {
        this.board = board;
    }

    int solve() {
        this.dfs(1);
        return this.maxValue;
    }

    private void dfs(final int round) {
        if (MAX_ROUND < round) {
            return;
        }

        for (var direction = 0; direction < 4; direction++) {
            // ready for recovery
            var beforeBoard = this.copyBoard();

            // move
            this.move(direction);

            // next
            this.dfs(round + 1);

            // recovery
            this.board = beforeBoard;
        }
    }

    private void move(final int direction) {
        var beforeNumbers = new ArrayList<Integer>();
        var afterNumbers = new ArrayDeque<Integer>();
        var isMerged = false;

        // UP
        if (direction == 0) {
            for (var x = 0; x < this.board.length; x++) {

                for (var y = 0; y < this.board.length; y++) {
                    if (this.board[y][x] > 0) {
                        beforeNumbers.add(this.board[y][x]);
                    }
                    this.board[y][x] = 0;
                }

                for (Integer value : beforeNumbers) {
                    if (Objects.equals(afterNumbers.peekFirst(), value) && !isMerged) {
                        afterNumbers.push(afterNumbers.pop() << 1);
                        isMerged = true;
                    } else {
                        afterNumbers.push(value);
                        isMerged = false;
                    }
                }
                beforeNumbers.clear();

                var afterY = 0;
                while (!afterNumbers.isEmpty()) {
                    var value = afterNumbers.pollLast();
                    this.board[afterY++][x] = value;
                    this.maxValue = Math.max(this.maxValue, value);
                }
            }
        }
        // DOWN
        else if (direction == 1) {
            for (var x = 0; x < this.board.length; x++) {
                for (var y = this.board.length - 1; y >= 0; y--) {
                    if (this.board[y][x] > 0) {
                        beforeNumbers.add(this.board[y][x]);
                    }
                    this.board[y][x] = 0;
                }

                for (Integer value : beforeNumbers) {
                    if (Objects.equals(afterNumbers.peekFirst(), value) && !isMerged) {
                        afterNumbers.push(afterNumbers.pop() << 1);
                        isMerged = true;
                    } else {
                        afterNumbers.push(value);
                        isMerged = false;
                    }
                }
                beforeNumbers.clear();

                var afterY = this.board.length - 1;
                while (!afterNumbers.isEmpty()) {
                    var value = afterNumbers.pollLast();
                    this.board[afterY--][x] = value;
                    this.maxValue = Math.max(this.maxValue, value);
                }
            }
        }
        // LEFT
        else if (direction == 2) {
            for (var y = 0; y < this.board.length; y++) {
                for (var x = 0; x < this.board.length; x++) {
                    if (this.board[y][x] > 0) {
                        beforeNumbers.add(this.board[y][x]);
                    }
                    this.board[y][x] = 0;
                }

                for (Integer value : beforeNumbers) {
                    if (Objects.equals(afterNumbers.peekFirst(), value) && !isMerged) {
                        afterNumbers.push(afterNumbers.pop() << 1);
                        isMerged = true;
                    } else {
                        afterNumbers.push(value);
                        isMerged = false;
                    }
                }
                beforeNumbers.clear();

                var afterX = 0;
                while (!afterNumbers.isEmpty()) {
                    var value = afterNumbers.pollLast();
                    this.board[y][afterX++] = value;
                    this.maxValue = Math.max(this.maxValue, value);
                }
            }
        }
        // RIGHT
        else if (direction == 3) {
            for (var y = 0; y < this.board.length; y++) {
                for (var x = this.board.length - 1; x >= 0; x--) {
                    if (this.board[y][x] > 0) {
                        beforeNumbers.add(this.board[y][x]);
                    }
                    this.board[y][x] = 0;
                }

                for (Integer value : beforeNumbers) {
                    if (Objects.equals(afterNumbers.peekFirst(), value) && !isMerged) {
                        afterNumbers.push(afterNumbers.pop() << 1);
                        isMerged = true;
                    } else {
                        afterNumbers.push(value);
                        isMerged = false;
                    }
                }
                beforeNumbers.clear();

                var afterX = this.board.length - 1;
                while (!afterNumbers.isEmpty()) {
                    var value = afterNumbers.pollLast();
                    this.board[y][afterX--] = value;
                    this.maxValue = Math.max(this.maxValue, value);
                }
            }
        }
    }

    private int[][] copyBoard() {
        var copy = new int[this.board.length][this.board.length];

        for (var y = 0; y < this.board.length; y++) {
            System.arraycopy(this.board[y], 0, copy[y], 0, board.length);
        }

        return copy;
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
