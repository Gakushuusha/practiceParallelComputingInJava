package blocking;

import additions.MatrixChunk;
import additions.UtilsForMatrix;
import additions.TimeResult;
import mpi.MPI;

public class BlockingMatrixMultiplication {
    private final int FROM_MASTER = 1;
    private final int FROM_WORKER = 2;

    private final int[][] A;
    private final int[][] B;

    private final int rowsA;
    private final int colsA;
    private final int rowsB;
    private final int colsB;
    private final String[] args;

    public BlockingMatrixMultiplication(int[][] A, int[][] B, String[] args) {
        this.A = A;
        this.B = B;

        this.rowsA = this.A.length;
        this.colsA = this.A[0].length;
        this.rowsB = this.B.length;
        this.colsB = this.B[0].length;

        this.args = args;
    }

    public TimeResult multiply() {
        try {
            MPI.Init(args);
            int size = MPI.COMM_WORLD.Size();
            int rank = MPI.COMM_WORLD.Rank();
            if (size < 2) {
                return null;
            }

            if (rank == 0) {
                return processMaster(size);
            } else {
                processWorker();
            }
            return null;
        } finally {
            MPI.Finalize();
        }
    }

    private TimeResult processMaster(int size) {
        long startTime = System.currentTimeMillis();

        int workerCount = size - 1;
        int rowsPerWorker = rowsA / workerCount;
        int extraRows = rowsA % workerCount;

        int[][] result = new int[rowsA][rowsB];
        byte[] matrixBBuffer = UtilsForMatrix.intMatrixToByteArray(B);

        MatrixChunk[] MatrixChunks = new MatrixChunk[workerCount];

        for (int worker = 1; worker <= workerCount; worker++) {
            int startRow = (worker - 1) * rowsPerWorker;
            int endRow = startRow + rowsPerWorker;

            if (worker == workerCount) {
                endRow += extraRows;
            }

            MatrixChunks[worker - 1] = new MatrixChunk(startRow, endRow);

            int[][] subMatrixA = UtilsForMatrix.getSubMatrix(A, startRow, endRow, rowsB);
            byte[] subMatrixABuffer = UtilsForMatrix.intMatrixToByteArray(subMatrixA);
            int subMatrixSize = (endRow - startRow + 1) * rowsA;

            MPI.COMM_WORLD.Send(new int[]{startRow}, 0, 1, MPI.INT, worker, FROM_MASTER);
            MPI.COMM_WORLD.Send(new int[]{endRow}, 0, 1, MPI.INT, worker, FROM_MASTER);
            MPI.COMM_WORLD.Send(subMatrixABuffer, 0,
                    UtilsForMatrix.int32ByteSize * subMatrixSize, MPI.BYTE, worker, FROM_MASTER);
            MPI.COMM_WORLD.Send(matrixBBuffer, 0,
                    UtilsForMatrix.int32ByteSize * rowsB * colsB, MPI.BYTE, worker, FROM_MASTER);
        }

        for (int worker = 1; worker <= workerCount; worker++) {
            MatrixChunk matrixChunk = MatrixChunks[worker - 1];

            int start = matrixChunk.startIndex();
            int end = matrixChunk.endIndex();

            int resultItemsCount = (end - start + 1) * rowsA;
            byte[] buffer = new byte[UtilsForMatrix.int32ByteSize * resultItemsCount];

            MPI.COMM_WORLD.Recv(buffer, 0,
                    UtilsForMatrix.int32ByteSize * resultItemsCount, MPI.BYTE, worker, FROM_WORKER);

            int[][] resultSubMatrix = UtilsForMatrix.bytesToIntMatrix(buffer, end - start, colsA);
            UtilsForMatrix.addSubMatrixToMatrix(resultSubMatrix, result, start, end);
        }

        return new TimeResult(result, (System.currentTimeMillis() - startTime));
    }

    private void processWorker() {
        int[] startRow = new int[1];
        int[] endRow = new int[1];

        MPI.COMM_WORLD.Recv(startRow, 0, 1, MPI.INT, 0, FROM_MASTER);
        MPI.COMM_WORLD.Recv(endRow, 0, 1, MPI.INT, 0, FROM_MASTER);

        int subMatrixSize = (endRow[0] - startRow[0] + 1) * rowsA;
        byte[] subMatrixBuffer = new byte[UtilsForMatrix.int32ByteSize * subMatrixSize];
        byte[] matrixBuffer = new byte[UtilsForMatrix.int32ByteSize * rowsB * colsB];

        MPI.COMM_WORLD.Recv(subMatrixBuffer, 0,
                UtilsForMatrix.int32ByteSize * subMatrixSize, MPI.BYTE, 0, FROM_MASTER);
        MPI.COMM_WORLD.Recv(matrixBuffer, 0,
                UtilsForMatrix.int32ByteSize * rowsB * colsB, MPI.BYTE, 0, FROM_MASTER);

        int[][] matrix = UtilsForMatrix.bytesToIntMatrix(matrixBuffer, rowsB, colsB);
        int[][] subMatrix = UtilsForMatrix.bytesToIntMatrix(subMatrixBuffer, endRow[0] - startRow[0], colsA);

        int[][] matrixResult = UtilsForMatrix.multiplyMatrices(subMatrix, matrix);
        byte[] matrixResultBuffer = UtilsForMatrix.intMatrixToByteArray(matrixResult);

        MPI.COMM_WORLD.Send(matrixResultBuffer, 0, matrixResultBuffer.length, MPI.BYTE, 0, FROM_WORKER);
    }
}