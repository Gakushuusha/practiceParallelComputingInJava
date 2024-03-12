package task3;

public class BallThread extends Thread {
    private Ball b;

    public BallThread(Ball ball) {
        this.b = ball;
    }

    @Override
    public void run() {
        try {
            for (int i = 1; i < 10000; i++) {
                b.move();
                Thread.sleep(5);
            }
        } catch (InterruptedException ex) {
            System.out.println(Thread.currentThread().getName() + " stopped.");
        }
    }
}