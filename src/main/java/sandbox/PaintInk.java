package sandbox;

import graphics.G;
import graphics.Window;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import music.UC;
import reactions.Ink;
import reactions.Shape;

public class PaintInk extends Window {

    public static Ink.List inkList = new Ink.List();
    // static {inkList.add(new Ink());}
    public static Shape.Prototype.List pList = new Shape.Prototype.List();

    public PaintInk() {super("PaintInk", UC.initialWindowWidth, UC.initialWindowHeight);}

    @Override
    public void paintComponent(Graphics g) {
        G.clear(g);
        inkList.show(g);
        g.setColor(Color.RED);
        Ink.BUFFER.show(g);
        if (inkList.size() > 1) {
            int last = inkList.size() - 1;
            int dist = inkList.get(last).norm.dist(inkList.get(last - 1).norm);
            // test distance is less than UC.noMatchDist
            g.setColor((dist < UC.noMatchDist) ? Color.GREEN : Color.RED);
            g.drawString("Dist: " + dist, 600, 60);
        }
        g.drawString("Points: " + Ink.BUFFER.n, 600, 30);
        pList.show(g);
    }

    @Override
    public void mousePressed(MouseEvent me) {Ink.BUFFER.dn(me.getX(), me.getY()); repaint();}

    @Override
    public void mouseDragged(MouseEvent me) {Ink.BUFFER.drag(me.getX(), me.getY()); repaint();}

    @Override
    public void mouseReleased(MouseEvent me) {
        Ink ink = new Ink();
        Shape.Prototype proto;
        inkList.add(ink);
        if (pList.bestDist(ink.norm) < UC.noMatchDist) {
            proto = Shape.Prototype.List.bestMatch;
            proto.blend(ink.norm);
        } else {
            proto = new Shape.Prototype();
            pList.add(proto);
        }
        ink.norm = proto;
        repaint();
    }

}
