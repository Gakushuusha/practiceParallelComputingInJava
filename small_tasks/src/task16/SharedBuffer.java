package task16;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

public class SharedBuffer {

    public static void main(String[] args) {
        ArrayBlockingQueue<String> sharedBuffer = new ArrayBlockingQueue<>(10);

        Thread threadA = new Thread(() -> {
            try {
                for (int i = 0; i < 20; i++) {
                    sharedBuffer.put("Item " + i);
                    System.out.println("Thread A added: Item " + i);
                    Thread.sleep(100);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        Runnable consumerTask = () -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    String item = sharedBuffer.poll(2, TimeUnit.SECONDS);
                    if (item == null) {
                        break;
                    }
                    System.out.println(Thread.currentThread().getName() + " removed: " + item);
                    Thread.sleep(100);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        };

        Thread threadB = new Thread(consumerTask, "Thread B");
        Thread threadC = new Thread(consumerTask, "Thread C");

        threadA.start();
        threadB.start();
        threadC.start();

        try {
            threadA.join();
            threadB.join();
            threadC.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}