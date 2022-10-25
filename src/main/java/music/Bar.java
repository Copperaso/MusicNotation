package music;

import static sandbox.Music.PAGE;

import java.awt.Color;
import java.awt.Graphics;
import reactions.Gesture;
import reactions.Mass;
import reactions.Reaction;

public class Bar extends Mass {

    private final int FAT = 2, RIGHT = 4, LEFT = 8;
    public Sys sys; // which system the Bar lies
    public int x; // the location of Bar in the sys
    public int barType = 0;

    public Bar(Sys sys, int x) {
        super("NOTE");
        this.sys = sys;
        this.x = x;

        addReaction(new Reaction("S-S") { // cycle the barType
            public int bid(Gesture gesture) {
                int x = gesture.vs.xM();
                if (Math.abs(x - Bar.this.x) > UC.barToMarginSnap) {return UC.noBid;}
                int y1 = gesture.vs.yL(), y2 = gesture.vs.yH();
                if (y1 < Bar.this.sys.yTop() - 20) {return UC.noBid;}
                if (y2 > Bar.this.sys.yBot() + 20) {return UC.noBid;}
                return Math.abs(x - Bar.this.x);
            }

            public void act(Gesture gesture) {
                Bar.this.cycleType();
            }
        });

        addReaction(new Reaction("S-S") { // set bar continues
            // TODO
            public int bid(Gesture gesture) {
                return 0;
            }
            // TODO
            public void act(Gesture gesture) {

            }
        });
    }

    // bar has 3 types(draw by 1line, 2lines, 3lines), only x value matters in toggle,
    // so can modify barType by draw in systems(y doesn't matter)
    public void cycleType() {this.barType++; if (barType > 2) {barType = 0;}}

    public void toggleLeft() {barType = barType^LEFT;} // ^ exclusive or
    public void toggleRight() {barType = barType^RIGHT;}

    public void show(Graphics g) {
        int yTop = sys.yTop(), N = sys.fmt.size();
        // test barTypes
        if (barType == 0) {g.setColor(Color.BLACK);}
        else if (barType == 1) {g.setColor(Color.RED);}
        else if (barType == 2) {g.setColor(Color.GREEN);}
        for (int i = 0; i < N; i++) {
            Staff.Fmt sf = sys.fmt.get(i);
            int topLine = yTop + sys.fmt.staffOffset.get(i);
            g.drawLine(x, topLine, x, topLine + sf.height());
        }
    }

    // TODO: test below draw methods
    public static void wings(Graphics g, int x, int y1, int y2, int dx, int dy) { // draw wings
        g.drawLine(x, y1, x + dx, y1 - dy);
        g.drawLine(x, y2, x + dx, y2 + dy);
    }

    public static void fatBar(Graphics g, int x, int y1, int y2, int dx) { // draw fat Bar
        g.fillRect(x, y1, dx, y2 - y1);
    }

    public static void thinBar(Graphics g, int x, int y1, int y2) { // draw thin Bar
        g.drawLine(x, y1, x, y2);
    }

    public void drawDots(Graphics g, int x, int top) {
        int H = UC.defaultStaffSpace; // H is half distance between two neighbor lines in one staff
        if ((barType & LEFT) != 0) {
            g.drawOval(x - 3 * H, top + 11 * H / 4, H / 2, H / 2);
            g.drawOval(x - 3 * H, top + 19 * H / 4, H / 2, H / 2);
        }
        if ((barType & RIGHT) != 0) {
            g.drawOval(x + 3 * H, top + 11 * H / 4, H / 2, H / 2);
            g.drawOval(x + 3 * H, top + 19 * H / 4, H / 2, H / 2);
        }
    }
}
