package music;

import static sandbox.Music.PAGE;

import java.awt.Graphics;
import java.util.ArrayList;
import reactions.Mass;

public class Sys extends Mass {
    public ArrayList<Staff> staffs = new ArrayList<>();
    public Page page; // which page this sys belongs to
    public int iSys;
    public Sys.Fmt fmt;

    public Sys(Page page, int iSys, Sys.Fmt fmt) {
        super("BACK");
        this.page = page;
        this.iSys = iSys;
        this.fmt = fmt;
    }

    public int yTop() {return page.sysTop(iSys);}

    public void show(Graphics g) {
        int y = yTop(), x = PAGE.margins.left;
        // draw a line on the left of staffs to identify that they are in the same system
        g.drawLine(x, y, x, y + fmt.height());
    }

    //----------------Fmt-------------------------
    public static class Fmt extends ArrayList<Staff.Fmt> {
        public ArrayList<Integer> staffOffset = new ArrayList<>();

        public int height() {
            int last = size() - 1;
            return staffOffset.get(last) + get(last).height(); // get last will return a Staff.Fmt
        }

        public void showAt(Graphics g, int y) {
            for (int i = 0; i < size(); i++) {
                get(i).showAt(g, y + staffOffset.get(i));
            }
        }
    }
}
