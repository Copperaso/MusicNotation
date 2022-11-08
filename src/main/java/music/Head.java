package music;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import reactions.Gesture;
import reactions.Mass;
import reactions.Reaction;

public class Head extends Mass {

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
    }

    public int w() {return 24 * staff.fmt.H / 10;}

    public void show(Graphics g) {
        int H = staff.fmt.H;
        g.setColor(stem == null ? Color.RED : Color.BLACK);
        (forcedGlyph != null ? forcedGlyph : normalGlyph()).showAt(g, H, x(), y());
    }

    public Glyph normalGlyph() {
        // stub  TODO
        return Glyph.HEAD_Q;
    }

    public int x() {
        // stub - placeholder TODO
        return time.x;
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

    //--------------List---------------------
    public static class List extends ArrayList<Head> {

    }
}
