class Player {
    static final int HEIGHT = 90;
    static final float LATERAL_VELOCITY = 5;
    static final float JUMP_SPEED = 20;
    private static final float GRAVITY = 1;
    private static final int HITBOX_WIDTH = 30;
    private static final int HITBOX_X_OFFSET = 10;
    private static final boolean GRAVITY_ENABLED = true;
    float x;
    float velocityY = 0;
    Platform landed = null;
    boolean isDead = false;
    private Falling_Platforms p = Falling_Platforms.sketch;
    private float y;
    private int score;

    Player(int x, int y) {
        this.x = x;
        this.y = y;
    }

    private void render() {
        p.image(p.playerImg, this.x, this.y);
    }

    private void renderScore() {
        p.fill(0);
        p.textSize(80);

        p.textAlign(p.LEFT, p.TOP);
        p.text(this.score, 0, -15);

        p.textAlign(p.RIGHT, p.TOP);
        p.text(p.highscore, p.width, -15);
    }

    private void move() {
        if (GRAVITY_ENABLED) {
            velocityY += Player.GRAVITY;
        }

        y += velocityY;
    }

    void update() {
        score++;

        float ix = x + Player.HITBOX_X_OFFSET;
        float iy = y;
        this.move();
        float fx = x + Player.HITBOX_X_OFFSET;
        float fy = y;

        this.handleCollisions(ix, iy, fx, fy);
        this.checkDead();
        this.render();
        this.renderScore();
    }

    private void handleCollisions(float ix, float iy, float fx, float fy) {
        landed = null;

        for (Platform platform : p.platforms) {
            collisions.Collision newCoords = collisions.findEntranceCheckForTunnelling(ix, iy, fx, fy, Player.HITBOX_WIDTH, Player.HEIGHT, platform.x, platform.y, platform.w, Platform.HEIGHT);

            if (newCoords != null && newCoords.side.equals("top")) {
                landed = platform;
                fx = newCoords.x;
                fy = newCoords.y;
            }
        }

        if (landed != null) {
            this.x = fx - Player.HITBOX_X_OFFSET;
            this.y = fy;

            this.velocityY = landed.speed;
        }
    }


    private void checkDead() {
        if (this.y > p.height - Lava.HEIGHT - Player.HEIGHT) {
            p.paused = true;
            isDead = true;

            if (score > p.highscore) {
                p.highscore = score;
                p.saveStrings("data/highscore.txt", new String[]{Integer.toString(p.highscore)});
            }

            p.pushStyle();
            p.fill(180, 0, 0);
            p.textSize(200);
            p.textAlign(p.CENTER, p.CENTER);
            p.text("DEAD", p.width / 2, p.height / 2);
            p.popStyle();
        }
    }
}
