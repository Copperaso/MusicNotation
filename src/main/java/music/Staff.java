package music;

import static sandbox.Music.PAGE;

import java.awt.Graphics;
import reactions.Gesture;
import reactions.Mass;
import reactions.Reaction;

public class Staff extends Mass {
    public Sys sys;
    public int iStaff;
    public Staff.Fmt fmt;

    public Staff(Sys sys, int iStaff, Staff.Fmt fmt) {
        super("BACK");
        this.sys = sys;
        this.iStaff = iStaff;
        this.fmt = fmt;

        addReaction(new Reaction("S-S") { // draw barLine from topLine
            public int bid(Gesture gesture) {
                int x = gesture.vs.xM(); // middle value of gesture
                int y1 = gesture.vs.yL(), y2 = gesture.vs.yH();
                if (x < PAGE.margins.left || x > PAGE.margins.right + UC.barToMarginSnap) { // x is out of screen
                    return UC.noBid;
                }
                System.out.println("Top " + y1 + " " + Staff.this.yTop());
                int d = Math.abs(y1 - Staff.this.yTop()) + Math.abs(y2 - Staff.this.yBot());
                return (d < 50) ? (d + UC.barToMarginSnap): UC.noBid;
            }
            public void act(Gesture gesture) {
                int x = gesture.vs.xM();
                if (Math.abs(x - PAGE.margins.right) <= UC.barToMarginSnap) { // draw at end of staff
                    x = PAGE.margins.right;
                }
                new Bar(Staff.this.sys, x);
            }
        });
    }

    public int sysOff() {return this.sys.fmt.staffOffset.get(iStaff);}

    public int yTop() {return this.sys.yTop() + this.sysOff();}

    public int yBot() {return this.yTop() + this.fmt.height();}

    //------------Fmt Format----------------------
    public static class Fmt {
        public int nLines = 5; // every staff has 5 lines
        public int H = 8;   // height, half distance between two lines

        public int height() {
            return 2 * H * (nLines - 1);
        }

        public void showAt(Graphics g, int y) {
            int LEFT = PAGE.margins.left, RIGHT = PAGE.margins.right;
            for (int i = 0; i < nLines; i++) {g.drawLine(LEFT, y + 2 * H * i, RIGHT, y + 2 * H * i);}
        }
    }
}
