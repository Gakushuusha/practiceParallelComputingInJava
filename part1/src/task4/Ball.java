package task4;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.Random;

class Ball {
    private Component canvas;
    private static final int SIZE = 20;
    private int x;
    private int y;
    private int dx = 3;
    private int dy = 3;
    private Color color;

    public Ball(Component c, Color color) {
        this.canvas = c;

        if (Math.random() < 0.5) {
            x = new Random().nextInt(this.canvas.getWidth());
            y = 0;
        } else {
            x = 0;
            y = new Random().nextInt(this.canvas.getHeight());
        }
        this.color = color;
    }

    public void draw(Graphics2D g2) {
        g2.setColor(color);
        g2.fill(new Ellipse2D.Double(x, y, SIZE, SIZE));
    }

    public void move() {
        int width = this.canvas.getWidth();
        int height = this.canvas.getHeight();

        x += dx;
        y += dy;

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
        if (y + SIZE >= height) {
            y = height - SIZE;
            dy = -dy;
        }
        this.canvas.repaint();
    }
}
