package music;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import reactions.Gesture;
import reactions.Mass;
import reactions.Reaction;

public class Head extends Mass implements Comparable<Head>{

    public Staff staff;
    public int line; // line is y coordinate
    public Time time;

    public Glyph forcedGlyph = null; // placeholder for future glyph
    public Stem stem = null;
    public boolean wrongSide = false;

    public Head(Staff staff, int x, int y) {
        super("NOTE");
        this.staff = staff;
        this.time = staff.sys.getTime(x);
        int H = staff.fmt.H;
//        int top = staff.yTop() - H;
//        this.line = (y - top - H / 2) / H - 1;
        this.line = staff.lineOfY(y);
//        System.out.println("Line: " + line);
        time.heads.add(this);

        addReaction(new Reaction("S-S") {
            public int bid(Gesture gesture) {
                int x = gesture.vs.xL(), y1 = gesture.vs.yL(), y2 = gesture.vs.yH();
                int w = Head.this.w(), hY = Head.this.y();
                if (y1 > y || y2 < y) {return UC.noBid;}
                int hLeft = Head.this.time.x, hRight = hLeft + w;
                if (x < hLeft - w || x > hRight + w) {return UC.noBid;}
                if (x < hLeft + w / 2) {return hLeft - x;}
                if (x > hRight - w / 2) {return x - hRight;}
                return UC.noBid;
            }
            public void act(Gesture gesture) {
                int x = gesture.vs.xL(), y1 = gesture.vs.yL(), y2 = gesture.vs.yH();
                Time t = Head.this.time;
                int w = Head.this.w();
                boolean isUp = x > (t.x + w / 2);
                if (Head.this.stem == null) {
                    t.stemHeads(isUp, y1, y2);
                } else {
                    t.unStemHeads(y1, y2);
                }
            }
        });

        addReaction(new Reaction("DOT") {
            public int bid(Gesture gesture) {
                if (Head.this.stem == null) {return UC.noBid;}
                int xH = Head.this.x(), yH = Head.this.y(), h = Head.this.staff.fmt.H, w = Head.this.w();
                int x = gesture.vs.xM(), y = gesture.vs.yM();
                if (x < xH || x > xH + 2 * w || y < yH - h || y > yH + h) {return UC.noBid;}
                return Math.abs(xH + w - x) + Math.abs(yH - y);
            }
            public void act(Gesture gesture) {
                Head.this.stem.cycleDot();
            }
        });
    }

    public int w() {return 24 * staff.fmt.H / 10;}

    public void show(Graphics g) {
        int H = staff.fmt.H;
        g.setColor(wrongSide ? Color.RED : Color.BLACK);
        (forcedGlyph != null ? forcedGlyph : normalGlyph()).showAt(g, H, x(), y());
        if (stem != null) {
            int off = UC.augDotOffset, sp = UC.augDotSpace;
            for (int i = 0; i < stem.nDot; i++) {
                g.fillOval(time.x + off + i * sp, y() - 3 * H / 2, 2 * H / 3, 2 * H / 3);
            }
        }
    }

    public Glyph normalGlyph() {
        if (stem == null) {return Glyph.HEAD_Q;}
        if (stem.nFlag == -1) {return Glyph.HEAD_HALF;}
        if (stem.nFlag == -2) {return Glyph.HEAD_W;}
        return Glyph.HEAD_Q;
    }

    public int x() {
        int res = time.x;
        if (wrongSide) {res += (stem != null && stem.isUp) ? w() : -w();}
        return res;
    }

    public int y() { return staff.yLine(line);}

    public void deleteHead() {
        // stub
        time.heads.remove(this);
    }

    public void unStem() {
        if (stem != null) {
            stem.heads.remove(this);
            if (stem.heads.size() == 0) {stem.deleteStem();}
            stem = null;
            wrongSide = false;
        }
    }

    public void joinStem(Stem s) {
        if (this.stem != null) {unStem();}
        s.heads.add(this);
        this.stem = s;
    }

    @Override
    public int compareTo(Head h) {
        return (this.staff.iStaff != h.staff.iStaff) ? (this.staff.iStaff - h.staff.iStaff) : (this.line - h.line);
    }

    //--------------List---------------------
    public static class List extends ArrayList<Head> {

    }
}
