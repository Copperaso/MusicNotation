package reactions;

import java.awt.Graphics;
import music.I;

public abstract class Mass extends Reaction.List implements I.Show {
    // parent of every shape drawn on the screen
    public Layer layer;

    public Mass(String layerName) {
        this.layer = Layer.byName.get(layerName);
        if (this.layer != null) {
            layer.add(this);
        } else {
            System.out.println("Bad layerName " + layerName);
        }
    }

    public void delete() {
        clearAll();
        layer.remove(this);
    }

    public void show(Graphics g) {

    }
}
