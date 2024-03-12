package task3;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class BounceFrame extends JFrame {
    private BallCanvas canvas;
    public static final int WIDTH = 500;
    public static final int HEIGHT = 500;

    public int BLUE_COUNT = 1;
    public int RED_COUNT = 1;

    public BounceFrame() {
        configureFrame();
        initializeComponents();
    }

    private void configureFrame() {
        setSize(WIDTH, HEIGHT);
        setTitle("Bounce program");
        canvas = new BallCanvas();
        System.out.println("In Frame Thread name = " + Thread.currentThread().getName());
    }

    private void initializeComponents() {
        Container content = getContentPane();
        content.add(canvas, BorderLayout.CENTER);
        JPanel buttonPanel = createButtonPanel();
        content.add(buttonPanel, BorderLayout.SOUTH);
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.lightGray);

        JButton buttonStart = new JButton("Start");
        buttonStart.addActionListener(e -> startBouncing());
        JButton buttonStop = new JButton("Stop");
        buttonStop.addActionListener(e -> System.exit(0));

        buttonPanel.add(buttonStart);
        buttonPanel.add(buttonStop);

        return buttonPanel;
    }

    private void startBouncing() {
        int x = 0;
        int y = 0;
        if (Math.random() < 0.5) {
            x = new Random().nextInt(canvas.getWidth());
        } else {
            y = new Random().nextInt(canvas.getHeight());
        }

        createBalls(x, y, RED_COUNT, Color.RED);
        createBalls(x, y, BLUE_COUNT, Color.BLUE);
    }

    private void createBalls(int x, int y, int count, Color color) {
        for (int i = 0; i < count; i++) {
            Ball ball = new Ball(canvas, color == Color.RED ? Thread.MAX_PRIORITY : Thread.MIN_PRIORITY, x, y);
            canvas.add(ball);
            BallThread thread = new BallThread(ball);
            thread.setPriority(color == Color.RED ? Thread.MAX_PRIORITY : Thread.MIN_PRIORITY);
            thread.start();
            System.out.println(color + ": thread name = " + thread.getName());
        }
    }
}