package music;

import static sandbox.Music.PAGE;

import java.awt.Graphics;
import reactions.Mass;

public class Staff extends Mass {
    // E-W to add staff to exist system
    public Sys sys;
    public int iStaff;
    public Staff.Fmt fmt;

    public Staff() {
        super("BACK");
    }

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
