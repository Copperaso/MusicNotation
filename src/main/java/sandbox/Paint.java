package sandbox;

import graphics.G;
import graphics.Window;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class Paint extends Window {

    public Paint() {
        super("Paint", 1000, 750);
    }

    public static Path thePath;
    public static Pic thePic = new Pic();

    public static int clicks = 0;
    public static Color c = G.rndColor();

    @Override
    public void mousePressed(MouseEvent me) {
        clicks++;
        thePath = new Path();
        thePath.add(me.getPoint());
        thePic.add(thePath);
        repaint();
    }

    @Override
    public void mouseDragged(MouseEvent me) {
        thePath.add(me.getPoint());
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        G.clear(g);
        g.setColor(c);
        g.fillOval(100, 100, 200, 300);
        g.drawLine(100, 600, 600, 100);
        String msg = "AAAAAHHHHHHHH";
        int x = 400, y = 200;
        g.drawString(msg, x, y);
        g.fillOval(400, 200, 2, 2);
        FontMetrics fm = g.getFontMetrics();
        int a = fm.getAscent(), d = fm.getDescent();
        int w = fm.stringWidth(msg);
        g.drawRect(x, y - a, w, a + d);

        g.setColor(Color.BLACK);
        g.drawString("Clicks, " + clicks, 400, 400);

        thePath.draw(g);
        thePic.draw(g);
    }

    //------------------Path----------------------
    // helper class only needed in this class, mouse moving path
    public static class Path extends ArrayList<Point> {
        public void draw(Graphics g) {
            // draw lines from point[0] to point[i]
            for (int i = 1; i < this.size(); i++) {
                Point p = this.get(i - 1), n = this.get(i); //p-previous, n-next
                g.drawLine(p.x, p.y, n.x, n.y);
            }
        }
    }

    //-----------------Pic------------------------
    // can draw multiple paths
    public static class Pic extends ArrayList<Path> {
        public void draw(Graphics g) {
            for (Path p : this) {
                p.draw(g);
            }
        }
    }

}
