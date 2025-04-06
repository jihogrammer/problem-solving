package dev.jihogrammer.problem.solving.boj13460.dfs01;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.*;

import static org.assertj.core.api.Assertions.assertThat;

public class SolutionTest {

    @ParameterizedTest
    @MethodSource("dev.jihogrammer.problem.solving.boj13460.TestCase#arguments")
    void test(final String input, final int expected) {
        // given
        var inputStream = new ByteArrayInputStream(input.getBytes());
        var boardHandler = Reader.readInput(inputStream);
        var solver = new Solver(boardHandler);

        // when
        int result = solver.solve();

        // then
        assertThat(result).isEqualTo(expected);
    }

}

/**
 * <blockquote><pre>
 *     - 회사 탈출을 위해 오랜만에 문제 풀이 시작.
 *     - 문제 이름에 있는 '탈출'이 마음에 든다.
 * </pre></blockquote>
 *
 * <pre>
 * 그래프? 보드 흔들기 때 처음에만 4 방향. 다음부터는 직전 방향 제외 3 방향으로 흔들기 가능.
 * '우' -> ['상', '하', '좌'] 중에서만 선택 가능. '우', '우' 순으로 연속해서 같은 방향으로 선택 불가. 맞나?
 * 어우 문제 오랜만에 푸니까 꽤 막힌다.
 * </pre>
 */
class Main {

    public static void main(final String[] args) {
        // given
        BoardHandler boardHandler = Reader.readInput(System.in);
        Solver solver = new Solver(boardHandler);

        // when
        int result = solver.solve();

        // then
        System.out.println(result);
    }

}

class Solver {

    static final int MAX_VALUE = 11;

    final BoardHandler boardHandler;

    int minValue = MAX_VALUE;

    Solver(final BoardHandler boardHandler) {
        this.boardHandler = boardHandler;
    }

    int solve() {
        this.dfs(0, null);
        return MAX_VALUE == this.minValue ? -1 : this.minValue;
    }

    void dfs(final int round, final Direction direction) {
        if (round >= this.minValue) {
            return;
        }

        switch (this.boardHandler.tilt(direction)) {
            case SUCCESS:
                this.minValue = Math.min(this.minValue, round);
            case FAIL:
                return;
        }

        Position redPosition = this.boardHandler.positionOf(BoardInfo.RED);
        Position bluePosition = this.boardHandler.positionOf(BoardInfo.BLUE);

        for (Direction nextDirection : Direction.values()) {
            if (direction == nextDirection) {
                continue;
            }
            this.dfs(round + 1, nextDirection);
            this.boardHandler.reset(BoardInfo.RED, redPosition);
            this.boardHandler.reset(BoardInfo.BLUE, bluePosition);
        }
    }

}

class BoardHandler {

    final int[][] board;

    BoardHandler(final int[][] board) {
        this.board = board;
    }

    MovementResult tilt(final Direction direction) {
        if (null == direction) {
            return MovementResult.IDLE;
        }

        Position redPosition = this.positionOf(BoardInfo.RED);
        Position bluePosition = this.positionOf(BoardInfo.BLUE);
        Position delta = Position.deltaOf(direction);

        final boolean isRedFallen;
        final boolean isBlueFallen;

        if (Direction.UP == direction) {
            if (redPosition.y <= bluePosition.y) {
                isRedFallen = this.move(redPosition, delta);
                isBlueFallen = this.move(bluePosition, delta);
            } else {
                isBlueFallen = this.move(bluePosition, delta);
                isRedFallen = this.move(redPosition, delta);
            }
        } else if (Direction.DOWN == direction) {
            if (redPosition.y <= bluePosition.y) {
                isBlueFallen = this.move(bluePosition, delta);
                isRedFallen = this.move(redPosition, delta);
            } else {
                isRedFallen = this.move(redPosition, delta);
                isBlueFallen = this.move(bluePosition, delta);
            }
        } else if (Direction.LEFT == direction) {
            if (redPosition.x <= bluePosition.x) {
                isRedFallen = this.move(redPosition, delta);
                isBlueFallen = this.move(bluePosition, delta);
            } else {
                isBlueFallen = this.move(bluePosition, delta);
                isRedFallen = this.move(redPosition, delta);
            }
        } else {
            if (redPosition.x <= bluePosition.x) {
                isBlueFallen = this.move(bluePosition, delta);
                isRedFallen = this.move(redPosition, delta);
            } else {
                isRedFallen = this.move(redPosition, delta);
                isBlueFallen = this.move(bluePosition, delta);
            }
        }

        if (isBlueFallen) {
            return MovementResult.FAIL;
        }
        if (isRedFallen) {
            return MovementResult.SUCCESS;
        }
        return MovementResult.IDLE;
    }

    void reset(final int marble, final Position resetPosition) {
        Position currentPosition = this.positionOf(marble);
        if (currentPosition.x > 0 && currentPosition.y > 0) {
            this.board[currentPosition.y][currentPosition.x] = BoardInfo.EMPTY;
        }

        this.board[resetPosition.y][resetPosition.x] = marble;
    }

    int width() {
        return this.board[0].length;
    }

    int height() {
        return this.board.length;
    }

    Position positionOf(final int marble) {
        for (int y = 1; y < this.height() - 1; y++) {
            for (int x = 0; x < this.width() - 1; x++) {
                if (marble == this.board[y][x]) {
                    return new Position(x, y);
                }
            }
        }
        return new Position(-1, -1);
    }

    private boolean move(final Position position, final Position delta) {
        int x = position.x;
        int y = position.y;
        int marble = this.board[y][x];
        boolean isFallen = false;

        this.board[y][x] = BoardInfo.EMPTY;

        while (true) {
            int next = this.board[y + delta.y][x + delta.x];

            if (BoardInfo.HOLE == next) {
                isFallen = true;
            }
            if (BoardInfo.EMPTY != next) {
                break;
            }

            x = x + delta.x;
            y = y + delta.y;
        }

        if (isFallen) {
            return true;
        }

        this.board[y][x] = marble;

        return false;
    }

}

class BoardInfo {

    static final String INPUT_META = ".#ORB";

    static final int EMPTY = INPUT_META.indexOf('.');

    static final int HOLE = INPUT_META.indexOf('O');

    static final int RED = INPUT_META.indexOf('R');

    static final int BLUE = INPUT_META.indexOf('B');

}

enum Direction {
    UP, DOWN, LEFT, RIGHT
}

enum MovementResult {
    IDLE, SUCCESS, FAIL
}

class Position {

    final int x;

    final int y;

    Position(final int x, final int y) {
        this.x = x;
        this.y = y;
    }

    static Position deltaOf(final Direction direction) {
        if (Direction.UP == direction) {
            return new Position(0, -1);
        }
        if (Direction.DOWN == direction) {
            return new Position(0, 1);
        }
        if (Direction.LEFT == direction) {
            return new Position(-1, 0);
        }
        return new Position(1, 0);
    }

}

class Reader {

    static BoardHandler readInput(final InputStream inputStream) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String[] size = br.readLine().split(" ");
            int height = Integer.parseInt(size[0]);
            int width = Integer.parseInt(size[1]);
            int[][] board = new int[height][width];

            for (int y = 0; y < height; y++) {
                String line = br.readLine();
                for (int x = 0; x < width; x++) {
                    board[y][x] = BoardInfo.INPUT_META.indexOf(line.charAt(x));
                }
            }

            return new BoardHandler(board);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
