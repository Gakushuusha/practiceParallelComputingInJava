import blocking.BlockingMatrixMultiplication;
import additions.UtilsForMatrix;
import additions.TimeResult;
import nonblocking.NonBlockingMatrixMultiplication;

public class Main {
    public static void main(String[] args) {


        int size = 2000;

        int[][] A = new int[size][size];
        int[][] B = new int[size][size];

        UtilsForMatrix.generateRandomMatrix(A);
        UtilsForMatrix.generateRandomMatrix(B);


        TimeResult blockingTimeResult = new BlockingMatrixMultiplication(A, B, args).multiply();
        if (blockingTimeResult == null) {
            return;
        }
        blockingTimeResult.printDuration();

//        TimeResult nonBlockingResult = new NonBlockingMatrixMultiplication(A, B, args).multiply();
//        if (nonBlockingResult == null) {
//           return;
//       }
//        nonBlockingResult.printDuration();
    }
}