package task5;

public class SymbolAsync implements Runnable {

    private char c;

    public SymbolAsync(char c) {
        this.c = c;
    }

    @Override
    public void run() {
        for (int i = 0; i < 50; i++) {
            for (int j = 0; j < 80; j++) {
                System.out.print(c);
            }
            System.out.println();
        }
    }
}