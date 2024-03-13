package task2;

import java.util.Random;

public class Producer implements Runnable {
    private Drop drop;
    private int[] arr;

    public Producer(Drop drop, int size) {
        this.drop = drop;
        this.arr = initArray(size);
    }

    public void run() {
        Random random = new Random();

        for (int i : arr) {
            drop.put(i);
            try {
                Thread.sleep(random.nextInt(50));
            } catch (InterruptedException ignored) {
                // ._.
            }
        }
        drop.put(0);
    }

    private int[] initArray(int size) {
        int[] arr = new int[size];
        for (int i = 0; i < size; i++) {
            arr[i] = i + 1;
        }
        return arr;
    }
}