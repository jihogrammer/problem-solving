package dev.jihogrammer.problem.solving.boj13460.bfs01;

import java.io.IOException;
import java.io.InputStream;

public class Agent {

    public static int solve(final InputStream inputStream) throws IOException {
        return Main.solver(inputStream).solve();
    }

}

/**
 * <blockquote><pre>
 *     - DFS 방식으로 억지로 풀어냈다가, 정배로 BFS 방식으로 다시 푼 방식
 *     - 개선 포인트: queue capacity, queue performance, initial marble position, and board type.
 * </pre></blockquote>
 * @see <a href="http://boj.kr/885ce65c4879416aa0d3cbd53a289402">submit</a>
 */
class Main {

    public static void main(final String[] args) throws IOException {
        System.out.println(solver(System.in).solve());
    }

    static Solver solver(final InputStream inputStream) throws IOException {
        var reader = new Reader(inputStream);
        var height = reader.nextInt();
        var width = reader.nextInt();
        var boardHandler = reader.readBoard(width, height);

        return new Solver(boardHandler);
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

    private final BoardHandler boardHandler;

    Solver(final BoardHandler boardHandler) {
        this.boardHandler = boardHandler;
    }

    int solve() {
        return this.bfs();
    }

    private int bfs() {
        var queue = new Queue();
        var visited = new boolean[1 << BIT][1 << BIT];

        visited[this.boardHandler.red()][this.boardHandler.blue()] = true;
        queue.push( 1
                | (4 << BIT)
                | (this.boardHandler.red() << BIT << BIT)
                | (this.boardHandler.blue() << BIT << BIT << BIT));

        while (queue.hasNext()) {
            var status = queue.pop();
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
                var redStatus = this.boardHandler.tilt(red, nextDirection);
                var blueStatus = this.boardHandler.tilt(blue, nextDirection);

                var nextRed = redStatus & MASK;
                var nextBlue = blueStatus & MASK;

                if (this.boardHandler.isFallen(nextBlue)) {
                    continue;
                }

                if (this.boardHandler.isFallen(nextRed)) {
                    return round;
                }

                if (nextRed == nextBlue) {
                    var redCount = redStatus >> BIT;
                    var blueCount = blueStatus >> BIT;

                    if (redCount > blueCount) {
                        nextRed = this.boardHandler.reposition(nextRed, nextDirection);
                    } else {
                        nextBlue = this.boardHandler.reposition(nextBlue, nextDirection);
                    }
                }

                if (!visited[nextRed][nextBlue]) {
                    visited[nextRed][nextBlue] = true;
                    queue.push((round + 1)
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

    private final byte[][] board;

    private int red;

    private int blue;

    BoardHandler(final byte[][] board) {
        this.board = board;
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
        return (((marble >> BIT) - DY[direction]) << BIT) | ((marble & MASK) - DX[direction]);
    }

    int red() {
        return this.red;
    }

    int blue() {
        return this.blue;
    }

    void red(final int x, final int y) {
        this.red = (y << BIT) | x;
    }

    void blue(final int x, final int y) {
        this.blue = (y << BIT) | x;
    }

    boolean isFallen(final int marble) {
        return BoardInfo.HOLE == this.board[marble >> BIT][marble & MASK];
    }

}

class Queue {

    /**
     * 1회차 4방향, 2회차 2방향, 3회차 2방향, ... 순으로 경우의 수가 결정된다.
     * <pre>{@code 4 * 2 * 2 * ... == (2 ^ 11) == (1 << 11) == (2 << 10)}</pre>
     * 따라서 큐의 크기는 {@code 2 << 10}을 넘지 않는다.
     * 큐가 항상 차 있는 것은 아니기 때문에 크기를 줄이고 pointer 마스킹 처리도 시도할 만하다.
     */
    private final int[] queue = new int[2 << BoardInfo.MAX_ROUND];

    private int head;

    private int tail;

    boolean hasNext() {
        return head != tail;
    }

    void push(final int v) {
        queue[tail++] = v;
    }

    int pop() {
        return queue[head++];
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
        var board = new byte[height][width];
        var boardHandler = new BoardHandler(board);
        var buf = new byte[width];

        for (var y = 0; y < height; y++) {
            this.is.read(buf, 0, buf.length);
            for (var x = 0; x < width; x++) {
                var tile = buf[x];
                if (BoardInfo.RED == tile) {
                    boardHandler.red(x, y);
                }
                if (BoardInfo.BLUE == tile) {
                    boardHandler.blue(x, y);
                }
                board[y][x] = tile;
            }
            this.is.read();
        }

        return boardHandler;
    }

}
