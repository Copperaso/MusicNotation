package sandbox;

import graphics.G;
import graphics.Window;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.Timer;
import music.*;

public class Squares extends Window implements ActionListener {
    // public static Color theColor = Color.CYAN;
    public static final int W = UC.initialWindowWidth;
    public static final int H = UC.initialWindowHeight;

    public static Square.List theList = new Square.List();
    public static Square theSquare; // Square extends G.VS, so theSquare can use any methods in G.VS
    public static G.V mouseDelta = new G.V(0, 0);
    public static Timer timer;
    public static I.Area currentArea;

    public static Square BACKGROUND = new Square(0, 0) {
        // when hit the background, the action of mouse pressed and drag is create a new square and resize by dragging
        public void dn(int x, int y) {
            theSquare = new Square(x, y);
            theList.add(theSquare);
        }
        public void drag(int x, int y) {
            theSquare.resize(x, y);
        }
    };

    static {
        BACKGROUND.c = Color.WHITE;
        BACKGROUND.size.set(5000, 5000);
        theList.add(BACKGROUND);
    }

    public Squares() {
        super("Squares", W, H);
        timer = new Timer(30, this);
        timer.setInitialDelay(1000);
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        G.clear(g);
        theList.draw(g);
    }

    @Override
    public void mousePressed(MouseEvent me) {
        int x = me.getX(), y = me.getY();
        currentArea = theList.hit(x, y); // current area is either a square or the background
        currentArea.dn(x, y);
        repaint();
    }

    @Override
    public void mouseDragged(MouseEvent me) {
        int x = me.getX(), y = me.getY();
        currentArea.drag(x, y);
        repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

    //-------------------Square-------------------
    public static class Square extends G.VS implements I.Draw, I.Area{
        public Color c = G.rndColor();
        // public G.V dv = new G.V(G.rnd(40) - 20, G.rnd(40) - 20); // random velocity, get a random number range from -20 to 20
        public G.V dv = new G.V(0, 0);
        // constructor for Square with fixed size
        public Square(int x, int y) {super(x, y, 100, 100);}

        public void move (int x, int y) {
            loc.x = x;
            loc.y = y;
        }

        public void moveAndBounce() {
            loc.add(dv);
            if (loc.x < 0 && dv.x < 0) {dv.x = - dv.x;}  // out of bound, change the velocity's direction
            if (loc.y < 0 && dv.y < 0) {dv.y = - dv.y;}
            if (loc.x + size.x > W && dv.x > 0) {dv.x = - dv.x;}
            if (loc.y + size.y > H && dv.y > 0) {dv.y = - dv.y;}
        }

        @Override
        public void draw(Graphics g) {
            this.fill(g, c);
            moveAndBounce();
        }

        @Override
        public void dn(int x, int y) {
            // hit theSquare
            mouseDelta.set(x - loc.x, y - loc.y);
        }

        @Override
        public void drag(int x, int y) {
            // actually dragging the square
            move(x - mouseDelta.x, y - mouseDelta.y);
        }

        @Override
        public void up(int x, int y) {

        }

        //--------------------List---------------------
        public static class List extends ArrayList<Square> implements I.Draw {
            @Override
            public void draw(Graphics g) {for (Square s : this) {s.draw(g);}} // fill all squares in the list
            public void addNew(int x, int y) {this.add(new Square(x, y));}

            // check if hit any square, if hit return the specific square
            public Square hit(int x, int y) {
                Square res = null;
                for (Square s : this) {
                    if (s.hit(x, y)) {
                        res = s;
                    }
                }
                return res;
            }
        }
    }
}
