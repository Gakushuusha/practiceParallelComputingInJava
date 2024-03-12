package task4;

public class BallThread extends Thread {
    private Ball b;
    private BallThread mainBall;

    public BallThread(Ball ball) {
        this.b = ball;
    }

    public BallThread(Ball ball, BallThread mainBall) {
        this.b = ball;
        this.mainBall = mainBall;
    }

    @Override
    public void run() {
        try {
            for (int i = 1; i <= 1500; i++) {
                b.move();
                System.out.println("--> Thread name = " + Thread.currentThread().getName() + " - " + i);
                Thread.sleep(6);


                if (i == 900 && mainBall != null) {
                    mainBall.join(6000);
                }
            }
        } catch (InterruptedException ex) {
            System.out.println(Thread.currentThread().getName() + " stopped.");
        }
    }
}