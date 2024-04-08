package additions;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

public class UtilsForMatrix {
    public static final int int32ByteSize = 4;

    public static int[][] generateRandomMatrix(int[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                matrix[i][j] = (int) (Math.random() * 100);
            }
        }
        return matrix;
    }

    public static byte[] intMatrixToByteArray(int[][] matrix) {
        ByteBuffer buffer = ByteBuffer.allocate(matrix.length * matrix[0].length * int32ByteSize);
        buffer.order(ByteOrder.nativeOrder());
        IntBuffer intBuffer = buffer.asIntBuffer();
        for (int[] items : matrix) {
            intBuffer.put(items);
        }
        return buffer.array();
    }

    public static int[][] getSubMatrix(int[][] matrix, int startRow, int endRow, int columnsNumber) {
        int[][] subMatrix = new int[endRow - startRow + 1][columnsNumber];
        int resultIndex = 0;
        for (var i = startRow; i < endRow; i++) {
            var temp = new int[columnsNumber];
            System.arraycopy(matrix[i], 0, temp, 0, columnsNumber);

            subMatrix[resultIndex] = temp;
            resultIndex++;
        }

        return subMatrix;
    }

    public static int[][] bytesToIntMatrix(byte[] bytes, int rows, int cols) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        buffer.order(ByteOrder.nativeOrder());
        int[][] matrix = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix[i][j] = buffer.getInt();
            }
        }
        return matrix;
    }

    public static void addSubMatrixToMatrix(int[][] subMatrix, int[][] matrix, int start, int end) {
        int rowIndex = 0;
        for (int i = start; i < end; i++) {
            System.arraycopy(subMatrix[rowIndex], 0, matrix[i], 0, matrix[0].length);
            rowIndex++;
        }
    }

    public static int[][] multiplyMatrices(int[][] A, int[][] B) {
        int[][] result = new int[A.length][B[0].length];
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < B[0].length; j++) {
                for (int k = 0; k < A[0].length; k++) {
                    result[i][j] += A[i][k] * B[k][j];
                }
            }
        }
        return result;
    }
}
