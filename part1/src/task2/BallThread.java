package task2;

import java.awt.*;

public class BallThread extends Thread {
    private Ball b;

    public BallThread(Ball ball) {
        this.b = ball;
    }

    @Override
    public void run() {
        try {
            for (int i = 1; i < 10000; i++) {
                int holeNum = b.move();
                if (holeNum != 0) {
                    System.out.println("The ball from" + Thread.currentThread().getName() + " landed in pocket " + holeNum);
                    break;
                }
                Thread.sleep(5);
            }
        } catch (InterruptedException ex) {
            System.out.println(Thread.currentThread().getName() + " stopped.");
        }
    }
}