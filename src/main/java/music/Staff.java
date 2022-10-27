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


        addReaction(new Reaction("S-S") { // set bar continues
            public int bid(Gesture gesture) {
                if (Staff.this.sys.iSys != 0) { // only change barContinues in the first system
                    return UC.noBid;
                }
                int y1 = gesture.vs.yL(), y2 = gesture.vs.yH(), iStaff = Staff.this.iStaff;
                if (iStaff == PAGE.sysFmt.size() - 1) { // if current staff is the last staff, we don't want to continue
                    return UC.noBid;
                }
                if (Math.abs(y1 - Staff.this.yBot()) > 20) {return UC.noBid;}
                Staff nextStaff = Staff.this.sys.staffs.get(iStaff + 1);
                if (Math.abs(y2 - nextStaff.yTop()) > 20) {return UC.noBid;}
                return 10;
            }
            public void act(Gesture gesture) {
                Staff.this.fmt.toggleBarContinues();
            }
        });

        addReaction(new Reaction("SW-SW") { // adding Quarter note
            public int bid(Gesture gesture) {
                int x = gesture.vs.xM(), y = gesture.vs.yM();
                if (x < PAGE.margins.left || x > PAGE.margins.right) {return UC.noBid;} // x is out of bounds
                int H = Staff.this.fmt.H, top = Staff.this.yTop() - H, bot = Staff.this.yBot() + H;
                if (y < top || y > bot) {return UC.noBid;} // y is out of bounds
                return 10;
            }
            public void act(Gesture gesture) {
                new Head(Staff.this, gesture.vs.xM(), gesture.vs.yM());
            }
        });
    }

    public int sysOff() {return this.sys.fmt.staffOffset.get(iStaff);}

    public int yTop() {return this.sys.yTop() + this.sysOff();}

    public int yBot() {return this.yTop() + this.fmt.height();}

    //------------Fmt Format----------------------
    public static class Fmt {
        public int nLines = 5; // every staff has 5 lines
        public int H = UC.defaultStaffSpace;   // height, half distance between two lines
        public boolean barContinues = false;

        public int height() {
            return 2 * H * (nLines - 1);
        }

        public void toggleBarContinues() {barContinues = !barContinues;}

        public void showAt(Graphics g, int y) {
            int LEFT = PAGE.margins.left, RIGHT = PAGE.margins.right;
            for (int i = 0; i < nLines; i++) {g.drawLine(LEFT, y + 2 * H * i, RIGHT, y + 2 * H * i);}
        }
    }
}
