package sandbox;

import graphics.G;
import graphics.Window;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import music.Page;
import music.UC;
import reactions.Gesture;
import reactions.Ink;
import reactions.Layer;

public class Music extends Window {
    static {new Layer("Back");new Layer("Foreground");}
    public static Page PAGE;
    public static void main(String[] args) {(PANEL = new Music()).launch();}

    public Music() {
        super("MusicEditor", UC.initialWindowWidth, UC.initialWindowHeight);
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
