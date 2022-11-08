package music;

import java.awt.Graphics;

public class Stem extends Duration {

    public Head.List heads = new Head.List();
    public boolean isUp = true;

    public Stem(boolean isUp) {
        this.isUp = isUp;
    }

    @Override
    public void show(Graphics g) {
        if (nFlag > -1 && heads.size() > 0) {
            int x = x(), H = UC.defaultStaffSpace, yH = yFirstHead(), yB = yBeamEnd();
            g.drawLine(x, yH, x, yB);
        }
    }

    public Head firstHead() {return heads.get(isUp ? heads.size() - 1 : 0);}
    public Head lastHead() {return heads.get(isUp ? 0 : heads.size() - 1);}

    public int yFirstHead() {
        Head h = firstHead();

        return h.staff.yLine(h.line);
    }

    public int yBeamEnd() {
        Head h = lastHead();
        int line = h.line;
        line += isUp ? - 7 : 7;
        int flagInc = (nFlag > 2) ? 2 * (nFlag - 2) : 0;
        line += isUp ? - flagInc : flagInc;
        if ((isUp && line > 4) ||(!isUp && line < 4))  {line = 4;}
        return h.staff.yLine(line);
    }

    public int x() {
        Head h = firstHead();
        return h.time.x + (isUp ? h.w() : 0);
    }

    public void deleteStem() {this.deleteMass();}

    public void setWrongSides() {
        // stub
    }
}
