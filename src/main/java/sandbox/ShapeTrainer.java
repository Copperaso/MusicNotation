package sandbox;

import graphics.G;
import graphics.Window;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import music.UC;
import reactions.Ink;
import reactions.Shape;

public class ShapeTrainer extends Window {

    public ShapeTrainer() {
        super("ShapeTrainer", UC.initialWindowWidth, UC.initialWindowHeight);
    }

    public static String UNKNOWN = " <- This name is unknown";
    public static String ILLEGAL = " <- This name is a not legal shape";
    public static String KNOWN = " <- This is a known shape";

    public static String currName = "";
    public static String currState = ILLEGAL;
    public static Shape.Prototype.List pList = new Shape.Prototype.List();
    
    public void setState() {
        currState = (currName.equals("") || currName.equals("DOT")) ? ILLEGAL : UNKNOWN;
        if (currState == UNKNOWN) {
            if (Shape.DB.containsKey(currName)) {
                currState = KNOWN;
                pList = Shape.DB.get(currName).prototypes;
            } else {
                pList = null;
            }
        }
    }

    public void paintComponent(Graphics g) {
        G.clear(g);
        g.setColor(Color.BLACK);
        g.drawString(currName, 600, 30);
        g.drawString(currState, 700, 30);
        g.setColor(Color.RED);
        Ink.BUFFER.show(g);
        if (pList != null) {
            pList.show(g);
        }
    }

    public void mousePressed(MouseEvent me) {
        Ink.BUFFER.dn(me.getX(), me.getY());
        repaint();
    }

    public void mouseDragged(MouseEvent me) {
        Ink.BUFFER.drag(me.getX(), me.getY());
        repaint();
    }

    public void mouseReleased(MouseEvent me) {
        if (currState != ILLEGAL) {
            Ink ink = new Ink();
            Shape.Prototype proto;
            if (pList == null) {
                Shape s = new Shape(currName);
                Shape.DB.put(currName, s);
                pList = s.prototypes;
            }
            if (pList.bestDist(ink.norm) < UC.noMatchDist) {
                proto = Shape.Prototype.List.bestMatch;
                proto.blend(ink.norm);
            } else {
                proto = new Shape.Prototype();
                pList.add(proto);
            }
        }
        repaint();
    }

    public void keyTyped(KeyEvent ke) {
        char c = ke.getKeyChar();
        System.out.println("type: " + c);
        // line ending character: 0x0D is ASCII return(ms), 0x0A line feed(linux/macos)
        currName = (c == ' ' || c == 0x0D ||c == 0x0A) ? "" : currName + c;
        setState();
        repaint();
    }


}
