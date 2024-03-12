package task2;

import javax.swing.*;
import java.awt.*;

public class BounceFrame extends JFrame {
    private BoardCanvas canvas;
    public static final int WIDTH = 500;
    public static final int HEIGHT = 700;
    public static int countFinishBall = 0;

    public BounceFrame() {
        this.setSize(WIDTH, HEIGHT);
        this.setTitle("Bounce program");
        this.canvas = new BoardCanvas();

        System.out.println("In Frame Thread name = " + Thread.currentThread().getName());

        Container content = this.getContentPane();
        content.add(this.canvas, BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.lightGray);
        JButton buttonStart = new JButton("Start");
        JButton buttonStop = new JButton("Stop");
        JLabel labelCount = new JLabel();

        Holes holes = new Holes(canvas);
        canvas.add(holes);

        buttonStart.addActionListener(e -> {
            Ball b = new Ball(canvas, labelCount);
            canvas.add(b);

            BallThread thread = new BallThread(b);
            thread.start();
            System.out.println("Thread name = " + thread.getName());
        });

        buttonStop.addActionListener(e -> System.exit(0));

        buttonPanel.add(buttonStart);
        buttonPanel.add(buttonStop);
        buttonPanel.add(labelCount);

        content.add(buttonPanel, BorderLayout.SOUTH);
    }
}