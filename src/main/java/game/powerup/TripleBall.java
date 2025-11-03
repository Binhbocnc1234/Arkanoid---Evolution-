package game.powerup;
import game.info.GameInfo;
import game.weapon.*;

public class TripleBall extends PowerUp{
    protected static float rarity = 0.3f;

    public TripleBall() {
        super(0, 0, 25, 25, "Powerup/Multiply Powerup.png");
    }
    
    public TripleBall(float x, float y, float width, float height, String imagePath) {
        super(x, y, width, height, imagePath);
    }

    public float getRarity() {
        return rarity;
    }

    public PowerUp summon() {
        return new TripleBall();
    }

    @Override
    public void ApplyPowerup() {
        /**GameInfo.getInstance().addGameObject(new Ball(ball.getX()/1.2f, ball.getY()/1.2f,
                                            "Ball.png", 25, paddle));
        GameInfo.getInstance().addGameObject(new Ball(ball.getX()/1.1f, ball.getY()/1.1f,
                                            "Ball.png", 25, paddle));
        */
        Ball b = new Ball(paddle.getX() + paddle.getWidth() / 2, paddle.getY() - 20, "Ball.png", 25);
        b.setVelocity(-10, -10);
        GameInfo.getInstance().addGameObject(b);
        b = new Ball(paddle.getX() - paddle.getWidth() / 2, paddle.getY() - 20, "Ball.png", 25);
        b.setVelocity(10, -10);
        GameInfo.getInstance().addGameObject(b);
    }

}