package task1;

import javax.swing.*;

public class Bounce {
    public static void main(String[] args) {
        BounceFrame frame = new BounceFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setVisible(true);
        System.out.println("Technologies of parallel computing\nPart 1");
        System.out.println("Task 1");
        System.out.println("Thread name = " + Thread.currentThread().getName());
    }
}