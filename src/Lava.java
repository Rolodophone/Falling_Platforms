import processing.core.PApplet;

class Lava {
    static final int HEIGHT = 50;
    static final int TILE_WIDTH = 32;
    static final int TILE_HEIGHT = 32;
    private PApplet p = Falling_Platforms.sketch;

    void render() {
        //iterate through x values
        for (int x = 0; x < p.width; x += Lava.TILE_WIDTH) {

            //render top of lava
            if ((p.frameCount / Falling_Platforms.FPS) % Falling_Platforms.lavaT.length < 1) {
                p.image(Falling_Platforms.lavaT[0], x, p.height - Lava.HEIGHT);
            } else if ((p.frameCount / Falling_Platforms.FPS) % Falling_Platforms.lavaT.length < 2) {
                p.image(Falling_Platforms.lavaT[1], x, p.height - Lava.HEIGHT);
            } else {
                p.image(Falling_Platforms.lavaT[2], x, p.height - Lava.HEIGHT);
            }

            //render rest of lava
            for (int y = p.height - Lava.HEIGHT + Lava.TILE_HEIGHT; y < p.height; y += Lava.TILE_HEIGHT) {
                p.image(Falling_Platforms.lavaM, x, y);
            }
        }
    }
}
