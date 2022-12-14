package music;

import static sandbox.Music.PAGE;

import java.awt.Graphics;
import java.util.ArrayList;
import reactions.Gesture;
import reactions.Mass;
import reactions.Reaction;

public class Sys extends Mass {
    public ArrayList<Staff> staffs = new ArrayList<>();
    public Page page; // which page this sys belongs to
    public int iSys;
    public Sys.Fmt fmt;
    public Time.List times;
    public Stem.List stems = new Stem.List();

    public Sys(Page page, int iSys, Sys.Fmt fmt) {
        super("BACK");
        this.page = page;
        this.iSys = iSys;
        this.fmt = fmt;
        this.times = new Time.List(this);
        // the for loop fix bugs that cannot draw notes in systems other than first sys
        for (int i = 0; i < fmt.size(); i++) {
            addStaff(new Staff(this, i, fmt.get(i)));
        }

        addReaction(new Reaction("E-E") { // add beam
            @Override
            public int bid(Gesture gesture) {
                int x1 = gesture.vs.xL(), y1 = gesture.vs.yL();
                int x2 = gesture.vs.xH(), y2 = gesture.vs.yH();
                if (stems.fastReject(y1, y2)) {return UC.noBid;}
                ArrayList<Stem> temp = stems.allIntersectors(x1, y1, x2, y2);
                if (temp.size() < 2) {return UC.noBid;}
                System.out.println("Crossed " + temp.size() + " stems");
                Beam beam = temp.get(0).beam;
                for (Stem s : temp) {if (s.beam != beam) {return UC.noBid;}}
                System.out.println("All stems share beam");
                if (beam == null && temp.size() != 2) {return UC.noBid;}
                if (beam == null && (temp.get(0).nFlag != 0 || temp.get(1).nFlag != 0)) {return UC.noBid;}
                return 50;
            }

            @Override
            public void act(Gesture gesture) {
                int x1 = gesture.vs.xL(), y1 = gesture.vs.yL();
                int x2 = gesture.vs.xH(), y2 = gesture.vs.yH();
                ArrayList<Stem> temp = stems.allIntersectors(x1, y1, x2, y2);
                Beam beam = temp.get(0).beam;
                if (beam == null) {
                    new Beam(temp.get(0), temp.get(1));
                } else {
                    for (Stem s : temp) {
                        s.incFlag();
                    }
                }
            }
        });

        addReaction(new Reaction("W-W") { // add beam
            @Override
            public int bid(Gesture gesture) {
                int x1 = gesture.vs.xL(), y1 = gesture.vs.yL();
                int x2 = gesture.vs.xH(), y2 = gesture.vs.yH();
                if (stems.fastReject(y1, y2)) {return UC.noBid;}
                ArrayList<Stem> temp = stems.allIntersectors(x1, y1, x2, y2);
                if (temp.size() < 2) {return UC.noBid;}
                System.out.println("Crossed " + temp.size() + " stems");
                Beam beam = temp.get(0).beam;
                for (Stem s : temp) {if (s.beam != beam) {return UC.noBid;}}
                System.out.println("All stems share beam");
                if (beam == null) {return UC.noBid;}
                return 50;
            }

            @Override
            public void act(Gesture gesture) {
                int x1 = gesture.vs.xL(), y1 = gesture.vs.yL();
                int x2 = gesture.vs.xH(), y2 = gesture.vs.yH();
                ArrayList<Stem> temp = stems.allIntersectors(x1, y1, x2, y2);
                Beam beam = temp.get(0).beam;
                for (Stem s : temp) {
                    s.decFlag();
                }
            }
        });
    }

    public Time getTime(int x) {return times.getTime(x);}

    public void addStaff(Staff staff) {
        staffs.add(staff);
    }

    public int yTop() {return page.sysTop(iSys);}
    public int yBot() {return staffs.get(staffs.size() - 1).yBot();} // last staff's bottom is the bottom of the system

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
