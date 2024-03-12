package task5;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Manager {
    private boolean permission = false;
    private boolean stop = false;
    private int currentCharCount = 0;
    private int currentLineCount = 0;

    private final int maxCharCountPerLine;
    private final int maxLineCount;

    public Manager(int maxCharCountPerLine, int maxLineCount) {
        this.maxCharCountPerLine = maxCharCountPerLine;
        this.maxLineCount = maxLineCount;
    }

    public synchronized void requestPrintPermission(boolean requestedPermission, char ch) {
        awaitPermissionAlignment(requestedPermission);
        togglePermission();
        incrementCharCounter();

        printCharacter(ch);
        checkAndIncrementLineCounter();
        checkCompletion();

        notifyAll();
    }

    private void awaitPermissionAlignment(boolean requestedPermission) {
        while (permission != requestedPermission) {
            try {
                wait();
            } catch (InterruptedException exception) {
                Logger.getLogger(Manager.class.getName()).log(Level.SEVERE, "Thread interrupted", exception);
            }
        }
    }

    private void togglePermission() {
        permission = !permission;
    }

    private void incrementCharCounter() {
        currentCharCount++;
    }

    private void printCharacter(char ch) {
        if (!stop) {
            System.out.print(ch);
        }
    }

    private void checkAndIncrementLineCounter() {
        if (currentCharCount == maxCharCountPerLine) {
            currentCharCount = 0;
            System.out.println(" " + currentLineCount);
            currentLineCount++;
        }
    }

    private void checkCompletion() {
        if (currentLineCount == maxLineCount) {
            stop = true;
        }
    }

    public synchronized boolean isStop() {
        return stop;
    }
}