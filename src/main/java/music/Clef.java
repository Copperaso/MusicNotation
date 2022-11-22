package music;

import static sandbox.Music.PAGE;

import java.awt.Graphics;
import java.util.ArrayList;
import reactions.Gesture;
import reactions.Mass;
import reactions.Reaction;

public class Clef extends Mass {
    public static int INITIAL_X = UC.defaultPageMargin + UC.initialClefOffset;
    public Glyph glyph;
    public Staff staff;
    public int x;

    public Clef(Glyph glyph, Staff staff, int x) {
        super("BACK");
        this.glyph = glyph;
        this.staff = staff;
        this.x = x;

        addReaction(new Reaction("S-S") { // delete Clef
            public int bid(Gesture gesture) {
                int x = gesture.vs.xL(), y = gesture.vs.yL();
                int dX = Math.abs(x - Clef.this.x), dY = Math.abs(y - Clef.this.staff.yLine(4));
                if (dX > 30 || dY > 30) {return UC.noBid;}
                return 10;
            }
            public void act(Gesture gesture) {
                Clef.this.deleteClef();
            }
        });
    }

    public static void setInitialClefs(Staff staff, Glyph glyph) {
        ArrayList<Sys> systems = PAGE.sysList;
        Sys firstSys = staff.sys;
        int iStaff = staff.iStaff;
        for (int i = firstSys.iSys; i < systems.size(); i++) {
            systems.get(i).staffs.get(iStaff).initialClef.glyph = glyph;
        }
    }

    public void show(Graphics g) {
        if (glyph != null) {
            glyph.showAt(g, staff.fmt.H, x, staff.yLine(4));
        }
    }

    public void deleteClef() {
        if (x == INITIAL_X) {glyph = null;} else {deleteMass();}
    }
}
