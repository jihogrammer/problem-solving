package dev.jihogrammer.problem.solving.boj13458.implementation01;

import java.io.IOException;
import java.io.InputStream;

public class Main {

    public static void main(final String[] args) throws IOException {
        System.out.print(solve(System.in));
    }

    public static long solve(final InputStream inputStream) throws IOException {
        var reader = new Reader(inputStream);

        var numberOfTestRoom = reader.nextInt();

        var testRooms = new int[numberOfTestRoom];
        for (var i = 0; i < numberOfTestRoom; i++) {
            testRooms[i] = reader.nextInt();
        }

        var solver = new Solver(numberOfTestRoom, testRooms);

        return solver.solve(reader.nextInt(), reader.nextInt());
    }

}

class Solver {

    private final int numberOfTestRoom;

    private final int[] testRooms;

    public Solver(final int numberOfTestRoom, final int[] testRooms) {
        this.numberOfTestRoom = numberOfTestRoom;
        this.testRooms = testRooms;
    }

    public long solve(final int capacityOfViceSupervisor, final int capacityOfDeputySupervisor) {
        var result = 0L;

        for (var i = 0; i < this.numberOfTestRoom; i++) {
            this.testRooms[i] -= capacityOfViceSupervisor;
            result++;

            if (this.testRooms[i] > 0) {
                result += this.testRooms[i] / capacityOfDeputySupervisor;

                if ((this.testRooms[i] % capacityOfDeputySupervisor) > 0) {
                    result++;
                }
            }
        }

        return result;
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

}
