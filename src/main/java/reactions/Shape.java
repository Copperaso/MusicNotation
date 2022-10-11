package reactions;

import graphics.G;
import graphics.G.VS;
import java.awt.Color;
import java.awt.Graphics;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import music.UC;

public class Shape implements Serializable {
    public String name;
    public Prototype.List prototypes = new Prototype.List();
    public static Database DB = Database.load();
    public static Shape DOT = DB.get("DOT"); // dot shape
    public static Collection<Shape> LIST = DB.values();

    public Shape(String name) {this.name = name;}
    public static void saveShapeDB() {Database.save();}

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
    public static class Prototype extends Ink.Norm implements Serializable {
        public int nBlend = 1; // numbers that have averaged together
        public void blend(Ink.Norm norm) {blend(norm, nBlend); nBlend++;}

        //-----------------List of Prototype------------------
        public static class List extends ArrayList<Prototype> implements Serializable {
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

            // check if norm matches prototype, blend it or add it as new prototype to list
            public void train(Ink ink) {
                if (isDeletePrototype(ink)) {return;}
                if (bestDist(ink.norm) < UC.noMatchDist) {
                    bestMatch.blend(ink.norm);
                } else  {
                    add(new Shape.Prototype());
                }
            }

            // If true, it deletes
            public boolean isDeletePrototype(Ink ink) {
                int DOT = UC.dotThreshold;
                if (ink.vs.size.x > DOT || ink.vs.size.y > DOT) {return false;}
                if (ink.vs.loc.y > m + w) {return false;} // m-margin, w-width
                int iProto = ink.vs.loc.x / (m + w); // index of proto
                if (iProto >= this.size()) {return false;}  // larger than protoList's size
                remove(iProto);
                return true;
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

    //-------------------Database class---------------------
    public static class Database extends HashMap<String, Shape> {
        private Database() {super(); addNewShape("DOT");}

        public void addNewShape(String name) {put(name, new Shape(name));}
        public Shape forceGet(String name) {
            if (!DB.containsKey(name)) {addNewShape(name);}
            return DB.get(name);
        }

        public void train(String name, Ink ink) {
            if (IsLegal(name)) {forceGet(name).prototypes.train(ink);}
        }

        public static boolean IsLegal(String name) {
            return (!name.equals("") && !name.equals("DOT"));
        }

        public static Database load() {
            String fileName = UC.shapeDBFileName;
            Database res;
            // write object into disk
            try {
                System.out.println("Attempting DB load...");
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName));
                res = (Database) ois.readObject();
                System.out.println("Successful load - found " + res.keySet());
                ois.close();
            } catch (Exception e) {
                System.out.println("Load failed.");
                System.out.println(e);
                res = new Database();
            }
            return res;
        }

        public static void save() {
            String fileName = UC.shapeDBFileName;
            try {
                System.out.println("Saving DB...");
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName));
                oos.writeObject(DB);
                System.out.println("Saved " + fileName);
                oos.close();
            } catch (IOException e) {
                System.out.println("Failed DB save.");
                System.out.println(e);
            }
        }
    }
}
