package task8;

import java.util.concurrent.atomic.AtomicBoolean;

public class ThreadStateSwitcher {

    private static volatile char state = 's';
    private static final AtomicBoolean running = new AtomicBoolean(true);

    public static void main(String[] args) {
        Thread threadA = new Thread(() -> {
            try {
                while (running.get()) {
                    Thread.sleep(100);
                    state = (state == 's') ? 'z' : 's';
                    System.out.println("State changed to " + state);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        Thread threadB = new Thread(() -> {
            try {
                while (running.get()) {
                    while (state != 's') {
                        Thread.sleep(1);
                    }
                    for (int i = 100; i > 0 && state == 's'; i--) {
                        System.out.println("B: " + i);
                        Thread.sleep(1);
                    }
                    System.out.println("B stopped as state changed");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        threadA.start();
        threadB.start();
    }
}