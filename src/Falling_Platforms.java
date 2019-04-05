import processing.core.PApplet;
import processing.core.PImage;

import java.util.ArrayList;
import java.util.HashMap;


public class Falling_Platforms extends PApplet {
    final static int FPS = 60;
    static PApplet sketch;
    static boolean paused = false;
    static PImage playerImg, platformL, platformM, platformR, lavaM;
    static PImage[] lavaT = new PImage[3];
    static int highscore;
    static ArrayList<Platform> platforms;
    private static int framesSincePlatform = 0;


    private static HashMap<String, Boolean> keysPressed = new HashMap<>(5);
    private static Player player;
    private static Lava lava;
    private final Integer BG_COLOUR = color(220);

    public static void main(String[] args) {
        PApplet.main("Falling_Platforms", args);
    }


    public void settings() {
        sketch = this;
        fullScreen();
    }


    public void setup() {

        //ensure platform width does not exceed window width
        assert Platform.MAX_WIDTH <= width : "MAX_PLATFORM_WIDTH must be less than the width of the window";

        frameRate(FPS);

        keysPressed.put("left", false);
        keysPressed.put("right", false);
        keysPressed.put("up", false);
        keysPressed.put("pause", false);

        try {
            highscore = Integer.parseInt(loadStrings("highscore.txt")[0]);
        } catch (NumberFormatException e) {
            println("Error: corrupted highscore.txt file");
        }

        //load images
        playerImg = loadImage("player.png");
        platformL = loadImage("platformLeft.png");
        platformM = loadImage("platformMiddle.png");
        platformR = loadImage("platformRight.png");
        lavaM = loadImage("lavaMiddle.png");
        PImage lavaTsheet = loadImage("lavaTop.png");
        for (int i = 0; i < lavaT.length; i++) {
            lavaT[i] = lavaTsheet.get(0, i * Lava.TILE_HEIGHT, Lava.TILE_WIDTH, Lava.TILE_HEIGHT);
        }

        startGame();
    }

    public void draw() {
        if (!paused) {

            background(BG_COLOUR);

            generatePlatforms();

            //------------UPDATE STUFF--------------

            handleKeys();

            //update platforms
            for (Platform platform : platforms) {
                platform.update();
            }

            //remove dead platforms
            platforms.removeIf(platform -> !platform.isOnScreen);

            player.update();
            lava.render();
        }
    }


    public void keyPressed() {
        if (key == CODED) {
            if (keyCode == LEFT) {
                keysPressed.put("left", true);
            }
            if (keyCode == RIGHT) {
                keysPressed.put("right", true);
            }
            if (keyCode == UP) {
                keysPressed.put("up", true);
            }
        } else {
            if (key == 'a') {
                keysPressed.put("left", true);
            }
            if (key == 'd') {
                keysPressed.put("right", true);
            }
            if (key == 'w') {
                keysPressed.put("up", true);
            }
        }
    }


    public void keyReleased() {
        if (key == CODED) {
            if (keyCode == LEFT) {
                keysPressed.put("left", false);
            }
            if (keyCode == RIGHT) {
                keysPressed.put("right", false);
            }
            if (keyCode == UP) {
                keysPressed.put("up", false);
            }
        } else {
            if (key == 'a') {
                keysPressed.put("left", false);
            }
            if (key == 'd') {
                keysPressed.put("right", false);
            }
            if (key == 'w') {
                keysPressed.put("up", false);
            }
            if (key == ' ' || key == 'p') {
                paused = !paused;
                if (player.isDead) {
                    startGame();
                }
            }
        }
    }


    private void startGame() {
        //set up variables
        platforms = new ArrayList<>();

        generateInitialPlatforms();

        Platform spawnPlatform = platforms.get(0);
        player = new Player((spawnPlatform.x + spawnPlatform.w) / 2, spawnPlatform.y - Player.HEIGHT);

        lava = new Lava();
    }


    //generate new platforms randomly
    private void generatePlatforms() {
        if ((int) random(Platform.SPAWN_CHANCE) == 0 || framesSincePlatform >= Platform.MAX_FRAMES_BETWEEN_SPAWNING) {
            framesSincePlatform = 0;

            int w = (int) random(Platform.MIN_WIDTH / Platform.TILE_WIDTH, Platform.MAX_WIDTH / Platform.TILE_WIDTH) * Platform.TILE_WIDTH;
            int x = (int) random(width - w);
            int y = -Platform.HEIGHT;
            int s = (int) random(Platform.MIN_SPEED, Platform.MAX_SPEED);

            platforms.add(new Platform(x, y, w, s));
        } else {
            framesSincePlatform++;
        }
    }


    //generate initial platforms randomly
    private void generateInitialPlatforms() {
        for (int i = 0; i < random(Platform.MIN_INITIAL_NUM, Platform.MAX_INITIAL_NUM); i++) {

            int w = (int) random(Platform.MIN_WIDTH / Platform.TILE_WIDTH, Platform.MAX_WIDTH / Platform.TILE_WIDTH) * Platform.TILE_WIDTH;
            int x = (int) random(width - w);
            int y = (int) random(Platform.MAX_INITIAL_Y);
            int s = (int) random(Platform.MIN_SPEED, Platform.MAX_SPEED);

            platforms.add(new Platform(x, y, w, s));
        }
    }


    private void handleKeys() {
        if (keysPressed.get("left")) {
            player.x -= Player.LATERAL_VELOCITY;
        }

        if (keysPressed.get("right")) {
            player.x += Player.LATERAL_VELOCITY;
        }

        if (keysPressed.get("up") && player.landed != null) {
            player.velocityY = -Player.JUMP_SPEED;
        }
    }
}
