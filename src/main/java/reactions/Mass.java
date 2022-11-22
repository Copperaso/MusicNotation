package reactions;

import graphics.G;
import java.awt.Graphics;
import music.I;

public abstract class Mass extends Reaction.List implements I.Show {
    // parent of every shape drawn on the screen
    public Layer layer;
    public int hashcode = G.rnd(1000000000);

    public Mass(String layerName) {
        this.layer = Layer.byName.get(layerName);
        if (this.layer != null) {
            layer.add(this);
        } else {
            System.out.println("Bad layerName " + layerName);
        }
    }

    public void deleteMass() {
        clearAll();
        layer.remove(this);
    }

    public void show(Graphics g) {}

    @Override
    public boolean equals(Object obj) {return this == obj;}

    @Override
    public int hashCode() {return hashcode;}
}
