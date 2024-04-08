package additions;

public final class TimeResult {
    private int[][] matrix;
    private final long duration;

    public TimeResult(int[][] matrix, long duration) {
        this.matrix = matrix;
        this.duration = duration;
    }

    public int[][] getMatrix() {
        return this.matrix;
    }

    public void printMatrix() {
        System.out.println("------");
        for (int[] ints : matrix) {
            for (int j = 0; j < matrix[0].length; j++) {
                System.out.print(ints[j] + "\t");
            }
            System.out.println();
        }
    }

    public void printDuration() {
        System.out.println("The number of milliseconds: " + duration + "\n");
    }

    public void printResult() {
        printMatrix();
        printDuration();
    }
}
