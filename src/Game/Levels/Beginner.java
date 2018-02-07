package Game.Levels;

import Game.GameController;
import Game.GameObjects.Coin;
import Game.GameObjects.Enemy;
import Game.GameObjects.Saw;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;


public class Beginner {

    public static final int GROUND_FLOOR_HEIGHT = 128;
    private GraphicsContext gc;
    private long startNanoTime;
    private long currentNanoTime;

    public Beginner(GraphicsContext gc) {
        this.gc = gc;
    }

    public void draw(long startNanoTime, long currentNanoTime) {
        this.startNanoTime = startNanoTime;
        this.currentNanoTime = currentNanoTime;
        drawTiles();
        //drawEnemies();
        //drawObstacles();
        //drawCoin();
    }

    private void drawTiles() {
        Image tile;
        for (int x = 0; x <= 1280 - GameController.TILE_SIZE; x += GameController.TILE_SIZE) {
            if (x == 0) {
                tile = new Image("Resources/tiles/GrassLeft.png");
            } else if (x == 1280 - GameController.TILE_SIZE) {
                tile = new Image("Resources/tiles/GrassRight.png");
            } else {
                tile = new Image("Resources/tiles/GrassMid.png");
            }
            gc.drawImage(tile, x, GameController.CANVAS_HEIGHT - GameController.TILE_SIZE);
        }
    }

    private void drawCoin() {
        double t = (currentNanoTime - startNanoTime) / 1000000000.0;
        Coin coin1 = new Coin(gc);
        coin1.setY((int) (300 + 128 * Math.sin(t)));
    }

    private void drawEnemies() {
        double t = (currentNanoTime - startNanoTime) / 1000000000.0;

        Enemy mace1 = new Enemy();
        mace1.setX((int) (900 + 128 * Math.cos(t)));
        mace1.setY((int) (300 + 128 * Math.sin(t)));
        gc.drawImage(new Image("Resources/enemies/Mace.png"), mace1.getX(), mace1.getY());

        Enemy mace2 = new Enemy();
        mace2.setX((int) (900 + 128 * Math.sin(t)));
        mace2.setY((int) (300 + 128 * Math.cos(t)));
        gc.drawImage(new Image("Resources/enemies/Mace.png"), mace2.getX(), mace2.getY());
    }

    private void drawObstacles() {
        double t = (currentNanoTime - startNanoTime) / 1000000000.0;
        Saw saw1 = new Saw();
        saw1.setY((int) (300 + 128 * Math.sin(t)));
        gc.drawImage(new Image("Resources/obstacles/Saw.png"), saw1.getX(), saw1.getY());
    }

}
