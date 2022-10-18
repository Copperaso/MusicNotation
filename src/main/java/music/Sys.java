package music;

import java.util.ArrayList;
import reactions.Mass;

public class Sys extends Mass {
    // E-E to create a system
    public ArrayList<Staff> staffs;
    public Page page; // which page this sys belongs to
    public int iSys;
    public Sys.Fmt fmt;

    public Sys() {
        super("BACK");
    }

    public int yTop() {return page.sysTop(iSys);}

    //----------------Fmt-------------------------
    public static class Fmt extends ArrayList<Staff.Fmt> {
        public ArrayList<Integer> staffOffset = new ArrayList<>();

        public int height() {
            int last = size() - 1;
            return staffOffset.get(last) + get(last).height(); // get last will return a Staff.Fmt
        }
    }
}
