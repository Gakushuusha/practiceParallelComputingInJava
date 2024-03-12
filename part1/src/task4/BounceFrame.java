package task4;

import javax.swing.*;
import java.awt.*;

public class BounceFrame extends JFrame {
    private BallCanvas canvas;
    public static final int WIDTH = 500;
    public static final int HEIGHT = 500;

    public BounceFrame() {
        this.setSize(WIDTH, HEIGHT);
        this.setTitle("Bounce program");
        this.canvas = new BallCanvas();

        System.out.println("In Frame Thread name = " + Thread.currentThread().getName());

        Container content = this.getContentPane();
        content.add(this.canvas, BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.lightGray);
        JButton buttonStart = new JButton("Start");
        JButton buttonStop = new JButton("Stop");
        buttonStart.addActionListener(e -> {

            Ball b1 = new Ball(canvas, Color.BLUE);
            canvas.add(b1);
            BallThread thread1 = new BallThread(b1);
            thread1.start();
            System.out.println("Blue thread name = " + thread1.getName());


            Ball b2 = new Ball(canvas, Color.RED);
            canvas.add(b2);
            BallThread thread2 = new BallThread(b2, thread1);
            thread2.start();
            System.out.println("Red thread name = " + thread2.getName());


        });
        buttonStop.addActionListener(e -> System.exit(0));

        buttonPanel.add(buttonStart);
        buttonPanel.add(buttonStop);

        content.add(buttonPanel, BorderLayout.SOUTH);
    }
}