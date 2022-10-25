package sandbox;

import graphics.G;
import graphics.Window;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import music.Page;
import music.Sys;
import music.UC;
import reactions.Gesture;
import reactions.Ink;
import reactions.Layer;
import reactions.Reaction;

public class Music extends Window {
    static {
        new Layer("BACK");
        new Layer("FORE");
        new Layer("NOTE");
    }
    public static Page PAGE;
    public static void main(String[] args) {(PANEL = new Music()).launch();}

    public Music() {
        super("MusicEditor", UC.initialWindowWidth, UC.initialWindowHeight);
        Reaction.initialReactions.addReaction(new Reaction("E-E") {
            // "E-E" initialize the first system and staff
            public int bid(Gesture gesture) {return 10;}
            public void act(Gesture gesture) {
                int y = gesture.vs.yM();
                Sys.Fmt sysFmt = new Sys.Fmt();
                PAGE = new Page(sysFmt);
                PAGE.margins.top = y;
                PAGE.addNewSys();
                PAGE.addNewStaff(0);
                this.disable();
            }
        });
    }

    public void paintComponent(Graphics g) {
        G.clear(g);
        g.setColor(Color.BLUE);
        g.drawString("Music", 100, 30);
        Ink.BUFFER.show(g);
        Layer.ALL.show(g);
    }

    public void mousePressed(MouseEvent me) {Gesture.AREA.dn(me.getX(), me.getY()); repaint();}
    public void mouseDragged(MouseEvent me) {Gesture.AREA.drag(me.getX(), me.getY()); repaint();}
    public void mouseReleased(MouseEvent me) {Gesture.AREA.up(me.getX(), me.getY()); repaint();}

}
