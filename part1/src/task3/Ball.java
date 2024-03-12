package task3;

import java.awt.*;
import java.awt.geom.Ellipse2D;

class Ball {
    private Component canvas;
    private static final int SIZE = 20;
    private int x;
    private int y;
    private int dx = 2;
    private int dy = 2;
    private Color color;


    public Ball(Component c, int priority, int x, int y) {
        this.canvas = c;
        this.x = x;
        this.y = y;

        if (priority < 8) {
            color = Color.BLUE;
        } else {
            color = Color.RED;
        }
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