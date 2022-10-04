package reactions;

import graphics.G;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import music.I;
import music.UC;

public class Ink implements I.Show {

    public static Buffer BUFFER = new Buffer();
    public static final int K = UC.normSampleSize;

    public Norm norm;
    public G.VS vs;

    public Ink() {
        norm = new Norm();
        vs = BUFFER.bbox.getNewVS();
    }

    @Override
    public void show(Graphics g) {
        g.setColor(Color.BLACK);
        norm.drawAt(g, vs);
    }

    //-------------------Norm----------------------
    public static class Norm extends G.PL{
        public static final int N = UC.normSampleSize;
        public static final int MAX = UC.normCoordinateSize;
        public static final G.VS NCS = new G.VS(0, 0, MAX, MAX);

        public Norm() {
            super(N);
            G.PL temp = BUFFER.subSample(N);
            G.V.T.set(BUFFER.bbox, NCS);
            temp.transform();
            for (int i = 0; i < N; i++) {
                this.points[i].set(temp.points[i]);
            }
        }

        public int dist(Norm n) {
            int res = 0;
            for (int i = 0; i < N; i++) {
                int dx = points[i].x - n.points[i].x;
                int dy = points[i].y - n.points[i].y;
                res += dx * dx + dy * dy;
                // do not take square root because it takes time and not efficiently work for int.
                // we only need dist for compare at this point
            }
            return res;
        }

        public void drawAt(Graphics g, G.VS vs) {
            G.V.T.set(NCS, vs);
            for (int i = 1; i < N; i++) {
                // draw line with coordinates transformed
                g.drawLine(points[i - 1].tx(), points[i - 1].ty(), points[i].tx(), points[i].ty());
            }
        }

        public void blend(Norm norm, int n) {
            for (int i = 0; i < N; i++) {
                points[i].blend(norm.points[i], n);
            }
        }
    }

    //-------------------Buffer--------------------
    public static class Buffer extends G.PL implements I.Show, I.Area {

        public static final int MAX = UC.inkBufferMax;
        public int n;  // number of points in Buffer
        public G.BBox bbox = new G.BBox();

        private Buffer() {super(MAX);}  // make a constructor private

        public G.PL subSample(int k) {
            G.PL res = new G.PL(k);
            for (int i = 0; i < k; i++) {
                res.points[i].set(this.points[i * (n - 1) / (k - 1)]);
            }
            return res;
        }

        public void add(int x, int y) {if (n < MAX) {points[n++].set(x, y); bbox.add(x, y);}}
        public void clear() {n = 0;}

        @Override
        public boolean hit(int x, int y) {return true;} // entire screen is a buffer
        @Override
        public void dn(int x, int y) {clear(); bbox.set(x, y); add(x, y);}
        @Override
        public void drag(int x, int y) {add(x, y);}
        @Override
        public void up(int x, int y) {

        }

        @Override
        public void show(Graphics g) {
            this.drawN(g, n);
            bbox.draw(g);
            g.setColor(Color.BLUE);
            this.drawNDots(g, n);
        }
    }

    //-------------------List--------------------
    public static class List extends ArrayList<Ink> implements I.Show {

        @Override
        public void show(Graphics g) {for (Ink ink : this) {ink.show(g);}}
    }

}
