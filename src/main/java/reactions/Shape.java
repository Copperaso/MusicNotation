package reactions;

import graphics.G;
import graphics.G.VS;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import music.UC;

public class Shape {
    public String name;
    public Prototype.List prototypes = new Prototype.List();
    public static HashMap<String,Shape> DB = loadShapeDB(); // database of shape and it's name
    public static Shape DOT = DB.get("DOT"); // dot shape
    public static Collection<Shape> LIST = DB.values();

    public Shape(String name) {this.name = name;}
    public static HashMap<String,Shape> loadShapeDB() {
        HashMap<String,Shape> res = new HashMap<>();
        res.put("DOT", new Shape("DOT"));
        return res;
    }

    public static void saveShapeDB() {
    }

    public static Shape recognize(Ink ink) { // can return null
        if (ink.vs.size.x < UC.dotThreshold && ink.vs.size.y < UC.dotThreshold) {
            return DOT;
        }
        Shape bestMatch = null;
        int bestSoFar = UC.noMatchDist;
        for (Shape s : LIST) {
            int d = s.prototypes.bestDist(ink.norm);
            if (d < bestSoFar) {
                bestMatch = s;
                bestSoFar = d;
            }
        }
        return bestMatch;
    }

    //--------------Prototype----------------------
    public static class Prototype extends Ink.Norm{
        public int nBlend = 1; // numbers that have averaged together
        public void blend(Ink.Norm norm) {blend(norm, nBlend); nBlend++;}

        //-----------------List of Prototype------------------
        public static class List extends ArrayList<Prototype> {
            public static Prototype bestMatch; // set by side effect of bestDist

            // for test
            private static int m = 10, w = 60; // margin and width for test
            private static G.VS showBox = new VS(m, m, w, w);
            //

            public int bestDist(Ink.Norm norm) {
                bestMatch = null;
                int bestSoFar = UC.noMatchDist;
                for (Prototype p : this) {
                    int d = p.dist(norm);
                    if (d < bestSoFar) {
                        bestSoFar = d;
                        bestMatch = p;
                    }
                }
                return bestSoFar;
            }

            // show and test
            public void show(Graphics g) {
                g.setColor(Color.BLUE);
                for (int i = 0; i < this.size(); i++) {
                    Prototype p = get(i);
                    int x = m + i * (m + w);
                    showBox.loc.set(x, m);
                    p.drawAt(g, showBox);
                    g.drawString("" + p.nBlend, x, 20);
                }
            }

        }

    }

}
