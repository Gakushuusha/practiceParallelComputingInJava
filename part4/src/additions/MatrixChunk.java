package additions;

public class MatrixChunk {
    private byte[] matrixBuffer;
    private final int startIndex;
    private final int endIndex;

    public MatrixChunk(int startIndex, int endIndex) {
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.matrixBuffer = null;
    }

    public int startIndex() {
        return startIndex;
    }

    public int endIndex() {
        return endIndex;
    }

    public byte[] getMatrixBuffer() {
        return this.matrixBuffer;
    }

    public void setMatrixBuffer(byte[] matrixBuffer) {
        this.matrixBuffer = matrixBuffer;
    }
}