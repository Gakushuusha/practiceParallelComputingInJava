package task2;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.Random;

class Ball {
    private Component canvas;
    private JLabel label;
    private static final int SIZE = 20;
    private int holeRadius = Holes.HOLE_RADIUS;
    private int x;
    private int y;
    private int dx = 2;
    private int dy = 2;


    public Ball(Component c, JLabel label) {
        this.canvas = c;
        this.label = label;
        if (Math.random() < 0.5) {
            x = new Random().nextInt(this.canvas.getWidth());
            y = 0;
        } else {
            x = 0;
            y = new Random().nextInt(this.canvas.getHeight());
        }
    }

    public void draw(Graphics2D g2) {
        g2.setColor(Color.green);
        g2.fill(new Ellipse2D.Double(x, y, SIZE, SIZE));
    }

    public int move() {
        int count;
        int holeNum = 0;
        int width = this.canvas.getWidth();
        int height = this.canvas.getHeight();

        x += dx;
        y += dy;

        // X 0
        // 0 0
        // 0 0
        if ((x < 0 && y < holeRadius - SIZE) || (y < 0 && x < holeRadius - SIZE + 15)) {
            count = ++BounceFrame.countFinishBall;
            label.setText(String.valueOf(count));
            holeNum = 1;
            return holeNum;
        }

        // 0 X
        // 0 0
        // 0 0
        if ((x > width - SIZE && y < holeRadius - SIZE) || (y < 0 && x > width - holeRadius)) {
            count = ++BounceFrame.countFinishBall;
            label.setText(String.valueOf(count));
            holeNum = 2;
            return holeNum;
        }

        // 0 0
        // 0 0
        // 0 X
        if ((x > width - SIZE && y > height - holeRadius) || (y > height - SIZE && x > width - holeRadius)) {
            count = ++BounceFrame.countFinishBall;
            label.setText(String.valueOf(count));
            holeNum = 3;
            return holeNum;
        }

        // 0 0
        // 0 0
        // X 0
        if ((x < 0 && y > height - holeRadius) || (y > height - SIZE && x < holeRadius - SIZE)) {
            count = ++BounceFrame.countFinishBall;
            label.setText(String.valueOf(count));
            holeNum = 4;
            return holeNum;
        }

        // 0 0
        // X 0
        // 0 0

        if (x < holeRadius-35 && Math.abs(y + SIZE / 2 - height / 2 - 30) < holeRadius / 2) {
            count = ++BounceFrame.countFinishBall;
            label.setText(String.valueOf(count));
            holeNum = 5;
            return holeNum;
        }

        // 0 0
        // 0 X
        // 0 0
        if (x + SIZE > width+30 - holeRadius && Math.abs(y + SIZE / 2 - height / 2 - 30) < holeRadius / 2 ) {
            count = ++BounceFrame.countFinishBall;
            label.setText(String.valueOf(count));
            holeNum = 6;
            return holeNum;
        }

        if (x < 0) {
            x = 0;
            dx = -dx;
        }
        if (x + SIZE >= width) {
            x = width - SIZE;
            dx = -dx;
        }
        if (y < 0) {
            y = 0;
            dy = -dy;
        }
        if (y + SIZE >= this.canvas.getHeight()) {
            y = this.canvas.getHeight() - SIZE;
            dy = -dy;
        }
        this.canvas.repaint();

        return holeNum;
    }
}