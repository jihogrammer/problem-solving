package dev.jihogrammer.problem.solving.boj13460.dfs02;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;

public class SolutionTest {

    @ParameterizedTest
    @MethodSource("dev.jihogrammer.problem.solving.boj13460.TestCase#arguments")
    void test(final String input, final int expected) throws IOException {
        // given
        var reader = new Reader(new ByteArrayInputStream(input.getBytes()));
        var height = reader.nextInt();
        var width = reader.nextInt();
        var board = reader.readBoard(width, height);
        var solver = new Solver(board);

        // when
        var result = solver.solve();

        // then
        assertThat(result).isEqualTo(expected);
    }

}

/**
 * <blockquote><pre>
 *     - visited 캐싱 및 비트 처리
 *     - position 비트화
 * </pre></blockquote>
 * {@link dev.jihogrammer.problem.solving.boj13460.dfs01.SolutionTest}
 *
 * @see <a href="http://boj.kr/10da5a45a0d64f21a7eb0b17fa1e6394">before</a>
 * @see <a href="https://www.acmicpc.net/source/88219820">direction 최적화</a>
 */
class Main {

    public static void main(final String[] args) throws IOException {
        // given
        var reader = new Reader(System.in);
        var height = reader.nextInt();
        var width = reader.nextInt();
        var board = reader.readBoard(width, height);
        var solver = new Solver(board);

        // when
        var result = solver.solve();

        // then
        System.out.println(result);
    }

}

class Solver {

    static final int MAX_VALUE = 10;

    final BoardHandler boardHandler;

    final int[][] visited;

    int minValue = MAX_VALUE + 1;

    Solver(final int[][] board) {
        this.boardHandler = new BoardHandler(board);
        this.visited = new int[1 << BoardHandler.BIT << BoardHandler.BIT][1 << BoardHandler.BIT << BoardHandler.BIT];
    }

    int solve() {
        this.dfs(1, -1);
        return MAX_VALUE < this.minValue ? -1 : this.minValue;
    }

    void dfs(final int round, final int direction) {
        if (round >= this.minValue) {
            return;
        }

        var directions = direction < 0
                ? new int[]{0, 1, 2, 3}
                : new int[]{(direction + 1) % 4, (direction + 3) % 4};
        var redPosition = this.boardHandler.red;
        var bluePosition = this.boardHandler.blue;

        for (var nextDirection : directions) {
            this.boardHandler.reset(redPosition, bluePosition);
            this.boardHandler.tilt(nextDirection);

            if (this.isVisited(round)) {
                continue;
            } else {
                this.record(round);
            }

            if (this.boardHandler.isBlueFallen()) {
                continue;
            }
            if (this.boardHandler.isRedFallen()) {
                this.minValue = Math.min(this.minValue, round);
                continue;
            }

            this.dfs(round + 1, nextDirection);
        }
    }

    void record(final int round) {
        this.visited[this.boardHandler.red][this.boardHandler.blue] = round;
    }

    boolean isVisited(final int round) {
        var v = this.visited[this.boardHandler.red][this.boardHandler.blue];
        return 0 < v && v < round;
    }
}

class BoardHandler {

    static final int[] DELTA_X = {0, -1, 0, 1};

    static final int[] DELTA_Y = {-1, 0, 1, 0};

    /**
     * {@code N, M <= 10}이므로 4비트({@code 1111(2) == 15(10)}, 괄호 안은 N진법)면 충분하다.
     */
    static final int BIT = 4;

    static final int MASK = ~(-1 << BIT);

    final int[][] board;

    int red;

    int blue;

    BoardHandler(final int[][] board) {
        this.board = board;
        for (var y = 1; y < board.length - 1; y++) {
            for (var x = 1; x < board[y].length - 1; x++) {
                if (BoardInfo.RED == this.board[y][x]) {
                    this.red = (y << BIT) | x;
                }
                if (BoardInfo.BLUE == this.board[y][x]) {
                    this.blue = (y << BIT) | x;
                }
            }
        }
    }

    void tilt(final int direction) {
        var redCount = this.move(BoardInfo.RED, direction);
        var blueCount = this.move(BoardInfo.BLUE, direction);

        if (this.areSameAndNotFallen()) {
            if (redCount > blueCount) {
                var x = (this.red & MASK) - DELTA_X[direction];
                var y = (this.red >> BIT) - DELTA_Y[direction];
                this.red = (y << BIT) | x;
            } else {
                var x = (this.blue & MASK) - DELTA_X[direction];
                var y = (this.blue >> BIT) - DELTA_Y[direction];
                this.blue = (y << BIT) | x;
            }
        }
    }

    void reset(final int red, final int blue) {
        this.red = red;
        this.blue = blue;
    }

    boolean isBlueFallen() {
        return BoardInfo.HOLE == this.board[this.blue >> BIT][this.blue & MASK];
    }

    boolean isRedFallen() {
        return BoardInfo.HOLE == this.board[this.red >> BIT][this.red & MASK];
    }

    private boolean areSameAndNotFallen() {
        return this.red == this.blue && !this.isRedFallen();
    }

    private int move(final int marble, final int direction) {
        var position = BoardInfo.RED == marble ? this.red : this.blue;
        var x = position & MASK;
        var y = position >> BIT;
        var count = 0;

        while (true) {
            var next = this.board[y + DELTA_Y[direction]][x + DELTA_X[direction]];

            if (BoardInfo.WALL == next) {
                break;
            }

            x = x + DELTA_X[direction];
            y = y + DELTA_Y[direction];
            count++;

            if (BoardInfo.HOLE == next) {
                break;
            }
        }

        if (BoardInfo.RED == marble) {
            this.red = (y << BIT) | x;
        } else {
            this.blue = (y << BIT) | x;
        }

        return count;
    }

}

class BoardInfo {

    static final String INPUT_META = ".#ORB";

    static final int EMPTY = INPUT_META.indexOf('.');

    static final int WALL = INPUT_META.indexOf('#');

    static final int HOLE = INPUT_META.indexOf('O');

    static final int RED = INPUT_META.indexOf('R');

    static final int BLUE = INPUT_META.indexOf('B');

}

class Reader {

    static final int NUMBER_MASK = ~(-1 << 4);

    static final int LAST_SPECIAL_CHAR = 32;

    final InputStream is;

    Reader(final InputStream is) {
        this.is = is;
    }

    int nextInt() throws IOException {
        int c;
        var n = this.read() & NUMBER_MASK;

        while (LAST_SPECIAL_CHAR < (c = this.read())) {
            n = (n << 3) + (n << 1) + (c & NUMBER_MASK);
        }

        return n;
    }

    int[][] readBoard(final int width, final int height) throws IOException {
        var board = new int[height][width];

        for (var y = 0; y < height; y++) {
            for (var x = 0; x < width; x++) {
                board[y][x] = BoardInfo.INPUT_META.indexOf(this.read());
            }
            this.read();
        }

        return board;
    }

    private int read() throws IOException {
        return this.is.read();
    }

}
