package music;

import java.awt.Graphics;
import java.util.ArrayList;
import reactions.Mass;

public class Page extends Mass {

    public Margins margins = new Margins();
    public Sys.Fmt sysFmt;
    public int sysGap;
    public ArrayList<Sys> sysList = new ArrayList<>();

    public Page(Sys.Fmt sysFmt) {
        super("BACK");
        this.sysFmt = sysFmt;
    }

    public int sysTop(int iSys) {
        return margins.top + iSys * (sysFmt.height() + sysGap);
    }

    public void show(Graphics g) {
        // TODO
    }

    //------------------Margins----------------
    public static class Margins {
        private static int M = 50; // default margin
        public int top = M, left = M, bot = UC.initialWindowHeight - M, right = UC.initialWindowWidth - M;
    }
}
