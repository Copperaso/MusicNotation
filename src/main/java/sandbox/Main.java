package sandbox;

import graphics.Window;

public class Main {

    public static void main(String[] args) {
        System.out.println("Music here!");
        Window.PANEL = new ShapeTrainer();
        // Window.PANEL = new PaintInk(); // test shape recognize
        Window.launch();
    }
}
