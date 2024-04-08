package nonblocking;

import additions.MatrixChunk;
import additions.UtilsForMatrix;
import additions.TimeResult;
import mpi.MPI;
import mpi.Request;

public class NonBlockingMatrixMultiplication {
    private final int FROM_MASTER = 1;
    private final int FROM_WORKER = 10;

    private final int[][] A;
    private final int[][] B;

    private final int rowsA;
    private final int colsA;
    private final int rowsB;
    private final int colsB;
    private final String[] args;

    public NonBlockingMatrixMultiplication(int[][] A, int[][] B, String[] args) {
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

        for (var worker = 1; worker <= workerCount; worker++) {
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

        Request[] results = new Request[workerCount];

        for (int worker = 1; worker <= workerCount; worker++) {
            MatrixChunk matrixChunk = MatrixChunks[worker - 1];

            int start = matrixChunk.startIndex();
            int end = matrixChunk.endIndex();

            int resultItemsCount = (end - start + 1) * rowsA;
            byte[] buffer = new byte[UtilsForMatrix.int32ByteSize * resultItemsCount];

            matrixChunk.setMatrixBuffer(buffer);
            results[worker - 1] = MPI.COMM_WORLD.Irecv(buffer, 0,
                    UtilsForMatrix.int32ByteSize * resultItemsCount, MPI.BYTE, worker, FROM_WORKER);
        }

        Request.Waitall(results);

        for (int worker = 1; worker <= workerCount; worker++) {
            MatrixChunk matrixChunk = MatrixChunks[worker - 1];

            int end = matrixChunk.endIndex();
            int start = matrixChunk.startIndex();
            byte[] buffer = matrixChunk.getMatrixBuffer();

            int[][] resultSubMatrix = UtilsForMatrix.bytesToIntMatrix(buffer, end - start, colsA);
            UtilsForMatrix.addSubMatrixToMatrix(resultSubMatrix, result, start, end);
        }

        return new TimeResult(result, (System.currentTimeMillis() - startTime));
    }

    private void processWorker() {
        int[] rowStartIndex = new int[1];
        int[] rowEndIndex = new int[1];

        Request startRow = MPI.COMM_WORLD.Irecv(rowStartIndex, 0, 1, MPI.INT, 0, FROM_MASTER);
        Request endRow = MPI.COMM_WORLD.Irecv(rowEndIndex, 0, 1, MPI.INT, 0, FROM_MASTER);

        Request.Waitall(new Request[]{startRow, endRow});

        int subMatrixSize = (rowEndIndex[0] - rowStartIndex[0] + 1) * rowsA;
        byte[] subMatrixBuffer = new byte[UtilsForMatrix.int32ByteSize * subMatrixSize];
        byte[] matrixBuffer = new byte[UtilsForMatrix.int32ByteSize * rowsB * colsB];

        Request subBuffer = MPI.COMM_WORLD.Irecv(subMatrixBuffer, 0,
                UtilsForMatrix.int32ByteSize * subMatrixSize, MPI.BYTE, 0, FROM_MASTER);

        Request buffer = MPI.COMM_WORLD.Irecv(matrixBuffer, 0,
                UtilsForMatrix.int32ByteSize * rowsB * colsB, MPI.BYTE, 0, FROM_MASTER);

        Request.Waitall(new Request[]{subBuffer, buffer});

        int[][] matrix = UtilsForMatrix.bytesToIntMatrix(matrixBuffer, rowsB, colsB);
        int[][] subMatrix = UtilsForMatrix.bytesToIntMatrix(subMatrixBuffer, rowEndIndex[0] - rowStartIndex[0], colsA);
        int[][] matrixResult = UtilsForMatrix.multiplyMatrices(subMatrix, matrix);
        byte[] matrixResultBuffer = UtilsForMatrix.intMatrixToByteArray(matrixResult);

        MPI.COMM_WORLD.Isend(matrixResultBuffer, 0, matrixResultBuffer.length, MPI.BYTE, 0, FROM_WORKER);
    }
}