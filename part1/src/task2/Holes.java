package task2;

import java.awt.*;
import java.awt.geom.Ellipse2D;

public class Holes {
    private Component canvas;
    public static final int HOLE_RADIUS = 70;

    public Holes(Component c) {
        this.canvas = c;
    }

    public void draw(Graphics2D g2) {
        g2.setColor(Color.LIGHT_GRAY);

        g2.fill(new Ellipse2D.Double(
                0 - HOLE_RADIUS,
                0 - HOLE_RADIUS,
                2 * HOLE_RADIUS,
                2 * HOLE_RADIUS));

        g2.fill(new Ellipse2D.Double(
                this.canvas.getWidth() - HOLE_RADIUS,
                0 - HOLE_RADIUS,
                2 * HOLE_RADIUS,
                2 * HOLE_RADIUS));

        g2.fill(new Ellipse2D.Double(
                this.canvas.getWidth() - HOLE_RADIUS,
                this.canvas.getHeight() - HOLE_RADIUS,
                2 * HOLE_RADIUS,
                2 * HOLE_RADIUS));

        g2.fill(new Ellipse2D.Double(
                0 - HOLE_RADIUS,
                this.canvas.getHeight() - HOLE_RADIUS,
                2 * HOLE_RADIUS,
                2 * HOLE_RADIUS));

        g2.fill(new Ellipse2D.Double(
                - 35 - HOLE_RADIUS / 2.0,
                this.canvas.getHeight() / 2.0 - HOLE_RADIUS / 2.0,
                2 * HOLE_RADIUS,
                2 * HOLE_RADIUS));

        g2.fill(new Ellipse2D.Double(
                this.canvas.getWidth() - HOLE_RADIUS / 2.0 - 35,
                this.canvas.getHeight() / 2.0 - HOLE_RADIUS / 2.0,
                2 * HOLE_RADIUS,
                2 * HOLE_RADIUS));
    }
}