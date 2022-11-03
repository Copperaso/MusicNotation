package music;

import java.awt.Graphics;
import reactions.Mass;

public class Head extends Mass {

    public Staff staff;
    public int line; // line is y coordinate
    public Time time;

    public Head(Staff staff, int x, int y) {
        super("NOTE");
        this.staff = staff;
        this.time = staff.sys.getTime(x);
        int H = staff.fmt.H;
//        int top = staff.yTop() - H;
//        this.line = (y - top - H / 2) / H - 1;
        this.line = staff.lineOfY(y);
        System.out.println("Line: " + line);
    }

    public void show(Graphics g) {
        int H = staff.fmt.H;
        Glyph.HEAD_Q.showAt(g, H, time.x, staff.yTop() + line * H);
    }
}
