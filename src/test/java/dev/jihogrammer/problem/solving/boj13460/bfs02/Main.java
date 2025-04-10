package dev.jihogrammer.problem.solving.boj13460.bfs02;

import java.io.IOException;
import java.io.InputStream;

/**
 * <blockquote><pre>
 *     - BFS
 *     - queue capacity
 *     - queue performance
 *     - initial marble position
 *     - board type
 * </pre></blockquote>
 *
 * <p>{@link dev.jihogrammer.problem.solving.boj13460.bfs01.Agent} 개선</p>
 *
 * @see <a href="TODO">submit</a>
 */
public class Main {

    public static void main(final String[] args) throws IOException {
        System.out.println(solve(System.in));
    }

    public static int solve(final InputStream inputStream) throws IOException {
        var reader = new Reader(inputStream);

        var height = reader.nextInt();
        var width = reader.nextInt();
        var boardHandler = reader.readBoard(width, height);

        return Solver.solve(boardHandler);
    }

}

class BoardInfo {

    static final int MAX_ROUND = 10;

    static final int WALL = '#';

    static final int HOLE = 'O';

    static final int RED = 'R';

    static final int BLUE = 'B';

}

class Solver {

    private static final int BIT = 4 << 1;

    private static final int MASK = ~(-1 << BIT);

    static int solve(final BoardHandler boardHandler) {
        return bfs(boardHandler);
    }

    private static int bfs(final BoardHandler boardHandler) {
        var queue = new Queue();
        var visited = new boolean[1 << BIT][1 << BIT];

        visited[boardHandler.initialRed][boardHandler.initialBlue] = true;
        queue.offer(1
                | (4 << BIT)
                | (boardHandler.initialRed << BIT << BIT)
                | (boardHandler.initialBlue << BIT << BIT << BIT));

        while (queue.hasNext()) {
            var status = queue.poll();
            var round = status & MASK;
            var direction = (status >> BIT) & MASK;
            var red = (status >> BIT >> BIT) & MASK;
            var blue = (status >> BIT >> BIT >> BIT) & MASK;

            if (round > BoardInfo.MAX_ROUND) {
                break;
            }

            var directions = direction == 4
                    ? new int[]{0, 1, 2, 3}
                    : new int[]{(direction + 1) % 4, (direction + 3) % 4};

            for (var nextDirection : directions) {
                var redStatus = boardHandler.tilt(red, nextDirection);
                var blueStatus = boardHandler.tilt(blue, nextDirection);

                var nextRed = redStatus & MASK;
                var nextBlue = blueStatus & MASK;

                if (boardHandler.isFallen(nextBlue)) {
                    continue;
                }

                if (boardHandler.isFallen(nextRed)) {
                    return round;
                }

                if (nextRed == nextBlue) {
                    var redCount = redStatus >> BIT;
                    var blueCount = blueStatus >> BIT;

                    if (redCount > blueCount) {
                        nextRed = boardHandler.reposition(nextRed, nextDirection);
                    } else {
                        nextBlue = boardHandler.reposition(nextBlue, nextDirection);
                    }
                }

                if (!visited[nextRed][nextBlue]) {
                    visited[nextRed][nextBlue] = true;
                    queue.offer((round + 1)
                            | (nextDirection << BIT)
                            | (nextRed << BIT << BIT)
                            | (nextBlue << BIT << BIT << BIT));
                }
            }
        }

        return -1;
    }

}

class BoardHandler {

    static final int[] DX = {0, -1, 0, 1};

    static final int[] DY = {-1, 0, 1, 0};

    private static final int BIT = 4;

    private static final int MASK = ~(-1 << BIT);

    private final int[][] board;

    final int initialRed;

    final int initialBlue;

    BoardHandler(final int[][] board, final int rx, final int ry, final int bx, final int by) {
        this.board = board;
        this.initialRed = (ry << BIT) | rx;
        this.initialBlue = (by << BIT) | bx;
    }

    int tilt(final int marble, final int direction) {
        var x = marble & MASK;
        var y = marble >> BIT;
        var count = 0;

        while (true) {
            var next = this.board[y + DY[direction]][x + DX[direction]];

            if (BoardInfo.WALL == next) {
                break;
            }

            x = x + DX[direction];
            y = y + DY[direction];
            count++;

            if (BoardInfo.HOLE == next) {
                break;
            }
        }

        return (count << BIT << BIT) | (y << BIT) | x;
    }

    int reposition(final int marble, final int direction) {
        var x = (marble & MASK) - DX[direction];
        var y = (marble >> BIT) - DY[direction];
        return (y << BIT) | x;
    }

    boolean isFallen(final int marble) {
        return BoardInfo.HOLE == this.board[marble >> BIT][marble & MASK];
    }

}

class Queue {

    /**
     * <blockquote><pre>
     *     계산 때려 보니까 최대 크기 4096 이긴 함.
     *     그냥 비트 내리면서 문제 풀리는 구간까지 처리함.
     * </pre></blockquote>
     * {@code 32(10) == 100000(2)}
     */
    private static final int MAX_CAPACITY = 1 << 5;

    private static final int MASK = MAX_CAPACITY - 1;

    private final int[] queue = new int[MAX_CAPACITY];

    private int head;

    private int tail;

    boolean hasNext() {
        return head < tail;
    }

    void offer(final int v) {
        queue[tail++ & MASK] = v;
    }

    int poll() {
        return queue[head++ & MASK];
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
        int c;
        int n = this.is.read() & NUMBER_MASK;

        while (LAST_SPECIAL_CHAR < (c = this.is.read())) {
            n = (n << 3) + (n << 1) + (c & NUMBER_MASK);
        }

        return n;
    }

    BoardHandler readBoard(final int width, final int height) throws IOException {
        var board = new int[height][width];
        var buf = new byte[width];

        var rx = -1;
        var ry = -1;
        var bx = -1;
        var by = -1;

        for (var y = 0; y < height; y++) {
            this.is.read(buf, 0, buf.length);
            for (var x = 0; x < width; x++) {
                var tile = buf[x];
                if (BoardInfo.RED == tile) {
                    rx = x;
                    ry = y;
                }
                if (BoardInfo.BLUE == tile) {
                    bx = x;
                    by = y;
                }
                board[y][x] = tile;
            }
            this.is.read();
        }

        if ((rx | ry | bx | by) < 0) {
            throw new IllegalArgumentException("구슬 없음");
        }

        return new BoardHandler(board, rx, ry, bx, by);
    }

}
