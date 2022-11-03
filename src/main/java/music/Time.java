package music;

import java.util.ArrayList;

// Time class used to share x value in a system
public class Time {
    public int x;

    private Time(Sys sys, int x) {
        this.x = x;
        sys.times.add(this);
    }

    //------------------List-------------------------
    public static class List extends ArrayList<Time> {
        public Sys sys;
        public List(Sys sys) {
            this.sys = sys;
        }

        public Time getTime(int x) {
            if (this.size() == 0) {return new Time(this.sys, x);}
            // compare x and existing time to see if they are close
            Time t = getClosestTime(x);
            return (Math.abs(x - t.x) < UC.snapTime) ? t : new Time(this.sys, x);
        }

        public Time getClosestTime(int x) {
            Time res = this.get(0); // already check if size == 0 in getTime(), so it's safe in this situation
            int bestSoFar = Math.abs(x - res.x);
            for (Time t : this) {
                int dist = Math.abs(x - t.x);
                if (dist < bestSoFar) {
                    res = t;
                    bestSoFar = dist;
                }
            }
            return res;
        }
    }
}
