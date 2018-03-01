package Game.GameObjects;

import Game.GameController;
import Game.SpriteSheets.SpriteSheet;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.awt.*;

public class Character extends GameObject {
    public Character(int x, int y) {
        super(x, y);
    }
    private final int WIDTH = 72;

    // Spritesheets
    private SpriteSheet idleRightSpriteSheet;
    private SpriteSheet idleLeftSpriteSheet;
    private SpriteSheet runRightSpriteSheet;
    private SpriteSheet runLeftSpriteSheet;
    private SpriteSheet jumpLeftSpriteSheet;
    private SpriteSheet fallLeftSpriteSheet;
    private SpriteSheet jumpRightSpriteSheet;
    private SpriteSheet fallRightSpriteSheet;

    private boolean lastSpriteRight = true;
    private boolean rightCollision = false;
    private boolean leftCollision = false;
    private boolean isAlive = true;

    EnemyType enemyType;


    private int hp = 100; //Hit Points

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getHp() {
        return hp;
    }

    public void setAlive(boolean isAlive) {
        this.isAlive = isAlive;
    }

    public boolean getAlive() {
        return isAlive;
    }

    public int getStartPosition() {
        return 0;
    }

    public boolean getLastSpriteRight() {
        return lastSpriteRight;
    }

    public void tick(GraphicsContext gc) {
        handleVelocityX();
        handleVelocityY();
        render(gc);
        drawHealthBar(gc);
    }

    @Override
    public void initializeSpriteSheets() {

    }

    public void drawHealthBar(GraphicsContext gc) {
        short healthX = 100;
        short healthY = 24;
        short healthWidth = 80;
        short healthHeight = 25;
        short healthArc = 10;

        // Health bar background
        gc.setGlobalAlpha(0.7);
        gc.setFill(Color.DARKGRAY);
        gc.fillRoundRect(healthX, healthY, healthWidth, healthHeight, healthArc, healthArc);

        // Health bar fill
        gc.setGlobalAlpha(1);
        if (hp >= 90) {
            gc.setFill(Color.LIMEGREEN);
        } else if (hp >= 75) {
            gc.setFill(Color.YELLOW);
        } else if (hp >= 50) {
            gc.setFill(Color.ORANGE);
        } else {
            gc.setFill(Color.RED);
        }
        gc.fillRoundRect(healthX, healthY, hp / 1.25, healthHeight, healthArc, healthArc);

        // Health bar stroke
        gc.setGlobalAlpha(0.5);
        gc.setStroke(Color.BLACK);
        gc.strokeRoundRect(healthX, healthY, healthWidth, healthHeight, healthArc, healthArc);

        // Health bar text
        gc.setGlobalAlpha(1);
        gc.setFill(Color.BLACK);
        String formattedHp = String.valueOf((double) hp / 100 * 100);
        gc.fillText(formattedHp.substring(0, formattedHp.length() - 2) + "%", 187, 40);
    }

    protected void handleVelocityX() {
        setX(getX() + getVelocityX());
    }

    protected void handleVelocityY() {
        int MAX_VELOCITY_FALLING = 13;
        int MAX_VELOCITY_JUMPING = -35;

        if (getVelocityY() >= MAX_VELOCITY_FALLING) {
            setY(getY() + MAX_VELOCITY_FALLING);
        } else if (getY() <= MAX_VELOCITY_JUMPING) {
            setY(getY() + MAX_VELOCITY_JUMPING);
        } else {
            setY(getY() + getVelocityY());
        }
    }

    public void initializeSpriteSheets(EnemyType enemyType) {
        idleRightSpriteSheet = new SpriteSheet("/Resources/" + enemyType.getFileName() + "/idle_right.png", 12, 72, 76);
        idleLeftSpriteSheet = new SpriteSheet("/Resources/" + enemyType.getFileName() + "/idle_left.png", 12, 72, 76);
        runRightSpriteSheet = new SpriteSheet("/Resources/" + enemyType.getFileName() + "/run_right.png", 18, 99, 77);
        runLeftSpriteSheet = new SpriteSheet("/Resources/" + enemyType.getFileName() + "/run_left.png", 18, 99, 77);
        jumpRightSpriteSheet = new SpriteSheet("/Resources/" + enemyType.getFileName() + "/jump_right.png", 1, 76, 77);
        jumpLeftSpriteSheet = new SpriteSheet("/Resources/" + enemyType.getFileName() + "/jump_left.png", 1, 76, 77);
        fallRightSpriteSheet = new SpriteSheet("/Resources/" + enemyType.getFileName() + "/fall_left.png", 1, 67, 77);
        fallLeftSpriteSheet = new SpriteSheet("/Resources/" + enemyType.getFileName() + "/fall_right.png", 1, 67, 77);
    }



    public Rectangle getBoundsBottom() {
        return new Rectangle(GameController.PLAYER_X_MARGIN + 20, getY() + getCurrentSpriteSheet().getSpriteHeight() - 20, WIDTH - 40, 20);
    }

    public Rectangle getBoundsTop() {
        return new Rectangle(GameController.PLAYER_X_MARGIN + 20, getY(), WIDTH - 40, 20);
    }

    public Rectangle getBoundsRight() {
        return new Rectangle(GameController.PLAYER_X_MARGIN + WIDTH - 20, getY() + 10, 20, getCurrentSpriteSheet().getSpriteHeight() - 20);
    }

    public Rectangle getBoundsLeft() {
        return new Rectangle(GameController.PLAYER_X_MARGIN, getY() + 10, 20, getCurrentSpriteSheet().getSpriteHeight() - 20);
    }

    public void setRightCollision(boolean rightCollision) {
        this.rightCollision = rightCollision;
    }

    public void setLeftCollision(boolean leftCollision) {
        this.leftCollision = leftCollision;
    }


}
