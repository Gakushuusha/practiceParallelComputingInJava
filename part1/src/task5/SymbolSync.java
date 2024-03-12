package task5;

public class SymbolSync implements Runnable {
    private final char characterToPrint;
    private final Manager printManager;
    private final boolean initialPermissionState;

    public SymbolSync(char characterToPrint, Manager printManager, boolean initialPermissionState) {
        this.characterToPrint = characterToPrint;
        this.printManager = printManager;
        this.initialPermissionState = initialPermissionState;
    }

    @Override
    public void run() {
        while (!printManager.isStop()) {
            printManager.requestPrintPermission(initialPermissionState, characterToPrint);
        }
    }
}