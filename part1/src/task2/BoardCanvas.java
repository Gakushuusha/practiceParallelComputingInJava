package task2;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class BoardCanvas extends JPanel {
    private ArrayList<Ball> balls;
    private Holes holes;

    public BoardCanvas() {
        this.balls = new ArrayList<>();
    }

    public void add(Ball b) {
        this.balls.add(b);
    }

    public void add(Holes holes) {
        this.holes = holes;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        holes.draw(g2);

        for (Ball b : balls) {
            b.draw(g2);
        }
    }
}